package lumaceon.mods.aeonicraft.util;

import lumaceon.mods.aeonicraft.api.hourglass.IHourglassFunction;
import lumaceon.mods.aeonicraft.capability.hourglass.CapabilityHourglass;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

import javax.annotation.Nullable;

public class InventoryHelper
{
    @CapabilityInject(CapabilityHourglass.IHourglassHandler.class)
    public static final Capability<CapabilityHourglass.IHourglassHandler> HOURGLASS = null;

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

    public static int getIndexOfStackInInventory(ItemStack stack, IInventory inventory)
    {
        for(int i = 0; i < inventory.getSizeInventory(); i++)
        {
            if(inventory.getStackInSlot(i) == stack)
            {
                return i;
            }
        }
        return -1;
    }

    @Nullable
    public static IHourglassFunction getHourglassFunctionFromHourglass(ItemStack hourglass)
    {
        CapabilityHourglass.IHourglassHandler cap = hourglass.getCapability(HOURGLASS, null);
        if(cap != null)
            return cap.getActiveFunction();
        return null;
    }
}
