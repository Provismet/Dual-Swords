package com.provismet.dualswords.interfaceMixin;

import net.minecraft.item.ItemStack;

public interface IMixinLivingEntity {
    public void setLungeTicks (ItemStack stack, int ticks);
}
