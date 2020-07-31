package lumaceon.mods.aeonicraft.api.clockwork;

import lumaceon.mods.aeonicraft.api.clockwork.baseStats.modifiers.ModifierParent;

import java.util.List;

/**
 * Marks an item as a component for clockwork items.
 */
public interface IClockworkComponentItem extends IClockworkComponent
{
    public List<ModifierParent> getClockworkCompModifiers();
}
