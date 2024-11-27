package com.provismet.dualswords.mixin;

import java.util.List;
import java.util.Objects;

import com.mojang.datafixers.util.Pair;
import com.provismet.dualswords.registry.DSEnchantmentComponentTypes;
import com.provismet.dualswords.util.tag.DSDamageTypeTags;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.UseAction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.sugar.Local;
import com.provismet.CombatPlusCore.interfaces.DualWeapon;
import com.provismet.CombatPlusCore.interfaces.MeleeWeapon;
import com.provismet.CombatPlusCore.utility.CPCEnchantmentHelper;
import com.provismet.dualswords.DSDamageTypes;
import com.provismet.dualswords.interfaceMixin.IMixinLivingEntity;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

@Mixin(value=LivingEntity.class, priority=999)
public abstract class LivingEntityMixin extends Entity implements IMixinLivingEntity {
    protected LivingEntityMixin (EntityType<?> type, World world) {
        super(type, world);
    }

    @Unique private int lungeTicks = 0;
    @Unique private ItemStack lungeWeapon = ItemStack.EMPTY;
    @Unique private EquipmentSlot lungeSlot = null;

    @Shadow protected ItemStack activeItemStack;
    @Shadow protected int itemUseTimeLeft;

    @Shadow public abstract double getAttributeValue (RegistryEntry<EntityAttribute> attribute);
    @Shadow public abstract boolean damage (DamageSource source, float amount);

    @Unique
    private boolean isParrying () {
        Pair<String, Integer> useAction = EnchantmentHelper.getEffectListAndLevel(this.activeItemStack, DSEnchantmentComponentTypes.USE_ACTION);
        return useAction != null && Objects.equals(useAction.getFirst(), UseAction.BLOCK.name());
    }

    @Override
    public void dual_Swords$setLungeTicks (ItemStack stack, EquipmentSlot slot, int ticks) {
        this.lungeTicks = ticks;
        this.lungeWeapon = stack;
        if (this.lungeWeapon == null || this.lungeWeapon.isEmpty()) this.lungeSlot = null;
        else this.lungeSlot = slot;
    }

    // Shields take 5 ticks to become active, swords should be faster than that.
    @Inject(method="isBlocking", at=@At(value="INVOKE", target="Lnet/minecraft/item/Item;getMaxUseTime(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/LivingEntity;)I", shift=At.Shift.BEFORE), cancellable=true)
    private void quickParry (CallbackInfoReturnable<Boolean> cir) {
        if (isParrying()) cir.setReturnValue(this.activeItemStack.getMaxUseTime((LivingEntity)(Object)this) - this.itemUseTimeLeft >= 2);
    }
    
