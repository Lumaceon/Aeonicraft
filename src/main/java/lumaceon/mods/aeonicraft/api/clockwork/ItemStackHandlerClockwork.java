package lumaceon.mods.aeonicraft.api.clockwork;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

/**
 * Special ItemStack Handler for updating a Clockwork capability (and optionally a modular energy storage) when changed.
 */
public class ItemStackHandlerClockwork extends ItemStackHandler
{
    IClockwork clockwork;
    EnergyStorageModular modularEnergyStorage = null;
    int matrixSizeX, matrixSizeY = 1;

    public ItemStackHandlerClockwork(int sizeX, int sizeY, IClockwork clockwork) {
        super((sizeX * sizeY) - 1);
        this.matrixSizeX = sizeX;
        this.matrixSizeY = sizeY;
        this.clockwork = clockwork;
    }

    public ItemStackHandlerClockwork(int sizeX, int sizeY, IClockwork clockwork, EnergyStorageModular energyStorageModular) {
        this(sizeX, sizeY, clockwork);
        this.modularEnergyStorage = energyStorageModular;
    }

    @Override
    protected void onContentsChanged(int slot)
    {
        IClockworkComponent[][] matrix = new IClockworkComponent[matrixSizeX][matrixSizeY];

        int matrixRadiusX = matrixSizeX / 2;
        int matrixRadiusY = matrixSizeY / 2;
        int index;
        ItemStack stack;
        for(int x = 0; x < matrixSizeX; x++)
        {
            for(int y = 0; y < matrixSizeY; y++)
            {
                if(x == matrixRadiusX && y == matrixRadiusY)
                    continue;

                index = x + (y * matrixSizeX);
                if(y > matrixRadiusY || (y == matrixRadiusY && x > matrixRadiusX))
                    index--;

                stack = stacks.get(index);
                if(stack.getItem() instanceof IClockworkComponent)
                    matrix[x][y] = (IClockworkComponent) stack.getItem();
                else
                    matrix[x][y] = null;
            }
        }
        clockwork.buildFromStacks(matrix);
    }

    @Override
    public boolean equals(@Nullable final Object obj)
    {
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;

        final ItemStackHandler that = (ItemStackHandler) obj;

        if(this.getSlots() != that.getSlots())
            return false;

        ItemStack thisItem;
        ItemStack thatItem;
        for(int i = 0; i < this.stacks.size(); i++)
        {
            thisItem = stacks.get(i);
            thatItem = that.getStackInSlot(i);

            if(!ItemStack.areItemStacksEqual(thisItem, thatItem)) return false;
        }

        return true;
    }
}
