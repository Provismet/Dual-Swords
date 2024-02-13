package com.provismet.dualswords.enchantments;

public class LungeEnchantment extends AbstractLungeTypeEnchantment {
    public LungeEnchantment () {
        super(Rarity.RARE);
    }

    @Override
    public int getMaxLevel () {
        return 3;
    }

    @Override
    public int getMinPower(int level) {
        return 15 + 5 * (level - 1);
    }

    @Override
    public int getMaxPower (int level) {
        return this.getMinPower(level) + 50;
    }
}
