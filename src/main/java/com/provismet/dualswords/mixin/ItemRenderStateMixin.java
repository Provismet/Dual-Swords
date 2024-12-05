package com.provismet.dualswords.mixin;

import com.provismet.dualswords.interfaceMixin.IMixinItemRenderState;
import net.minecraft.client.render.item.ItemRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ItemRenderState.class)
public abstract class ItemRenderStateMixin implements IMixinItemRenderState {
    @Unique private boolean shouldReverseRender;
    @Unique private boolean shouldFlipSpear;
    @Unique private boolean active;

    @Override
    public void dual_Swords$setReverseRender (boolean value) {
        this.shouldReverseRender = value;
    }

    @Override
    public void dual_Swords$setFlippedSpear (boolean value) {
        this.shouldFlipSpear = value;
    }

    @Override
    public void dual_Swords$setIsActive (boolean value) {
        this.active = value;
    }

    @Override
    public boolean dual_Swords$shouldReverseRender () {
        return this.shouldReverseRender;
    }

    @Override
    public boolean dual_Swords$shouldFlipSpear () {
        return this.shouldFlipSpear;
    }

    @Override
    public boolean dual_Swords$isActive () {
        return this.active;
    }
}
