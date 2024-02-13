package com.provismet.datagen.dualswords;

import java.util.concurrent.CompletableFuture;

import com.provismet.CombatPlusCore.utility.CPCEnchantmentTags;
import com.provismet.dualswords.registry.DSEnchantments;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider.EnchantmentTagProvider;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;

public class EnchantmentTagGenerator extends EnchantmentTagProvider {
    public EnchantmentTagGenerator(FabricDataOutput output, CompletableFuture<WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure (WrapperLookup arg) {
        getOrCreateTagBuilder(CPCEnchantmentTags.OFFHAND)
            .add(DSEnchantments.PARRY)
            .add(DSEnchantments.RIPOSTE)
            .add(DSEnchantments.DEFLECT)
            .add(DSEnchantments.LUNGE)
            .add(DSEnchantments.FORCEFUL)
            .add(DSEnchantments.THRUST)
            .add(DSEnchantments.DAISHO);
    }
}
