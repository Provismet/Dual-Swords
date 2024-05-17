package com.provismet.dualswords.enchantments;

import com.provismet.CombatPlusCore.enchantments.OffHandEnchantment;
import com.provismet.CombatPlusCore.utility.CPCItemTags;
import com.provismet.CombatPlusCore.utility.WeaponTypes;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;

public class DaishoEnchantment extends OffHandEnchantment {
    public DaishoEnchantment () {
        super(Enchantment.properties(
                CPCItemTags.DUAL_WEAPON,
                2,
                5,
                Enchantment.leveledCost(20, 5),
                Enchantment.leveledCost(30, 10),
                8,
                EquipmentSlot.OFFHAND
        ));
    }

    @Override
    public float getAttackDamage (int level, EquipmentSlot slot, LivingEntity user, LivingEntity target) {
        if (slot == EquipmentSlot.OFFHAND && WeaponTypes.isDualWeapon(user.getMainHandStack()) && WeaponTypes.isDualWeapon(user.getOffHandStack())) {
            return 0.8f * level;
        }
        return 0f;
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
    public boolean canAccept (Enchantment other) {
        return super.canAccept(other) &&
            !(other instanceof RiposteEnchantment) &&
            !(other instanceof ThrustingEnchantment);
    }

    @Override
    protected String getGroup () {
        return null;
    }
}
