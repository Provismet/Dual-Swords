package com.provismet.datagen.dualswords;

import com.provismet.dualswords.registry.DSEnchantments;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.enchantment.Enchantment;
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
        LanguageGenerator.addEnchantment(translationBuilder, DSEnchantments.THRUST, "Thrusting", "Increases damage dealt from a lunge.");
        LanguageGenerator.addEnchantment(translationBuilder, DSEnchantments.FORCEFUL, "Forceful", "Increases knockback dealt from a lunge.");
        LanguageGenerator.addEnchantment(translationBuilder, DSEnchantments.DAISHO, "Daisho", "Increases the damage bonus from dual wielding.");

        LanguageGenerator.addDeathMessage(translationBuilder, "riposte", "couldn't handle the swordplay of");
        LanguageGenerator.addDeathMessage(translationBuilder, "lunge", "couldn't outrun the blade of");
    }

    private static void addEnchantment (TranslationBuilder translationBuilder, Enchantment enchantment, String name, String description) {
        translationBuilder.add(enchantment, name);
        translationBuilder.add(enchantment.getTranslationKey() + ".desc", description);
    }

    private static void addDeathMessage (TranslationBuilder translationBuilder, String base, String message) {
        translationBuilder.add("death.attack." + base, "%1$s " + message + " %2$s");
        translationBuilder.add("death.attack." + base + ".item", "%1$s " + message + " %2$s using %3$s");
    }
}
