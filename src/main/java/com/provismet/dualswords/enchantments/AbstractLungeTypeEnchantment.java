package com.provismet.dualswords.enchantments;

import com.provismet.CombatPlusCore.enchantments.OffHandEnchantment;

public abstract class AbstractLungeTypeEnchantment extends OffHandEnchantment {
    protected AbstractLungeTypeEnchantment (Properties properties) {
        super(properties);
    }

    @Override
    protected String getGroup () {
        return "dualswords:lunge";
    }
}
