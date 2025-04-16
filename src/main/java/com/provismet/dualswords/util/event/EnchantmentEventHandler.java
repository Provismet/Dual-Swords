package com.provismet.dualswords.util.event;

import com.provismet.dualswords.registry.DSEnchantments;
import com.provismet.dualswords.util.tag.DSEnchantmentTags;
import net.fabricmc.fabric.api.item.v1.EnchantmentEvents;
import net.fabricmc.fabric.api.util.TriState;

public abstract class EnchantmentEventHandler {
    public static void registerAllowEnchanting () {
        EnchantmentEvents.ALLOW_ENCHANTING.register((enchantment, target, enchantingContext) -> {
            if (enchantment.isIn(DSEnchantmentTags.REQUIRES_PARRY) && target.getEnchantments().getEnchantments().stream().noneMatch(entry -> entry.matchesKey(DSEnchantments.PARRY.getKey()))) {
                return TriState.FALSE;
            }
            else if (enchantment.isIn(DSEnchantmentTags.REQUIRES_LUNGE) && target.getEnchantments().getEnchantments().stream().noneMatch(entry -> entry.matchesKey(DSEnchantments.LUNGE.getKey()))) {
                return TriState.FALSE;
            }
            return TriState.DEFAULT;
        });
    }
}
