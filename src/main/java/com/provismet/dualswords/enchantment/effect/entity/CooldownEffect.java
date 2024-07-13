package com.provismet.dualswords.enchantment.effect.entity;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.provismet.dualswords.util.DSEnchantmentHelper;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.EnchantmentLevelBasedValue;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

public record CooldownEffect (EnchantmentLevelBasedValue value) implements EnchantmentEntityEffect {
    public static final MapCodec<CooldownEffect> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(EnchantmentLevelBasedValue.CODEC.fieldOf("value").forGetter(CooldownEffect::value)).apply(instance, CooldownEffect::new));

    @Override
    public void apply (ServerWorld world, int level, EnchantmentEffectContext context, Entity user, Vec3d pos) {
        if (!(user instanceof PlayerEntity player)) return;

        int cooldown = DSEnchantmentHelper.modifyEnchantedCooldown(world, player, (int)this.value.getValue(level));
        player.getItemCooldownManager().set(context.stack().getItem(), cooldown);
    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> getCodec () {
        return CODEC;
    }
}
