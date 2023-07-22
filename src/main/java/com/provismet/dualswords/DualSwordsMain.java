package com.provismet.dualswords;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.provismet.dualswords.enchantments.Enchantments;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

public class DualSwordsMain implements ModInitializer {
    public static final String MODID = "dualswords";
    public static final Logger LOGGER = LoggerFactory.getLogger("Dual Swords");

    public static Identifier identifier (String path) {
        return new Identifier(MODID, path);
    }

    @Override
    public void onInitialize () {
        Enchantments.register();
    }
    
}
