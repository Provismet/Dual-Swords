package com.provismet.dualswords.enchantments;

import com.provismet.CombatPlusCore.enchantments.OffHandEnchantment;

import net.minecraft.enchantment.EnchantmentTarget;

public abstract class AbstractLungeTypeEnchantment extends OffHandEnchantment {
    protected AbstractLungeTypeEnchantment (Rarity weight) {
        super(weight);
    }

    protected AbstractLungeTypeEnchantment (Rarity weight, EnchantmentTarget target) {
        super(weight, target);
    }

    @Override
    protected String getGroup () {
        return "dualswords:lunge";
    }
}
