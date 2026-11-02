package com.provismet.dualswords.registry;

import com.provismet.combatplus.api.enchantment.effect.component.BlocksAttacksComponentEntityEffect;
import com.provismet.combatplus.api.enchantment.effect.component.CooldownGroupComponentEntityEffect;
import com.provismet.combatplus.api.enchantment.effect.component.MaxUseTimeComponentEntityEffect;
import com.provismet.combatplus.api.enchantment.effect.doubleEntity.CodeExecutionDoubleEntityEffect;
import com.provismet.combatplus.api.enchantment.effect.singleEntity.CodeExecutionSingleEntityEffect;
import com.provismet.combatplus.api.enchantment.effect.singleEntity.SetCooldownEnchantmentEffect;
import com.provismet.combatplus.api.loot.condition.singleEntity.ApplyToAttackerCondition;
import com.provismet.combatplus.api.loot.condition.singleEntity.SingleEntityLambdaCondition;
import com.provismet.combatplus.api.tag.CombatPlusItemTags;
import com.provismet.combatplus.registries.CombatPlusEnchantmentComponentTypes;
import com.provismet.dualswords.DualSwordsMain;
import com.provismet.dualswords.enchantment.effect.stopped.ApplyToUserEffect;
import com.provismet.dualswords.enchantment.effect.stopped.ReverseScalingCooldownEffect;
import com.provismet.dualswords.util.tag.DSDamageTypeTags;
import com.provismet.dualswords.util.tag.DSEnchantmentTags;
import com.provismet.lilylib.container.EnchantmentContainer;
import net.minecraft.advancements.criterion.DamageSourcePredicate;
import net.minecraft.advancements.criterion.TagPredicate;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.component.BlocksAttacks;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.AddValue;
import net.minecraft.world.level.storage.loot.predicates.DamageSourceCondition;

import java.util.List;
import java.util.Optional;

