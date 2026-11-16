package com.provismet.dualswords.cmixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.provismet.dualswords.config.DSSettings;
import com.provismet.dualswords.registry.DSEnchantments;
import com.provismet.dualswords.util.tag.DSEnchantmentTags;
import net.minecraft.client.renderer.FirstPersonHandsAndItemsRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.state.level.FirstPersonHandsAndItemsRenderState;
import net.minecraft.client.renderer.state.level.PlayerRenderState;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FirstPersonHandsAndItemsRenderer.class)
public abstract class HeldItemRendererMixin {
    @Shadow protected abstract void applyItemArmTransform (PoseStack matrices, HumanoidArm arm, float equipProgress);
    @Shadow protected abstract void swingArm (float swingProgress, PoseStack matrices, int armX, HumanoidArm arm);

    @Shadow protected abstract void applyItemArmAttackTransform (PoseStack matrices, HumanoidArm arm, float swingProgress);

    @Unique
    private void applyParryOffset (PoseStack matrices, HumanoidArm arm, float equipProgress) {
        float armMultiplier = arm == HumanoidArm.RIGHT ? -1f : 1f;
        matrices.translate(0.75f * armMultiplier, 0f, 0.1f);
        matrices.rotate(Axis.XP.rotationDegrees(55f * (1f - equipProgress)));
        matrices.rotate(Axis.ZP.rotationDegrees(armMultiplier * 125f * (1f - equipProgress)));
    }

    @Unique
    private void applyEquipOffsetUpwards (PoseStack matrices, HumanoidArm arm, float equipProgress) {
        float sideMultiplier = arm == HumanoidArm.RIGHT ? 1f : -1f;
        matrices.translate(sideMultiplier * 0.56F, -0.52F + equipProgress * 0.6F, -0.72F);
    }

    @Redirect(
        method = "submitArmWithItem",
        slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getUseAnimation()Lnet/minecraft/world/item/ItemUseAnimation;")),
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/FirstPersonHandsAndItemsRenderer;applyItemArmTransform(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/world/entity/HumanoidArm;F)V",
            ordinal = 0
        )
    )
    private void animateParry (FirstPersonHandsAndItemsRenderer instance, PoseStack poseStack, HumanoidArm arm, float inverseArmHeight, PlayerRenderState playerState, FirstPersonHandsAndItemsRenderState state, float partialTicks, float xRot, InteractionHand hand, float attack, ItemStack itemStack, float inverseArmHeight1, PoseStack poseStack1, SubmitNodeCollector submitNodeCollector, int lightCoords) {
        if (EnchantmentHelper.hasTag(itemStack, DSEnchantmentTags.REVERSE_RENDER) && DSSettings.renderFlippedSwords()) {
            this.applyEquipOffsetUpwards(poseStack, arm, inverseArmHeight);
            this.applyParryOffset(poseStack, arm, inverseArmHeight);
        }
        else {
            this.applyItemArmTransform(poseStack, arm, inverseArmHeight);
        }
    }

    @Inject(
        method = "submitArmWithItem",
        slice = @Slice(from=@At(value="INVOKE", target="Lnet/minecraft/world/item/ItemStack;getUseAnimation()Lnet/minecraft/world/item/ItemUseAnimation;")),
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/FirstPersonHandsAndItemsRenderer;applyItemArmTransform(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/world/entity/HumanoidArm;F)V",
            shift = At.Shift.AFTER,
            ordinal = 0
        )
    )
    private void animateLunge (PlayerRenderState playerState, FirstPersonHandsAndItemsRenderState state, float partialTicks, float xRot, InteractionHand hand, float attack, ItemStack itemStack, float inverseArmHeight, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, CallbackInfo ci) {
        if (itemStack.getEnchantments().keySet().stream().anyMatch(holder -> holder.is(DSEnchantments.LUNGE.getKey()))) {
            poseStack.rotate(Axis.XN.rotationDegrees(75f * (1 - inverseArmHeight)));
            poseStack.translate(0f, -0.75f, 0.75f);
        }
    }

    @Redirect(
        method = "submitArmWithItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/FirstPersonHandsAndItemsRenderer;applyItemArmTransform(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/world/entity/HumanoidArm;F)V",
            ordinal = 4
        )
    )
    private void applyUpwardsEquip (FirstPersonHandsAndItemsRenderer instance, PoseStack poseStack, HumanoidArm arm, float inverseArmHeight, PlayerRenderState playerState, FirstPersonHandsAndItemsRenderState state, float partialTicks, float xRot, InteractionHand hand, float attack, ItemStack itemStack, float inverseArmHeight1, PoseStack poseStack1, SubmitNodeCollector submitNodeCollector, int lightCoords) {
        if (EnchantmentHelper.hasTag(itemStack, DSEnchantmentTags.REVERSE_RENDER) && DSSettings.renderFlippedSwords() && attack == 0) {
            this.applyEquipOffsetUpwards(poseStack, arm, inverseArmHeight);
        }
        else this.applyItemArmTransform(poseStack, arm, inverseArmHeight);
    }

    @Inject(method="submitArmWithItem", at=@At(value="INVOKE", target="Lcom/mojang/blaze3d/vertex/PoseStack;pushPose()V", shift=At.Shift.AFTER))
    private void flipBlade (PlayerRenderState playerState, FirstPersonHandsAndItemsRenderState state, float partialTicks, float xRot, InteractionHand hand, float attack, ItemStack itemStack, float inverseArmHeight, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, CallbackInfo ci) {
        if (EnchantmentHelper.hasTag(itemStack, DSEnchantmentTags.REVERSE_RENDER) && DSSettings.renderFlippedSwords() && attack == 0) {
            AvatarRenderState avatarRenderState = playerState.avatarRenderState;
            if (avatarRenderState == null) return;

            HumanoidArm arm = (hand == InteractionHand.MAIN_HAND) ? avatarRenderState.mainArm : avatarRenderState.mainArm.getOpposite();
            float sideMultiplier = arm == HumanoidArm.RIGHT ? -1f : 1f;
            poseStack.translate(-0.15f * sideMultiplier, -0.9f, -1.5f);
            poseStack.rotate(Axis.XP.rotationDegrees(180f));
        }
    }
}
