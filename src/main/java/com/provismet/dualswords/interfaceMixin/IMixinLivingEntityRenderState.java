package com.provismet.dualswords.interfaceMixin;

import net.minecraft.item.ItemStack;

public interface IMixinLivingEntityRenderState {
    void dual_Swords$setIsUsingItem (boolean value);
    void dual_Swords$setActiveItem (ItemStack stack);

    boolean dual_Swords$isUsingItem ();
    ItemStack dual_Swords$getActiveItem ();
}
