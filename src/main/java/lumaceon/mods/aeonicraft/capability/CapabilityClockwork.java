package lumaceon.mods.aeonicraft.capability;

import lumaceon.mods.aeonicraft.Aeonicraft;
import lumaceon.mods.aeonicraft.api.clockwork.IClockwork;
import lumaceon.mods.aeonicraft.api.clockwork.IClockworkComponent;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class CapabilityClockwork
{
    @CapabilityInject(IClockwork.class)
    public static final Capability<IClockwork> CAP = null;

    public static void register()
    {
        CapabilityManager.INSTANCE.register(IClockwork.class, new Capability.IStorage<IClockwork>()
        {
            @Override
            public NBTBase writeNBT(Capability<IClockwork> capability, IClockwork instance, EnumFacing side) {
                return instance.serializeNBT();
            }

            @Override
            public void readNBT(Capability<IClockwork> capability, IClockwork instance, EnumFacing side, NBTBase base) {
                instance.deserializeNBT((NBTTagCompound) base);
            }
        }, Clockwork::new);
    }

    public static class Clockwork implements IClockwork
    {
        private int summedTestInt;
        public Clockwork() {
            this(3);
        }

        public Clockwork(int matrixSize) {

        }

        @Override
        public void buildFromStacks(IClockworkComponent[][] components) {
            summedTestInt = 0;
            for (int x = 0; x < components.length ; x++) {
                for (int y = 0; y < components[x].length; y++) {
                    if(components[x][y] != null){
                        summedTestInt += components[x][y].getTestInt();
                    }
                }
            }
            //TODO Implement.
        }

        @Override
        public NBTTagCompound serializeNBT() {
            NBTTagCompound compound = new NBTTagCompound();
            compound.setInteger("MAX_THINGY", summedTestInt);
            // TODO Save data to compound.
            return compound;
        }

        @Override
        public void deserializeNBT(NBTTagCompound compound) {
            summedTestInt = compound.getInteger("MAX_THINGY");
        }

        @Override
        public int getSummedTestInt() {
            return summedTestInt;
        }
    }

    // Default provider
    public static class Provider implements ICapabilitySerializable<NBTTagCompound>
    {
        Clockwork implementation;

        public Provider() {
            implementation = new Clockwork();
        }

        @Override
        public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
            return CAP == capability;
        }

        @Override
        public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
            if(hasCapability(capability, facing))
                return CAP.cast(implementation);
            return null;
        }

        @Override
        public NBTTagCompound serializeNBT() {
            return implementation.serializeNBT();
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt) {
            implementation.deserializeNBT(nbt);
        }
    }
}
