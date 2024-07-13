package com.provismet.dualswords.interfaceMixin;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;

public interface IMixinLivingEntity {
    public void setLungeTicks (ItemStack stack, EquipmentSlot slot, int ticks);
}
