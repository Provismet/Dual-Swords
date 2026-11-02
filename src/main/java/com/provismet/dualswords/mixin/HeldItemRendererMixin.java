package com.provismet.dualswords.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.provismet.dualswords.config.DSSettings;
import com.provismet.dualswords.registry.DSEnchantments;
import com.provismet.dualswords.util.tag.DSEnchantmentTags;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
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

@Mixin(ItemInHandRenderer.class)
public abstract class HeldItemRendererMixin {
    @Shadow protected abstract void applyItemArmTransform (PoseStack matrices, HumanoidArm arm, float equipProgress);
    @Shadow protected abstract void swingArm (float swingProgress, PoseStack matrices, int armX, HumanoidArm arm);

    @Shadow protected abstract void applyItemArmAttackTransform (PoseStack matrices, HumanoidArm arm, float swingProgress);

    @Unique
    private void applyParryOffset (PoseStack matrices, HumanoidArm arm, float equipProgress) {
        float armMultiplier = arm == HumanoidArm.RIGHT ? -1f : 1f;
        matrices.translate(0.75f * armMultiplier, 0f, 0.1f);
        matrices.mulPose(Axis.XP.rotationDegrees(55f * (1f - equipProgress)));
        matrices.mulPose(Axis.ZP.rotationDegrees(armMultiplier * 125f * (1f - equipProgress)));
    }

    @Unique
    private void applyEquipOffsetUpwards (PoseStack matrices, HumanoidArm arm, float equipProgress) {
        float sideMultiplier = arm == HumanoidArm.RIGHT ? 1f : -1f;
        matrices.translate(sideMultiplier * 0.56F, -0.52F + equipProgress * 0.6F, -0.72F);
    }

    @Redirect(
        method = "renderArmWithItem",
        slice = @Slice(
            from = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getUseAnimation()Lnet/minecraft/world/item/ItemUseAnimation;"),
            to = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/AbstractClientPlayer;isAutoSpinAttack()Z")
        ),
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;applyItemArmTransform(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/world/entity/HumanoidArm;F)V",
            ordinal = 0
        )
    )
    private void animateParry (ItemInHandRenderer instance, PoseStack matrices, HumanoidArm arm, float equipProgress, AbstractClientPlayer player, float tickProgress, float pitch, InteractionHand hand, float swingProgress, ItemStack item, float equipProgress1, PoseStack matrices1, SubmitNodeCollector orderedRenderCommandQueue, int light) {
        if (EnchantmentHelper.hasTag(item, DSEnchantmentTags.REVERSE_RENDER) && DSSettings.renderFlippedSwords()) {
            this.applyEquipOffsetUpwards(matrices1, arm, equipProgress1);
            this.applyParryOffset(matrices1, arm, equipProgress1);
        }
        else {
            this.applyItemArmTransform(matrices1, arm, equipProgress1);
        }
    }

    @Inject(
        method = "renderArmWithItem",
        slice = @Slice(from=@At(value="INVOKE", target="Lnet/minecraft/world/item/ItemStack;getUseAnimation()Lnet/minecraft/world/item/ItemUseAnimation;")),
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;applyItemArmTransform(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/world/entity/HumanoidArm;F)V",
            shift = At.Shift.AFTER,
            ordinal = 0
        )
    )
    private void animateLunge (AbstractClientPlayer player, float tickProgress, float pitch, InteractionHand hand, float swingProgress, ItemStack item, float equipProgress, PoseStack matrices, SubmitNodeCollector orderedRenderCommandQueue, int light, CallbackInfo ci) {
        if (EnchantmentHelper.getItemEnchantmentLevel(DSEnchantments.LUNGE.getEntryOrThrow(player.level().registryAccess()), item) > 0) {
            matrices.mulPose(Axis.XN.rotationDegrees(75f * (1 - equipProgress)));
            matrices.translate(0f, -0.75f, 0.75f);
        }
    }

    @Redirect(
        method = "renderArmWithItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;applyItemArmTransform(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/world/entity/HumanoidArm;F)V",
            ordinal = 4
        )
    )
    private void applyUpwardsEquip (ItemInHandRenderer instance, PoseStack matrices, HumanoidArm arm, float equipProgress, AbstractClientPlayer player, float tickProgress, float pitch, InteractionHand hand, float swingProgress, ItemStack item, float equipProgress1, PoseStack matrices1, SubmitNodeCollector orderedRenderCommandQueue, int light) {
        if (EnchantmentHelper.hasTag(item, DSEnchantmentTags.REVERSE_RENDER) && DSSettings.renderFlippedSwords() && swingProgress == 0) {
            this.applyEquipOffsetUpwards(matrices1, arm, equipProgress);
        }
        else this.applyItemArmTransform(matrices, arm, equipProgress);
    }

    @Inject(method="renderArmWithItem", at=@At(value="INVOKE", target="Lcom/mojang/blaze3d/vertex/PoseStack;pushPose()V", shift=At.Shift.AFTER))
    private void flipBlade (AbstractClientPlayer player, float tickProgress, float pitch, InteractionHand hand, float swingProgress, ItemStack item, float equipProgress, PoseStack matrices, SubmitNodeCollector orderedRenderCommandQueue, int light, CallbackInfo ci) {
        if (EnchantmentHelper.hasTag(item, DSEnchantmentTags.REVERSE_RENDER) && DSSettings.renderFlippedSwords() && swingProgress == 0) {
            HumanoidArm arm = hand == InteractionHand.MAIN_HAND ? player.getMainArm() : player.getMainArm().getOpposite();
            float sideMultiplier = arm == HumanoidArm.RIGHT ? -1f : 1f;
            matrices.translate(-0.15f * sideMultiplier, -0.9f, -1.5f);
            matrices.mulPose(Axis.XP.rotationDegrees(180f));
        }
    }
}
