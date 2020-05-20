package lumaceon.mods.aeonicraft.api.clockwork;

import net.minecraft.item.ItemStack;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;

/**
 * Simple energy storage subclass for modifying capacity and optionally updating an itemstack's damage values on change.
 */
public class EnergyStorageModular extends EnergyStorage
{
    protected ItemStack stack;

    public EnergyStorageModular(int capacity) {
        super(capacity);
    }

    public EnergyStorageModular(int capacity, ItemStack stack) {
        super(capacity);
        this.stack = stack;
    }

    public EnergyStorageModular(int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
    }

    public EnergyStorageModular(int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract);
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int ret = super.receiveEnergy(maxReceive, simulate);
        updateDamageIfStackExists();
        return ret;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        int ret = super.extractEnergy(maxExtract, simulate);
        updateDamageIfStackExists();
        return ret;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
        updateDamageIfStackExists();
    }

    public void setMaxCapacity(int capacity)
    {
        this.capacity = capacity;
        this.maxExtract = capacity;
        this.maxReceive = capacity;
        this.energy = Math.min(capacity, energy);
        updateDamageIfStackExists();
    }

    protected void updateDamageIfStackExists()
    {
        if(stack != null)
        {
            stack.setItemDamage(stack.getMaxDamage());
        }
    }

    @Override
    public boolean equals(@Nullable final Object obj)
    {
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;

        final IEnergyStorage that = (IEnergyStorage) obj;

        return this.getMaxEnergyStored() == that.getMaxEnergyStored() && this.getEnergyStored() == that.getEnergyStored();
    }

    @Override
    public int hashCode() {
        return capacity + energy % 100;
    }
}
