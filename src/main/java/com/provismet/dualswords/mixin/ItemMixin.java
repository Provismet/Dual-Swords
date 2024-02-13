package com.provismet.dualswords.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.provismet.CombatPlusCore.interfaces.DualWeapon;
import com.provismet.dualswords.DualSwordsMain;
import com.provismet.dualswords.interfaceMixin.IMixinLivingEntity;
import com.provismet.dualswords.registry.DSEnchantments;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

@Mixin(Item.class)
public abstract class ItemMixin {
    @Unique
    private Multimap<EntityAttribute, EntityAttributeModifier> dualswords_offHandAttributes = null;

    @Shadow
    public abstract int getMaxUseTime (ItemStack stack);

    @Inject(method="use", at=@At("HEAD"), cancellable=true)
    private void attemptParry (World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (EnchantmentHelper.getLevel(DSEnchantments.PARRY, itemStack) > 0 || EnchantmentHelper.getLevel(DSEnchantments.LUNGE, itemStack) > 0) {
            user.setCurrentHand(hand);
            cir.setReturnValue(TypedActionResult.consume(itemStack));
        }
    }
    
    @Inject(method="getUseAction", at=@At("HEAD"), cancellable=true)
    private void setParryAction (ItemStack itemStack, CallbackInfoReturnable<UseAction> cir) {
        if (EnchantmentHelper.getLevel(DSEnchantments.PARRY, itemStack) > 0) cir.setReturnValue(UseAction.BLOCK);
        else if (EnchantmentHelper.getLevel(DSEnchantments.LUNGE, itemStack) > 0) cir.setReturnValue(UseAction.SPEAR);
    }

    @Inject(method="getMaxUseTime", at=@At("HEAD"), cancellable=true)
    private void setMaxParryTime (ItemStack itemStack, CallbackInfoReturnable<Integer> cir) {
        int parryLevel = 0;
        if ((parryLevel = EnchantmentHelper.getLevel(DSEnchantments.PARRY, itemStack)) > 0) cir.setReturnValue(10 * parryLevel);
        else if (EnchantmentHelper.getLevel(DSEnchantments.LUNGE, itemStack) > 0) cir.setReturnValue(72000);
    }

    @Inject(method="finishUsing", at=@At("HEAD"), cancellable=true)
    private void finishParrying (ItemStack itemStack, World world, LivingEntity user, CallbackInfoReturnable<ItemStack> cir) {
        if (EnchantmentHelper.getLevel(DSEnchantments.PARRY, itemStack) > 0) {
            if (user instanceof PlayerEntity player) {
                player.getItemCooldownManager().set(itemStack.getItem(), 30 + 8 * EnchantmentHelper.getLevel(DSEnchantments.DAISHO, itemStack));
            }
        }
    }

    @Inject(method="onStoppedUsing", at=@At("HEAD"))
    private void onStoppedParrying (ItemStack itemStack, World world, LivingEntity user, int remainingUseTicks, CallbackInfo info) {
        if (user instanceof PlayerEntity player) {
            int lungeLevel = 0;
            if (EnchantmentHelper.getLevel(DSEnchantments.PARRY, itemStack) > 0) {
                if (!player.getItemCooldownManager().isCoolingDown(itemStack.getItem())) {
                    player.getItemCooldownManager().set(
                        itemStack.getItem(),
                        (int)((30 + 8 * EnchantmentHelper.getLevel(DSEnchantments.DAISHO, itemStack)) * (1f - ((float)remainingUseTicks / (float)getMaxUseTime(itemStack))))
                    );
                }
            }
            else if ((lungeLevel = EnchantmentHelper.getLevel(DSEnchantments.LUNGE, itemStack)) > 0 && this.getMaxUseTime(itemStack) - remainingUseTicks > 8) {
                if (!player.getItemCooldownManager().isCoolingDown(itemStack.getItem())) {
                    player.getItemCooldownManager().set(itemStack.getItem(), (int)((60 + 8 * EnchantmentHelper.getLevel(DSEnchantments.DAISHO, itemStack))));
                }
                if (player.isOnGround()) player.move(MovementType.SELF, new Vec3d(0.0, 0.5, 0.0));
                
                double dx = -MathHelper.sin(user.getHeadYaw() / MathHelper.DEGREES_PER_RADIAN);
                double dz = MathHelper.cos(user.getHeadYaw() / MathHelper.DEGREES_PER_RADIAN);
                Vec3d velocity = new Vec3d(dx, 0.0, dz).multiply(0.5 * lungeLevel);
                player.addVelocity(velocity);
                ((IMixinLivingEntity)player).setLungeTicks(itemStack, 30);
                player.getWorld().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.PLAYERS, 1f, 1f);
                itemStack.damage(1, user, p -> p.sendToolBreakStatus(p.getActiveHand()));
            }
        }
    }

    @Inject(method="getAttributeModifiers", at=@At("HEAD"), cancellable=true)
    private void applyOffhandMods (EquipmentSlot slot, CallbackInfoReturnable<Multimap<EntityAttribute, EntityAttributeModifier>> cir) {
        if (slot == EquipmentSlot.OFFHAND && (Item)(Object)this instanceof DualWeapon dualWeapon) {
            if (this.dualswords_offHandAttributes == null) {
                ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
                builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(DualSwordsMain.OFFHAND_ATTRIBUTE_ID, "Offhand Weapon modifier", (double)dualWeapon.getOffhandDamage(), EntityAttributeModifier.Operation.ADDITION));
                this.dualswords_offHandAttributes = builder.build();
            }
            cir.setReturnValue(this.dualswords_offHandAttributes);
        }
    }
}
