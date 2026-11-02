package com.provismet.dualswords.util.event;

import com.provismet.CombatPlusCore.interfaces.DualWeapon;
import com.provismet.CombatPlusCore.items.component.MeleeWeaponComponent;
import com.provismet.CombatPlusCore.registries.CPCDataComponentTypes;
import com.provismet.CombatPlusCore.utility.item.AttributeIdentifiers;
import com.provismet.CombatPlusCore.utility.tag.CPCItemTags;
import com.provismet.dualswords.DualSwordsMain;
import com.provismet.lilylib.util.MoreMath;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.component.ItemAttributeModifiers;

public abstract class ItemEvents {
    public static final Identifier POST_DEFAULT = DualSwordsMain.identifier("post_default");

    public static void RegisterComponentPhase () {
        DefaultItemComponentEvents.MODIFY.addPhaseOrdering(Event.DEFAULT_PHASE, POST_DEFAULT);

        DefaultItemComponentEvents.MODIFY.register(POST_DEFAULT, context -> {
            context.modify(item -> true, (builder, item) -> {
                if (item instanceof DualWeapon dualWeapon) {
                    ItemAttributeModifiers component = builder
                        .getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY)
                        .withModifierAdded(
                            Attributes.ATTACK_DAMAGE,
                            new AttributeModifier(
                                AttributeIdentifiers.OFFHAND_DAMAGE,
                                dualWeapon.getOffhandDamage(item.getDefaultInstance()),
                                AttributeModifier.Operation.ADD_VALUE
                            ),
                            EquipmentSlotGroup.OFFHAND
                        );

                    builder.set(DataComponents.ATTRIBUTE_MODIFIERS, component);
                }
                else {
                    MeleeWeaponComponent component = item.getDefaultInstance().getOrDefault(CPCDataComponentTypes.MELEE_WEAPON, MeleeWeaponComponent.DEFAULT);
                    if (!component.isDual()) return;

                    ItemAttributeModifiers attributes = builder
                        .getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY)
                        .withModifierAdded(
                            Attributes.ATTACK_DAMAGE,
                            new AttributeModifier(
                                AttributeIdentifiers.OFFHAND_DAMAGE,
                                component.dualDamage(),
                                AttributeModifier.Operation.ADD_VALUE
                            ),
                            EquipmentSlotGroup.OFFHAND
                        );

                    builder.set(DataComponents.ATTRIBUTE_MODIFIERS, attributes);
                }
            });
        });
    }
}
