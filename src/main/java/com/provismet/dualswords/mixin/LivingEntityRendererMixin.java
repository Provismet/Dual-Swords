package com.provismet.dualswords.mixin;

import com.provismet.dualswords.interfaceMixin.IMixinLivingEntityRenderState;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin <T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<? super S>> extends EntityRenderer<T,S> {
    protected LivingEntityRendererMixin (EntityRendererFactory.Context context) {
        super(context);
    }

    @Inject(method="updateRenderState(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;F)V", at=@At("TAIL"))
    private void addHeldItems (T livingEntity, S state, float f, CallbackInfo info) {
        ((IMixinLivingEntityRenderState)state).dual_Swords$setIsUsingItem(livingEntity.isUsingItem());
    }
}
