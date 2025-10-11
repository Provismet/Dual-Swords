package com.provismet.dualswords.registry;

import com.provismet.CombatPlusCore.enchantment.effect.doubleEntity.CodeExecutionDoubleEntityEffect;
import com.provismet.CombatPlusCore.enchantment.effect.singleEntity.CodeExecutionSingleEntityEffect;
import com.provismet.CombatPlusCore.interfaces.DualWeapon;
import com.provismet.CombatPlusCore.items.component.MeleeWeaponComponent;
import com.provismet.CombatPlusCore.registries.CPCDataComponentTypes;
import com.provismet.CombatPlusCore.utility.CPCCallbackUtil;
import com.provismet.CombatPlusCore.utility.CPCEnchantmentHelper;
import com.provismet.CombatPlusCore.utility.CPCRegistries;
import com.provismet.CombatPlusCore.utility.tag.CPCItemTags;
import com.provismet.dualswords.DSDamageTypes;
import com.provismet.dualswords.DualSwordsMain;
import com.provismet.dualswords.interfaceMixin.IMixinLivingEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.function.Predicate;

public abstract class DSLambdas {
    public static void register () {
        registerSingleEntityEffect("lunge", (world, level, context, user, pos) -> {
            if (!(user instanceof PlayerEntity player)) return;

            if (player.isOnGround()) player.teleport(player.getX(), player.getY() + 0.5, player.getZ(), false);
            double dx = -MathHelper.sin(user.getHeadYaw() / MathHelper.DEGREES_PER_RADIAN);
            double dz = MathHelper.cos(user.getHeadYaw() / MathHelper.DEGREES_PER_RADIAN);
            Vec3d velocity = new Vec3d(dx, 0.0, dz).multiply(0.5 * level);
            player.addVelocity(velocity);
            player.velocityModified = true;
            ((IMixinLivingEntity)player).dual_Swords$setLungeTicks(context.stack(), context.slot(), 30);
            player.getEntityWorld().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.PLAYERS, 1f, 1f);
            context.stack().damage(1, player, player.getActiveHand().getEquipmentSlot());
        });

        registerSingleEntityCondition("dual_wielder", entity -> {
            if (entity instanceof LivingEntity living) return living.getMainHandStack().isIn(CPCItemTags.DUAL_WEAPON) && living.getOffHandStack().isIn(CPCItemTags.DUAL_WEAPON);
            return false;
        });

        registerDoubleEntityEffect("parry", (world, level, context, user, attacker, pos) -> {
            if (user instanceof PlayerEntity player) {
                if (attacker instanceof LivingEntity target) {
                    if (player.distanceTo(target) <= player.getAttributeValue(EntityAttributes.ENTITY_INTERACTION_RANGE)) {
                        DamageSource riposte = DSDamageTypes.RIPOSTE.createDamageSource(player);

                        float itemDamage = 0f;
                        if (context.stack().getItem() instanceof DualWeapon dual) {
                            itemDamage = dual.getOffhandDamage(context.stack()) * 1.2f;
                        }
                        else {
                            itemDamage += context.stack().getOrDefault(CPCDataComponentTypes.MELEE_WEAPON, MeleeWeaponComponent.DEFAULT).dualDamage() * 1.2f;
                        }

                        float finalDamage = CPCEnchantmentHelper.getDamage(world, context.stack(), target, riposte, itemDamage);
                        target.damage(world, riposte, finalDamage);
                        float knockback = EnchantmentHelper.modifyKnockback(world, context.stack(), target, riposte, (float) player.getAttributeValue(EntityAttributes.ATTACK_KNOCKBACK));
                        target.takeKnockback(knockback * 0.5, MathHelper.sin(player.getYaw() * ((float) Math.PI / 180)), -MathHelper.cos(player.getYaw() * ((float) Math.PI / 180)));
                        CPCCallbackUtil.postChargedHit(world, context.stack(), context.slot(), player, target);
                    }
                    context.stack().damage(1, player, player.getActiveHand().getEquipmentSlot()); // Do not use posthit, it ONLY breaks the mainhand.
                }
                else if (attacker instanceof PersistentProjectileEntity persistentProjectile) {
                    float deflectionLevel = CPCEnchantmentHelper.modifyValue(DSEnchantmentComponentTypes.DEFLECTION_SPEED, world, context.stack(), player, 1);
                    persistentProjectile.setVelocity(persistentProjectile.getVelocity().multiply(deflectionLevel)); // This gets multiplied by -0.1 in onEntityHit();
                }
                player.spawnSweepAttackParticles();
                player.itemUseTimeLeft = 1;
            }
        });
    }

    private static void registerSingleEntityEffect (String name, CodeExecutionSingleEntityEffect.Lambda lambda) {
        Registry.register(CPCRegistries.SINGLE_ENTITY_LAMBDA, DualSwordsMain.identifier(name), lambda);
    }

    private static void registerSingleEntityCondition (String name, Predicate<Entity> predicate) {
        Registry.register(CPCRegistries.SINGLE_ENTITY_LAMBDA_CONDITION, DualSwordsMain.identifier(name), predicate);
    }

    private static void registerDoubleEntityEffect (String name, CodeExecutionDoubleEntityEffect.Lambda lambda) {
        Registry.register(CPCRegistries.DOUBLE_ENTITY_LAMBDA, DualSwordsMain.identifier(name), lambda);
    }
}
