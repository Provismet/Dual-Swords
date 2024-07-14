package com.provismet.dualswords.registry;

import com.provismet.CombatPlusCore.enchantment.effect.singleEntity.CodeExecutionSingleEntityEffect;
import com.provismet.CombatPlusCore.enchantment.loot.condition.singleEntity.ApplyToAttackerCondition;
import com.provismet.CombatPlusCore.enchantment.loot.condition.singleEntity.SingleEntityLambdaCondition;
import com.provismet.CombatPlusCore.registries.CPCEnchantmentComponentTypes;
import com.provismet.CombatPlusCore.utility.tag.CPCItemTags;
import com.provismet.dualswords.DualSwordsMain;
import com.provismet.dualswords.enchantment.effect.entity.CooldownEffect;
import com.provismet.dualswords.enchantment.effect.stopped.ApplyToUserEffect;
import com.provismet.dualswords.enchantment.effect.stopped.ReverseScalingCooldownEffect;

import com.provismet.dualswords.util.tag.DSDamageTypeTags;
import com.provismet.dualswords.util.tag.DSEnchantmentTags;
import com.provismet.lilylib.container.EnchantmentContainer;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelBasedValue;
import net.minecraft.enchantment.effect.value.AddEnchantmentEffect;
import net.minecraft.loot.condition.DamageSourcePropertiesLootCondition;
import net.minecraft.predicate.TagPredicate;
import net.minecraft.predicate.entity.DamageSourcePredicate;
import net.minecraft.registry.Registerable;
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
        ).addEffect(
            DSEnchantmentComponentTypes.ON_FINISHED_USING,
            new CooldownEffect(
                EnchantmentLevelBasedValue.constant(30)
            )
        ).addEffect(
            DSEnchantmentComponentTypes.ON_STOPPED_USING,
            new ReverseScalingCooldownEffect(
                EnchantmentLevelBasedValue.constant(30)
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
            DSEnchantmentComponentTypes.ON_STOPPED_USING,
            new ApplyToUserEffect(
                new CodeExecutionSingleEntityEffect(DualSwordsMain.identifier("lunge")),
                EnchantmentLevelBasedValue.constant(9)
            )
        ).addEffect(
            DSEnchantmentComponentTypes.ON_STOPPED_USING,
            new ApplyToUserEffect(
                new CooldownEffect(
                    EnchantmentLevelBasedValue.constant(60)
                ),
                EnchantmentLevelBasedValue.constant(9)
            )
        ).exclusiveSet(
            enchantmentLookup.getOrThrow(DSEnchantmentTags.LUNGE_EXCLUSIVE)
        )
    );
    public static final EnchantmentContainer THRUSTING = new EnchantmentContainer(
        DualSwordsMain.identifier("thrusting"),
        (itemLookup, enchantmentLookup, damageLookup, blockLookup) -> Enchantment.builder(
            Enchantment.definition(
                itemLookup.getOrThrow(CPCItemTags.OFFHAND_ENCHANTABLE),
                itemLookup.getOrThrow(CPCItemTags.OFFHAND_PRIMARY_ENCHANTABLE),
                3,
                2,
                Enchantment.leveledCost(5, 10),
                Enchantment.leveledCost(50, 10),
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
            enchantmentLookup.getOrThrow(DSEnchantmentTags.LUNGE_BONUS_EXCLUSIVE)
        )
    );
    public static final EnchantmentContainer FORCEFUL = new EnchantmentContainer(
        DualSwordsMain.identifier("forceful"),
        (itemLookup, enchantmentLookup, damageLookup, blockLookup) -> Enchantment.builder(
            Enchantment.definition(
                itemLookup.getOrThrow(CPCItemTags.OFFHAND_ENCHANTABLE),
                itemLookup.getOrThrow(CPCItemTags.OFFHAND_PRIMARY_ENCHANTABLE),
                3,
                2,
                Enchantment.leveledCost(5, 10),
                Enchantment.leveledCost(20, 10),
                4,
                AttributeModifierSlot.HAND
            )
        ).addEffect(
            EnchantmentEffectComponentTypes.KNOCKBACK,
            new AddEnchantmentEffect(
                EnchantmentLevelBasedValue.linear(1)
            ),
            DamageSourcePropertiesLootCondition.builder(
                DamageSourcePredicate.Builder.create().tag(
                    TagPredicate.expected(DSDamageTypeTags.IS_OFFHANDED)
                )
            )
        ).exclusiveSet(
            enchantmentLookup.getOrThrow(DSEnchantmentTags.LUNGE_BONUS_EXCLUSIVE)
        )
    );

    public static final EnchantmentContainer DAISHO = new EnchantmentContainer(
        DualSwordsMain.identifier("daisho"),
        (itemLookup, enchantmentLookup, damageLookup, blockLookup) -> Enchantment.builder(
            Enchantment.definition(
                itemLookup.getOrThrow(CPCItemTags.OFFHAND_ENCHANTABLE),
                itemLookup.getOrThrow(CPCItemTags.OFFHAND_PRIMARY_ENCHANTABLE),
                2,
                5,
                Enchantment.leveledCost(20, 5),
                Enchantment.leveledCost(30, 10),
                8,
                AttributeModifierSlot.OFFHAND
            )
        ).addEffect(
            CPCEnchantmentComponentTypes.BONUS_DAMAGE,
            new AddEnchantmentEffect(
                EnchantmentLevelBasedValue.linear(0.8f)
            ),
            ApplyToAttackerCondition.builder(
                SingleEntityLambdaCondition.builder(DualSwordsMain.identifier("dual_wielder"))
            )
        ).addEffect(
            DSEnchantmentComponentTypes.MODIFY_COOLDOWN,
            new AddEnchantmentEffect(
                EnchantmentLevelBasedValue.linear(8)
            )
        )
    );

    public static void bootstrap (Registerable<Enchantment> registerable) {
        register(registerable, PARRY);
        register(registerable, RIPOSTE);
        register(registerable, DEFLECT);
        register(registerable, LUNGE);
        register(registerable, THRUSTING);
        register(registerable, FORCEFUL);
        register(registerable, DAISHO);
    }

    private static void register (Registerable<Enchantment> registerable, EnchantmentContainer container) {
        registerable.register(container.getKey(), container.getBuilder(registerable).build(container.getKey().getValue()));
    }
}
