package com.provismet.dualswords.util.registry;

import com.mojang.serialization.MapCodec;
import com.provismet.dualswords.enchantment.component.EnchantmentStoppedUsingEffect;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.registry.Registry;

public abstract class DSRegistries {
    public static final Registry<MapCodec<? extends EnchantmentStoppedUsingEffect>> ENCHANTMENT_STOPPED_USING_EFFECT_TYPE = FabricRegistryBuilder.createSimple(DSRegistryKeys.STOPPED_USING_ENCHANTMENT_EFFECT).buildAndRegister();

    public static void init () {}
}
