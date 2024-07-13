package com.provismet.datagen.dualswords;

import com.provismet.dualswords.DSDamageTypes;
import com.provismet.dualswords.util.tag.DSDamageTypeTags;
import com.provismet.lilylib.datagen.tag.LilyTagProviders;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class DamageTypeTagGenerator extends LilyTagProviders.LilyDamageTypeTagProvider {
    public DamageTypeTagGenerator (FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure (RegistryWrapper.WrapperLookup wrapperLookup) {
        getOrCreateTagBuilder(DSDamageTypeTags.IS_OFFHANDED)
            .add(DSDamageTypes.LUNGE.getKey())
            .add(DSDamageTypes.RIPOSTE.getKey());

        getOrCreateTagBuilder(DSDamageTypeTags.BYPASSES_PARRY)
            .add(DSDamageTypes.RIPOSTE.getKey())
            .addOptionalTag(DamageTypeTags.BYPASSES_SHIELD)
            .addOptionalTag(DamageTypeTags.IS_EXPLOSION)
            .addOptionalTag(Identifier.of("proviorigins", "disables_shields"));

        getOrCreateTagBuilder(TagKey.of(RegistryKeys.DAMAGE_TYPE, Identifier.of("extended-enchanting", "melee")))
            .addOptionalTag(DSDamageTypeTags.IS_OFFHANDED);
    }
}
