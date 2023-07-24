package com.provismet.dualswords.enchantments;

import net.minecraft.enchantment.DamageEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.FireAspectEnchantment;
import net.minecraft.enchantment.LuckEnchantment;
import net.minecraft.enchantment.SweepingEnchantment;
import net.minecraft.entity.EquipmentSlot;

public abstract class OffhandEnchantment extends Enchantment {
    protected OffhandEnchantment(Rarity weight) {
        super(weight, EnchantmentTarget.WEAPON, new EquipmentSlot[] {EquipmentSlot.OFFHAND});
    }
    
    @Override
    public boolean canAccept (Enchantment other) {
        return super.canAccept(other) &&
            !(other instanceof DamageEnchantment) &&
            !(other instanceof FireAspectEnchantment) &&
            !(other instanceof SweepingEnchantment) &&
            !(other instanceof LuckEnchantment);
    }
}
