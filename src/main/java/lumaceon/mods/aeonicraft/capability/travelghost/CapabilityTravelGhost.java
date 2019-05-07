package lumaceon.mods.aeonicraft.capability.travelghost;

import lumaceon.mods.aeonicraft.entity.EntityTravelGhost;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class CapabilityTravelGhost
{
    @CapabilityInject(ITravelGhostHandler.class)
    public static final Capability<ITravelGhostHandler> TRAVEL_GHOST = null;

    public static void register()
    {
        //TODO - technically might not need an IStorage...
        CapabilityManager.INSTANCE.register(CapabilityTravelGhost.ITravelGhostHandler.class, new Capability.IStorage<CapabilityTravelGhost.ITravelGhostHandler>()
                {
                    @Override
                    public NBTBase writeNBT(Capability<CapabilityTravelGhost.ITravelGhostHandler> capability, CapabilityTravelGhost.ITravelGhostHandler instance, EnumFacing side) {return new NBTTagCompound();
                    }

                    @Override
                    public void readNBT(Capability<CapabilityTravelGhost.ITravelGhostHandler> capability, CapabilityTravelGhost.ITravelGhostHandler instance, EnumFacing side, NBTBase base) {}
                },
                () -> new TravelGhostHandler()
        );
    }

    public interface ITravelGhostHandler
    {
        void setTravelGhost(EntityTravelGhost travelGhost);
        EntityTravelGhost getTravelGhost();
    }

    public static class TravelGhostHandler implements ITravelGhostHandler
    {
        EntityTravelGhost travelGhost;

        @Override
        public void setTravelGhost(EntityTravelGhost travelGhost) {
            this.travelGhost = travelGhost;
        }

        @Override
        public EntityTravelGhost getTravelGhost() {
            return this.travelGhost;
        }
    }

    // Default provider
    public static class Provider implements ICapabilitySerializable<NBTTagCompound>
    {
        TravelGhostHandler implementation;

        public Provider() {
            implementation = new TravelGhostHandler();
        }

        @Override
        public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
            return TRAVEL_GHOST == capability;
        }

        @Override
        public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
            if(hasCapability(capability, facing))
                return TRAVEL_GHOST.cast(implementation);
            return null;
        }

        @Override
        public NBTTagCompound serializeNBT() {
            return new NBTTagCompound();
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt) {}
    }
}
