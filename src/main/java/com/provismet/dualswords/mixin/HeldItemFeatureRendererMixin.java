package com.provismet.dualswords.mixin;

import com.provismet.dualswords.interfaceMixin.IMixinLivingEntityRenderState;
import com.provismet.dualswords.util.tag.DSEnchantmentTags;
import net.minecraft.client.render.entity.model.ModelWithArms;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.item.ModelTransformationMode;
import net.minecraft.item.consume.UseAction;
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
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.math.RotationAxis;

@Mixin(HeldItemFeatureRenderer.class)
public abstract class HeldItemFeatureRendererMixin<S extends LivingEntityRenderState, M extends EntityModel<S> & ModelWithArms> extends FeatureRenderer<S, M> {
    protected HeldItemFeatureRendererMixin(FeatureRendererContext<S, M> context) {
        super(context);
    }

    @Inject(method="renderItem", at=@At(value="INVOKE", target="Lnet/minecraft/client/render/item/ItemRenderer;renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V", shift=At.Shift.BEFORE))
    private void flipBlade (S state, BakedModel model, ItemStack stack, ModelTransformationMode modelTransformation, Arm arm, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo info) {
        if (EnchantmentHelper.hasAnyEnchantmentsIn(stack, DSEnchantmentTags.REVERSE_RENDER)) {
            matrices.translate(0f, -0.25f, 0.2f);
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180f));
            
            if (((IMixinLivingEntityRenderState)state).dual_Swords$isUsingItem() && ItemStack.areEqual(((IMixinLivingEntityRenderState)state).dual_Swords$getActiveItem(), stack)) {
                float armMultiplier = arm == Arm.RIGHT ? -1f : 1f;
                matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(armMultiplier * 45f));
            }
        }
        else if (((IMixinLivingEntityRenderState)state).dual_Swords$isUsingItem() && ItemStack.areEqual(((IMixinLivingEntityRenderState)state).dual_Swords$getActiveItem(), stack) && stack.getUseAction() == UseAction.SPEAR && EnchantmentHelper.hasAnyEnchantmentsIn(stack, DSEnchantmentTags.FLIPPED_SPEAR)) {
            matrices.translate(0f, -0.25f, 0.2f);
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180f));
        }
    }
}
