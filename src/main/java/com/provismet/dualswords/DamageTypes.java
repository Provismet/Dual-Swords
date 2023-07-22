package com.provismet.dualswords;

import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public class DamageTypes {
    public static final RegistryKey<DamageType> RIPOSTE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, DualSwordsMain.identifier("riposte"));
}
