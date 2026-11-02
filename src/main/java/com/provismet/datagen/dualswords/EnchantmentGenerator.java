package com.provismet.datagen.dualswords;

import com.provismet.dualswords.registry.DSEnchantments;
import com.provismet.lilylib.datagen.provider.LilyEnchantmentProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class EnchantmentGenerator extends LilyEnchantmentProvider {
    protected EnchantmentGenerator (FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void generate (HolderLookup.Provider wrapperLookup, EnchantmentBuilder builder) {
        builder.add(DSEnchantments.PARRY);
        builder.add(DSEnchantments.RIPOSTE);
        builder.add(DSEnchantments.DEFLECT);
        builder.add(DSEnchantments.LUNGE);
        builder.add(DSEnchantments.THRUSTING);
        builder.add(DSEnchantments.FORCEFUL);
        builder.add(DSEnchantments.DAISHO);
    }
}
