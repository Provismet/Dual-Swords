package com.provismet.dualswords.registry;

import com.provismet.CombatPlusCore.utility.tag.CPCItemTags;
import com.provismet.dualswords.DualSwordsMain;
import com.provismet.dualswords.enchantments.DaishoEnchantment;
import com.provismet.dualswords.enchantments.DeflectEnchantment;
import com.provismet.dualswords.enchantments.ForcefulEnchantment;
import com.provismet.dualswords.enchantments.LungeEnchantment;
import com.provismet.dualswords.enchantments.ParryEnchantment;
import com.provismet.dualswords.enchantments.RiposteEnchantment;
import com.provismet.dualswords.enchantments.ThrustingEnchantment;

import com.provismet.dualswords.util.tag.DSDamageTypeTags;
import com.provismet.dualswords.util.tag.DSEnchantmentTags;
import com.provismet.lilylib.container.EnchantmentContainer;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelBasedValue;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.enchantment.effect.value.AddEnchantmentEffect;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.loot.condition.DamageSourcePropertiesLootCondition;
import net.minecraft.predicate.TagPredicate;
import net.minecraft.predicate.entity.DamageSourcePredicate;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.UseAction;

public class DSEnchantments {
    public static final EnchantmentContainer PARRY = new EnchantmentContainer(
        DualSwordsMain.identifier("parry"),
        (itemLookup, enchantmentLookup, damageLookup, blockLookup) -> Enchantment.builder(
            Enchantment.definition(
                itemLookup.getOrThrow(CPCItemTags.OFFHAND_ENCHANTABLE),
                itemLookup.getOrThrow(CPCItemTags.OFFHAND_PRIMARY_ENCHANTABLE),
                5,
                3,
                Enchantment.leveledCost(5, 5),
                Enchantment.leveledCost(45, 5),
                2,
                AttributeModifierSlot.HAND
            )
        ).addNonListEffect(
            DSEnchantmentComponentTypes.USE_ACTION,
            UseAction.BLOCK.name()
        ).addNonListEffect(
            DSEnchantmentComponentTypes.USE_ACTION_DURATION,
            new AddEnchantmentEffect(
                EnchantmentLevelBasedValue.linear(10)
            )
        ).exclusiveSet(
            enchantmentLookup.getOrThrow(DSEnchantmentTags.PARRY_EXCLUSIVE)
        )
    );
    public static final EnchantmentContainer RIPOSTE = new EnchantmentContainer(
        DualSwordsMain.identifier("riposte"),
        (itemLookup, enchantmentLookup, damageLookup, blockLookup) -> Enchantment.builder(
            Enchantment.definition(
                itemLookup.getOrThrow(CPCItemTags.OFFHAND_ENCHANTABLE),
                itemLookup.getOrThrow(CPCItemTags.OFFHAND_PRIMARY_ENCHANTABLE),
                3,
                3,
                Enchantment.leveledCost(5, 5),
                Enchantment.leveledCost(35, 5),
                2,
                AttributeModifierSlot.HAND
            )
        ).addEffect(
            EnchantmentEffectComponentTypes.DAMAGE,
            new AddEnchantmentEffect(
                EnchantmentLevelBasedValue.linear(1.5f)
            ),
            DamageSourcePropertiesLootCondition.builder(
                DamageSourcePredicate.Builder.create().tag(
                    TagPredicate.expected(DSDamageTypeTags.IS_OFFHANDED)
                )
            )
        ).exclusiveSet(
            enchantmentLookup.getOrThrow(DSEnchantmentTags.PARRY_BONUS_EXCLUSIVE)
        )
    );
    public static final EnchantmentContainer DEFLECT = new EnchantmentContainer(
        DualSwordsMain.identifier("deflect"),
        (itemLookup, enchantmentLookup, damageLookup, blockLookup) -> Enchantment.builder(
            Enchantment.definition(
                itemLookup.getOrThrow(CPCItemTags.OFFHAND_ENCHANTABLE),
                itemLookup.getOrThrow(CPCItemTags.OFFHAND_PRIMARY_ENCHANTABLE),
                3,
                3,
                Enchantment.leveledCost(5, 5),
                Enchantment.leveledCost(25, 5),
                2,
                AttributeModifierSlot.HAND
            )
        ).addEffect(
            DSEnchantmentComponentTypes.DEFLECTION_SPEED,
            new AddEnchantmentEffect(
                EnchantmentLevelBasedValue.linear(3)
            )
        ).exclusiveSet(
            enchantmentLookup.getOrThrow(DSEnchantmentTags.PARRY_BONUS_EXCLUSIVE)
        )
    );

    public static final EnchantmentContainer LUNGE = new EnchantmentContainer(
        DualSwordsMain.identifier("lunge"),
        (itemLookup, enchantmentLookup, damageLookup, blockLookup) -> Enchantment.builder(
            Enchantment.definition(
                itemLookup.getOrThrow(CPCItemTags.OFFHAND_ENCHANTABLE),
                itemLookup.getOrThrow(CPCItemTags.OFFHAND_PRIMARY_ENCHANTABLE),
                5,
                3,
                Enchantment.leveledCost(5, 5),
                Enchantment.leveledCost(45, 5),
                2,
                AttributeModifierSlot.HAND
            )
        ).addNonListEffect(
            DSEnchantmentComponentTypes.USE_ACTION,
            UseAction.SPEAR.name()
        ).addEffect(
            DSEnchantmentComponentTypes.LUNGE_POWER,
            new AddEnchantmentEffect(
                EnchantmentLevelBasedValue.linear(0.5f)
            )
        ).exclusiveSet(
            enchantmentLookup.getOrThrow(DSEnchantmentTags.LUNGE_EXCLUSIVE)
        )
    );
    public static final ThrustingEnchantment THRUST = new ThrustingEnchantment();
    public static final ForcefulEnchantment FORCEFUL = new ForcefulEnchantment();

    public static final DaishoEnchantment DAISHO = new DaishoEnchantment();
}
