package com.provismet.dualswords.utility.event;

import com.provismet.CombatPlusCore.interfaces.DualWeapon;
import com.provismet.dualswords.DualSwordsMain;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.item.v1.ModifyItemAttributeModifiersCallback;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.util.Identifier;

public abstract class ItemEvents {
    public static final Identifier POST_DEFAULT = DualSwordsMain.identifier("post_default");

    public static void RegisterComponentPhase () {
        ModifyItemAttributeModifiersCallback.EVENT.register(POST_DEFAULT, (stack, slot, attributeModifiers) -> {
            if (stack.getItem() instanceof DualWeapon dualWeapon && slot == EquipmentSlot.OFFHAND) {
                attributeModifiers.put(
                    EntityAttributes.GENERIC_ATTACK_DAMAGE,
                    new EntityAttributeModifier(DualSwordsMain.OFFHAND_ATTRIBUTE_ID, "Offhand Weapon modifier", dualWeapon.getOffhandDamage(stack), EntityAttributeModifier.Operation.ADDITION)
                );
            }
        });

        ModifyItemAttributeModifiersCallback.EVENT.addPhaseOrdering(Event.DEFAULT_PHASE, POST_DEFAULT);
    }
}
