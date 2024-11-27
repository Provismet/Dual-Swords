package com.provismet.dualswords.mixin;

import com.mojang.datafixers.util.Pair;
import com.provismet.dualswords.registry.DSEnchantmentComponentTypes;
import com.provismet.dualswords.util.DSConstants;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.UseCooldownComponent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.item.ItemStack;
import net.minecraft.item.consume.UseAction;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(ItemCooldownManager.class)
public abstract class ItemCooldownManagerMixin {
    @Inject(method="getGroup", at=@At("HEAD"), cancellable=true)
    private void setEnchantmentGroups (ItemStack stack, CallbackInfoReturnable<Identifier> cir) {
        UseCooldownComponent component = stack.get(DataComponentTypes.USE_COOLDOWN);
        if (component == null) {
            Pair<String, Integer> useAction = EnchantmentHelper.getHighestLevelEffect(stack, DSEnchantmentComponentTypes.USE_ACTION);
            if (useAction != null) {
                if (Objects.equals(useAction.getFirst(), UseAction.BLOCK.name())) cir.setReturnValue(DSConstants.COOLDOWN_GROUP_PARRY);
                else if (Objects.equals(useAction.getFirst(), UseAction.SPEAR.name())) cir.setReturnValue(DSConstants.COOLDOWN_GROUP_LUNGE);
            }
        }
    }
}
