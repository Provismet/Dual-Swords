package com.provismet.dualswords.util.event;

import com.provismet.CombatPlusCore.interfaces.DualWeapon;
import com.provismet.CombatPlusCore.utility.item.AttributeIdentifiers;
import com.provismet.dualswords.DualSwordsMain;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.util.Identifier;

public abstract class ItemEvents {
    public static final Identifier POST_DEFAULT = DualSwordsMain.identifier("post_default");

    public static void RegisterComponentPhase () {
        DefaultItemComponentEvents.MODIFY.addPhaseOrdering(Event.DEFAULT_PHASE, POST_DEFAULT);

        DefaultItemComponentEvents.MODIFY.register(POST_DEFAULT, context -> {
            context.modify(item -> item instanceof DualWeapon, (builder, item) -> {
                if (item instanceof DualWeapon dualWeapon) {
                    AttributeModifiersComponent component = builder
                        .getOrDefault(DataComponentTypes.ATTRIBUTE_MODIFIERS, AttributeModifiersComponent.DEFAULT)
                        .with(
                            EntityAttributes.ATTACK_DAMAGE,
                            new EntityAttributeModifier(
                                AttributeIdentifiers.OFFHAND_DAMAGE,
                                dualWeapon.getOffhandDamage(item.getDefaultStack()),
                                EntityAttributeModifier.Operation.ADD_VALUE
                            ),
                            AttributeModifierSlot.OFFHAND
                        );

                    builder.add(DataComponentTypes.ATTRIBUTE_MODIFIERS, component);
                }
            });
        });
    }
}
