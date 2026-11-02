package com.provismet.dualswords.enchantment.effect.stopped;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.provismet.CombatPlusCore.registries.CPCEnchantmentComponentTypes;
import com.provismet.CombatPlusCore.utility.CPCEnchantmentHelper;
import com.provismet.dualswords.enchantment.component.EnchantmentStoppedUsingEffect;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.LevelBasedValue;

public record ReverseScalingCooldownEffect (LevelBasedValue value) implements EnchantmentStoppedUsingEffect {
    public static final MapCodec<ReverseScalingCooldownEffect> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(LevelBasedValue.CODEC.fieldOf("ticks").forGetter(ReverseScalingCooldownEffect::value)).apply(instance, ReverseScalingCooldownEffect::new));

    @Override
    public void onStoppedUsing (ServerLevel world, int level, EnchantedItemInUse context, LivingEntity user, int remainingTicks) {
        if (!(user instanceof Player player)) return;

        float usage = 1f - ((float)remainingTicks / (float)context.itemStack().getUseDuration(user));
        int cooldown = (int)CPCEnchantmentHelper.modifyValue(CPCEnchantmentComponentTypes.MODIFY_COOLDOWN, world, context.itemStack(), user, (int)(this.value.calculate(level) * usage));
        player.getCooldowns().addCooldown(context.itemStack(), cooldown);
    }

    @Override
    public MapCodec<ReverseScalingCooldownEffect> getCodec () {
        return CODEC;
    }
}
