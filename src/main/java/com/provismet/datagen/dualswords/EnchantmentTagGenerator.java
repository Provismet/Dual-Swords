package com.provismet.datagen.dualswords;

import com.provismet.CombatPlusCore.utility.tag.CPCEnchantmentTags;
import com.provismet.dualswords.registry.DSEnchantments;
import com.provismet.dualswords.util.tag.DSEnchantmentTags;
import com.provismet.lilylib.datagen.tag.LilyTagProviders;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.registry.tag.EnchantmentTags;

import java.util.concurrent.CompletableFuture;

public class EnchantmentTagGenerator extends LilyTagProviders.LilyEnchantmentTagProvider {
    public EnchantmentTagGenerator(FabricDataOutput output, CompletableFuture<WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure (WrapperLookup arg) {
        this.builder(CPCEnchantmentTags.OFFHAND)
            .add(DSEnchantments.PARRY.getKey())
            .add(DSEnchantments.RIPOSTE.getKey())
            .add(DSEnchantments.DEFLECT.getKey())
            .add(DSEnchantments.LUNGE.getKey())
            .add(DSEnchantments.FORCEFUL.getKey())
            .add(DSEnchantments.THRUSTING.getKey())
            .add(DSEnchantments.DAISHO.getKey());

        this.builder(DSEnchantmentTags.PARRY)
            .add(DSEnchantments.PARRY.getKey())
            .addOptionalTag(DSEnchantmentTags.REQUIRES_PARRY);

        this.builder(DSEnchantmentTags.LUNGE)
            .add(DSEnchantments.LUNGE.getKey())
            .addOptionalTag(DSEnchantmentTags.REQUIRES_LUNGE);

        this.builder(DSEnchantmentTags.REQUIRES_PARRY)
            .add(DSEnchantments.RIPOSTE.getKey())
            .add(DSEnchantments.DEFLECT.getKey());

        this.builder(DSEnchantmentTags.REQUIRES_LUNGE)
            .add(DSEnchantments.THRUSTING.getKey())
            .add(DSEnchantments.FORCEFUL.getKey());

        this.builder(DSEnchantmentTags.PARRY_EXCLUSIVE)
            .addOptionalTag(DSEnchantmentTags.LUNGE)
            .addOptionalTag(CPCEnchantmentTags.OFFHAND_EXCLUSIVE);

        this.builder(DSEnchantmentTags.LUNGE_EXCLUSIVE)
            .addOptionalTag(DSEnchantmentTags.PARRY)
            .addOptionalTag(CPCEnchantmentTags.OFFHAND_EXCLUSIVE);

        this.builder(DSEnchantmentTags.PARRY_BONUS_EXCLUSIVE)
            .add(DSEnchantments.RIPOSTE.getKey())
            .add(DSEnchantments.DEFLECT.getKey())
            .addOptionalTag(CPCEnchantmentTags.OFFHAND_EXCLUSIVE);

        this.builder(DSEnchantmentTags.LUNGE_BONUS_EXCLUSIVE)
            .add(DSEnchantments.THRUSTING.getKey())
            .add(DSEnchantments.FORCEFUL.getKey())
            .addOptionalTag(CPCEnchantmentTags.OFFHAND_EXCLUSIVE);

        this.builder(DSEnchantmentTags.OFFHAND_DAMAGE_EXCLUSIVE)
            .add(DSEnchantments.RIPOSTE.getKey())
            .add(DSEnchantments.THRUSTING.getKey())
            .add(DSEnchantments.DAISHO.getKey())
            .addOptionalTag(CPCEnchantmentTags.OFFHAND_EXCLUSIVE);

        this.builder(DSEnchantmentTags.REVERSE_RENDER)
            .addOptionalTag(DSEnchantmentTags.PARRY);

        this.builder(DSEnchantmentTags.FLIPPED_SPEAR)
            .addOptionalTag(DSEnchantmentTags.LUNGE);

        this.builder(EnchantmentTags.TREASURE)
            .add(DSEnchantments.RIPOSTE.getKey())
            .add(DSEnchantments.DEFLECT.getKey())
            .add(DSEnchantments.THRUSTING.getKey())
            .add(DSEnchantments.FORCEFUL.getKey())
            .add(DSEnchantments.DAISHO.getKey());

        this.builder(EnchantmentTags.NON_TREASURE)
            .add(DSEnchantments.PARRY.getKey())
            .add(DSEnchantments.LUNGE.getKey());

        this.builder(EnchantmentTags.IN_ENCHANTING_TABLE)
            .add(DSEnchantments.PARRY.getKey())
            .add(DSEnchantments.LUNGE.getKey());

        this.builder(EnchantmentTags.TRADEABLE)
            .addOptionalTag(DSEnchantmentTags.PARRY)
            .addOptionalTag(DSEnchantmentTags.LUNGE);
    }
}
