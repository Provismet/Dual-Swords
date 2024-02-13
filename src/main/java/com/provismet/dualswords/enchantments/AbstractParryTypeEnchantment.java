package com.provismet.dualswords.enchantments;

import com.provismet.CombatPlusCore.enchantments.OffHandEnchantment;

import net.minecraft.enchantment.EnchantmentTarget;

public abstract class AbstractParryTypeEnchantment extends OffHandEnchantment {
    protected AbstractParryTypeEnchantment (Rarity weight) {
        super(weight);
    }
    
    protected AbstractParryTypeEnchantment (Rarity weight, EnchantmentTarget target) {
        super(weight, target);
    }

    @Override
    protected String getGroup () {
        return "dualswords:parry";
    }
}
