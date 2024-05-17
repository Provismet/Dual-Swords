package com.provismet.dualswords.enchantments;

import com.provismet.CombatPlusCore.utility.CPCEnchantmentHelper;
import com.provismet.CombatPlusCore.utility.CPCItemTags;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;

public class DeflectEnchantment extends AbstractParryTypeEnchantment {
    public DeflectEnchantment () {
        super(Enchantment.properties(
                CPCItemTags.DUAL_WEAPON,
                3,
                3,
                Enchantment.leveledCost(5, 5),
                Enchantment.leveledCost(25, 5),
                2,
                EquipmentSlot.OFFHAND
        ));
    }
    
    @Override
    public boolean canAccept (Enchantment other) {
        return super.canAccept(other) &&
            !(other instanceof DaishoEnchantment) &&
            !(other instanceof RiposteEnchantment) &&
            !CPCEnchantmentHelper.isWeaponUtility(other);
    }
}
