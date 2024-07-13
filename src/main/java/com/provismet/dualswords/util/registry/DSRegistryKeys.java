package com.provismet.dualswords.util.registry;

import com.mojang.serialization.MapCodec;
import com.provismet.dualswords.DualSwordsMain;
import com.provismet.dualswords.enchantment.component.EnchantmentStoppedUsingEffect;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

public abstract class DSRegistryKeys {
    public static final RegistryKey<Registry<MapCodec<? extends EnchantmentStoppedUsingEffect>>> STOPPED_USING_ENCHANTMENT_EFFECT = RegistryKey.ofRegistry(DualSwordsMain.identifier("stopped_using_enchantment_effect"));
}
