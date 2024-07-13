package com.provismet.dualswords.util;

import com.provismet.CombatPlusCore.utility.CPCEnchantmentHelper;
import com.provismet.dualswords.registry.DSEnchantmentComponentTypes;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;

public abstract class DSEnchantmentHelper {
    public static int modifyEnchantedCooldown (ServerWorld world, PlayerEntity player, int base) {
        float cooldown = base;
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack item = player.getEquippedStack(slot);
            cooldown = CPCEnchantmentHelper.modifyValue(DSEnchantmentComponentTypes.MODIFY_COOLDOWN, world, item, player, cooldown);
        }
        return (int)cooldown;
    }
}
