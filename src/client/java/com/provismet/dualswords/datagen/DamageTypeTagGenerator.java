package com.provismet.dualswords.datagen;

import com.provismet.combatplus.api.tag.CombatPlusDamageTypeTags;
import com.provismet.dualswords.DSDamageTypes;
import com.provismet.dualswords.util.tag.DSDamageTypeTags;
import com.provismet.lilylib.datagen.tag.LilyTagProviders;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import java.util.concurrent.CompletableFuture;

public class DamageTypeTagGenerator extends LilyTagProviders.LilyDamageTypeTagProvider {
    public DamageTypeTagGenerator (FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags (HolderLookup.Provider wrapperLookup) {
        this.builder(DSDamageTypeTags.IS_OFFHANDED)
            .add(DSDamageTypes.LUNGE.getKey())
            .add(DSDamageTypes.RIPOSTE.getKey());

        this.builder(DSDamageTypeTags.BYPASSES_PARRY)
            .add(DSDamageTypes.RIPOSTE.getKey())
            .addOptionalTag(DamageTypeTags.BYPASSES_SHIELD)
            .addOptionalTag(DamageTypeTags.IS_EXPLOSION)
            .addOptionalTag(TagKey.create(Registries.DAMAGE_TYPE, Identifier.fromNamespaceAndPath("proviorigins", "disables_shields")));

        this.builder(CombatPlusDamageTypeTags.IS_MELEE)
            .addOptionalTag(DSDamageTypeTags.IS_OFFHANDED);
    }
}
