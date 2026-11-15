package com.provismet.dualswords.registry;

import com.provismet.combatplus.api.enchantment.effect.doubleEntity.CodeExecutionDoubleEntityEffect;
import com.provismet.combatplus.api.enchantment.effect.singleEntity.CodeExecutionSingleEntityEffect;
import com.provismet.combatplus.api.item.component.MeleeWeaponComponent;
import com.provismet.combatplus.api.item.interfaces.DualWeapon;
import com.provismet.combatplus.api.tag.CombatPlusItemTags;
import com.provismet.combatplus.registries.CombatPlusDataComponentTypes;
import com.provismet.combatplus.utility.CombatPlusCallbackUtil;
import com.provismet.combatplus.utility.CombatPlusEnchantmentHelper;
import com.provismet.combatplus.utility.CombatPlusRegistries;
import com.provismet.dualswords.DSDamageTypes;
import com.provismet.dualswords.DualSwordsMain;
import com.provismet.dualswords.interfaceMixin.IMixinLivingEntity;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.Vec3;

import java.util.function.Predicate;

public abstract class DSLambdas {
    public static void register () {
        registerSingleEntityEffect("lunge", (world, level, context, user, pos) -> {
            if (!(user instanceof Player player)) return;

            if (player.onGround()) player.randomTeleport(player.getX(), player.getY() + 0.5, player.getZ(), false);
            double dx = -Mth.sin(user.getYHeadRot() / Mth.RAD_TO_DEG);
            double dz = Mth.cos(user.getYHeadRot() / Mth.RAD_TO_DEG);
            Vec3 velocity = new Vec3(dx, 0.0, dz).scale(0.5 * level);
            player.push(velocity);
            player.hurtMarked = true; // This is pretty much the new "velocityDirty". Not sure if Minecraft bug or not.
            ((IMixinLivingEntity)player).dual_Swords$setLungeTicks(context.itemStack(), context.inSlot(), 30);
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS, 1f, 1f);
            context.itemStack().hurtAndBreak(1, player, player.getUsedItemHand().asEquipmentSlot());
        });

        registerSingleEntityCondition("dual_wielder", entity -> {
            if (entity instanceof LivingEntity living) return living.getMainHandItem().is(CombatPlusItemTags.DUAL_WEAPON) && living.getOffhandItem().is(CombatPlusItemTags.DUAL_WEAPON);
            return false;
        });

        registerDoubleEntityEffect("parry", (world, level, context, user, attacker, pos) -> {
            if (user instanceof Player player) {
                if (attacker instanceof LivingEntity target) {
                    if (player.distanceTo(target) <= player.getAttributeValue(Attributes.ENTITY_INTERACTION_RANGE)) {
                        DamageSource riposte = DSDamageTypes.RIPOSTE.createDamageSource(player);

                        float itemDamage = 0f;
                        if (context.itemStack().getItem() instanceof DualWeapon dual) {
                            itemDamage = dual.getOffhandDamage(context.itemStack()) * 1.2f;
                        }
                        else {
                            itemDamage += context.itemStack().getOrDefault(CombatPlusDataComponentTypes.MELEE_WEAPON, MeleeWeaponComponent.DEFAULT).dualDamage() * 1.2f;
                        }

                        float finalDamage = CombatPlusEnchantmentHelper.getDamage(world, context.itemStack(), target, riposte, itemDamage);
                        target.hurtServer(world, riposte, finalDamage);
                        float knockback = EnchantmentHelper.modifyKnockback(world, context.itemStack(), target, riposte, (float) player.getAttributeValue(Attributes.ATTACK_KNOCKBACK));
                        target.knockback(knockback * 0.5, Mth.sin(player.getYRot() * ((float) Math.PI / 180)), -Mth.cos(player.getYRot() * ((float) Math.PI / 180)), riposte, finalDamage);
                        CombatPlusCallbackUtil.postChargedHit(world, context.itemStack(), context.inSlot(), player, target);
                    }
                    context.itemStack().hurtAndBreak(1, player, player.getUsedItemHand().asEquipmentSlot()); // Do not use posthit, it ONLY breaks the mainhand.
                }
                else if (attacker instanceof AbstractArrow persistentProjectile) {
                    float deflectionLevel = CombatPlusEnchantmentHelper.modifyValue(DSEnchantmentComponentTypes.DEFLECTION_SPEED, world, context.itemStack(), player, 1);
                    persistentProjectile.setDeltaMovement(persistentProjectile.getDeltaMovement().scale(deflectionLevel)); // This gets multiplied by -0.1 in onEntityHit();
                }
                player.useItemRemaining = 1;

                double offsetX = -Mth.sin(player.getYRot() * (float) (Math.PI / 180.0));
                double offsetZ = Mth.cos(player.getYRot() * (float) (Math.PI / 180.0));
                world.sendParticles(ParticleTypes.SWEEP_ATTACK, player.getX() + offsetX, player.getY(0.5), player.getZ() + offsetZ, 0, offsetX, 0.0, offsetZ, 0.0);
            }
        });
    }

    private static void registerSingleEntityEffect (String name, CodeExecutionSingleEntityEffect.Lambda lambda) {
        Registry.register(CombatPlusRegistries.SINGLE_ENTITY_LAMBDA, DualSwordsMain.identifier(name), lambda);
    }

    private static void registerSingleEntityCondition (String name, Predicate<Entity> predicate) {
        Registry.register(CombatPlusRegistries.SINGLE_ENTITY_LAMBDA_CONDITION, DualSwordsMain.identifier(name), predicate);
    }

    private static void registerDoubleEntityEffect (String name, CodeExecutionDoubleEntityEffect.Lambda lambda) {
        Registry.register(CombatPlusRegistries.DOUBLE_ENTITY_LAMBDA, DualSwordsMain.identifier(name), lambda);
    }
}
