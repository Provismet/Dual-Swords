package com.provismet.dualswords.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.provismet.dualswords.enchantments.DaishoEnchantment;
import com.provismet.dualswords.enchantments.Enchantments;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    protected PlayerEntityMixin (EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }
    
    @ModifyVariable(method="attack", at=@At("STORE"), ordinal=1)
    private float addDaishoDamage (float currentDamage) {
        int DaishoLevel = EnchantmentHelper.getLevel(Enchantments.DAISHO, ((PlayerEntity)(Object)(this)).getOffHandStack());
        return currentDamage + DaishoEnchantment.getOffhandDamage(DaishoLevel);
    }
}