public class DSEnchantments {
    public static final EnchantmentContainer PARRY = new EnchantmentContainer(
        DualSwordsMain.identifier("parry"),
        (itemLookup, enchantmentLookup, damageLookup, blockLookup, entityLookup) -> Enchantment.enchantment(
            Enchantment.definition(
                itemLookup.getOrThrow(CombatPlusItemTags.OFFHAND_ENCHANTABLE),
                itemLookup.getOrThrow(CombatPlusItemTags.OFFHAND_PRIMARY_ENCHANTABLE),
                5,
                3,
                Enchantment.dynamicCost(5, 5),
                Enchantment.dynamicCost(45, 5),
                2,
                EquipmentSlotGroup.HAND
            )
        ).withSpecialEffect(
            DSEnchantmentComponentTypes.USE_ACTION,
            ItemUseAnimation.BLOCK.name()
        ).withEffect(
            CombatPlusEnchantmentComponentTypes.POST_BLOCK,
            new CodeExecutionDoubleEntityEffect(DualSwordsMain.identifier("parry"))
        ).withEffect(
            CombatPlusEnchantmentComponentTypes.DATA_COMPONENT,
            new MaxUseTimeComponentEntityEffect(
                LevelBasedValue.perLevel(10)
            )
        ).withEffect(
            DSEnchantmentComponentTypes.ON_FINISHED_USING,
            new SetCooldownEnchantmentEffect(
                LevelBasedValue.constant(30)
            )
        ).withEffect(
            DSEnchantmentComponentTypes.ON_STOPPED_USING,
            new ReverseScalingCooldownEffect(
                LevelBasedValue.constant(30)
            )
        ).withEffect(
            CombatPlusEnchantmentComponentTypes.DATA_COMPONENT,
            new BlocksAttacksComponentEntityEffect(
                LevelBasedValue.constant(0.1f),
                LevelBasedValue.constant(1f),
                List.of(new BlocksAttacks.DamageReduction(90.0F, Optional.empty(), 0.0F, 1.0F)),
                new BlocksAttacks.ItemDamageFunction(3.0F, 1.0F, 1.0F),
                Optional.of(damageLookup.getOrThrow(DSDamageTypeTags.BYPASSES_PARRY)),
                Optional.of(Holder.direct(SoundEvents.PLAYER_ATTACK_SWEEP)),
                Optional.of(SoundEvents.ITEM_BREAK)
            )
        ).withEffect(
            CombatPlusEnchantmentComponentTypes.DATA_COMPONENT,
            new CooldownGroupComponentEntityEffect(DualSwordsMain.identifier("parry_cooldown"))
        ).exclusiveWith(
            enchantmentLookup.getOrThrow(DSEnchantmentTags.PARRY_EXCLUSIVE)
        )
    );
    public static final EnchantmentContainer RIPOSTE = new EnchantmentContainer(
        DualSwordsMain.identifier("riposte"),
        (itemLookup, enchantmentLookup, damageLookup, blockLookup, entityLookup) -> Enchantment.enchantment(
            Enchantment.definition(
                itemLookup.getOrThrow(CombatPlusItemTags.OFFHAND_ENCHANTABLE),
                itemLookup.getOrThrow(CombatPlusItemTags.OFFHAND_PRIMARY_ENCHANTABLE),
                3,
                3,
                Enchantment.dynamicCost(5, 5),
                Enchantment.dynamicCost(35, 5),
                2,
                EquipmentSlotGroup.HAND
            )
        ).withEffect(
            EnchantmentEffectComponents.DAMAGE,
            new AddValue(
                LevelBasedValue.perLevel(1.5f)
            ),
            DamageSourceCondition.hasDamageSource(
                DamageSourcePredicate.Builder.damageType().tag(
                    TagPredicate.is(DSDamageTypeTags.IS_OFFHANDED)
                )
            )
        ).exclusiveWith(
            enchantmentLookup.getOrThrow(DSEnchantmentTags.PARRY_BONUS_EXCLUSIVE)
        )
    );
    public static final EnchantmentContainer DEFLECT = new EnchantmentContainer(
        DualSwordsMain.identifier("deflect"),
        (itemLookup, enchantmentLookup, damageLookup, blockLookup, entityLookup) -> Enchantment.enchantment(
            Enchantment.definition(
                itemLookup.getOrThrow(CombatPlusItemTags.OFFHAND_ENCHANTABLE),
                itemLookup.getOrThrow(CombatPlusItemTags.OFFHAND_PRIMARY_ENCHANTABLE),
                3,
                3,
                Enchantment.dynamicCost(5, 5),
                Enchantment.dynamicCost(25, 5),
                2,
                EquipmentSlotGroup.HAND
            )
        ).withEffect(
            DSEnchantmentComponentTypes.DEFLECTION_SPEED,
            new AddValue(
                LevelBasedValue.perLevel(3)
            )
        ).exclusiveWith(
            enchantmentLookup.getOrThrow(DSEnchantmentTags.PARRY_BONUS_EXCLUSIVE)
        )
    );

