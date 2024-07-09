package com.provismet.dualswords.registry;

import com.mojang.serialization.Codec;
import com.provismet.dualswords.DualSwordsMain;
import net.minecraft.component.ComponentType;
import net.minecraft.enchantment.effect.EnchantmentEffectEntry;
import net.minecraft.enchantment.effect.EnchantmentValueEffect;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import java.util.List;
import java.util.function.UnaryOperator;

public abstract class DSEnchantmentComponentTypes {
    public static final ComponentType<String> USE_ACTION = register("use_action", builder -> builder.codec(Codec.STRING));
    public static final ComponentType<EnchantmentValueEffect> USE_ACTION_DURATION = register("action_duration", builder -> builder.codec(EnchantmentValueEffect.CODEC));
    public static final ComponentType<List<EnchantmentEffectEntry<EnchantmentValueEffect>>> COOLDOWN = register("cooldown", builder -> builder.codec(EnchantmentEffectEntry.createCodec(EnchantmentValueEffect.CODEC, LootContextTypes.ENCHANTED_ENTITY).listOf()));
    public static final ComponentType<List<EnchantmentEffectEntry<EnchantmentValueEffect>>> DEFLECTION_SPEED = register("projectile_deflection_speed", builder -> builder.codec(EnchantmentEffectEntry.createCodec(EnchantmentValueEffect.CODEC, LootContextTypes.ENCHANTED_ENTITY).listOf()));
    public static final ComponentType<List<EnchantmentEffectEntry<EnchantmentValueEffect>>> LUNGE_POWER = register("lunge_power", builder -> builder.codec(EnchantmentEffectEntry.createCodec(EnchantmentValueEffect.CODEC, LootContextTypes.ENCHANTED_ENTITY).listOf()));

    public static void init () {}

    private static <T> ComponentType<T> register (String name, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.ENCHANTMENT_EFFECT_COMPONENT_TYPE, DualSwordsMain.identifier(name), (builderOperator.apply(ComponentType.builder())).build());
    }
}
