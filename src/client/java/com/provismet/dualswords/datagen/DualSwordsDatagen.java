package com.provismet.dualswords.datagen;

import com.provismet.dualswords.DSDamageTypes;
import com.provismet.dualswords.registry.DSEnchantments;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;

public class DualSwordsDatagen implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator (FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(EnchantmentGenerator::new);
        pack.addProvider(DamageTypeGenerator::new);
        pack.addProvider(LanguageGenerator::new);
        pack.addProvider(EnchantmentTagGenerator::new);
        pack.addProvider(DamageTypeTagGenerator::new);
    }

    @Override
    public void buildRegistry (RegistrySetBuilder registryBuilder) {
        registryBuilder.add(Registries.ENCHANTMENT, DSEnchantments::bootstrap);
        registryBuilder.add(Registries.DAMAGE_TYPE, DSDamageTypes::bootstrap);
    }
}
