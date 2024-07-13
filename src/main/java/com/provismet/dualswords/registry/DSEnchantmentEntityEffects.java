package com.provismet.dualswords.registry;

import com.mojang.serialization.MapCodec;
import com.provismet.dualswords.DualSwordsMain;
import com.provismet.dualswords.enchantment.effect.entity.CooldownEffect;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public abstract class DSEnchantmentEntityEffects {
    public static void register () {
        register("modify_cooldown", CooldownEffect.CODEC);
    }

    private static void register (String name, MapCodec<? extends EnchantmentEntityEffect> codec) {
        Registry.register(Registries.ENCHANTMENT_ENTITY_EFFECT_TYPE, DualSwordsMain.identifier(name), codec);
    }
}
