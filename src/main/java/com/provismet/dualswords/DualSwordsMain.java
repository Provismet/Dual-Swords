package com.provismet.dualswords;

import com.provismet.CombatPlusCore.loot.functions.EnchantRandomlyFromKeyLootFunction;
import com.provismet.dualswords.registry.DSEnchantmentComponentTypes;
import com.provismet.dualswords.registry.DSLambdas;
import com.provismet.dualswords.registry.OnStoppedUsingEffects;
import com.provismet.dualswords.util.event.EnchantmentEventHandler;
import com.provismet.dualswords.util.event.ItemEvents;
import com.provismet.dualswords.util.registry.DSRegistries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.provismet.dualswords.config.DSSettings;
import com.provismet.dualswords.registry.DSEnchantments;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.BinomialLootNumberProvider;
import net.minecraft.util.Identifier;

public class DualSwordsMain implements ModInitializer {
    public static final String MODID = "dualswords";
    public static final Logger LOGGER = LoggerFactory.getLogger("Dual Swords");
    
    public static Identifier identifier (String path) {
        return Identifier.of(MODID, path);
    }

    @Override
    public void onInitialize () {
        DSRegistries.init();
        DSEnchantmentComponentTypes.init();
        OnStoppedUsingEffects.register();
        DSLambdas.register();
        DSSettings.read();
        ItemEvents.RegisterComponentPhase();
        EnchantmentEventHandler.registerAllowEnchanting();

        LootTableEvents.MODIFY.register((key, tableBuilder, source, wrapperLookup) -> {
            if (source.isBuiltin() || DSSettings.shouldOverrideDatapacks()) {
                if (LootTables.STRONGHOLD_LIBRARY_CHEST.equals(key)) {
                    LootPool.Builder lootPool = LootPool.builder().rolls(BinomialLootNumberProvider.create(1, 0.333f));
                    lootPool.with(ItemEntry.builder(Items.BOOK).apply(EnchantRandomlyFromKeyLootFunction.create().option(DSEnchantments.DAISHO)));
                    tableBuilder.pool(lootPool);
                }
                else if (LootTables.WOODLAND_MANSION_CHEST.equals(key)) {
                    LootPool.Builder lootPool = LootPool.builder().rolls(BinomialLootNumberProvider.create(1, 0.2f));
                    lootPool.with(ItemEntry.builder(Items.BOOK).apply(EnchantRandomlyFromKeyLootFunction.create().option(DSEnchantments.DAISHO)));
                    tableBuilder.pool(lootPool);
                }
                else if (LootTables.PILLAGER_OUTPOST_CHEST.equals(key)) {
                    LootPool.Builder lootPool = LootPool.builder().rolls(BinomialLootNumberProvider.create(1, 0.15f));
                    lootPool.with(ItemEntry.builder(Items.BOOK).apply(EnchantRandomlyFromKeyLootFunction.create().option(DSEnchantments.DAISHO)));
                    tableBuilder.pool(lootPool);
                }
                else if (LootTables.HERO_OF_THE_VILLAGE_WEAPONSMITH_GIFT_GAMEPLAY.equals(key)) {
                    LootPool.Builder lootPool = LootPool.builder().rolls(BinomialLootNumberProvider.create(1, 0.05f));
                    lootPool.with(ItemEntry.builder(Items.BOOK).apply(EnchantRandomlyFromKeyLootFunction.create().option(DSEnchantments.DAISHO)));
                    tableBuilder.pool(lootPool);
                }
            }
        });
    }
}
