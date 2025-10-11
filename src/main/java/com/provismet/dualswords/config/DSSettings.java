package com.provismet.dualswords.config;

import com.provismet.CombatPlusCore.utility.CPCConfig;
import com.provismet.dualswords.DualSwordsMain;
import com.provismet.lilylib.util.json.JsonBuilder;
import com.provismet.lilylib.util.json.JsonReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

public class DSSettings {
    private static final Path FILE = CPCConfig.getConfigDirectory().resolve("dualswords.json");

    private static boolean overrideDatapacks = true;

    public static void write () {
        String jsonString = new JsonBuilder()
            .append(CPCConfig.KEY_OVERRIDE_DATAPACK_LOOT_TABLES, overrideDatapacks)
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
                .flatMap(reader -> reader.getBoolean(CPCConfig.KEY_OVERRIDE_DATAPACK_LOOT_TABLES)).ifPresent(val -> DSSettings.overrideDatapacks = val);
        }
        catch (FileNotFoundException e) {
            DualSwordsMain.LOGGER.info("No config found for Dual Swords, creating one now.");
            DSSettings.write();
        }
        catch (Exception e2) {
            DualSwordsMain.LOGGER.error("Error whilst parsing config:", e2);
        }
    }

    public static boolean shouldOverrideDatapacks () {
        return DSSettings.overrideDatapacks;
    }
}