    public static final EnchantmentContainer LUNGE = new EnchantmentContainer(
        DualSwordsMain.identifier("lunge"),
        (itemLookup, enchantmentLookup, damageLookup, blockLookup, entityLookup) -> Enchantment.enchantment(
            Enchantment.definition(
                itemLookup.getOrThrow(CombatPlusItemTags.OFFHAND_ENCHANTABLE),
                itemLookup.getOrThrow(CombatPlusItemTags.OFFHAND_PRIMARY_ENCHANTABLE),
                5,
                3,
                Enchantment.dynamicCost(5, 5),
                Enchantment.dynamicCost(45, 5),
                2,
                EquipmentSlotGroup.HAND
            )
        ).withSpecialEffect(
            DSEnchantmentComponentTypes.USE_ACTION,
            ItemUseAnimation.TRIDENT.name()
        ).withEffect(
            DSEnchantmentComponentTypes.ON_STOPPED_USING,
            new ApplyToUserEffect(
                new CodeExecutionSingleEntityEffect(DualSwordsMain.identifier("lunge")),
                LevelBasedValue.constant(9)
            )
        ).withEffect(
            DSEnchantmentComponentTypes.ON_STOPPED_USING,
            new ApplyToUserEffect(
                new SetCooldownEnchantmentEffect(
                    LevelBasedValue.constant(60)
                ),
                LevelBasedValue.constant(9)
            )
        ).withEffect(
            CombatPlusEnchantmentComponentTypes.DATA_COMPONENT,
            new MaxUseTimeComponentEntityEffect(LevelBasedValue.constant(72000))
        ).withEffect(
            CombatPlusEnchantmentComponentTypes.DATA_COMPONENT,
            new CooldownGroupComponentEntityEffect(DualSwordsMain.identifier("lunge_cooldown"))
        ).exclusiveWith(
            enchantmentLookup.getOrThrow(DSEnchantmentTags.LUNGE_EXCLUSIVE)
        )
    );
    public static final EnchantmentContainer THRUSTING = new EnchantmentContainer(
        DualSwordsMain.identifier("thrusting"),
        (itemLookup, enchantmentLookup, damageLookup, blockLookup, entityLookup) -> Enchantment.enchantment(
            Enchantment.definition(
                itemLookup.getOrThrow(CombatPlusItemTags.OFFHAND_ENCHANTABLE),
                itemLookup.getOrThrow(CombatPlusItemTags.OFFHAND_PRIMARY_ENCHANTABLE),
                3,
                2,
                Enchantment.dynamicCost(5, 10),
                Enchantment.dynamicCost(50, 10),
                2,
                EquipmentSlotGroup.HAND
            )
        ).withEffect(
            EnchantmentEffectComponents.DAMAGE,
            new AddValue(
                LevelBasedValue.perLevel(1.5f)
            ),
            DamageSourceCondition.hasDamageSource(
                DamageSourcePredicate.Builder.damageType().tag(
                    TagPredicate.is(DSDamageTypeTags.IS_OFFHANDED)
                )
            )
        ).exclusiveWith(
            enchantmentLookup.getOrThrow(DSEnchantmentTags.LUNGE_BONUS_EXCLUSIVE)
        )
    );
    public static final EnchantmentContainer FORCEFUL = new EnchantmentContainer(
        DualSwordsMain.identifier("forceful"),
        (itemLookup, enchantmentLookup, damageLookup, blockLookup, entityLookup) -> Enchantment.enchantment(
            Enchantment.definition(
                itemLookup.getOrThrow(CombatPlusItemTags.OFFHAND_ENCHANTABLE),
                itemLookup.getOrThrow(CombatPlusItemTags.OFFHAND_PRIMARY_ENCHANTABLE),
                3,
                2,
                Enchantment.dynamicCost(5, 10),
                Enchantment.dynamicCost(20, 10),
                4,
                EquipmentSlotGroup.HAND
            )
        ).withEffect(
            EnchantmentEffectComponents.KNOCKBACK,
            new AddValue(
                LevelBasedValue.perLevel(1)
            ),
            DamageSourceCondition.hasDamageSource(
                DamageSourcePredicate.Builder.damageType().tag(
                    TagPredicate.is(DSDamageTypeTags.IS_OFFHANDED)
                )
            )
        ).exclusiveWith(
            enchantmentLookup.getOrThrow(DSEnchantmentTags.LUNGE_BONUS_EXCLUSIVE)
        )
    );

    public static final EnchantmentContainer DAISHO = new EnchantmentContainer(
        DualSwordsMain.identifier("daisho"),
        (itemLookup, enchantmentLookup, damageLookup, blockLookup, entityLookup) -> Enchantment.enchantment(
            Enchantment.definition(
                itemLookup.getOrThrow(CombatPlusItemTags.OFFHAND_ENCHANTABLE),
                itemLookup.getOrThrow(CombatPlusItemTags.OFFHAND_PRIMARY_ENCHANTABLE),
                2,
                5,
                Enchantment.dynamicCost(20, 5),
                Enchantment.dynamicCost(30, 10),
                8,
                EquipmentSlotGroup.OFFHAND
            )
        ).withEffect(
            CombatPlusEnchantmentComponentTypes.BONUS_DAMAGE,
            new AddValue(
                LevelBasedValue.perLevel(0.8f)
            ),
            ApplyToAttackerCondition.builder(
                SingleEntityLambdaCondition.builder(DualSwordsMain.identifier("dual_wielder"))
            )
        ).withEffect(
            CombatPlusEnchantmentComponentTypes.MODIFY_COOLDOWN,
            new AddValue(
                LevelBasedValue.perLevel(8)
            )
        )
    );

    public static void bootstrap (BootstrapContext<Enchantment> registerable) {
        register(registerable, PARRY);
        register(registerable, RIPOSTE);
        register(registerable, DEFLECT);
        register(registerable, LUNGE);
        register(registerable, THRUSTING);
        register(registerable, FORCEFUL);
        register(registerable, DAISHO);
    }

    private static void register (BootstrapContext<Enchantment> registerable, EnchantmentContainer container) {
        registerable.register(container.getKey(), container.getBuilder(registerable).build(container.getKey().identifier()));
    }
}
