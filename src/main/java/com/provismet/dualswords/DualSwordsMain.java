package com.provismet.dualswords;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.provismet.dualswords.enchantments.Enchantments;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.EnchantRandomlyLootFunction;
import net.minecraft.loot.provider.number.BinomialLootNumberProvider;
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

        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
            if (source.isBuiltin()) {
                if (LootTables.STRONGHOLD_LIBRARY_CHEST.equals(id)) {
                    LootPool.Builder lootPool = LootPool.builder().rolls(BinomialLootNumberProvider.create(1, 0.333f));
                    lootPool.with(ItemEntry.builder(Items.BOOK).apply(new EnchantRandomlyLootFunction.Builder().add(Enchantments.DAISHO)));
                    tableBuilder.pool(lootPool);
                }
                else if (LootTables.WOODLAND_MANSION_CHEST.equals(id)) {
                    LootPool.Builder lootPool = LootPool.builder().rolls(BinomialLootNumberProvider.create(1, 0.2f));
                    lootPool.with(ItemEntry.builder(Items.BOOK).apply(new EnchantRandomlyLootFunction.Builder().add(Enchantments.DAISHO)));
                    tableBuilder.pool(lootPool);
                }
                else if (LootTables.PILLAGER_OUTPOST_CHEST.equals(id)) {
                    LootPool.Builder lootPool = LootPool.builder().rolls(BinomialLootNumberProvider.create(1, 0.15f));
                    lootPool.with(ItemEntry.builder(Items.BOOK).apply(new EnchantRandomlyLootFunction.Builder().add(Enchantments.DAISHO)));
                    tableBuilder.pool(lootPool);
                }
            }
            if (LootTables.HERO_OF_THE_VILLAGE_WEAPONSMITH_GIFT_GAMEPLAY.equals(id)) {
                LootPool.Builder lootPool = LootPool.builder().rolls(BinomialLootNumberProvider.create(1, 0.05f));
                lootPool.with(ItemEntry.builder(Items.BOOK).apply(new EnchantRandomlyLootFunction.Builder().add(Enchantments.DAISHO)));
                tableBuilder.pool(lootPool);
            }
        });
    }
    
}
