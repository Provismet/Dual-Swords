package com.provismet.dualswords.mixin;

import com.provismet.dualswords.config.DSSettings;
import com.provismet.dualswords.interfaceMixin.IMixinItemRenderState;
import com.provismet.dualswords.util.tag.DSEnchantmentTags;
import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.consume.UseAction;
import net.minecraft.util.HeldItemContext;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemModelManager.class)
public abstract class ItemModelManagerMixin {
    @Inject(method="update", at=@At("HEAD"))
    private void modifyItemRender (ItemRenderState renderState, ItemStack stack, ItemDisplayContext displayContext, World world, HeldItemContext heldItemContext, int seed, CallbackInfo ci) {
        ((IMixinItemRenderState)renderState).dual_Swords$setReverseRender(EnchantmentHelper.hasAnyEnchantmentsIn(stack, DSEnchantmentTags.REVERSE_RENDER) && DSSettings.renderFlippedSwords());
        ((IMixinItemRenderState)renderState).dual_Swords$setFlippedSpear(stack.getUseAction() == UseAction.SPEAR && EnchantmentHelper.hasAnyEnchantmentsIn(stack, DSEnchantmentTags.FLIPPED_SPEAR) && DSSettings.renderFlippedLunge());
        if (heldItemContext != null) {
            ((IMixinItemRenderState)renderState).dual_Swords$setIsActive(
                heldItemContext.getEntity() != null
                    && heldItemContext.getEntity().isUsingItem()
                    && ItemStack.areEqual(heldItemContext.getEntity().getActiveItem(), stack)
            );
        }
    }
}
