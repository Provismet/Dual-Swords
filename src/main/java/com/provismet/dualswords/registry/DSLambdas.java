package com.provismet.dualswords.registry;

import com.provismet.CombatPlusCore.enchantment.effect.singleEntity.CodeExecutionSingleEntityEffect;
import com.provismet.CombatPlusCore.utility.CPCRegistries;
import com.provismet.CombatPlusCore.utility.tag.CPCItemTags;
import com.provismet.dualswords.DualSwordsMain;
import com.provismet.dualswords.interfaceMixin.IMixinLivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
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
            ((IMixinLivingEntity)player).setLungeTicks(context.stack(), context.slot(), 30);
            player.getWorld().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.PLAYERS, 1f, 1f);
            context.stack().damage(1, player, LivingEntity.getSlotForHand(player.getActiveHand()));
        });

        registerSingleEntityCondition("dual_wielder", entity -> {
            if (entity instanceof LivingEntity living) return living.getMainHandStack().isIn(CPCItemTags.DUAL_WEAPON) && living.getOffHandStack().isIn(CPCItemTags.DUAL_WEAPON);
            return false;
        });
    }

    private static void registerSingleEntityEffect (String name, CodeExecutionSingleEntityEffect.Lambda lambda) {
        Registry.register(CPCRegistries.SINGLE_ENTITY_LAMBDA, DualSwordsMain.identifier(name), lambda);
    }

    private static void registerSingleEntityCondition (String name, Predicate<Entity> predicate) {
        Registry.register(CPCRegistries.SINGLE_ENTITY_LAMBDA_CONDITION, DualSwordsMain.identifier(name), predicate);
    }
}
