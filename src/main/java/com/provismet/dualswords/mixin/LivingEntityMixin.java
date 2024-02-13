package com.provismet.dualswords.mixin;

import java.util.List;

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
import com.provismet.dualswords.Tags;
import com.provismet.dualswords.interfaceMixin.IMixinLivingEntity;
import com.provismet.dualswords.registry.DSEnchantments;

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
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

@Mixin(value=LivingEntity.class, priority=999)
public abstract class LivingEntityMixin extends Entity implements IMixinLivingEntity {
    protected LivingEntityMixin (EntityType<?> type, World world) {
        super(type, world);
    }

    @Unique
    private int lungeTicks = 0;

    @Unique
    private ItemStack lungeWeapon = ItemStack.EMPTY;

    @Shadow
    protected ItemStack activeItemStack;

    @Shadow
    protected int itemUseTimeLeft;

    @Shadow
    protected abstract void attackLivingEntity (LivingEntity target);

    @Shadow
    public abstract ItemStack getMainHandStack ();

    @Shadow
    public abstract ItemStack getOffHandStack ();

    @Unique
    private boolean isParrying () {
        return EnchantmentHelper.getLevel(DSEnchantments.PARRY, this.activeItemStack) > 0;
    }

    @Override
    public void setLungeTicks (ItemStack stack, int ticks) {
        this.lungeTicks = ticks;
        this.lungeWeapon = stack == null ? ItemStack.EMPTY : stack;
    }

    // Shields take 5 ticks to become active, swords should be faster than that.
    @Inject(method="isBlocking", at=@At(value="INVOKE", target="Lnet/minecraft/item/Item;getMaxUseTime(Lnet/minecraft/item/ItemStack;)I", shift=At.Shift.BEFORE), cancellable=true)
    private void quickParry (CallbackInfoReturnable<Boolean> cir) {
        if (isParrying()) cir.setReturnValue(this.activeItemStack.getMaxUseTime() - this.itemUseTimeLeft >= 2);
    }
    
    @Inject(method="blockedByShield", at=@At("RETURN"), cancellable=true)
    private void preventParry (DamageSource source, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValueZ() && isParrying()) {
            if (source.isIn(Tags.BYPASSES_PARRY)) cir.setReturnValue(false);
            else if (!source.isIndirect() && source.getAttacker() instanceof LivingEntity living && living.disablesShield()) cir.setReturnValue(false);
        }
    }

    @ModifyArg(method="handleStatus(B)V", at=@At(value="INVOKE", target="Lnet/minecraft/entity/LivingEntity;playSound(Lnet/minecraft/sound/SoundEvent;FF)V", ordinal=2))
    private SoundEvent playParrySound (SoundEvent soundEvent) {
        if (isParrying()) return SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP;
        return soundEvent;
    }

    @Inject(method="damage", at=@At("TAIL"))
    private void combatCallbacks (DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir, @Local(ordinal=0) boolean blocked) {
        if (blocked && isParrying() && (LivingEntity)(Object)this instanceof PlayerEntity player) {
            if (!source.isIndirect() && source.getAttacker() instanceof LivingEntity attacker) {
                if (this.distanceTo(attacker) <= 4.5f) {
                    float itemDamage = 0f;
                    float enchantDamage = DSEnchantments.RIPOSTE.getDamage(EnchantmentHelper.getLevel(DSEnchantments.RIPOSTE, this.activeItemStack));

                    if (this.activeItemStack.getItem() instanceof DualWeapon dual) {
                        itemDamage = dual.getOffhandDamage() * 1.2f;
                    }

                    attacker.damage(DSDamageTypes.riposte(attacker), itemDamage + enchantDamage);
                }

                if (this.activeItemStack.getItem() instanceof MeleeWeapon melee) {
                    melee.postChargedHit(this.activeItemStack, player, attacker);
                }
                CPCEnchantmentHelper.postChargedHit(player, attacker, this.activeItemStack);
                this.activeItemStack.damage(1, player, p -> p.sendToolBreakStatus(p.getActiveHand())); // Do not use posthit, it ONLY breaks the mainhand.
            }
            else if (source.getSource() instanceof PersistentProjectileEntity persistentProjectile) {
                double deflectionLevel = EnchantmentHelper.getLevel(DSEnchantments.DEFLECT, this.activeItemStack);
                if (deflectionLevel > 0) {
                    persistentProjectile.setVelocity(persistentProjectile.getVelocity().multiply(deflectionLevel * 3.0)); // This gets multiplied by -0.1 in onEntityHit();
                }
            }
            player.getItemCooldownManager().set(this.activeItemStack.getItem(), 30 + 8 * EnchantmentHelper.getLevel(DSEnchantments.DAISHO, this.activeItemStack));
            player.spawnSweepAttackParticles();
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
                for (int i = 0; i < 2; ++i) {
                    double deltaX = this.random.nextDouble() * 2.0 * MathHelper.PI;
                    double deltaZ = this.random.nextDouble() * 2.0 * MathHelper.PI;
    
                    float angle = this.random.nextFloat() * MathHelper.PI * 2f;
                    double x = -MathHelper.sin(angle) * 0.5 + this.getX();
                    double y = this.random.nextDouble() * 1.0 - 0.5 + this.getBodyY(0.5);
                    double z = MathHelper.cos(angle) * 0.5 + this.getZ();

                    serverWorld.spawnParticles(ParticleTypes.SWEEP_ATTACK, x, y, z, 1, deltaX, 0.0, deltaZ, 0.0);
                }

                Vec3d attackPos = this.getPos().add(this.getVelocity().normalize().multiply(3));
                Box hitbox = Box.of(attackPos, 2.0, 0.5, 2.0).union(this.getBoundingBox().expand(1.0, 0.5, 1.0));
                List<Entity> others = this.getWorld().getOtherEntities(this, hitbox);
                for (Entity other : others) {
                    if (other instanceof LivingEntity target) {
                        this.lungeTicks = 0;
                        this.setVelocity(this.getVelocity().multiply(-0.2));
                        this.velocityModified = true;

                        if ((LivingEntity)(Object)this instanceof PlayerEntity player && (this.lungeWeapon.equals(this.getOffHandStack()) || this.lungeWeapon.equals(this.getMainHandStack()))) {
                            float damage = 0f;
                            int knockbackAmount = EnchantmentHelper.getLevel(DSEnchantments.FORCEFUL, this.lungeWeapon);

                            if (this.lungeWeapon.getItem() instanceof DualWeapon dual) {
                                damage += dual.getOffhandDamage() * 2f;
                            }
                            damage += 1.5f * EnchantmentHelper.getLevel(DSEnchantments.THRUST, this.lungeWeapon);

                            target.damage(DSDamageTypes.lunge(player), damage);
                            if (knockbackAmount > 0) target.takeKnockback(knockbackAmount * 0.5, this.getX() - target.getX(), this.getZ() - target.getZ());
                            if (this.lungeWeapon.getItem() instanceof MeleeWeapon melee) {
                                melee.postChargedHit(this.lungeWeapon, player, target);
                            }
                            CPCEnchantmentHelper.postChargedHit(player, target, this.lungeWeapon);
                            if (this.lungeWeapon.equals(this.getMainHandStack())) this.lungeWeapon.postHit(target, player);
                            else this.lungeWeapon.damage(1, player, p -> p.sendToolBreakStatus(Hand.OFF_HAND));
                        }

                        this.lungeWeapon = ItemStack.EMPTY;
                        break;
                    }
                }
            }
        }
    }
}
