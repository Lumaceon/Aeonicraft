package lumaceon.mods.aeonicraft.item;

import lumaceon.mods.aeonicraft.Aeonicraft;
import net.minecraft.item.Item;

import java.util.Objects;

public class ItemAeonicraft extends Item
{
    public ItemAeonicraft(int maxStack, int maxDamage, String name)
    {
        super();
        this.setMaxStackSize(maxStack);
        this.setMaxDamage(maxDamage);
        this.setNoRepair();
        this.setCreativeTab(Aeonicraft.instance.CREATIVE_TAB);
        this.setRegistryName(Aeonicraft.MOD_ID, name);
        this.setUnlocalizedName(Objects.requireNonNull(this.getRegistryName()).toString());
    }
}