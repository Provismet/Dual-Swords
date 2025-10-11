package com.provismet.dualswords.mixin;

import com.provismet.CombatPlusCore.interfaces.DualWeapon;
import com.provismet.CombatPlusCore.interfaces.MeleeWeapon;
import com.provismet.CombatPlusCore.items.component.MeleeWeaponComponent;
import com.provismet.CombatPlusCore.registries.CPCDataComponentTypes;
import com.provismet.CombatPlusCore.utility.CPCEnchantmentHelper;
import com.provismet.dualswords.DSDamageTypes;
import com.provismet.dualswords.interfaceMixin.IMixinLivingEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value=LivingEntity.class, priority=999)
public abstract class LivingEntityMixin extends Entity implements IMixinLivingEntity {
    protected LivingEntityMixin (EntityType<?> type, World world) {
        super(type, world);
    }

    @Unique private int lungeTicks = 0;
    @Unique private ItemStack lungeWeapon = ItemStack.EMPTY;
    @Unique private EquipmentSlot lungeSlot = null;

    @Shadow public abstract double getAttributeValue (RegistryEntry<EntityAttribute> attribute);
    @Shadow public abstract boolean damage (ServerWorld world, DamageSource source, float amount);

    @Override
    public void dual_Swords$setLungeTicks (ItemStack stack, EquipmentSlot slot, int ticks) {
        this.lungeTicks = ticks;
        this.lungeWeapon = stack;
        if (this.lungeWeapon == null || this.lungeWeapon.isEmpty()) this.lungeSlot = null;
        else this.lungeSlot = slot;
    }

    @Inject(method="tick", at=@At("HEAD"))
    private void applyLunge (CallbackInfo info) {
        if (this.lungeTicks > 0) {
            --this.lungeTicks;
            if (this.isOnGround()) {
                this.lungeTicks = 0;
                this.lungeWeapon = ItemStack.EMPTY;
            }
            else if (this.getEntityWorld() instanceof ServerWorld serverWorld) {
                double deltaX = this.random.nextDouble() * 0.5 * MathHelper.PI;
                double deltaZ = this.random.nextDouble() * 0.5 * MathHelper.PI;

                float angle = this.random.nextFloat() * MathHelper.PI * 2f;
                double x = -MathHelper.sin(angle) * 0.15 + this.getX();
                double y = this.random.nextDouble() - 0.5 + this.getBodyY(0.5);
                double z = MathHelper.cos(angle) * 0.15 + this.getZ();
                serverWorld.spawnParticles(ParticleTypes.SWEEP_ATTACK, x, y, z, 3, deltaX, 0.0, deltaZ, 0.0);

                Vec3d attackPos = this.getEntityPos().add(this.getVelocity().normalize().multiply(3));
                Box hitbox = Box.of(attackPos, 2.0, 0.5, 2.0).union(this.getBoundingBox().expand(1.0, 0.5, 1.0));
                List<Entity> others = this.getEntityWorld().getOtherEntities(this, hitbox);
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
                            else {
                                damage += this.lungeWeapon.getOrDefault(CPCDataComponentTypes.MELEE_WEAPON, MeleeWeaponComponent.DEFAULT).dualDamage() * 2f;
                            }

                            DamageSource lunge = DSDamageTypes.LUNGE.createDamageSource(player);
                            damage = CPCEnchantmentHelper.getDamage(serverWorld, this.lungeWeapon, target, lunge, damage);
                            float knockbackAmount = EnchantmentHelper.modifyKnockback(serverWorld, this.lungeWeapon, target, lunge, (float)this.getAttributeValue(EntityAttributes.ATTACK_KNOCKBACK));
                            target.damage(serverWorld, lunge, damage);
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
