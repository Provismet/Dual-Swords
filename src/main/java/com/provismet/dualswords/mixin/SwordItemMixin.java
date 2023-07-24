package com.provismet.dualswords.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.provismet.dualswords.enchantments.Enchantments;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

@Mixin(SwordItem.class)
public abstract class SwordItemMixin extends ToolItem {
    @Unique
    private Multimap<EntityAttribute, EntityAttributeModifier> dualswords_offHandAttributes = null;

    @Shadow
    @Final
    private float attackDamage;

    private SwordItemMixin (ToolMaterial material, Settings settings) {
        super(material, settings);
    }

    @Override
    public TypedActionResult<ItemStack> use (World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (EnchantmentHelper.getLevel(Enchantments.PARRY, itemStack) > 0) {
            user.setCurrentHand(hand);
            return TypedActionResult.consume(itemStack);
        }
        return super.use(world, user, hand);
    }
    
    @Override
    public UseAction getUseAction (ItemStack itemStack) {
        if (EnchantmentHelper.getLevel(Enchantments.PARRY, itemStack) > 0) return UseAction.BLOCK;
        return super.getUseAction(itemStack);
    }

    @Override
    public int getMaxUseTime (ItemStack itemStack) {
        if (EnchantmentHelper.getLevel(Enchantments.PARRY, itemStack) > 0) return 20;
        return super.getMaxUseTime(itemStack);
    }

    @Override
    public ItemStack finishUsing (ItemStack itemStack, World world, LivingEntity user) {
        if (EnchantmentHelper.getLevel(Enchantments.PARRY, itemStack) > 0) {
            if (user instanceof PlayerEntity player) {
                player.getItemCooldownManager().set(itemStack.getItem(), 30 + 5 * EnchantmentHelper.getLevel(Enchantments.DAISHO, itemStack));
            }
        }
        return itemStack;
    }

    @Override
    public void onStoppedUsing (ItemStack itemStack, World world, LivingEntity user, int remainingUseTicks) {
        if (EnchantmentHelper.getLevel(Enchantments.PARRY, itemStack) > 0 && user instanceof PlayerEntity player) {
            if (!player.getItemCooldownManager().isCoolingDown(itemStack.getItem()))
                player.getItemCooldownManager().set(itemStack.getItem(), (int)((30 + 5 * EnchantmentHelper.getLevel(Enchantments.DAISHO, itemStack)) * (1f - ((float)remainingUseTicks / 20f))));
        }
    }

    @Inject(method="getAttributeModifiers", at=@At("HEAD"), cancellable=true)
    private void applyOffhandMods (EquipmentSlot slot, CallbackInfoReturnable<Multimap<EntityAttribute, EntityAttributeModifier>> cir) {
        if (slot == EquipmentSlot.OFFHAND) {
            if (this.dualswords_offHandAttributes == null) {
                ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
                builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Offhand Weapon modifier", (double)this.attackDamage / 2.5, EntityAttributeModifier.Operation.ADDITION));
                this.dualswords_offHandAttributes = builder.build();
            }
            cir.setReturnValue(this.dualswords_offHandAttributes);
        }
    }
}
