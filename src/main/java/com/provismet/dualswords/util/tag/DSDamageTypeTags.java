package com.provismet.dualswords.util.tag;

import com.provismet.dualswords.DualSwordsMain;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;

public abstract class DSDamageTypeTags {
    public static final TagKey<DamageType> IS_OFFHANDED = DSDamageTypeTags.of("is_offhanded");
    public static final TagKey<DamageType> BYPASSES_PARRY = DSDamageTypeTags.of("bypasses_parry");

    private static TagKey<DamageType> of (String path) {
        return TagKey.create(Registries.DAMAGE_TYPE, DualSwordsMain.identifier(path));
    }
}
