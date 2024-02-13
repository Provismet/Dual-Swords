package com.provismet.dualswords.enchantments;

import com.provismet.CombatPlusCore.utility.CPCEnchantmentHelper;

import net.minecraft.enchantment.Enchantment;

public class ThrustingEnchantment extends AbstractLungeTypeEnchantment {
    public ThrustingEnchantment () {
        super(Rarity.RARE);
    }

    @Override
    public int getMaxLevel () {
        return 2;
    }

    @Override
    public int getMinPower (int level) {
        return level * 10;
    }

    @Override
    public int getMaxPower (int level) {
        return this.getMinPower(level) + 50;
    }

    @Override
    public boolean canAccept (Enchantment other) {
        return super.canAccept(other) &&
            !(other instanceof ForcefulEnchantment) &&
            !CPCEnchantmentHelper.isWeaponUtility(other);
    }
}