    @Inject(method="blockedByShield", at=@At("RETURN"), cancellable=true)
    private void preventParry (DamageSource source, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValueZ() && isParrying()) {
            if (source.isIn(DSDamageTypeTags.BYPASSES_PARRY)) cir.setReturnValue(false);
            else if (source.isDirect() && source.getAttacker() instanceof LivingEntity living && living.disablesShield()) cir.setReturnValue(false);
        }
    }

    @ModifyArg(method="handleStatus(B)V", at=@At(value="INVOKE", target="Lnet/minecraft/entity/LivingEntity;playSound(Lnet/minecraft/sound/SoundEvent;FF)V", ordinal=2))
    private SoundEvent playParrySound (SoundEvent soundEvent) {
        if (isParrying()) return SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP;
        return soundEvent;
    }

    @Inject(method="damage", at=@At("TAIL"))
    private void combatCallbacks (DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir, @Local(ordinal=0) boolean blocked) {
        if (blocked && isParrying() && (LivingEntity)(Object)this instanceof PlayerEntity player && player.getWorld() instanceof ServerWorld serverWorld) {
            if (source.isDirect() && source.getAttacker() instanceof LivingEntity target) {
                if (this.distanceTo(target) <= 4.5f) {
                    DamageSource riposte = DSDamageTypes.RIPOSTE.createDamageSource(player);

                    float itemDamage = 0f;
                    if (this.activeItemStack.getItem() instanceof DualWeapon dual) {
                        itemDamage = dual.getOffhandDamage(this.activeItemStack) * 1.2f;
                    }
                    float finalDamage = CPCEnchantmentHelper.getDamage(serverWorld, this.activeItemStack, target, riposte, itemDamage);
                    target.damage(riposte, finalDamage);
                    float knockback = EnchantmentHelper.modifyKnockback(serverWorld, this.activeItemStack, target, riposte, (float)this.getAttributeValue(EntityAttributes.GENERIC_ATTACK_KNOCKBACK));
                    target.takeKnockback(knockback * 0.5, MathHelper.sin(this.getYaw() * ((float)Math.PI / 180)), -MathHelper.cos(this.getYaw() * ((float)Math.PI / 180)));
                }

                if (this.activeItemStack.getItem() instanceof MeleeWeapon melee) {
                    melee.postChargedHit(this.activeItemStack, player, target);
                }
                CPCEnchantmentHelper.postChargedHit(serverWorld, player, target, LivingEntity.getSlotForHand(player.getActiveHand()));
                this.activeItemStack.damage(1, player, LivingEntity.getSlotForHand(player.getActiveHand())); // Do not use posthit, it ONLY breaks the mainhand.
            }
            else if (source.getSource() instanceof PersistentProjectileEntity persistentProjectile) {
                float deflectionLevel = CPCEnchantmentHelper.modifyValue(DSEnchantmentComponentTypes.DEFLECTION_SPEED, serverWorld, this.activeItemStack, player, 1);
                persistentProjectile.setVelocity(persistentProjectile.getVelocity().multiply(deflectionLevel)); // This gets multiplied by -0.1 in onEntityHit();
            }
            player.spawnSweepAttackParticles();
            this.itemUseTimeLeft = 0;
            player.stopUsingItem();
        }
    }

    @Inject(method="tick", at=@At("HEAD"))
    private void applyLunge (CallbackInfo info) {
        if (this.lungeTicks > 0) {
            --this.lungeTicks;
            if (this.isOnGround()) {
                this.lungeTicks = 0;
                this.lungeWeapon = ItemStack.EMPTY;
            }
            else if (this.getWorld() instanceof ServerWorld serverWorld) {
                double deltaX = this.random.nextDouble() * 0.5 * MathHelper.PI;
                double deltaZ = this.random.nextDouble() * 0.5 * MathHelper.PI;

                float angle = this.random.nextFloat() * MathHelper.PI * 2f;
                double x = -MathHelper.sin(angle) * 0.15 + this.getX();
                double y = this.random.nextDouble() - 0.5 + this.getBodyY(0.5);
                double z = MathHelper.cos(angle) * 0.15 + this.getZ();
                serverWorld.spawnParticles(ParticleTypes.SWEEP_ATTACK, x, y, z, 3, deltaX, 0.0, deltaZ, 0.0);

                Vec3d attackPos = this.getPos().add(this.getVelocity().normalize().multiply(3));
                Box hitbox = Box.of(attackPos, 2.0, 0.5, 2.0).union(this.getBoundingBox().expand(1.0, 0.5, 1.0));
                List<Entity> others = this.getWorld().getOtherEntities(this, hitbox);
                for (Entity other : others) {
                    if (other instanceof LivingEntity target) {
                        this.lungeTicks = 0;
                        this.setVelocity(this.getVelocity().multiply(-0.2));
                        this.velocityModified = true;

                        if ((LivingEntity)(Object)this instanceof PlayerEntity player && this.lungeSlot != null) {
                            float damage = 0f;

                            if (this.lungeWeapon.getItem() instanceof DualWeapon dual) {
                                damage += dual.getOffhandDamage(this.lungeWeapon) * 2f;
                            }

                            DamageSource lunge = DSDamageTypes.LUNGE.createDamageSource(player);
                            damage = CPCEnchantmentHelper.getDamage(serverWorld, this.lungeWeapon, target, lunge, damage);
                            float knockbackAmount = EnchantmentHelper.modifyKnockback(serverWorld, this.lungeWeapon, target, lunge, (float)this.getAttributeValue(EntityAttributes.GENERIC_ATTACK_KNOCKBACK));
                            target.damage(lunge, damage);
                            if (knockbackAmount > 0) target.takeKnockback(knockbackAmount * 0.5, this.getX() - target.getX(), this.getZ() - target.getZ());

                            if (this.lungeWeapon.getItem() instanceof MeleeWeapon melee) {
                                melee.postChargedHit(this.lungeWeapon, player, target);
                            }
                            CPCEnchantmentHelper.postChargedHit(serverWorld, player, target, this.lungeSlot);
                            this.lungeWeapon.damage(1, player, this.lungeSlot);
                        }

                        this.lungeWeapon = ItemStack.EMPTY;
                        break;
                    }
                }
            }
        }
    }
}
