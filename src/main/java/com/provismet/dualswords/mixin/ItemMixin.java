package com.provismet.dualswords.mixin;

import com.mojang.datafixers.util.Pair;
import com.provismet.CombatPlusCore.enchantment.loot.context.CPCLootContext;
import com.provismet.CombatPlusCore.utility.CPCEnchantmentHelper;
import com.provismet.dualswords.DualSwordsMain;
import com.provismet.dualswords.enchantment.component.EnchantmentStoppedUsingEffect;
import com.provismet.dualswords.registry.DSEnchantmentComponentTypes;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.effect.EnchantmentEffectEntry;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.consume.UseAction;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

@Mixin(Item.class)
public abstract class ItemMixin {
    @Inject(method="use", at=@At("HEAD"), cancellable=true)
    private void attemptParry (World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (EnchantmentHelper.hasAnyEnchantmentsWith(itemStack, DSEnchantmentComponentTypes.USE_ACTION)) {
            user.setCurrentHand(hand);
            cir.setReturnValue(ActionResult.CONSUME);
        }
    }
    
    @Inject(method="getUseAction", at=@At("HEAD"), cancellable=true)
    private void setParryAction (ItemStack itemStack, CallbackInfoReturnable<UseAction> cir) {
        Pair<String, Integer> action = EnchantmentHelper.getHighestLevelEffect(itemStack, DSEnchantmentComponentTypes.USE_ACTION);
        if (action != null) {
            try {
                cir.setReturnValue(UseAction.valueOf(action.getFirst()));
            }
            catch (IllegalArgumentException e) {
                DualSwordsMain.LOGGER.error("Invalid use action attempted: ", e);
            }
        }
    }

    @Inject(method="finishUsing", at=@At("HEAD"))
    private void finishParrying (ItemStack itemStack, World world, LivingEntity user, CallbackInfoReturnable<ItemStack> cir) {
        if (world instanceof ServerWorld serverWorld) {
            EquipmentSlot slot;
            if (ItemStack.areEqual(itemStack, user.getMainHandStack())) slot = EquipmentSlot.MAINHAND;
            else if (ItemStack.areEqual(itemStack, user.getOffHandStack())) slot = EquipmentSlot.OFFHAND;
            else slot = null;

            if (slot != null) {
                CPCEnchantmentHelper.forEachEnchantment((enchantment, level, context) -> {
                    for (EnchantmentEffectEntry<EnchantmentEntityEffect> effect : enchantment.value().getEffect(DSEnchantmentComponentTypes.ON_FINISHED_USING)) {
                        if (effect.test(CPCLootContext.createSingleEntity(serverWorld, level, user, itemStack)))
                            effect.effect().apply(serverWorld, level, new EnchantmentEffectContext(itemStack, slot, user), user, user.getEntityPos());
                    }
                }, user, slot);
            }
        }
    }

    @Inject(method="onStoppedUsing", at=@At("HEAD"))
    private void onStoppedParrying (ItemStack itemStack, World world, LivingEntity user, int remainingUseTicks, CallbackInfoReturnable<Boolean> cir) {
        if (world instanceof ServerWorld serverWorld) {
            EquipmentSlot slot;
            if (ItemStack.areEqual(itemStack, user.getMainHandStack())) slot = EquipmentSlot.MAINHAND;
            else if (ItemStack.areEqual(itemStack, user.getOffHandStack())) slot = EquipmentSlot.OFFHAND;
            else slot = null;

            if (slot != null) {
                CPCEnchantmentHelper.forEachEnchantment((enchantment, level, context) -> {
                    for (EnchantmentEffectEntry<EnchantmentStoppedUsingEffect> effect : enchantment.value().getEffect(DSEnchantmentComponentTypes.ON_STOPPED_USING)) {
                        if (effect.test(CPCLootContext.createSingleEntity(serverWorld, level, user, itemStack)))
                            effect.effect().onStoppedUsing(serverWorld, level, new EnchantmentEffectContext(itemStack, slot, user), user, remainingUseTicks);
                    }
                }, user, slot);
            }
        }
    }
}
