package lumaceon.mods.aeonicraft.item.clockwork;

import lumaceon.mods.aeonicraft.api.clockwork.IClockworkComponentItem;
import lumaceon.mods.aeonicraft.item.ItemAeonicraft;

public class ItemClockworkComponent extends ItemAeonicraft implements IClockworkComponentItem
{
    public ItemClockworkComponent(int maxStack, int maxDamage, String name) {
        super(maxStack, maxDamage, name);
    }
}
