package com.provismet.dualswords.registry;

import com.mojang.serialization.Codec;
import com.provismet.dualswords.DualSwordsMain;
import com.provismet.dualswords.enchantment.component.EnchantmentStoppedUsingEffect;
import java.util.List;
import java.util.function.UnaryOperator;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.enchantment.ConditionalEffect;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

public abstract class DSEnchantmentComponentTypes {
    public static final DataComponentType<String> USE_ACTION = register("use_action", builder -> builder.persistent(Codec.STRING));
    public static final DataComponentType<List<ConditionalEffect<EnchantmentValueEffect>>> DEFLECTION_SPEED = register("projectile_deflection_speed", builder -> builder.persistent(ConditionalEffect.codec(EnchantmentValueEffect.CODEC, LootContextParamSets.ENCHANTED_ENTITY).listOf()));

    public static final DataComponentType<List<ConditionalEffect<EnchantmentStoppedUsingEffect>>> ON_STOPPED_USING = register("on_stopped_using", builder -> builder.persistent(ConditionalEffect.codec(EnchantmentStoppedUsingEffect.CODEC, LootContextParamSets.ENCHANTED_ENTITY).listOf()));
    public static final DataComponentType<List<ConditionalEffect<EnchantmentEntityEffect>>> ON_FINISHED_USING = register("on_finished_using", builder -> builder.persistent(ConditionalEffect.codec(EnchantmentEntityEffect.CODEC, LootContextParamSets.ENCHANTED_ENTITY).listOf()));

    public static void init () {}

    private static <T> DataComponentType<T> register (String name, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return Registry.register(BuiltInRegistries.ENCHANTMENT_EFFECT_COMPONENT_TYPE, DualSwordsMain.identifier(name), (builderOperator.apply(DataComponentType.builder())).build());
    }
}
