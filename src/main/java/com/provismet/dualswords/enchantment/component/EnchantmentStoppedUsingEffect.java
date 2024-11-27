package com.provismet.dualswords.enchantment.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.provismet.dualswords.util.registry.DSRegistries;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;

import java.util.function.Function;

public interface EnchantmentStoppedUsingEffect {
    Codec<EnchantmentStoppedUsingEffect> CODEC = DSRegistries.ENCHANTMENT_STOPPED_USING_EFFECT_TYPE.getCodec().dispatch(EnchantmentStoppedUsingEffect::getCodec, Function.identity());

    void onStoppedUsing (ServerWorld world, int level, EnchantmentEffectContext context, LivingEntity user, int remainingTicks);
    MapCodec<? extends EnchantmentStoppedUsingEffect> getCodec ();
}
