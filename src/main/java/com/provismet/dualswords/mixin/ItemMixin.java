package com.provismet.dualswords.mixin;

import com.mojang.datafixers.util.Pair;
import com.provismet.CombatPlusCore.enchantment.loot.context.CPCLootContext;
import com.provismet.CombatPlusCore.utility.CPCEnchantmentHelper;
import com.provismet.CombatPlusCore.utility.item.AttributeIdentifiers;
import com.provismet.dualswords.DualSwordsMain;
import com.provismet.dualswords.enchantment.component.EnchantmentStoppedUsingEffect;
import com.provismet.dualswords.registry.DSEnchantmentComponentTypes;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.effect.EnchantmentEffectEntry;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.provismet.CombatPlusCore.interfaces.DualWeapon;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

@Mixin(Item.class)
public abstract class ItemMixin {
    @Shadow @Final @Mutable private ComponentMap components;

    @Shadow public abstract ItemStack getDefaultStack();

    @Unique private boolean appliedOffhand = false;

    @Inject(method="getComponents", at=@At("HEAD"))
    private void placeOffhandAttributes (CallbackInfoReturnable<ComponentMap> cir) {
        if (this instanceof DualWeapon dualWeapon && !this.appliedOffhand) {
            this.appliedOffhand = true;
            AttributeModifiersComponent attributes = this.components.getOrDefault(DataComponentTypes.ATTRIBUTE_MODIFIERS, AttributeModifiersComponent.DEFAULT);
            attributes = attributes.with(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(AttributeIdentifiers.OFFHAND_DAMAGE, dualWeapon.getOffhandDamage(this.getDefaultStack()), EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.OFFHAND);
            this.components = ComponentMap.builder().addAll(this.components).add(DataComponentTypes.ATTRIBUTE_MODIFIERS, attributes).build();
        }
    }

    @Inject(method="use", at=@At("HEAD"), cancellable=true)
    private void attemptParry (World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (EnchantmentHelper.hasAnyEnchantmentsWith(itemStack, DSEnchantmentComponentTypes.USE_ACTION)) {
            user.setCurrentHand(hand);
            cir.setReturnValue(TypedActionResult.consume(itemStack));
        }
    }
    
    @Inject(method="getUseAction", at=@At("HEAD"), cancellable=true)
    private void setParryAction (ItemStack itemStack, CallbackInfoReturnable<UseAction> cir) {
        Pair<String, Integer> action = EnchantmentHelper.getEffectListAndLevel(itemStack, DSEnchantmentComponentTypes.USE_ACTION);
        if (action != null) {
            try {
                cir.setReturnValue(UseAction.valueOf(action.getFirst()));
            }
            catch (IllegalArgumentException e) {
                DualSwordsMain.LOGGER.error("Invalid use action attempted: ", e);
            }
        }
    }

    @Inject(method="getMaxUseTime", at=@At("HEAD"), cancellable=true)
    private void setMaxParryTime (ItemStack itemStack, LivingEntity user, CallbackInfoReturnable<Integer> cir) {
        if (EnchantmentHelper.hasAnyEnchantmentsWith(itemStack, DSEnchantmentComponentTypes.USE_ACTION)) {
            if (EnchantmentHelper.hasAnyEnchantmentsWith(itemStack, DSEnchantmentComponentTypes.USE_ACTION_DURATION))
                cir.setReturnValue((int)CPCEnchantmentHelper.modifyValue(DSEnchantmentComponentTypes.USE_ACTION_DURATION, user.getRandom(), itemStack, 0));
            else cir.setReturnValue(72000);
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
                            effect.effect().apply(serverWorld, level, new EnchantmentEffectContext(itemStack, slot, user), user, user.getPos());
                    }
                }, user, slot);
            }
        }
    }

    @Inject(method="onStoppedUsing", at=@At("HEAD"))
    private void onStoppedParrying (ItemStack itemStack, World world, LivingEntity user, int remainingUseTicks, CallbackInfo info) {
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
