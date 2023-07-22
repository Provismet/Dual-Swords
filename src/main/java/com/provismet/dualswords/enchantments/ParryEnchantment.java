package com.provismet.dualswords.enchantments;

import net.minecraft.enchantment.DamageEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;

public class ParryEnchantment extends Enchantment {
    public ParryEnchantment () {
        super(Rarity.UNCOMMON, EnchantmentTarget.WEAPON, new EquipmentSlot[] {EquipmentSlot.OFFHAND});
    }

    @Override
    public boolean canAccept (Enchantment other) {
        return super.canAccept(other) && !(other instanceof DamageEnchantment);
    }
}
