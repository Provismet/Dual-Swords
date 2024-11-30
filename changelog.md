Mod update to work on version 1.21.2 and 1.21.3.

Along with updating the mod, this update also addresses some existing issues with cooldowns.

## Changes
- Updated to 1.21.3 Minecraft.
- Parry and Lunge now change the cooldown group of the enchanted item.
  - Parrying an attack will put _all_ items with parry on cooldown and _only_ those items.
  - Lunging works similarly, putting all Lunge weapons on cooldown.
  - Items without either of these enchantments are unaffected.
- Parrying and attack will now apply the maximum cooldown, as if you had held the weapon out for too long.
- Offhand damage no longer uses mixins and now makes use of Fabric API for improved compatibility.