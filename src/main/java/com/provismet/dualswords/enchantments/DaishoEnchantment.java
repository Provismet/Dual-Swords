package com.provismet.dualswords.enchantments;

import net.minecraft.enchantment.Enchantment;

public class DaishoEnchantment extends OffhandEnchantment {
    public DaishoEnchantment () {
        super(Rarity.RARE);
    }
    
    @Override
    public boolean canAccept (Enchantment other) {
        return super.canAccept(other) && !(other instanceof RiposteEnchantment);

    }

    @Override
    public int getMaxLevel () {
        return 5;
    }

    @Override
    public boolean isAvailableForRandomSelection () {
        return false;
    }

    @Override
    public boolean isAvailableForEnchantedBookOffer () {
        return false;
    }

    @Override
    public boolean isTreasure () {
        return true;
    }

    @Override
    public float getOffhandDamage (int level) {
        return getOffhandDamageStatic(level);
    }

    public static float getOffhandDamageStatic (int level) {
        return 1.2f * level;
    }
}
