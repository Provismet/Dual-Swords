package com.provismet.dualswords.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.provismet.dualswords.registry.DSEnchantments;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.RotationAxis;

@Mixin(HeldItemRenderer.class)
public abstract class HeldItemRendererMixin {
    @Shadow
    private void applyEquipOffset(MatrixStack matrices, Arm arm, float equipProgress) {}

    @Unique
    private void applyParryOffset (MatrixStack matrices, float tickDelta, Arm arm, ItemStack stack, float equipProgress) {
        float armMultiplier = arm == Arm.RIGHT ? -1f : 1f;
        matrices.translate(0.5f * armMultiplier, 0f, 0.25f);
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(armMultiplier * 45 * (1f - equipProgress)));
    }

    @Unique
    private void applyEquipOffsetUpwards (MatrixStack matrices, Arm arm, float equipProgress) {
        float sideMultiplier = arm == Arm.RIGHT ? 1f : -1f;
        matrices.translate(sideMultiplier * 0.56F, -0.52F + equipProgress * 0.6F, -0.72F);
    }

    @Redirect(method="renderFirstPersonItem",
        slice=@Slice(from=@At(value="INVOKE", target="Lnet/minecraft/item/ItemStack;getUseAction()Lnet/minecraft/util/UseAction;")),
        at=@At(value="INVOKE",
            target="Lnet/minecraft/client/render/item/HeldItemRenderer;applyEquipOffset(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/Arm;F)V",
            ordinal=2))
    private void animateParry (HeldItemRenderer thisRenderer, MatrixStack matrices1, Arm arm, float equipProgress1, AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        if (EnchantmentHelper.getLevel(DSEnchantments.PARRY, item) > 0) {
            applyEquipOffsetUpwards(matrices1, arm, equipProgress1);
            applyParryOffset(matrices1, tickDelta, arm, item, equipProgress1);
        }
        else {
            this.applyEquipOffset(matrices1, arm, equipProgress1);
        }
    }

    @Inject(method="renderFirstPersonItem",
        slice=@Slice(from=@At(value="INVOKE", target="Lnet/minecraft/item/ItemStack;getUseAction()Lnet/minecraft/util/UseAction;")),
        at=@At(value="INVOKE",
            target="Lnet/minecraft/client/render/item/HeldItemRenderer;applyEquipOffset(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/Arm;F)V",
            ordinal=4,
            shift=At.Shift.AFTER))
    private void animateLunge (AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo info) {
        if (EnchantmentHelper.getLevel(DSEnchantments.LUNGE, item) > 0) {
            matrices.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(75f * (1 - equipProgress)));
            matrices.translate(0f, -0.75f, 0.75f);
        }
    }

    @Redirect(method="renderFirstPersonItem",
        at=@At(value="INVOKE",
            target="Lnet/minecraft/client/render/item/HeldItemRenderer;applyEquipOffset(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/Arm;F)V",
            ordinal=8))
    private void applyUpwardsEquip (HeldItemRenderer thisRenderer, MatrixStack matrices1, Arm arm, float equipProgress1, AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        if (EnchantmentHelper.getLevel(DSEnchantments.PARRY, item) > 0 && swingProgress == 0) applyEquipOffsetUpwards(matrices1, arm, equipProgress1);
        else this.applyEquipOffset(matrices1, arm, equipProgress1);
    }

    @Inject(method="renderFirstPersonItem", at=@At(value="INVOKE", target="Lnet/minecraft/client/util/math/MatrixStack;push()V", shift=At.Shift.AFTER))
    private void flipBlade (AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo info) {
        if (EnchantmentHelper.getLevel(DSEnchantments.PARRY, item) > 0 && swingProgress == 0) {
            Arm arm = hand == Hand.MAIN_HAND ? player.getMainArm() : player.getMainArm().getOpposite();
            float sideMultiplier = arm == Arm.RIGHT ? -1f : 1f;
            matrices.translate(0.1f * sideMultiplier, -0.8f, -1.25f);
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180f));
        }
    }
}
