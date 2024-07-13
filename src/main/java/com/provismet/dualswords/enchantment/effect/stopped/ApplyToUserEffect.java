package com.provismet.dualswords.enchantment.effect.stopped;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.provismet.dualswords.enchantment.component.EnchantmentStoppedUsingEffect;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.EnchantmentLevelBasedValue;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;

public record ApplyToUserEffect (EnchantmentEntityEffect effect, EnchantmentLevelBasedValue minTicks) implements EnchantmentStoppedUsingEffect {
    public static final MapCodec<ApplyToUserEffect> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(EnchantmentEntityEffect.CODEC.fieldOf("effect").forGetter(ApplyToUserEffect::effect), EnchantmentLevelBasedValue.CODEC.fieldOf("minimum_charging_ticks").forGetter(ApplyToUserEffect::minTicks)).apply(instance, ApplyToUserEffect::new));

    @Override
    public void onStoppedUsing (ServerWorld world, int level, EnchantmentEffectContext context, LivingEntity user, int remainingTicks) {
        if (context.stack().getMaxUseTime(user) - remainingTicks >= this.minTicks.getValue(level))
            this.effect.apply(world, level, context, user, user.getPos());
    }

    @Override
    public MapCodec<? extends EnchantmentStoppedUsingEffect> getCodec () {
        return CODEC;
    }
}
