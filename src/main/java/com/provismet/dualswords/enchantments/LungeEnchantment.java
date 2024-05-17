package com.provismet.dualswords.enchantments;

import com.provismet.CombatPlusCore.utility.CPCItemTags;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;

public class LungeEnchantment extends AbstractLungeTypeEnchantment {
    public LungeEnchantment () {
        super(Enchantment.properties(
                CPCItemTags.DUAL_WEAPON,
                5,
                3,
                Enchantment.leveledCost(5, 5),
                Enchantment.leveledCost(45, 5),
                2,
                EquipmentSlot.OFFHAND
        ));
    }
}
