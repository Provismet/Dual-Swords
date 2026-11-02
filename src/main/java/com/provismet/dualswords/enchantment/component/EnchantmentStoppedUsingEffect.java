package com.provismet.dualswords.enchantment.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.provismet.dualswords.util.registry.DSRegistries;
import java.util.function.Function;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;

public interface EnchantmentStoppedUsingEffect {
    Codec<EnchantmentStoppedUsingEffect> CODEC = DSRegistries.ENCHANTMENT_STOPPED_USING_EFFECT_TYPE.byNameCodec().dispatch(EnchantmentStoppedUsingEffect::getCodec, Function.identity());

    void onStoppedUsing (ServerLevel world, int level, EnchantedItemInUse context, LivingEntity user, int remainingTicks);
    MapCodec<? extends EnchantmentStoppedUsingEffect> getCodec ();
}
