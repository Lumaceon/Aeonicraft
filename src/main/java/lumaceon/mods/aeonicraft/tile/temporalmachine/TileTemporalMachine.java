package lumaceon.mods.aeonicraft.tile.temporalmachine;

import lumaceon.mods.aeonicraft.machine.Machine;
import lumaceon.mods.aeonicraft.machine.TemporalMachine;
import lumaceon.mods.aeonicraft.tile.TileAeonicraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.energy.IEnergyStorage;

public abstract class TileTemporalMachine extends TileAeonicraft implements ITickable
{
    @CapabilityInject(IEnergyStorage.class)
    static Capability<IEnergyStorage> ENERGY_STORAGE_CAPABILITY = null;

    public TemporalMachine temporalMachine;

    protected EnumFacing rotation;


    public TileTemporalMachine()
    {
        this.rotation = EnumFacing.NORTH;
    }

    public abstract int getMaxActions(Machine machine);
    public abstract int takeActions(int max);

    @Override
    public void update() {
        if(temporalMachine.gameTick())
            markDirty();
    }

    /**
     * Translates from a global EnumFacing to a local EnumFacing.
     * In local EnumFacing: front translates to north, and only rotates about the Y-axis are accepted.
     */
    protected EnumFacing globalToLocalFacing(EnumFacing facing)
    {
        if(facing.equals(EnumFacing.UP) || facing.equals(EnumFacing.DOWN))
            return facing;

        if(this.rotation.equals(EnumFacing.NORTH))
            return facing;

        facing = facing.rotateAround(EnumFacing.Axis.Y);
        if(this.rotation.equals(EnumFacing.WEST))
            return facing;

        facing = facing.rotateAround(EnumFacing.Axis.Y);
        if(this.rotation.equals(EnumFacing.SOUTH))
            return facing;

        facing = facing.rotateAround(EnumFacing.Axis.Y);
        if(this.rotation.equals(EnumFacing.EAST))
            return facing;

        return facing;
    }

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
        compound.setInteger("rotation", rotation.getIndex());

        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);

        if(compound.hasKey("temporal_machine_compound"))
            this.temporalMachine.deserializeNBT(compound.getCompoundTag("temporal_machine_compound"));

        if(compound.hasKey("rotation"))
            this.rotation = EnumFacing.VALUES[compound.getInteger("rotation")];
    }
}