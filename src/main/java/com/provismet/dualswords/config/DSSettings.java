package com.provismet.dualswords.config;

import com.provismet.CombatPlusCore.utility.CPCConfig;
import com.provismet.dualswords.DualSwordsMain;
import com.provismet.lilylib.util.json.JsonBuilder;
import com.provismet.lilylib.util.json.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

public class DSSettings {
    private static final Path FILE = CPCConfig.getConfigDirectory().resolve("dualswords.json");

    private static boolean overrideDatapacks = true;
    private static boolean allowFlippedSwords = true;
    private static boolean allowFlippedLunge = true;

    public static void write () {
        String jsonString = new JsonBuilder()
            .append(CPCConfig.KEY_OVERRIDE_DATAPACK_LOOT_TABLES, overrideDatapacks)
            .append("render_flipped_swords", allowFlippedSwords)
            .append("render_flipped_lunge", allowFlippedLunge)
            .toString();
        
        try (FileWriter writer = new FileWriter(FILE.toFile())) {
            writer.write(jsonString);
        }
        catch (IOException e) {
            DualSwordsMain.LOGGER.error("Error whilst saving config: ", e);
        }
    }

    public static void read () {
        try {
            Optional.ofNullable(JsonReader.file(FILE.toFile()))
                .ifPresent(reader -> {
                    reader.getBoolean(CPCConfig.KEY_OVERRIDE_DATAPACK_LOOT_TABLES).ifPresent(val -> DSSettings.overrideDatapacks = val);
                    reader.getBoolean("render_flipped_swords").ifPresent(val -> DSSettings.allowFlippedSwords = val);
                    reader.getBoolean("render_flipped_lunge").ifPresent(val -> DSSettings.allowFlippedLunge = val);
                });
        }
        catch (FileNotFoundException e) {
            DualSwordsMain.LOGGER.info("No config found for Dual Swords, creating one now.");
        }
        catch (Exception e2) {
            DualSwordsMain.LOGGER.error("Error whilst parsing config:", e2);
        }
        DSSettings.write();
    }

    public static boolean shouldOverrideDatapacks () {
        return DSSettings.overrideDatapacks;
    }

    public static boolean renderFlippedSwords () {
        return DSSettings.allowFlippedSwords;
    }

    public static boolean renderFlippedLunge () {
        return DSSettings.allowFlippedLunge;
    }
}
