package com.provismet.dualswords.mixin;

import com.provismet.dualswords.interfaceMixin.IMixinItemRenderState;
import com.provismet.dualswords.interfaceMixin.IMixinLivingEntityRenderState;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.ModelWithArms;
import net.minecraft.client.render.entity.state.ArmedEntityRenderState;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Arm;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemFeatureRenderer.class)
public abstract class HeldItemFeatureRendererMixin<S extends ArmedEntityRenderState, M extends EntityModel<S> & ModelWithArms> extends FeatureRenderer<S, M> {
    protected HeldItemFeatureRendererMixin(FeatureRendererContext<S, M> context) {
        super(context);
    }

    @Inject(method="renderItem", at=@At(value="INVOKE", target="Lnet/minecraft/client/render/item/ItemRenderState;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;III)V", shift=At.Shift.BEFORE))
    private void flipBlade (S entityState, ItemRenderState itemRenderState, Arm arm, MatrixStack matrices, OrderedRenderCommandQueue orderedRenderCommandQueue, int light, CallbackInfo ci) {
        boolean activeItem = ((IMixinLivingEntityRenderState)entityState).dual_Swords$isUsingItem() && ((IMixinItemRenderState)itemRenderState).dual_Swords$isActive();

        if (((IMixinItemRenderState)itemRenderState).dual_Swords$shouldReverseRender()) {
            matrices.translate(0f, -0.25f, 0.2f);
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180f));
            
            if (activeItem) {
                float armMultiplier = arm == Arm.RIGHT ? -1f : 1f;
                matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(armMultiplier * 45f));
            }
        }
        else if (activeItem && ((IMixinItemRenderState)itemRenderState).dual_Swords$shouldFlipSpear()) {
            matrices.translate(0f, -0.25f, 0.2f);
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180f));
        }
    }
}
