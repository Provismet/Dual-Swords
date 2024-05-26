package com.provismet.dualswords.enchantments;

import com.provismet.CombatPlusCore.utility.CPCEnchantmentHelper;

import com.provismet.CombatPlusCore.utility.CPCItemTags;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;

public class RiposteEnchantment extends AbstractParryTypeEnchantment {
    public RiposteEnchantment () {
        super(Enchantment.properties(
                CPCItemTags.DUAL_WEAPON,
                3,
                3,
                Enchantment.leveledCost(5, 5),
                Enchantment.leveledCost(35, 5),
                2,
                EquipmentSlot.OFFHAND
        ));
    }

    public float getDamage (int level) {
        return level * 1.5f;
    }
    
    @Override
    public boolean canAccept (Enchantment other) {
        return super.canAccept(other) &&
            !(other instanceof DaishoEnchantment) &&
            !(other instanceof DeflectEnchantment) &&
            !CPCEnchantmentHelper.isWeaponUtility(other);
    }

    @Override
    public boolean isTreasure () {
        return true;
    }
}
