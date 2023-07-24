package com.provismet.dualswords.enchantments;

import net.minecraft.enchantment.Enchantment;

public class RiposteEnchantment extends OffhandEnchantment {
    public RiposteEnchantment () {
        super(Rarity.UNCOMMON);
    }

    @Override
    public int getMaxLevel () {
        return 3;
    }
    
    @Override
    public boolean canAccept (Enchantment other) {
        return super.canAccept(other) && !(other instanceof DaishoEnchantment);
    }

    public float getDamage (int level) {
        if (level == 0) return 0;
        return level;
    }
}
