package com.provismet.dualswords.enchantments;

import net.minecraft.enchantment.DamageEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;

public class RiposteEnchantment extends Enchantment {
    public RiposteEnchantment () {
        super(Rarity.UNCOMMON, EnchantmentTarget.WEAPON, new EquipmentSlot[] {EquipmentSlot.OFFHAND});
    }

    @Override
    public int getMaxLevel () {
        return 3;
    }
    
    @Override
    public boolean canAccept (Enchantment other) {
        return super.canAccept(other) && !(other instanceof DamageEnchantment);
    }

    public float getDamage (int level) {
        if (level == 0) return 0;
        return level;
    }
}
