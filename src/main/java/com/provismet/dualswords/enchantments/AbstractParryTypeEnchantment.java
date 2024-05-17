package com.provismet.dualswords.enchantments;

import com.provismet.CombatPlusCore.enchantments.OffHandEnchantment;

public abstract class AbstractParryTypeEnchantment extends OffHandEnchantment {
    protected AbstractParryTypeEnchantment (Properties properties) {
        super(properties);
    }

    @Override
    protected String getGroup () {
        return "dualswords:parry";
    }
}
