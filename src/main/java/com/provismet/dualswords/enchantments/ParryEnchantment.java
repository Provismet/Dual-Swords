package com.provismet.dualswords.enchantments;

import com.provismet.CombatPlusCore.utility.CPCItemTags;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;

public class ParryEnchantment extends AbstractParryTypeEnchantment {
    public ParryEnchantment () {
        super(Enchantment.properties(
            CPCItemTags.OFFHAND_ENCHANTABLE,
            CPCItemTags.OFFHAND_PRIMARY_ENCHANTABLE,
            5,
            3,
            Enchantment.leveledCost(5, 5),
            Enchantment.leveledCost(45, 5),
            2,
            EquipmentSlot.OFFHAND
        ));
    }
}
