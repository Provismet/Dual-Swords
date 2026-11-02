package com.provismet.dualswords.interfaceMixin;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

public interface IMixinLivingEntity {
    void dual_Swords$setLungeTicks (ItemStack stack, EquipmentSlot slot, int ticks);
}
