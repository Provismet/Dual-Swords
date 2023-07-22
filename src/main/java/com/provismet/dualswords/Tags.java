package com.provismet.dualswords;

import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class Tags {
    public static TagKey<DamageType> BYPASSES_PARRY = TagKey.of(RegistryKeys.DAMAGE_TYPE, DualSwordsMain.identifier("bypasses_parry"));
}
