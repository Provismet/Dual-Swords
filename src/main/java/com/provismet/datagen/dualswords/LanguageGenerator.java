package com.provismet.datagen.dualswords;

import com.provismet.dualswords.DSDamageTypes;
import com.provismet.dualswords.registry.DSEnchantments;

import com.provismet.lilylib.container.DamageTypeContainer;
import com.provismet.lilylib.container.EnchantmentContainer;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class LanguageGenerator extends FabricLanguageProvider {
    protected LanguageGenerator (FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generateTranslations (RegistryWrapper.WrapperLookup registryLookup, TranslationBuilder translationBuilder) {
        LanguageGenerator.addEnchantment(translationBuilder, DSEnchantments.PARRY, "Parry", "Enables the ability to parry and riposte attacks.");
        LanguageGenerator.addEnchantment(translationBuilder, DSEnchantments.RIPOSTE, "Riposte", "Increases the damage of ripostes.");
        LanguageGenerator.addEnchantment(translationBuilder, DSEnchantments.DEFLECT, "Deflection", "Increases return speed of parried projectiles.");
        LanguageGenerator.addEnchantment(translationBuilder, DSEnchantments.LUNGE, "Lunge", "Enables the ability to thrust forwards with a weapon.");
        LanguageGenerator.addEnchantment(translationBuilder, DSEnchantments.THRUSTING, "Thrusting", "Increases damage dealt from a lunge.");
        LanguageGenerator.addEnchantment(translationBuilder, DSEnchantments.FORCEFUL, "Forceful", "Increases knockback dealt from a lunge.");
        LanguageGenerator.addEnchantment(translationBuilder, DSEnchantments.DAISHO, "Daisho", "Increases the damage bonus from dual wielding.");

        LanguageGenerator.addDeathMessage(translationBuilder, DSDamageTypes.RIPOSTE, "couldn't handle the swordplay of");
        LanguageGenerator.addDeathMessage(translationBuilder, DSDamageTypes.LUNGE, "couldn't outrun the blade of");
    }

    private static void addEnchantment (TranslationBuilder translationBuilder, EnchantmentContainer enchantment, String name, String description) {
        translationBuilder.add(enchantment.getTranslationKey(), name);
        translationBuilder.add(enchantment.getTranslationKey("desc"), description);
    }

    private static void addDeathMessage (TranslationBuilder translationBuilder, DamageTypeContainer container, String message) {
        translationBuilder.add(container.getDeathTranslationKey(), "%1$s " + message + " %2$s");
        translationBuilder.add(container.getDeathTranslationKey() + ".item", "%1$s " + message + " %2$s using %3$s");
    }
}
