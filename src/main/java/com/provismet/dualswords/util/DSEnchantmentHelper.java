package com.provismet.dualswords.util;

import com.mojang.datafixers.util.Pair;
import com.provismet.dualswords.registry.DSEnchantmentComponentTypes;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;

import java.util.Objects;

public abstract class DSEnchantmentHelper {
    public static int getUseActionLevel (ItemStack itemStack, String useAction) {
        Pair<String, Integer> enchantment = EnchantmentHelper.getEffectListAndLevel(itemStack, DSEnchantmentComponentTypes.USE_ACTION);
        if (enchantment == null) return 0;
        else if (!Objects.equals(enchantment.getFirst(), useAction)) return 0;
        return enchantment.getSecond();
    }
}
