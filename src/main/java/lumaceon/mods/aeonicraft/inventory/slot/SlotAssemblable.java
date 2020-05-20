package lumaceon.mods.aeonicraft.inventory.slot;

import lumaceon.mods.aeonicraft.api.IAssemblable;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotAssemblable extends Slot
{
    public SlotAssemblable(IInventory inventory, int slotID, int x, int y) {
        super(inventory, slotID, x, y);
    }

    @Override
    public boolean isItemValid(ItemStack is) {
        return is.getItem() instanceof IAssemblable;
    }
}
