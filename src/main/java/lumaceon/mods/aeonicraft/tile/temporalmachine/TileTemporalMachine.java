package lumaceon.mods.aeonicraft.tile.temporalmachine;

import lumaceon.mods.aeonicraft.tile.TileAeonicraft;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.energy.IEnergyStorage;

public class TileTemporalMachine extends TileAeonicraft
{
    @CapabilityInject(IEnergyStorage.class)
    static Capability<IEnergyStorage> ENERGY_STORAGE_CAPABILITY = null;

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing)
    {
        /*if(capability == ITEM_HANDLER_CAPABILITY || capability == ENERGY_STORAGE_CAPABILITY)
            return true;

        if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
        {
            EnumFacing localDirection = rotate(facing);
            for(FluidTankSided tank : fluidTanks)
            {
                if(tank != null && tank.isAvailableForSide(localDirection))
                    return true;
            }
        }*/

        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing)
    {
        if(hasCapability(capability, facing))
        {
            /*if(capability == ENERGY_STORAGE_CAPABILITY)
            {
                return ENERGY_STORAGE_CAPABILITY.cast(energyStorage);
            }
            else if(capability == ITEM_HANDLER_CAPABILITY)
            {
                if(facing == EnumFacing.UP)
                    return ITEM_HANDLER_CAPABILITY.cast(invUP);
                if(facing == EnumFacing.DOWN)
                    return ITEM_HANDLER_CAPABILITY.cast(invDOWN);
                if(facing == EnumFacing.NORTH)
                    return ITEM_HANDLER_CAPABILITY.cast(invNORTH);
                if(facing == EnumFacing.SOUTH)
                    return ITEM_HANDLER_CAPABILITY.cast(invSOUTH);
                if(facing == EnumFacing.EAST)
                    return ITEM_HANDLER_CAPABILITY.cast(invEAST);
                if(facing == EnumFacing.WEST)
                    return ITEM_HANDLER_CAPABILITY.cast(invWEST);
            }
            else if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            {
                EnumFacing localDirection = rotate(facing);
                for(FluidTankSided tank : fluidTanks)
                {
                    if(tank != null && tank.isAvailableForSide(localDirection))
                    {
                        return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(tank);
                    }
                }
            }*/
        }
        return super.getCapability(capability, facing);
    }
}