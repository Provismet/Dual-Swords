package com.provismet.datagen.dualswords;

import com.provismet.dualswords.DSDamageTypes;
import com.provismet.dualswords.registry.DSEnchantments;

import com.provismet.lilylib.container.DamageTypeContainer;
import com.provismet.lilylib.datagen.provider.LilyLanguageProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class LanguageGenerator extends LilyLanguageProvider {
    protected LanguageGenerator (FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generateTranslations (RegistryWrapper.WrapperLookup registryLookup, TranslationBuilder translationBuilder) {
        this.addEnchantment(translationBuilder, DSEnchantments.PARRY, "Parry", "Enables the ability to parry and riposte attacks.");
        this.addEnchantment(translationBuilder, DSEnchantments.RIPOSTE, "Riposte", "Increases the damage of ripostes.");
        this.addEnchantment(translationBuilder, DSEnchantments.DEFLECT, "Deflection", "Increases return speed of parried projectiles.");
        this.addEnchantment(translationBuilder, DSEnchantments.LUNGE, "Lunge", "Enables the ability to thrust forwards with a weapon.");
        this.addEnchantment(translationBuilder, DSEnchantments.THRUSTING, "Thrusting", "Increases damage dealt from a lunge.");
        this.addEnchantment(translationBuilder, DSEnchantments.FORCEFUL, "Forceful", "Increases knockback dealt from a lunge.");
        this.addEnchantment(translationBuilder, DSEnchantments.DAISHO, "Daisho", "Increases the damage bonus from dual wielding.");

        LanguageGenerator.addDeathMessage(translationBuilder, DSDamageTypes.RIPOSTE, "couldn't handle the swordplay of");
        LanguageGenerator.addDeathMessage(translationBuilder, DSDamageTypes.LUNGE, "couldn't outrun the blade of");
    }

    private static void addDeathMessage (TranslationBuilder translationBuilder, DamageTypeContainer container, String message) {
        translationBuilder.add(container.getTranslationKey(), "%1$s " + message + " %2$s");
        translationBuilder.add(container.getTranslationKey("item"), "%1$s " + message + " %2$s using %3$s");
    }
}
