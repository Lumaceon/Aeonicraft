package lumaceon.mods.aeonicraft.util;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class InventoryHelper
{
    @Nullable
    public static ItemStack getFirstStackOfTypeInInventory(IInventory inventory, Item item)
    {
        for(int i = 0; i < inventory.getSizeInventory(); i++)
        {
            ItemStack is = inventory.getStackInSlot(i);
            if(is.getItem().equals(item))
                return is;
        }
        return ItemStack.EMPTY;
    }
}
