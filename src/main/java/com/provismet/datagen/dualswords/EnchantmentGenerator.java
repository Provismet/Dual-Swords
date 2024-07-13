package com.provismet.datagen.dualswords;

import com.provismet.dualswords.registry.DSEnchantments;
import com.provismet.lilylib.datagen.provider.LilyEnchantmentProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class EnchantmentGenerator extends LilyEnchantmentProvider {
    protected EnchantmentGenerator (FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void generate (RegistryWrapper.WrapperLookup wrapperLookup, EnchantmentBuilder builder) {
        builder.add(DSEnchantments.PARRY);
        builder.add(DSEnchantments.RIPOSTE);
        builder.add(DSEnchantments.DEFLECT);
        builder.add(DSEnchantments.LUNGE);
        builder.add(DSEnchantments.THRUSTING);
        builder.add(DSEnchantments.FORCEFUL);
        builder.add(DSEnchantments.DAISHO);
    }
}
