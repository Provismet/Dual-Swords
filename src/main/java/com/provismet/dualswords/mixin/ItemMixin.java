package com.provismet.dualswords.mixin;

import com.mojang.datafixers.util.Pair;
import com.provismet.CombatPlusCore.enchantment.loot.context.CPCLootContext;
import com.provismet.CombatPlusCore.utility.CPCEnchantmentHelper;
import com.provismet.dualswords.DualSwordsMain;
import com.provismet.dualswords.enchantment.component.EnchantmentStoppedUsingEffect;
import com.provismet.dualswords.registry.DSEnchantmentComponentTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.enchantment.ConditionalEffect;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class ItemMixin {
    @Inject(method="use", at=@At("HEAD"), cancellable=true)
    private void attemptParry (Level world, Player user, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack itemStack = user.getItemInHand(hand);
        if (EnchantmentHelper.has(itemStack, DSEnchantmentComponentTypes.USE_ACTION)) {
            user.startUsingItem(hand);
            cir.setReturnValue(InteractionResult.CONSUME);
        }
    }
    
    @Inject(method="getUseAnimation", at=@At("HEAD"), cancellable=true)
    private void setParryAction (ItemStack itemStack, CallbackInfoReturnable<ItemUseAnimation> cir) {
        Pair<String, Integer> action = EnchantmentHelper.getHighestLevel(itemStack, DSEnchantmentComponentTypes.USE_ACTION);
        if (action != null) {
            try {
                cir.setReturnValue(ItemUseAnimation.valueOf(action.getFirst()));
            }
            catch (IllegalArgumentException e) {
                DualSwordsMain.LOGGER.error("Invalid use action attempted: ", e);
            }
        }
    }

    @Inject(method="finishUsingItem", at=@At("HEAD"))
    private void finishParrying (ItemStack itemStack, Level world, LivingEntity user, CallbackInfoReturnable<ItemStack> cir) {
        if (world instanceof ServerLevel serverWorld) {
            EquipmentSlot slot;
            if (ItemStack.matches(itemStack, user.getMainHandItem())) slot = EquipmentSlot.MAINHAND;
            else if (ItemStack.matches(itemStack, user.getOffhandItem())) slot = EquipmentSlot.OFFHAND;
            else slot = null;

            if (slot != null) {
                CPCEnchantmentHelper.forEachEnchantment((enchantment, level, context) -> {
                    for (ConditionalEffect<EnchantmentEntityEffect> effect : enchantment.value().getEffects(DSEnchantmentComponentTypes.ON_FINISHED_USING)) {
                        if (effect.matches(CPCLootContext.createSingleEntity(serverWorld, level, user, itemStack)))
                            effect.effect().apply(serverWorld, level, new EnchantedItemInUse(itemStack, slot, user), user, user.position());
                    }
                }, user, slot);
            }
        }
    }

    @Inject(method="releaseUsing", at=@At("HEAD"))
    private void onStoppedParrying (ItemStack itemStack, Level world, LivingEntity user, int remainingUseTicks, CallbackInfoReturnable<Boolean> cir) {
        if (world instanceof ServerLevel serverWorld) {
            EquipmentSlot slot;
            if (ItemStack.matches(itemStack, user.getMainHandItem())) slot = EquipmentSlot.MAINHAND;
            else if (ItemStack.matches(itemStack, user.getOffhandItem())) slot = EquipmentSlot.OFFHAND;
            else slot = null;

            if (slot != null) {
                CPCEnchantmentHelper.forEachEnchantment((enchantment, level, context) -> {
                    for (ConditionalEffect<EnchantmentStoppedUsingEffect> effect : enchantment.value().getEffects(DSEnchantmentComponentTypes.ON_STOPPED_USING)) {
                        if (effect.matches(CPCLootContext.createSingleEntity(serverWorld, level, user, itemStack)))
                            effect.effect().onStoppedUsing(serverWorld, level, new EnchantedItemInUse(itemStack, slot, user), user, remainingUseTicks);
                    }
                }, user, slot);
            }
        }
    }
}
