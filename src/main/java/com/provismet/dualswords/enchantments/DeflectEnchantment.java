package com.provismet.dualswords.enchantments;

import com.provismet.CombatPlusCore.utility.CPCEnchantmentHelper;

import net.minecraft.enchantment.Enchantment;

public class DeflectEnchantment extends AbstractParryTypeEnchantment {
    public DeflectEnchantment () {
        super(Rarity.UNCOMMON);
    }
    
    @Override
    public int getMaxLevel () {
        return 3;
    }
    
    @Override
    public boolean canAccept (Enchantment other) {
        return super.canAccept(other) &&
            !(other instanceof DaishoEnchantment) &&
            !(other instanceof RiposteEnchantment) &&
            !CPCEnchantmentHelper.isWeaponUtility(other);
    }

    @Override
    public int getMinPower (int level) {
        return 5 + level * 5;
    }

    @Override
    public int getMaxPower(int level) {
        return super.getMinPower(level) + 30;
    }
}
