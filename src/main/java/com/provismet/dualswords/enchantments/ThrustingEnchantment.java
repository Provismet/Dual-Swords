package com.provismet.dualswords.enchantments;

import com.provismet.CombatPlusCore.utility.CPCEnchantmentHelper;

import com.provismet.CombatPlusCore.utility.CPCItemTags;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;

public class ThrustingEnchantment extends AbstractLungeTypeEnchantment {
    public ThrustingEnchantment () {
        super(Enchantment.properties(
            CPCItemTags.OFFHAND_ENCHANTABLE,
            CPCItemTags.OFFHAND_PRIMARY_ENCHANTABLE,
            3,
            2,
            Enchantment.leveledCost(5, 10),
            Enchantment.leveledCost(50, 10),
            2,
            EquipmentSlot.OFFHAND
        ));
    }

    @Override
    public boolean canAccept (Enchantment other) {
        return super.canAccept(other) &&
            !(other instanceof ForcefulEnchantment) &&
            !CPCEnchantmentHelper.isWeaponUtility(other);
    }

    @Override
    public boolean isTreasure () {
        return true;
    }
}
