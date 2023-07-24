package com.provismet.dualswords.enchantments;

import com.provismet.dualswords.DualSwordsMain;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class Enchantments {
    public static final ParryEnchantment PARRY = new ParryEnchantment();
    public static final RiposteEnchantment RIPOSTE = new RiposteEnchantment();
    public static final DaishoEnchantment DAISHO = new DaishoEnchantment();

    public static void register () {
        Registry.register(Registries.ENCHANTMENT, DualSwordsMain.identifier("parry"), PARRY);
        Registry.register(Registries.ENCHANTMENT, DualSwordsMain.identifier("riposte"), RIPOSTE);
        Registry.register(Registries.ENCHANTMENT, DualSwordsMain.identifier("daisho"), DAISHO);
    }
}
