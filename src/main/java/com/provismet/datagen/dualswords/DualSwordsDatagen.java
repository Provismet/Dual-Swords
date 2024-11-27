package com.provismet.datagen.dualswords;

import com.provismet.dualswords.DSDamageTypes;
import com.provismet.dualswords.registry.DSEnchantments;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;

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
    public void buildRegistry (RegistryBuilder registryBuilder) {
        registryBuilder.addRegistry(RegistryKeys.ENCHANTMENT, DSEnchantments::bootstrap);
        registryBuilder.addRegistry(RegistryKeys.DAMAGE_TYPE, DSDamageTypes::bootstrap);
    }
}
