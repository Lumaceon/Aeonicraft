package lumaceon.mods.aeonicraft.capability;

import lumaceon.mods.aeonicraft.Aeonicraft;
import lumaceon.mods.aeonicraft.api.clockwork.ClockworkTooltipDummy;
import lumaceon.mods.aeonicraft.api.clockwork.IClockwork;
import lumaceon.mods.aeonicraft.api.clockwork.IClockworkComponent;
import lumaceon.mods.aeonicraft.api.clockwork.tooltips.IClockworkTooltips;
import lumaceon.mods.aeonicraft.api.clockwork.baseStats.*;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

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
        private int matrixSize;

        private ClockworkEfficiencyStat summedEfficiency  = new ClockworkEfficiencyStat(0);
        private ClockworkMaxWindUpStat summedMaxWindUp  = new ClockworkMaxWindUpStat(0);
        private ClockworkProgressStat summedProgress = new ClockworkProgressStat(0);
        private ClockworkWindUpStat summedWindUp = new ClockworkWindUpStat(0);

        private List<String>[][] matrixCompDescription;



        public Clockwork() {
            this(3);
        }

        public Clockwork(int matrixSize) {
            matrixCompDescription = new ArrayList[matrixSize][matrixSize];
            this.matrixSize = matrixSize;
        }

        private void resetClockworkStats(){
            summedProgress = new ClockworkProgressStat(0);
            summedEfficiency = new ClockworkEfficiencyStat(0);
            summedMaxWindUp = new ClockworkMaxWindUpStat(0);
            summedWindUp = new ClockworkWindUpStat(0);
        }

        public List<String> clockworkMatrixComponentTooltips(ClockworkTooltipDummy dummy){
            List<String> returnValue = new ArrayList<>();
            for (ClockworkBaseStat stat : dummy.getClockworkStatCollection()) {
                returnValue.addAll(dummy.getTooltipCollection(stat));
            }
            
            return returnValue;
        }
        @Override
        public int getDiameter() {
            return this.matrixSize;
        }

        @Override
        public void buildFromStacks(IClockworkComponent[][] components) {
            resetClockworkStats();
            for (int x = 0; x < components.length ; x++) {
                for (int y = 0; y < components[x].length; y++) {
                    if(components[x][y] != null){
                        ClockworkTooltipDummy valueHolder = new ClockworkTooltipDummy();
                        IClockworkComponent currentComp = components[x][y];

                        //set/calculate the singular statvalues, add it to the valueHolder(Dummy) and then add it to the sum


                        List<IClockworkComponent> componentAndNeighbours = getCompAndNeighbours(components,x,y);
                        summedProgress.StatValue += setDummyProgress(componentAndNeighbours,valueHolder, comp -> comp.getProgress(), true);
                        summedEfficiency.StatValue += setDummyProgress(componentAndNeighbours,valueHolder, comp -> comp.getEfficiency(), false);
                        summedMaxWindUp.StatValue += setDummyProgress(componentAndNeighbours,valueHolder, comp -> comp.getWindUpMaxMod(), false);
                        summedWindUp.StatValue += setDummyProgress(componentAndNeighbours,valueHolder, comp -> comp.getWindUpCost(), false);


                        //populate the matrix with the proper tooltip description, feeding it the modified values from the valueHolder
                        matrixCompDescription[x][y] = clockworkMatrixComponentTooltips(valueHolder);

                        //todo why does this log three times? Shouldn't it be two(Server, Client?) Also seems to be some sort of small delay when placing two.
                        //Aeonicraft.logger.info("Matrix in position " + x + " " + y + ": " + matrixCompDescription[x][y]);
                    }else{
                        matrixCompDescription[x][y] = null;
                    }
                }
            }
            //TODO Implement.
        }

        @Override
        @Nullable
        public List<String> getAdditionalTooltipsForMatrixPosition(int x, int y) {
            return this.matrixCompDescription[x][y];
        }



        /**
         * Gets the absolute/final progress of the component
         * @param components ArrayList that should contain components and its neighbours
         * @return returns a float that represents the final progress value of the component at this position
         */

        /**
         * Calculate the total amount of stats gained and sets the relevant stats of the dummy PlaceHolder to properly add the tooltip in the matrix interface to the component position
         * @param components List of components, first position is the "main", rest can be neighbours (or other components) that do relevant calculations (currently only neighbour bonus)
         * @param dummy the dummy/placeholder object that has stats saved to it
         * @param foo function that returns the relevant BaseStat to do calculations and other shenanigans with
         * @param countNeighbours should it count the neighbours for bonus?
         * @return returns the total value gained (or lost)
         */
        private float setDummyProgress(List<IClockworkComponent> components, ClockworkTooltipDummy dummy, Function<IClockworkBaseStats, ClockworkBaseStat> foo, boolean countNeighbours){
            //Actual final number that is being worked with
            float returnValue = 0f;

            //Get the relevant Clockwork component from the actual component
            IClockworkComponent mainComponent = components.get(0);

            //get the relevant stat from the dummy
            ClockworkBaseStat dummyClockworkStat = foo.apply(dummy);

            //set the float values of the dummyClockworkStat, the mainCompStat and the returnValue to the mainCompStat
            returnValue = dummyClockworkStat.StatValue = foo.apply(mainComponent).StatValue;

            //If Value is 0, return 0 and don't apply any modifiers since the stat doesn't actually exist
            if(returnValue == 0f){
                return returnValue;
            }


            List<String> tooltipModifiers = dummy.getTooltipCollection(dummyClockworkStat);
            tooltipModifiers.addAll(dummyClockworkStat.getBasicTooltipDescription());

            //if Neighbours aren't supposed to be counted, return. Same if no neighbours exist
            if(!countNeighbours) return returnValue;
            if(components.size() == 1) return returnValue;

            float multiplier = 1;
            //Math shenanigans to double/divide if neighbours are same, or different, type.
            for (int i = 1; i < components.size(); i++) {
                IClockworkComponent neighbourComponent = components.get(i);
                if(mainComponent.getType() == neighbourComponent.getType()){
                    multiplier *= 2;
                }else{
                    multiplier /= 2;
                }
            }
            //apply the multiplier to the returnValue
            returnValue *= multiplier;

            //difference between baseStat and modifiedValue to properly shenanigans the tooltip
            float addedBonus = returnValue - dummyClockworkStat.StatValue;

            //set proper string stuff
            String placeHolder = tooltipModifiers.get(0);
            tooltipModifiers.set(0,placeHolder + "+(" + addedBonus + ")");
            tooltipModifiers.add("  Neighbour Multiplierbonus: x" + multiplier);

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
        private List<IClockworkComponent> getCompAndNeighbours(IClockworkComponent[][] components, int x, int y){
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
            if(y+1 < components[x].length){
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
