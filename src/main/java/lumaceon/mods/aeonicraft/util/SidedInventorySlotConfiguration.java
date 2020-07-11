package lumaceon.mods.aeonicraft.util;

import net.minecraft.inventory.Slot;
import net.minecraft.nbt.NBTTagCompound;
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

    public SidedInventorySlotConfiguration(NBTTagCompound nbt) {
        this();
        deserializeNBT(nbt);
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

    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound nbt = new NBTTagCompound();

        for(EnumFacing f : EnumFacing.VALUES)
        {
            int[] slots = getSlotsForSideIntArray(f);
            nbt.setIntArray("slots_" + f.getIndex(), slots);
        }

        return nbt;
    }

    public void deserializeNBT(NBTTagCompound nbt)
    {
        for(EnumFacing f : EnumFacing.VALUES)
        {
            int[] slotArray = nbt.getIntArray("slots_" + f.getIndex());

            ArrayList<Integer> list = new ArrayList<>();
            for(int n = 0; n < slotArray.length; n++) {
                list.add(slotArray[n]);
            }

            slotsAvailableFromSides[f.getIndex()] = list;
        }
    }
}
