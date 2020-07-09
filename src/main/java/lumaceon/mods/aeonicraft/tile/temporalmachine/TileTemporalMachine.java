package lumaceon.mods.aeonicraft.tile.temporalmachine;

import lumaceon.mods.aeonicraft.machine.TemporalMachine;
import lumaceon.mods.aeonicraft.tile.TileAeonicraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.energy.IEnergyStorage;

public abstract class TileTemporalMachine extends TileAeonicraft
{
    @CapabilityInject(IEnergyStorage.class)
    static Capability<IEnergyStorage> ENERGY_STORAGE_CAPABILITY = null;

    public TemporalMachine temporalMachine;

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing)
    {
        if(capability == ENERGY_STORAGE_CAPABILITY)
            return true;

        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing)
    {
        if(capability == ENERGY_STORAGE_CAPABILITY)
            return ENERGY_STORAGE_CAPABILITY.cast(temporalMachine.getEnergyStorage());

        return super.getCapability(capability, facing);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        compound = super.writeToNBT(compound);

        compound.setTag("temporal_machine_compound", temporalMachine.serializeNBT());

        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);

        if(compound.hasKey("temporal_machine_compound"))
            this.temporalMachine.deserializeNBT(compound.getCompoundTag("temporal_machine_compound"));
    }
}