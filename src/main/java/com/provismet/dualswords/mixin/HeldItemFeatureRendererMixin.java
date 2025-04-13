package com.provismet.dualswords.mixin;

import com.provismet.dualswords.interfaceMixin.IMixinItemRenderState;
import com.provismet.dualswords.interfaceMixin.IMixinLivingEntityRenderState;
import net.minecraft.client.render.entity.model.ModelWithArms;
import net.minecraft.client.render.entity.state.ArmedEntityRenderState;
import net.minecraft.client.render.item.ItemRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Arm;
import net.minecraft.util.math.RotationAxis;

@Mixin(HeldItemFeatureRenderer.class)
public abstract class HeldItemFeatureRendererMixin<S extends ArmedEntityRenderState, M extends EntityModel<S> & ModelWithArms> extends FeatureRenderer<S, M> {
    protected HeldItemFeatureRendererMixin(FeatureRendererContext<S, M> context) {
        super(context);
    }

    @Inject(method="renderItem", at=@At(value="INVOKE", target="Lnet/minecraft/client/render/item/ItemRenderState;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II)V", shift=At.Shift.BEFORE))
    private void flipBlade (S entityState, ItemRenderState itemState, Arm arm, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo info) {
        boolean activeItem = ((IMixinLivingEntityRenderState)entityState).dual_Swords$isUsingItem() && ((IMixinItemRenderState)itemState).dual_Swords$isActive();

        if (((IMixinItemRenderState)itemState).dual_Swords$shouldReverseRender()) {
            matrices.translate(0f, -0.25f, 0.2f);
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180f));
            
            if (activeItem) {
                float armMultiplier = arm == Arm.RIGHT ? -1f : 1f;
                matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(armMultiplier * 45f));
            }
        }
        else if (activeItem && ((IMixinItemRenderState)itemState).dual_Swords$shouldFlipSpear()) {
            matrices.translate(0f, -0.25f, 0.2f);
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180f));
        }
    }
}
