package com.provismet.dualswords.enchantment.effect.stopped;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.provismet.CombatPlusCore.registries.CPCEnchantmentComponentTypes;
import com.provismet.CombatPlusCore.utility.CPCEnchantmentHelper;
import com.provismet.dualswords.enchantment.component.EnchantmentStoppedUsingEffect;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.EnchantmentLevelBasedValue;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;

public record ReverseScalingCooldownEffect (EnchantmentLevelBasedValue value) implements EnchantmentStoppedUsingEffect {
    public static final MapCodec<ReverseScalingCooldownEffect> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(EnchantmentLevelBasedValue.CODEC.fieldOf("ticks").forGetter(ReverseScalingCooldownEffect::value)).apply(instance, ReverseScalingCooldownEffect::new));

    @Override
    public void onStoppedUsing (ServerWorld world, int level, EnchantmentEffectContext context, LivingEntity user, int remainingTicks) {
        if (!(user instanceof PlayerEntity player)) return;

        float usage = 1f - ((float)remainingTicks / (float)context.stack().getMaxUseTime(user));
        int cooldown = (int)CPCEnchantmentHelper.modifyValue(CPCEnchantmentComponentTypes.MODIFY_COOLDOWN, world, context.stack(), user, (int)(this.value.getValue(level) * usage));
        player.getItemCooldownManager().set(context.stack(), cooldown);
    }

    @Override
    public MapCodec<ReverseScalingCooldownEffect> getCodec () {
        return CODEC;
    }
}
