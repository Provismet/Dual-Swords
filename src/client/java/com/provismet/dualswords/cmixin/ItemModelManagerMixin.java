package com.provismet.dualswords.cmixin;

import com.provismet.dualswords.config.DSSettings;
import com.provismet.dualswords.interfaceMixin.IMixinItemRenderState;
import com.provismet.dualswords.util.tag.DSEnchantmentTags;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemModelResolver.class)
public abstract class ItemModelManagerMixin {
    @Inject(method="appendItemLayers", at=@At("HEAD"))
    private void modifyItemRender (ItemStackRenderState renderState, ItemStack stack, ItemDisplayContext displayContext, Level world, ItemOwner heldItemContext, int seed, CallbackInfo ci) {
        ((IMixinItemRenderState)renderState).dual_Swords$setReverseRender(EnchantmentHelper.hasTag(stack, DSEnchantmentTags.REVERSE_RENDER) && DSSettings.renderFlippedSwords());
        ((IMixinItemRenderState)renderState).dual_Swords$setFlippedSpear(stack.getUseAnimation() == ItemUseAnimation.TRIDENT && EnchantmentHelper.hasTag(stack, DSEnchantmentTags.FLIPPED_SPEAR) && DSSettings.renderFlippedLunge());
        if (heldItemContext != null) {
            ((IMixinItemRenderState)renderState).dual_Swords$setIsActive(
                heldItemContext.asLivingEntity() != null
                    && heldItemContext.asLivingEntity().isUsingItem()
                    && ItemStack.matches(heldItemContext.asLivingEntity().getUseItem(), stack)
            );
        }
    }
}
