package com.provismet.dualswords.registry;

import com.mojang.serialization.Codec;
import com.provismet.dualswords.DualSwordsMain;
import com.provismet.dualswords.enchantment.component.EnchantmentStoppedUsingEffect;
import java.util.List;
import java.util.function.UnaryOperator;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.context.ContextKeySet;
import net.minecraft.world.item.enchantment.ConditionalEffect;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect;
import net.minecraft.world.level.storage.loot.Validatable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

public interface DSEnchantmentComponentTypes {
    DataComponentType<String> USE_ACTION = register("use_action", builder -> builder.persistent(Codec.STRING));
    DataComponentType<List<ConditionalEffect<EnchantmentValueEffect>>> DEFLECTION_SPEED = register("projectile_deflection_speed", builder -> builder.persistent(conditionalCodec(EnchantmentValueEffect.CODEC, LootContextParamSets.ENCHANTED_ENTITY)));
    DataComponentType<List<ConditionalEffect<EnchantmentStoppedUsingEffect>>> ON_STOPPED_USING = register("on_stopped_using", builder -> builder.persistent(conditionalCodec(EnchantmentStoppedUsingEffect.CODEC, LootContextParamSets.ENCHANTED_ENTITY)));
    DataComponentType<List<ConditionalEffect<EnchantmentEntityEffect>>> ON_FINISHED_USING = register("on_finished_using", builder -> builder.persistent(conditionalCodec(EnchantmentEntityEffect.CODEC, LootContextParamSets.ENCHANTED_ENTITY)));

    static void init () {}

    private static <T> DataComponentType<T> register (String name, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return Registry.register(BuiltInRegistries.ENCHANTMENT_EFFECT_COMPONENT_TYPE, DualSwordsMain.identifier(name), (builderOperator.apply(DataComponentType.builder())).build());
    }

    private static <T extends Validatable> Codec<List<T>> validatedListCodec (Codec<T> elementCodec, ContextKeySet paramSet) {
        return elementCodec.listOf().validate(Validatable.listValidatorForContext(paramSet));
    }

    private static <T> Codec<List<ConditionalEffect<T>>> conditionalCodec (Codec<T> codec, ContextKeySet params) {
        return validatedListCodec(ConditionalEffect.codec(codec), params);
    }
}
