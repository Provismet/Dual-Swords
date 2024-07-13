package com.provismet.dualswords.registry;

import com.mojang.serialization.MapCodec;
import com.provismet.dualswords.DualSwordsMain;
import com.provismet.dualswords.enchantment.component.EnchantmentStoppedUsingEffect;
import com.provismet.dualswords.enchantment.effect.stopped.ReverseScalingCooldownEffect;
import com.provismet.dualswords.enchantment.effect.stopped.ScalingCooldownEffect;
import com.provismet.dualswords.enchantment.effect.stopped.ApplyToUserEffect;
import com.provismet.dualswords.util.registry.DSRegistries;
import net.minecraft.registry.Registry;

public abstract class OnStoppedUsingEffects {
    public static void register () {
        register("apply_to_user", ApplyToUserEffect.CODEC);
        register("scaling_cooldown", ScalingCooldownEffect.CODEC);
        register("inverted_scaling_cooldown", ReverseScalingCooldownEffect.CODEC);
    }

    private static void register (String name, MapCodec<? extends EnchantmentStoppedUsingEffect> codec) {
        Registry.register(DSRegistries.ENCHANTMENT_STOPPED_USING_EFFECT_TYPE, DualSwordsMain.identifier(name), codec);
    }
}
