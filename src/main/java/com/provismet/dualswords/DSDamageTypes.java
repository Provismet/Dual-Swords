package com.provismet.dualswords;

import com.provismet.lilylib.container.DamageTypeContainer;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.damagesource.DamageType;

public class DSDamageTypes {
    public static final DamageTypeContainer RIPOSTE = createDamageType("riposte");
    public static final DamageTypeContainer LUNGE = createDamageType("lunge");

    private static DamageTypeContainer createDamageType (String name) {
        return new DamageTypeContainer(DualSwordsMain.identifier(name), new DamageType(name, 0.1f));
    }

    public static void bootstrap (BootstrapContext<DamageType> registerable) {
        registerable.register(RIPOSTE.getKey(), RIPOSTE.getDamageType());
        registerable.register(LUNGE.getKey(), LUNGE.getDamageType());
    }
}
