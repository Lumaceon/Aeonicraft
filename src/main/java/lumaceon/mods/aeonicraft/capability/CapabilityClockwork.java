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

import java.util.ArrayList;

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
        private  float summedProgress;
        public Clockwork() {
            this(3);
        }

        public Clockwork(int matrixSize) {

        }

        @Override
        public void buildFromStacks(IClockworkComponent[][] components) {
            summedProgress = 0f;
            for (int x = 0; x < components.length ; x++) {
                for (int y = 0; y < components[x].length; y++) {
                    if(components[x][y] != null){
                        summedProgress += getActualProgress(getCompAndNeighbours(components,x,y));
                    }
                }
            }
            //TODO Implement.
        }

        private float getActualProgress(ArrayList<IClockworkComponent> components){
            float returnValue = 0f;
            IClockworkComponent component = components.get(0);
            returnValue = component.getProgress().StatValue;

            for (int i = 1; i < components.size(); i++) {
                IClockworkComponent neighbourComponent = components.get(i);
                if(component.getType() == component.getType()){
                    returnValue *= 2;
                }else{
                    returnValue /= 2;
                }
            }
            return returnValue;
        }

        private ArrayList<IClockworkComponent> getCompAndNeighbours(IClockworkComponent[][] components, int x, int y){
            ArrayList<IClockworkComponent> returnComps = new ArrayList<IClockworkComponent>();
            returnComps.add(components[x][y]);

            if(x > 0){
                returnComps.add(components[x-1][y]);
            }
            if(y > 0){
                returnComps.add(components[x][y-1]);
            }
            if(x+1 < components.length){
                returnComps.add(components[x+1][y]);
            }
            if(y+1 > components[x].length){
                returnComps.add(components[x][y+1]);
            }

           while(returnComps.remove(null)){ }

           return returnComps;
        }

        @Override
        public NBTTagCompound serializeNBT() {
            NBTTagCompound compound = new NBTTagCompound();
            compound.setFloat("PROGRESS", summedProgress);
            // TODO Save data to compound.
            return compound;
        }

        @Override
        public void deserializeNBT(NBTTagCompound compound) {
            summedProgress = compound.getFloat("PROGRESS");
        }

        @Override
        public float getSummedProgress() {
            return summedProgress;
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
