package com.provismet.dualswords.util.registry;

import com.mojang.serialization.MapCodec;
import com.provismet.dualswords.DualSwordsMain;
import com.provismet.dualswords.enchantment.component.EnchantmentStoppedUsingEffect;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public abstract class DSRegistryKeys {
    public static final ResourceKey<Registry<MapCodec<? extends EnchantmentStoppedUsingEffect>>> STOPPED_USING_ENCHANTMENT_EFFECT = ResourceKey.createRegistryKey(DualSwordsMain.identifier("stopped_using_enchantment_effect"));
}
