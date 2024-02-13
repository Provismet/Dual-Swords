package com.provismet.dualswords.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.stream.JsonReader;
import com.provismet.dualswords.DualSwordsMain;
import com.provismet.lilylib.util.JsonBuilder;

public class DSSettings {
    private static boolean overrideDatapacks = true;

    public static void write () {
        JsonBuilder builder = new JsonBuilder();
        String jsonString = builder.start()
            .append("override_datapack_loot_tables", overrideDatapacks).newLine(false)
            .closeObject()
            .toString();
        
        try {
            FileWriter writer = new FileWriter("config/combat-plus/dualswords.json");
            writer.write(jsonString);
            writer.close();
        }
        catch (IOException e) {
            DualSwordsMain.LOGGER.error("Error whilst saving config: ", e);
        }
    }

    public static void read () {
        try {
            FileReader reader = new FileReader("config/combat-plus/dualswords.json");
            JsonReader parser = new JsonReader(reader);
            
            parser.beginObject();
            while (parser.hasNext()) {
                String name = parser.nextName();
                switch (name) {
                    case "override_datapack_loot_tables":
                        DSSettings.overrideDatapacks = parser.nextBoolean();
                        break;
                
                    default:
                        break;
                }
            }
            parser.endObject();
            parser.close();
        }
        catch (FileNotFoundException e) {
            DualSwordsMain.LOGGER.info("No config found for Dual Swords, creating one now.");
            try {
                (new File("config/combat-plus")).mkdirs();
            }
            catch (Exception e3) {

            }
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
