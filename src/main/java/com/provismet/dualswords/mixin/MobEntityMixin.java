package com.provismet.dualswords.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import com.provismet.dualswords.enchantments.DaishoEnchantment;
import com.provismet.dualswords.enchantments.Enchantments;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Targeter;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.World;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends LivingEntity implements Targeter {
    protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }
    
    @ModifyArg(method="tryAttack", at=@At(value="INVOKE", target="Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"))
    private float addDaishoDamage (float currentDamage) {
        int daishoLevel = EnchantmentHelper.getLevel(Enchantments.DAISHO, ((MobEntity)(Object)this).getOffHandStack());
        return currentDamage + DaishoEnchantment.getOffhandDamage(daishoLevel);
    }
}
