package com.provismet.dualswords.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.provismet.dualswords.DamageTypes;
import com.provismet.dualswords.Tags;
import com.provismet.dualswords.enchantments.Enchantments;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MiningToolItem;
import net.minecraft.item.SwordItem;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    protected LivingEntityMixin (EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow
    protected ItemStack activeItemStack;

    @Shadow
    protected int itemUseTimeLeft;

    private boolean isParrying () {
        if (!((LivingEntity)(Object)this).isBlocking()) return false;
        return EnchantmentHelper.getLevel(Enchantments.PARRY, this.activeItemStack) > 0;
    }

    // Shields take 5 ticks to become active, swords should be faster than that.
    @Inject(method="isBlocking", at=@At("TAIL"), cancellable=true)
    private void quickParry (CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValueZ() && EnchantmentHelper.getLevel(Enchantments.PARRY, this.activeItemStack) > 0)
            cir.setReturnValue(this.activeItemStack.getMaxUseTime() - this.itemUseTimeLeft >= 1);
    }
    
    @Inject(method="blockedByShield", at=@At("RETURN"), cancellable=true)
    private void preventParry (DamageSource source, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValueZ() && isParrying()) {
            if (source.isIn(Tags.BYPASSES_PARRY)) cir.setReturnValue(false);
            else if (!source.isIndirect() && source.getAttacker() instanceof LivingEntity living && living.disablesShield()) cir.setReturnValue(false);
        }
    }

    @Inject(method="takeShieldHit", at=@At("HEAD"))
    private void riposte (LivingEntity attacker, CallbackInfo info) {
        if (isParrying()) {
            float itemDamage = 0f;
            float enchantDamage = Enchantments.RIPOSTE.getDamage(EnchantmentHelper.getLevel(Enchantments.RIPOSTE, this.activeItemStack));

            if (this.activeItemStack.getItem() instanceof SwordItem sword) itemDamage = sword.getAttackDamage();
            else if (this.activeItemStack.getItem() instanceof MiningToolItem tool) itemDamage = tool.getAttackDamage();

            attacker.damage(((LivingEntity)(Object)this).getDamageSources().create(DamageTypes.RIPOSTE, (LivingEntity)(Object)this), (itemDamage / 2) + enchantDamage);

            if ((LivingEntity)(Object)this instanceof PlayerEntity player) {
                player.getItemCooldownManager().set(this.activeItemStack.getItem(), 30);
            }
        }
    }

    @ModifyArg(method="handleStatus", at=@At(value="INVOKE", target="Lnet/minecraft/entity/Entity;playSound(Lnet/minecraft/sound/SoundEvent;FF)V"), index=0)
    private SoundEvent playParrySound (SoundEvent soundEvent) {
        if (soundEvent == SoundEvents.ITEM_SHIELD_BLOCK && EnchantmentHelper.getLevel(Enchantments.PARRY, this.activeItemStack) > 0)
            return SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP;
        return soundEvent;
    }
}
