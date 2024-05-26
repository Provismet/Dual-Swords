package com.provismet.dualswords.enchantments;

import com.provismet.CombatPlusCore.utility.CPCEnchantmentHelper;

import com.provismet.CombatPlusCore.utility.CPCItemTags;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;

public class ForcefulEnchantment extends AbstractLungeTypeEnchantment {
    public ForcefulEnchantment () {
        super(Enchantment.properties(
                CPCItemTags.DUAL_WEAPON,
                3,
                2,
                Enchantment.leveledCost(5, 10),
                Enchantment.leveledCost(20, 10),
                4,
                EquipmentSlot.OFFHAND
        ));
    }

    @Override
    public boolean canAccept (Enchantment other) {
        return super.canAccept(other) &&
            !(other instanceof ThrustingEnchantment) &&
            !CPCEnchantmentHelper.isWeaponUtility(other);
    }

    @Override
    public boolean isTreasure () {
        return true;
    }
}
