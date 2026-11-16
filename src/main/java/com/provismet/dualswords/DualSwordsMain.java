package com.provismet.dualswords;

import com.provismet.dualswords.config.DSSettings;
import com.provismet.dualswords.registry.DSEnchantmentComponentTypes;
import com.provismet.dualswords.registry.DSEnchantments;
import com.provismet.dualswords.registry.DSLambdas;
import com.provismet.dualswords.registry.OnStoppedUsingEffects;
import com.provismet.dualswords.util.event.EnchantmentEventHandler;
import com.provismet.dualswords.util.event.ItemEvents;
import com.provismet.dualswords.util.registry.DSRegistries;
import com.provismet.lilylib.container.EnchantmentContainer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

public class DualSwordsMain implements ModInitializer {
    public static final String MODID = "dualswords";
    public static final Logger LOGGER = LoggerFactory.getLogger("Dual Swords");
    
    public static Identifier identifier (String path) {
        return Identifier.fromNamespaceAndPath(MODID, path);
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
            Function<EnchantmentContainer, LootItemFunction.Builder> singleEnchantment = container -> new EnchantRandomlyFunction.Builder().withEnchantment(container.getEntryOrThrow(wrapperLookup));
            
            if (source.isBuiltin() || DSSettings.shouldOverrideDatapacks()) {
                if (BuiltInLootTables.STRONGHOLD_LIBRARY.equals(key)) {
                    LootPool.Builder lootPool = LootPool.lootPool().setRolls(ContextIntProviders.binomial(1, 0.333f));
                    lootPool.add(LootItem.lootTableItem(Items.BOOK).apply(singleEnchantment.apply(DSEnchantments.DAISHO)));
                    tableBuilder.withPool(lootPool);
                }
                else if (BuiltInLootTables.WOODLAND_MANSION.equals(key)) {
                    LootPool.Builder lootPool = LootPool.lootPool().setRolls(ContextIntProviders.binomial(1, 0.2f));
                    lootPool.add(LootItem.lootTableItem(Items.BOOK).apply(singleEnchantment.apply(DSEnchantments.DAISHO)));
                    tableBuilder.withPool(lootPool);
                }
                else if (BuiltInLootTables.PILLAGER_OUTPOST.equals(key)) {
                    LootPool.Builder lootPool = LootPool.lootPool().setRolls(ContextIntProviders.binomial(1, 0.15f));
                    lootPool.add(LootItem.lootTableItem(Items.BOOK).apply(singleEnchantment.apply(DSEnchantments.DAISHO)));
                    tableBuilder.withPool(lootPool);
                }
                else if (BuiltInLootTables.WEAPONSMITH_GIFT.equals(key)) {
                    LootPool.Builder lootPool = LootPool.lootPool().setRolls(ContextIntProviders.binomial(1, 0.05f));
                    lootPool.add(LootItem.lootTableItem(Items.BOOK).apply(singleEnchantment.apply(DSEnchantments.DAISHO)));
                    tableBuilder.withPool(lootPool);
                }
            }
        });
    }
}
