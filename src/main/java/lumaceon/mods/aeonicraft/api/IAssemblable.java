package lumaceon.mods.aeonicraft.api;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * To be implemented on items which can be placed into the assembly table's main assembly slot. These methods control
 * various aspects of the sub-GUI that is shown.
 */
public interface IAssemblable
{
    /**
     * Determines what background to draw for the assembly table.
     * @param stack Stack representation of this item in the GUI.
     * @return The GUI background to display.
     */
    ResourceLocation getGUIBackground(ItemStack stack);

    /**
     * Initialize the slots for the internal assembly inventory.
     */
    Slot[] getContainerSlots(IInventory inventory, int centerSlotX, int centerSlotY);
}
