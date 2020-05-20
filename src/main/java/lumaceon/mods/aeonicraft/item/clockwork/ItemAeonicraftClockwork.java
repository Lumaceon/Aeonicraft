package lumaceon.mods.aeonicraft.item.clockwork;

import lumaceon.mods.aeonicraft.Aeonicraft;
import lumaceon.mods.aeonicraft.api.clockwork.ItemClockwork;
import net.minecraft.creativetab.CreativeTabs;

public class ItemAeonicraftClockwork extends ItemClockwork
{
    public ItemAeonicraftClockwork(int matrixSize, int maxStack, int maxDamage, String name) {
        super(matrixSize, maxStack, maxDamage, Aeonicraft.MOD_ID, Aeonicraft.instance.CREATIVE_TAB, name);
    }
}
