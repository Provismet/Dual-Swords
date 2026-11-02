package com.provismet.dualswords.enchantment.effect.stopped;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.provismet.dualswords.enchantment.component.EnchantmentStoppedUsingEffect;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;

public record ApplyToUserEffect (EnchantmentEntityEffect effect, LevelBasedValue minTicks) implements EnchantmentStoppedUsingEffect {
    public static final MapCodec<ApplyToUserEffect> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(EnchantmentEntityEffect.CODEC.fieldOf("effect").forGetter(ApplyToUserEffect::effect), LevelBasedValue.CODEC.fieldOf("minimum_charging_ticks").forGetter(ApplyToUserEffect::minTicks)).apply(instance, ApplyToUserEffect::new));

    @Override
    public void onStoppedUsing (ServerLevel world, int level, EnchantedItemInUse context, LivingEntity user, int remainingTicks) {
        if (context.itemStack().getUseDuration(user) - remainingTicks >= this.minTicks.calculate(level))
            this.effect.apply(world, level, context, user, user.position());
    }

    @Override
    public MapCodec<? extends EnchantmentStoppedUsingEffect> getCodec () {
        return CODEC;
    }
}
