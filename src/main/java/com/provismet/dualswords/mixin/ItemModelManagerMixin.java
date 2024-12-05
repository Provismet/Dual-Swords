package com.provismet.dualswords.mixin;

import com.provismet.dualswords.interfaceMixin.IMixinItemRenderState;
import com.provismet.dualswords.util.tag.DSEnchantmentTags;
import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ModelTransformationMode;
import net.minecraft.item.consume.UseAction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemModelManager.class)
public abstract class ItemModelManagerMixin {
    @Inject(method="update(Lnet/minecraft/client/render/item/ItemRenderState;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ModelTransformationMode;ZLnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;I)V", at=@At("HEAD"))
    private void modifyItemRender (ItemRenderState renderState, ItemStack stack, ModelTransformationMode transformationMode, boolean leftHand, World world, LivingEntity entity, int seed, CallbackInfo info) {
        ((IMixinItemRenderState)renderState).dual_Swords$setReverseRender(EnchantmentHelper.hasAnyEnchantmentsIn(stack, DSEnchantmentTags.REVERSE_RENDER));
        ((IMixinItemRenderState)renderState).dual_Swords$setFlippedSpear(stack.getUseAction() == UseAction.SPEAR && EnchantmentHelper.hasAnyEnchantmentsIn(stack, DSEnchantmentTags.FLIPPED_SPEAR));
        ((IMixinItemRenderState)renderState).dual_Swords$setIsActive(entity != null && entity.isUsingItem() && ItemStack.areEqual(entity.getActiveItem(), stack));
    }
}
