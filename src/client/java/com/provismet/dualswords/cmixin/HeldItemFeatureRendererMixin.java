package com.provismet.dualswords.cmixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.provismet.dualswords.interfaceMixin.IMixinItemRenderState;
import com.provismet.dualswords.interfaceMixin.IMixinLivingEntityRenderState;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandLayer.class)
public abstract class HeldItemFeatureRendererMixin<S extends ArmedEntityRenderState, M extends EntityModel<S> & ArmedModel> extends RenderLayer<S, M> {
    protected HeldItemFeatureRendererMixin(RenderLayerParent<S, M> context) {
        super(context);
    }

    @Inject(method="submitArmWithItem", at=@At(value="INVOKE", target="Lnet/minecraft/client/renderer/item/ItemStackRenderState;submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;III)V", shift=At.Shift.BEFORE))
    private void flipBlade (S entityState, ItemStackRenderState itemRenderState, ItemStack stack, HumanoidArm arm, PoseStack matrices, SubmitNodeCollector queue, int light, CallbackInfo ci) {
        boolean activeItem = ((IMixinLivingEntityRenderState)entityState).dual_Swords$isUsingItem() && ((IMixinItemRenderState)itemRenderState).dual_Swords$isActive();

        if (((IMixinItemRenderState)itemRenderState).dual_Swords$shouldReverseRender()) {
            matrices.translate(0f, -0.25f, 0.2f);
            matrices.rotate(Axis.XP.rotationDegrees(180f));
            
            if (activeItem) {
                float armMultiplier = arm == HumanoidArm.RIGHT ? -1f : 1f;
                matrices.rotate(Axis.ZP.rotationDegrees(armMultiplier * 45f));
            }
        }
        else if (activeItem && ((IMixinItemRenderState)itemRenderState).dual_Swords$shouldFlipSpear()) {
            matrices.translate(0f, -0.25f, 0.2f);
            matrices.rotate(Axis.XP.rotationDegrees(180f));
        }
    }
}
