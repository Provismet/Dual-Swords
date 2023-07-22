package com.provismet.dualswords.mixin;

import org.spongepowered.asm.mixin.Mixin;

import com.provismet.dualswords.enchantments.Enchantments;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
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
                player.getItemCooldownManager().set((SwordItem)(Object)this, 30);
            }
        }
        return itemStack;
    }
}
