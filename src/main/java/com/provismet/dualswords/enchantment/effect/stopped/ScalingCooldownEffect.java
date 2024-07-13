package com.provismet.dualswords.enchantment.effect.stopped;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.provismet.dualswords.enchantment.component.EnchantmentStoppedUsingEffect;
import com.provismet.dualswords.util.DSEnchantmentHelper;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.EnchantmentLevelBasedValue;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;

public record ScalingCooldownEffect (EnchantmentLevelBasedValue value) implements EnchantmentStoppedUsingEffect {
    public static final MapCodec<ScalingCooldownEffect> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(EnchantmentLevelBasedValue.CODEC.fieldOf("ticks").forGetter(ScalingCooldownEffect::value)).apply(instance, ScalingCooldownEffect::new));

    @Override
    public void onStoppedUsing (ServerWorld world, int level, EnchantmentEffectContext context, LivingEntity user, int remainingTicks) {
        if (!(user instanceof PlayerEntity player)) return;

        float usage = (float)remainingTicks / (float)context.stack().getMaxUseTime(user);
        int cooldown = (int)(this.value.getValue(level) * usage);
        cooldown = DSEnchantmentHelper.modifyEnchantedCooldown(world, player, cooldown);
        player.getItemCooldownManager().set(context.stack().getItem(), cooldown);
    }

    @Override
    public MapCodec<ScalingCooldownEffect> getCodec () {
        return CODEC;
    }
}
