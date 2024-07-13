package com.provismet.datagen.dualswords;

import java.util.concurrent.CompletableFuture;

import com.provismet.CombatPlusCore.utility.tag.CPCEnchantmentTags;
import com.provismet.dualswords.registry.DSEnchantments;

import com.provismet.dualswords.util.tag.DSEnchantmentTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider.EnchantmentTagProvider;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.registry.tag.EnchantmentTags;

public class EnchantmentTagGenerator extends EnchantmentTagProvider {
    public EnchantmentTagGenerator(FabricDataOutput output, CompletableFuture<WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure (WrapperLookup arg) {
        getOrCreateTagBuilder(CPCEnchantmentTags.OFFHAND)
            .add(DSEnchantments.PARRY.getKey())
            .add(DSEnchantments.RIPOSTE.getKey())
            .add(DSEnchantments.DEFLECT.getKey())
            .add(DSEnchantments.LUNGE.getKey())
            .add(DSEnchantments.FORCEFUL.getKey())
            .add(DSEnchantments.THRUSTING.getKey())
            .add(DSEnchantments.DAISHO.getKey());

        getOrCreateTagBuilder(DSEnchantmentTags.PARRY)
            .add(DSEnchantments.PARRY.getKey())
            .add(DSEnchantments.RIPOSTE.getKey())
            .add(DSEnchantments.DEFLECT.getKey());

        getOrCreateTagBuilder(DSEnchantmentTags.LUNGE)
            .add(DSEnchantments.LUNGE.getKey())
            .add(DSEnchantments.THRUSTING.getKey())
            .add(DSEnchantments.FORCEFUL.getKey());

        getOrCreateTagBuilder(DSEnchantmentTags.PARRY_EXCLUSIVE)
            .addOptionalTag(DSEnchantmentTags.LUNGE)
            .addOptionalTag(CPCEnchantmentTags.OFFHAND_EXCLUSIVE);

        getOrCreateTagBuilder(DSEnchantmentTags.LUNGE_EXCLUSIVE)
            .addOptionalTag(DSEnchantmentTags.PARRY)
            .addOptionalTag(CPCEnchantmentTags.OFFHAND_EXCLUSIVE);

        getOrCreateTagBuilder(DSEnchantmentTags.PARRY_BONUS_EXCLUSIVE)
            .add(DSEnchantments.RIPOSTE.getKey())
            .add(DSEnchantments.DEFLECT.getKey())
            .addOptionalTag(CPCEnchantmentTags.OFFHAND_EXCLUSIVE);

        getOrCreateTagBuilder(DSEnchantmentTags.LUNGE_BONUS_EXCLUSIVE)
            .add(DSEnchantments.THRUSTING.getKey())
            .add(DSEnchantments.FORCEFUL.getKey())
            .addOptionalTag(CPCEnchantmentTags.OFFHAND_EXCLUSIVE);

        getOrCreateTagBuilder(DSEnchantmentTags.OFFHAND_DAMAGE_EXCLUSIVE)
            .add(DSEnchantments.RIPOSTE.getKey())
            .add(DSEnchantments.THRUSTING.getKey())
            .add(DSEnchantments.DAISHO.getKey())
            .addOptionalTag(CPCEnchantmentTags.OFFHAND_EXCLUSIVE);

        getOrCreateTagBuilder(DSEnchantmentTags.REVERSE_RENDER)
            .addOptionalTag(DSEnchantmentTags.PARRY);

        getOrCreateTagBuilder(DSEnchantmentTags.FLIPPED_SPEAR)
            .addOptionalTag(DSEnchantmentTags.LUNGE);

        getOrCreateTagBuilder(EnchantmentTags.TREASURE)
            .add(DSEnchantments.RIPOSTE.getKey())
            .add(DSEnchantments.DEFLECT.getKey())
            .add(DSEnchantments.THRUSTING.getKey())
            .add(DSEnchantments.FORCEFUL.getKey())
            .add(DSEnchantments.DAISHO.getKey());

        getOrCreateTagBuilder(EnchantmentTags.NON_TREASURE)
            .add(DSEnchantments.PARRY.getKey())
            .add(DSEnchantments.LUNGE.getKey());

        getOrCreateTagBuilder(EnchantmentTags.IN_ENCHANTING_TABLE)
            .add(DSEnchantments.PARRY.getKey())
            .add(DSEnchantments.LUNGE.getKey());

        getOrCreateTagBuilder(EnchantmentTags.TRADEABLE)
            .addOptionalTag(DSEnchantmentTags.PARRY)
            .addOptionalTag(DSEnchantmentTags.LUNGE);
    }
}
