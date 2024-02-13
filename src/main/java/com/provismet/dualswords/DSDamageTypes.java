package com.provismet.dualswords;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public class DSDamageTypes {
    private static final RegistryKey<DamageType> RIPOSTE = createDamageType("riposte");
    private static final RegistryKey<DamageType> LUNGE = createDamageType("lunge");

    public static DamageSource riposte (Entity attacker) {
        return DSDamageTypes.createSource(attacker.getDamageSources(), RIPOSTE, attacker);
    }

    public static DamageSource lunge (Entity attacker) {
        return DSDamageTypes.createSource(attacker.getDamageSources(), LUNGE, attacker);
    }

    private static DamageSource createSource (DamageSources sources, RegistryKey<DamageType> damageType, Entity attacker) {
        return sources.create(damageType, attacker);
    }

    private static RegistryKey<DamageType> createDamageType (String name) {
        return RegistryKey.of(RegistryKeys.DAMAGE_TYPE, DualSwordsMain.identifier(name));
    }
}
