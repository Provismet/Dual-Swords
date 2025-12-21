package com.provismet.dualswords.mixin;

import com.provismet.dualswords.config.DSSettings;
import com.provismet.dualswords.registry.DSEnchantments;
import com.provismet.dualswords.util.tag.DSEnchantmentTags;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemRenderer.class)
public abstract class HeldItemRendererMixin {
    @Shadow protected abstract void applyEquipOffset (MatrixStack matrices, Arm arm, float equipProgress);
    @Shadow protected abstract void swingArm (float swingProgress, MatrixStack matrices, int armX, Arm arm);

    @Shadow protected abstract void applySwingOffset (MatrixStack matrices, Arm arm, float swingProgress);

    @Unique
    private void applyParryOffset (MatrixStack matrices, Arm arm, float equipProgress) {
        float armMultiplier = arm == Arm.RIGHT ? -1f : 1f;
        matrices.translate(0.75f * armMultiplier, 0f, 0.1f);
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(55f * (1f - equipProgress)));
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(armMultiplier * 125f * (1f - equipProgress)));
    }

    @Unique
    private void applyEquipOffsetUpwards (MatrixStack matrices, Arm arm, float equipProgress) {
        float sideMultiplier = arm == Arm.RIGHT ? 1f : -1f;
        matrices.translate(sideMultiplier * 0.56F, -0.52F + equipProgress * 0.6F, -0.72F);
    }

    @Redirect(
        method = "renderFirstPersonItem",
        slice = @Slice(
            from = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getUseAction()Lnet/minecraft/item/consume/UseAction;"),
            to = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;isUsingRiptide()Z")
        ),
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/render/item/HeldItemRenderer;applyEquipOffset(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/Arm;F)V",
            ordinal = 0
        )
    )
    private void animateParry (HeldItemRenderer instance, MatrixStack matrices, Arm arm, float equipProgress, AbstractClientPlayerEntity player, float tickProgress, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress1, MatrixStack matrices1, OrderedRenderCommandQueue orderedRenderCommandQueue, int light) {
        if (EnchantmentHelper.hasAnyEnchantmentsIn(item, DSEnchantmentTags.REVERSE_RENDER) && DSSettings.renderFlippedSwords()) {
            this.applyEquipOffsetUpwards(matrices1, arm, equipProgress1);
            this.applyParryOffset(matrices1, arm, equipProgress1);
        }
        else {
            this.applyEquipOffset(matrices1, arm, equipProgress1);
        }
    }

    @Inject(
        method = "renderFirstPersonItem",
        slice = @Slice(from=@At(value="INVOKE", target="Lnet/minecraft/item/ItemStack;getUseAction()Lnet/minecraft/item/consume/UseAction;")),
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/render/item/HeldItemRenderer;applyEquipOffset(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/Arm;F)V",
            shift = At.Shift.AFTER,
            ordinal = 0
        )
    )
    private void animateLunge (AbstractClientPlayerEntity player, float tickProgress, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, OrderedRenderCommandQueue orderedRenderCommandQueue, int light, CallbackInfo ci) {
        if (EnchantmentHelper.getLevel(DSEnchantments.LUNGE.getEntryOrThrow(player.getEntityWorld().getRegistryManager()), item) > 0) {
            matrices.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(75f * (1 - equipProgress)));
            matrices.translate(0f, -0.75f, 0.75f);
        }
    }

    @Redirect(
        method = "renderFirstPersonItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/render/item/HeldItemRenderer;applyEquipOffset(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/Arm;F)V",
            ordinal = 4
        )
    )
    private void applyUpwardsEquip (HeldItemRenderer instance, MatrixStack matrices, Arm arm, float equipProgress, AbstractClientPlayerEntity player, float tickProgress, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress1, MatrixStack matrices1, OrderedRenderCommandQueue orderedRenderCommandQueue, int light) {
        if (EnchantmentHelper.hasAnyEnchantmentsIn(item, DSEnchantmentTags.REVERSE_RENDER) && DSSettings.renderFlippedSwords() && swingProgress == 0) {
            this.applyEquipOffsetUpwards(matrices1, arm, equipProgress);
        }
        else this.applyEquipOffset(matrices, arm, equipProgress);
    }

    @Inject(method="renderFirstPersonItem", at=@At(value="INVOKE", target="Lnet/minecraft/client/util/math/MatrixStack;push()V", shift=At.Shift.AFTER))
    private void flipBlade (AbstractClientPlayerEntity player, float tickProgress, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, OrderedRenderCommandQueue orderedRenderCommandQueue, int light, CallbackInfo ci) {
        if (EnchantmentHelper.hasAnyEnchantmentsIn(item, DSEnchantmentTags.REVERSE_RENDER) && DSSettings.renderFlippedSwords() && swingProgress == 0) {
            Arm arm = hand == Hand.MAIN_HAND ? player.getMainArm() : player.getMainArm().getOpposite();
            float sideMultiplier = arm == Arm.RIGHT ? -1f : 1f;
            matrices.translate(-0.15f * sideMultiplier, -0.9f, -1.5f);
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180f));
        }
    }
}
