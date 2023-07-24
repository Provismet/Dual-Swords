package com.provismet.dualswords.enchantments;

import net.minecraft.enchantment.DamageEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;

public class DaishoEnchantment extends Enchantment {
    public DaishoEnchantment () {
        super(Rarity.UNCOMMON, EnchantmentTarget.WEAPON, new EquipmentSlot[] {EquipmentSlot.OFFHAND});
    }
    
    @Override
    public boolean canAccept (Enchantment other) {
        return super.canAccept(other) && !(other instanceof DamageEnchantment) && !(other instanceof RiposteEnchantment);
    }

    @Override
    public int getMaxLevel () {
        return 5;
    }

    public static float getOffhandDamage (int level) {
        return 1.5f * level;
    }
}
