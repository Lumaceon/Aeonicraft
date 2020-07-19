package lumaceon.mods.aeonicraft.capability;

import lumaceon.mods.aeonicraft.Aeonicraft;
import lumaceon.mods.aeonicraft.api.clockwork.IClockwork;
import lumaceon.mods.aeonicraft.api.clockwork.IClockworkComponent;
import lumaceon.mods.aeonicraft.api.clockwork.baseStats.ClockworkEfficiencyStat;
import lumaceon.mods.aeonicraft.api.clockwork.baseStats.ClockworkMaxWindUpStat;
import lumaceon.mods.aeonicraft.api.clockwork.baseStats.ClockworkProgressStat;
import lumaceon.mods.aeonicraft.api.clockwork.baseStats.ClockworkWindUpStat;
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
        private ClockworkEfficiencyStat summedEfficiency  = new ClockworkEfficiencyStat(0);
        private ClockworkMaxWindUpStat summedMaxWindUp  = new ClockworkMaxWindUpStat(0);
        private ClockworkProgressStat summedProgress = new ClockworkProgressStat(0);
        private ClockworkWindUpStat summedWindUp = new ClockworkWindUpStat(0);

        public Clockwork() {
            this(3);
        }

        public Clockwork(int matrixSize) {

        }

        private void resetClockworkStats(){
            summedProgress = new ClockworkProgressStat(0);
            summedEfficiency = new ClockworkEfficiencyStat(0);
            summedMaxWindUp = new ClockworkMaxWindUpStat(0);
            summedWindUp = new ClockworkWindUpStat(0);
        }
        @Override
        public void buildFromStacks(IClockworkComponent[][] components) {

            resetClockworkStats();
            for (int x = 0; x < components.length ; x++) {
                for (int y = 0; y < components[x].length; y++) {
                    if(components[x][y] != null){
                        IClockworkComponent currentComp = components[x][y];
                        summedProgress.StatValue += getActualProgress(getCompAndNeighbours(components,x,y));
                        summedEfficiency.StatValue += currentComp.getEfficiency().StatValue;
                        summedMaxWindUp.StatValue += currentComp.getWindUpMaxMod().StatValue;
                        summedWindUp.StatValue += currentComp.getWindUpCost().StatValue;
                    }
                }
            }
            //TODO Implement.
        }


        /**
         * Gets the absolute/final progress of the component
         * @param components ArrayList that should contain components and its neighbours
         * @return returns a float that represents the final progress value of the component at this position
         */
        private float getActualProgress(ArrayList<IClockworkComponent> components){
            float returnValue = 0f;
            IClockworkComponent component = components.get(0);
            returnValue = component.getProgress().StatValue;

            //If Value is 0, return 0
            if(returnValue == 0f){
                return returnValue;
            }
            //Math shenanigans to double/divide if neighbours are same, or different, type.
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

        /**
         * Checks if a component at a given location in the matrix has neighbours and gives it back.
         * @param components entire matrix that contains the ClockworkComponents
         * @param x x coordinate of the "main" component, surrounding neighbours will be checked
         * @param y y coordinate of the "main" component, surrounding neighbours will be checked
         * @return return value of all components, index 0 is the base component, anything after the neighbours(direction doesn't matter)
         */
        //ToDo Better way/Keep it to getActualProgress only because only that is influenced?
        private ArrayList<IClockworkComponent> getCompAndNeighbours(IClockworkComponent[][] components, int x, int y){
            ArrayList<IClockworkComponent> returnComps = new ArrayList<IClockworkComponent>();
            returnComps.add(components[x][y]);

            //Routine checks against outOfBounds exceptions
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

            //In case of any null values added, remove them here
           while(returnComps.remove(null)){ }

           return returnComps;
        }

        @Override
        public NBTTagCompound serializeNBT() {
            NBTTagCompound compound = new NBTTagCompound();
            compound.setFloat("PROGRESS", summedProgress.StatValue);
            compound.setFloat("EFFICIENCY",summedEfficiency.StatValue);
            compound.setFloat("WINDUP",summedWindUp.StatValue);
            compound.setFloat("WINDUPMAX",summedMaxWindUp.StatValue);
            // TODO Save data to compound.
            return compound;
        }

        @Override
        public void deserializeNBT(NBTTagCompound compound) {
            summedProgress.StatValue = compound.getFloat("PROGRESS");
            summedEfficiency.StatValue = compound.getFloat("EFFICIENCY");
            summedWindUp.StatValue = compound.getFloat("WINDUP");
            summedMaxWindUp.StatValue = compound.getFloat("WINDUPMAX");

        }

        @Override
        public ClockworkProgressStat getProgress() {
            return summedProgress;
        }

        @Override
        public ClockworkWindUpStat getWindUpCost() {
            return summedWindUp;
        }

        @Override
        public ClockworkMaxWindUpStat getWindUpMaxMod() {
            return summedMaxWindUp;
        }

        @Override
        public ClockworkEfficiencyStat getEfficiency() {
            return summedEfficiency;
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
