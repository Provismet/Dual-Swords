package com.provismet.dualswords.util.tag;

import com.provismet.dualswords.DualSwordsMain;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public abstract class DSEnchantmentTags {
    public static final TagKey<Enchantment> MODIFIES_USE_ACTION = DSEnchantmentTags.of("modifies_use_action");
    public static final TagKey<Enchantment> PARRY = DSEnchantmentTags.of("parry");
    public static final TagKey<Enchantment> LUNGE = DSEnchantmentTags.of("lunge");
    public static final TagKey<Enchantment> REVERSE_RENDER = DSEnchantmentTags.of("render_upside-down");

    // Exclusive Set
    public static final TagKey<Enchantment> PARRY_EXCLUSIVE = DSEnchantmentTags.of("exclusive_set/parry");
    public static final TagKey<Enchantment> PARRY_BONUS_EXCLUSIVE = DSEnchantmentTags.of("exclusive_set/parry_bonus");
    public static final TagKey<Enchantment> LUNGE_EXCLUSIVE = DSEnchantmentTags.of("exclusive_set/lunge");
    public static final TagKey<Enchantment> LUNGE_BONUS_EXCLUSIVE = DSEnchantmentTags.of("exclusive_set/lunge_bonus");
    public static final TagKey<Enchantment> OFFHAND_DAMAGE_EXCLUSIVE = DSEnchantmentTags.of("exclusive_set/offhand_damage");

    private static TagKey<Enchantment> of (String path) {
        return TagKey.of(RegistryKeys.ENCHANTMENT, DualSwordsMain.identifier(path));
    }
}
