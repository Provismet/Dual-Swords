package com.provismet.dualswords.util.tag;

import com.provismet.dualswords.DualSwordsMain;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public abstract class DSDamageTypeTags {
    public static final TagKey<DamageType> IS_OFFHANDED = DSDamageTypeTags.of("is_offhanded");
    public static final TagKey<DamageType> BYPASSES_PARRY = DSDamageTypeTags.of("bypasses_parry");

    private static TagKey<DamageType> of (String path) {
        return TagKey.of(RegistryKeys.DAMAGE_TYPE, DualSwordsMain.identifier(path));
    }
}
