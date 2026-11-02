package com.provismet.dualswords.enchantment.effect.stopped;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.provismet.combatplus.registries.CombatPlusEnchantmentComponentTypes;
import com.provismet.combatplus.utility.CombatPlusEnchantmentHelper;
import com.provismet.dualswords.enchantment.component.EnchantmentStoppedUsingEffect;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.LevelBasedValue;

public record ScalingCooldownEffect (LevelBasedValue value) implements EnchantmentStoppedUsingEffect {
    public static final MapCodec<ScalingCooldownEffect> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(LevelBasedValue.CODEC.fieldOf("ticks").forGetter(ScalingCooldownEffect::value)).apply(instance, ScalingCooldownEffect::new));

    @Override
    public void onStoppedUsing (ServerLevel world, int level, EnchantedItemInUse context, LivingEntity user, int remainingTicks) {
        if (!(user instanceof Player player)) return;

        float usage = (float)remainingTicks / (float)context.itemStack().getUseDuration(user);
        int cooldown = (int)CombatPlusEnchantmentHelper.modifyValue(CombatPlusEnchantmentComponentTypes.MODIFY_COOLDOWN, world, context.itemStack(), user, this.value.calculate(level) * usage);
        player.getCooldowns().addCooldown(context.itemStack(), cooldown);
    }

    @Override
    public MapCodec<ScalingCooldownEffect> getCodec () {
        return CODEC;
    }
}
