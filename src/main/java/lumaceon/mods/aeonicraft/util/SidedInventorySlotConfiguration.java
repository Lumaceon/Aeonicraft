package lumaceon.mods.aeonicraft.util;

import net.minecraft.inventory.Slot;
import net.minecraft.util.EnumFacing;

import java.util.ArrayList;

public class SidedInventorySlotConfiguration
{
    public Slot[] slots; //Must remain in the same order as the inventory.

    private ArrayList[] slotsAvailableFromSides;

    public SidedInventorySlotConfiguration()
    {
        slotsAvailableFromSides = new ArrayList[] {
            new ArrayList<Integer>(),
            new ArrayList<Integer>(),
            new ArrayList<Integer>(),
            new ArrayList<Integer>(),
            new ArrayList<Integer>(),
            new ArrayList<Integer>(),
        };
    }

    public ArrayList<Integer> getSlotsForSide(EnumFacing side) {
        return slotsAvailableFromSides[side.getIndex()];
    }

    public int[] getSlotsForSideIntArray(EnumFacing side)
    {
        ArrayList<Integer> a = slotsAvailableFromSides[side.getIndex()];

        int[] ret = new int[a.size()];

        for(int i = 0; i < a.size(); i++) {
            ret[i] = a.get(i);
        }

        return ret;
    }
}
