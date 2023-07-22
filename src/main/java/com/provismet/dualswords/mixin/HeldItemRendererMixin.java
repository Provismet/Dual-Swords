package com.provismet.dualswords.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.provismet.dualswords.DualSwordsMain;
import com.provismet.dualswords.enchantments.Enchantments;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;

@Mixin(HeldItemRenderer.class)
public abstract class HeldItemRendererMixin {
    public void applyParryOffset (MatrixStack matrices, float tickDelta, Arm arm, ItemStack stack) {
        DualSwordsMain.LOGGER.info("Now parrying.");
        matrices.translate(1f, 0f, 1f);
    }

    @Inject(method="renderFirstPersonItem",
        slice=@Slice(from=@At(value="INVOKE", target="Lnet/minecraft/item/ItemStack;getUseAction()Lnet/minecraft/util/UseAction;")),
        at=@At(value="INVOKE",
            target="Lnet/minecraft/client/render/item/HeldItemRenderer;applyEquipOffset(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/Arm;F)V",
            ordinal=2,
            shift=At.Shift.AFTER))
    private void animateParry (AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo info) {
        if (EnchantmentHelper.getLevel(Enchantments.PARRY, item) > 0) {
            Arm arm = hand == Hand.MAIN_HAND ? player.getMainArm() : player.getMainArm().getOpposite();
            applyParryOffset(matrices, tickDelta, arm, item);
        }
    }
}
