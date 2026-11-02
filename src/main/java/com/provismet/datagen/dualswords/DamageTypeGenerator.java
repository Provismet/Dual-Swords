package com.provismet.datagen.dualswords;

import com.provismet.dualswords.DSDamageTypes;
import com.provismet.lilylib.datagen.provider.LilyDamageTypeProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class DamageTypeGenerator extends LilyDamageTypeProvider {
    protected DamageTypeGenerator (FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void generate (HolderLookup.Provider registries, DamageConsumer consumer) {
        consumer.add(DSDamageTypes.RIPOSTE);
        consumer.add(DSDamageTypes.LUNGE);
    }
}
