package com.provismet.dualswords.registry;

import com.provismet.dualswords.DualSwordsMain;
import com.provismet.dualswords.enchantments.DaishoEnchantment;
import com.provismet.dualswords.enchantments.DeflectEnchantment;
import com.provismet.dualswords.enchantments.ForcefulEnchantment;
import com.provismet.dualswords.enchantments.LungeEnchantment;
import com.provismet.dualswords.enchantments.ParryEnchantment;
import com.provismet.dualswords.enchantments.RiposteEnchantment;
import com.provismet.dualswords.enchantments.ThrustingEnchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class DSEnchantments {
    public static final ParryEnchantment PARRY = new ParryEnchantment();
    public static final RiposteEnchantment RIPOSTE = new RiposteEnchantment();
    public static final DeflectEnchantment DEFLECT = new DeflectEnchantment();

    public static final LungeEnchantment LUNGE = new LungeEnchantment();
    public static final ThrustingEnchantment THRUST = new ThrustingEnchantment();
    public static final ForcefulEnchantment FORCEFUL = new ForcefulEnchantment();

    public static final DaishoEnchantment DAISHO = new DaishoEnchantment();

    private static void register (Enchantment enchantment, String name) {
        Registry.register(Registries.ENCHANTMENT, DualSwordsMain.identifier(name), enchantment);
    }

    public static void register () {
        register(PARRY, "parry");
        register(RIPOSTE, "riposte");
        register(DEFLECT, "deflect");

        register(LUNGE, "lunge");
        register(THRUST, "thrusting");
        register(FORCEFUL, "forceful");

        register(DAISHO, "daisho");
    }
}
