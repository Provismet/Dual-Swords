package com.provismet.dualswords.mixin;

import com.provismet.dualswords.interfaceMixin.IMixinLivingEntityRenderState;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LivingEntityRenderState.class)
public abstract class LivingEntityRenderStateMixin extends EntityRenderState implements IMixinLivingEntityRenderState {
    @Unique
    private boolean usingItem;

    @Unique
    private ItemStack stack;

    @Override
    public void dual_Swords$setIsUsingItem (boolean value) {
        this.usingItem = value;
    }

    @Override
    public void dual_Swords$setActiveItem (ItemStack stack) {
        this.stack = stack;
    }

    @Override
    public boolean dual_Swords$isUsingItem () {
        return this.usingItem;
    }

    @Override
    public ItemStack dual_Swords$getActiveItem () {
        return this.stack;
    }
}
