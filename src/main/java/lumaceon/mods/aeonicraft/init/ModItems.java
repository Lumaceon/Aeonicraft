package lumaceon.mods.aeonicraft.init;

import lumaceon.mods.aeonicraft.item.ItemAeonicraftOreDict;
import lumaceon.mods.aeonicraft.item.ItemTemporalHourglass;
import net.minecraft.item.Item;

import java.util.ArrayList;

public class ModItems
{
    public static final ArrayList<Item> ITEMS = new ArrayList<>();

    public static final Item ingotTemporal = init(new ItemAeonicraftOreDict(64, 100, "ingot_temporal", "ingotTemporal"));
    public static final Item ingotBrass = init(new ItemAeonicraftOreDict(64, 100, "ingot_brass", "ingotBrass"));
    public static final Item temporalHourglass = init(new ItemTemporalHourglass(1, 10000, "temporal_hourglass"));

    private static Item init(Item item) {
        ITEMS.add(item);
        return item;
    }
}
