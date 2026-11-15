package com.provismet.dualswords.mixin;

import com.provismet.combatplus.api.item.component.MeleeWeaponComponent;
import com.provismet.combatplus.api.item.interfaces.DualWeapon;
import com.provismet.combatplus.api.item.interfaces.MeleeWeapon;
import com.provismet.combatplus.registries.CombatPlusDataComponentTypes;
import com.provismet.combatplus.utility.CombatPlusEnchantmentHelper;
import com.provismet.dualswords.DSDamageTypes;
import com.provismet.dualswords.interfaceMixin.IMixinLivingEntity;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value=LivingEntity.class, priority=999)
public abstract class LivingEntityMixin extends Entity implements IMixinLivingEntity {
    protected LivingEntityMixin (EntityType<?> type, Level world) {
        super(type, world);
    }

    @Unique private int lungeTicks = 0;
    @Unique private ItemStack lungeWeapon = ItemStack.EMPTY;
    @Unique private EquipmentSlot lungeSlot = null;

    @Shadow public abstract double getAttributeValue (Holder<Attribute> attribute);
    @Shadow public abstract boolean hurtServer (ServerLevel world, DamageSource source, float amount);

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
            if (this.onGround()) {
                this.lungeTicks = 0;
                this.lungeWeapon = ItemStack.EMPTY;
            }
            else if (this.level() instanceof ServerLevel serverWorld) {
                double deltaX = this.random.nextDouble() * 0.5 * Mth.PI;
                double deltaZ = this.random.nextDouble() * 0.5 * Mth.PI;

                float angle = this.random.nextFloat() * Mth.PI * 2f;
                double x = -Mth.sin(angle) * 0.15 + this.getX();
                double y = this.random.nextDouble() - 0.5 + this.getY(0.5);
                double z = Mth.cos(angle) * 0.15 + this.getZ();
                serverWorld.sendParticles(ParticleTypes.SWEEP_ATTACK, x, y, z, 3, deltaX, 0.0, deltaZ, 0.0);

                Vec3 attackPos = this.position().add(this.getDeltaMovement().normalize().scale(3));
                AABB hitbox = AABB.ofSize(attackPos, 2.0, 0.5, 2.0).minmax(this.getBoundingBox().inflate(1.0, 0.5, 1.0));
                List<Entity> others = this.level().getEntities(this, hitbox);
                for (Entity other : others) {
                    if (other instanceof LivingEntity target) {
                        this.lungeTicks = 0;
                        this.setDeltaMovement(this.getDeltaMovement().scale(-0.2));
                        this.hurtMarked = true;

                        if ((LivingEntity)(Object)this instanceof Player player && this.lungeSlot != null) {
                            float damage = 0f;

                            if (this.lungeWeapon.getItem() instanceof DualWeapon dual) {
                                damage += dual.getOffhandDamage(this.lungeWeapon) * 2f;
                            }
                            else {
                                damage += this.lungeWeapon.getOrDefault(CombatPlusDataComponentTypes.MELEE_WEAPON, MeleeWeaponComponent.DEFAULT).dualDamage() * 2f;
                            }

                            DamageSource lunge = DSDamageTypes.LUNGE.createDamageSource(player);
                            damage = CombatPlusEnchantmentHelper.getDamage(serverWorld, this.lungeWeapon, target, lunge, damage);
                            float knockbackAmount = EnchantmentHelper.modifyKnockback(serverWorld, this.lungeWeapon, target, lunge, (float)this.getAttributeValue(Attributes.ATTACK_KNOCKBACK));
                            target.hurtServer(serverWorld, lunge, damage);
                            if (knockbackAmount > 0) target.knockback(knockbackAmount * 0.5, this.getX() - target.getX(), this.getZ() - target.getZ(), lunge, damage);

                            if (this.lungeWeapon.getItem() instanceof MeleeWeapon melee) {
                                melee.postChargedHit(this.lungeWeapon, player, target);
                            }
                            CombatPlusEnchantmentHelper.postChargedHit(serverWorld, player, target, this.lungeSlot);
                            this.lungeWeapon.hurtAndBreak(1, player, this.lungeSlot);
                        }

                        this.lungeWeapon = ItemStack.EMPTY;
                        break;
                    }
                }
            }
        }
    }
}
