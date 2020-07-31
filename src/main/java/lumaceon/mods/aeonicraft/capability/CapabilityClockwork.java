package lumaceon.mods.aeonicraft.capability;

import lumaceon.mods.aeonicraft.Aeonicraft;
import lumaceon.mods.aeonicraft.api.clockwork.ClockworkTooltipDummy;
import lumaceon.mods.aeonicraft.api.clockwork.IClockwork;
import lumaceon.mods.aeonicraft.api.clockwork.IClockworkComponent;
import lumaceon.mods.aeonicraft.api.clockwork.IClockworkComponentItem;
import lumaceon.mods.aeonicraft.api.clockwork.baseStats.*;
import lumaceon.mods.aeonicraft.api.clockwork.baseStats.modifiers.ModifierChild;
import lumaceon.mods.aeonicraft.api.clockwork.baseStats.modifiers.ModifierCollection;
import lumaceon.mods.aeonicraft.api.clockwork.baseStats.modifiers.ModifierParent;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

        private ClockworkBaseStat summedEfficiency  =   BaseStatBuilder.getNewEfficiencyStatInstance(0);
        private ClockworkBaseStat summedMaxWindUp  =    BaseStatBuilder.getNewWindupMaxStatInstance(0);
        private ClockworkBaseStat summedProgress =      BaseStatBuilder.getNewProgressStatInstance(0);
        private ClockworkBaseStat summedWindUp =        BaseStatBuilder.getNewWindupStatInstance(0);

        private List<String>[][] matrixCompDescription;
        private List<ModifierChild>[][] matrixCellModifiers;
        private ClockworkTooltipDummy[][] dummyMatrix;


        public Clockwork() {
            this(3);
        }

        public Clockwork(int matrixSize) {
            matrixCompDescription = new ArrayList[matrixSize][matrixSize];
            matrixCellModifiers = new ArrayList[matrixSize][matrixSize];
            dummyMatrix = new ClockworkTooltipDummy[matrixSize][matrixSize];
            this.matrixSize = matrixSize;
        }

        private void resetClockworkStats() {
            summedEfficiency = BaseStatBuilder.getNewEfficiencyStatInstance(0);
            summedMaxWindUp = BaseStatBuilder.getNewWindupMaxStatInstance(0);
            summedProgress = BaseStatBuilder.getNewProgressStatInstance(0);
            summedWindUp = BaseStatBuilder.getNewWindupStatInstance(0);
            matrixCompDescription = new ArrayList[matrixSize][matrixSize];
            matrixCellModifiers = new ArrayList[matrixSize][matrixSize];
            dummyMatrix = new ClockworkTooltipDummy[matrixSize][matrixSize];
        }

        public List<String> clockworkMatrixComponentTooltips(ClockworkTooltipDummy dummy){
            List<String> returnValue = new ArrayList<>();
            for (ClockworkBaseStat stat : dummy.getClockworkStatCollection()) {
                returnValue.add("TODO");
                return returnValue;
            }
            
            return returnValue;
        }
        @Override
        public int getDiameter() {
            return this.matrixSize;
        }

        @Override
        public void buildFromStacks(IClockworkComponentItem[][] components) {
            resetClockworkStats();
            for (int x = 0; x < components.length ; x++) {
                for (int y = 0; y < components[x].length; y++) {
                    if(components[x][y] != null){
                        for (ModifierParent origin: components[x][y].getClockworkCompModifiers()) {
                            populateModifierList(origin,x,y);
                        }
                        giveNeighborboon(x,y);
                        dummyMatrix[x][y] = new ClockworkTooltipDummy(components[x][y]);
                    }
                }
            }

            for (int x = 0; x < dummyMatrix.length ; x++) {
                for (int y = 0; y < dummyMatrix[x].length; y++) {
                    if(dummyMatrix[x][y] != null){
                        ClockworkTooltipDummy dummy = dummyMatrix[x][y];
                        if(matrixCellModifiers[x][y] != null){

                            List<ModifierChild> originalList = matrixCellModifiers[x][y];
                            List<ModifierChild> clonedList = new ArrayList<>();

                            int j = 0;
                            while(originalList.size() > 0){
                                List<ModifierChild> hits = new ArrayList<ModifierChild>();
                                clonedList.add(originalList.get(0));
                                originalList.remove(0);
                                for (int i = 0; i < originalList.size(); i++) {

                                    boolean success = clonedList.get(j).mergeModChilds(originalList.get(i));
                                    if(success){
                                        hits.add(originalList.get(i));
                                    }
                                }
                                j++;
                                originalList.removeAll(hits);
                            }
                            matrixCellModifiers[x][y] = clonedList;

                        for (ModifierChild mod : matrixCellModifiers[x][y]) {
                            mod.stuff2(dummy);
                        }}

                        matrixCompDescription[x][y] = new ArrayList<String>();
                        matrixCompDescription[x][y].addAll(dummy.getTooltip(dummy.getClockworkStatCollection()));
                        summedWindUp.modifiedValue = summedWindUp.statValue += dummy.getWindupCost().modifiedValue;
                        summedProgress.modifiedValue = summedProgress.statValue += dummy.getProgress().modifiedValue;
                        summedEfficiency.modifiedValue = summedEfficiency.statValue += dummy.getEfficiency().modifiedValue;

                    }
                }
            }
        }

        private void giveNeighborboon(int xStart, int yStart){
            int range = 1;
            int xLower = xStart - range;
            int yLower = yStart - range;
            int xUpper = xStart + range;
            int yUpper = yStart + range;

            for (int x = xLower; x <= xUpper; x++) {
                for (int y = yLower; y <= yUpper; y++) {
                    if(x < 0 || y < 0 || x >= matrixSize || y >= matrixSize) continue;
                    if(x == xStart && y == yStart) continue;
                    if((x == xUpper  && y == yUpper) || (x == xLower && y == yLower) || (x == xLower && y == yUpper) || (x == xUpper && y == yLower)){
                        continue;
                    }
                    if(matrixCellModifiers[x][y] == null){
                        matrixCellModifiers[x][y] = new ArrayList<ModifierChild>();
                    }

                    ModifierChild child = ModifierCollection.getNeighbourChildBoon();
                    matrixCellModifiers[x][y].add(child);

                }
            }

        }

        private void populateModifierList(ModifierParent origin, int xStart, int yStart){
            int range = origin.getRange();
            int xLower = xStart - range;
            int yLower = yStart - range;
            int xUpper = xStart + range;
            int yUpper = yStart + range;

            for (int x = xLower; x <= xUpper; x++) {
                for (int y = yLower; y <= yUpper; y++) {
                    if(x < 0 || y < 0 || x >= matrixSize || y >= matrixSize) continue;
                    if(x == xStart && y == yStart) continue;
                    if((x == xUpper  && y == yUpper) || (x == xLower && y == yLower) || (x == xLower && y == yUpper) || (x == xUpper && y == yLower)){
                        continue;
                    }
                    if(matrixCellModifiers[x][y] == null){
                        matrixCellModifiers[x][y] = new ArrayList<ModifierChild>();
                    }

                    ModifierChild child = origin.spawnChild();
                    matrixCellModifiers[x][y].add(child);

                }
            }

        }

        @Override
        @Nullable
        public List<String> getAdditionalTooltipsForMatrixPosition(int x, int y) {
            return this.matrixCompDescription[x][y];
        }




        @Override
        public NBTTagCompound serializeNBT() {
            NBTTagCompound compound = new NBTTagCompound();
            compound.setFloat("PROGRESS", summedProgress.statValue);
            compound.setFloat("EFFICIENCY", summedEfficiency.statValue);
            compound.setFloat("WINDUP", summedWindUp.statValue);
            compound.setFloat("WINDUPMAX", summedMaxWindUp.statValue);
            // TODO Save data to compound.
            return compound;
        }

        @Override
        public void deserializeNBT(NBTTagCompound compound) {
            summedProgress.modifiedValue = summedProgress.statValue = compound.getFloat("PROGRESS");
            summedEfficiency.modifiedValue = summedEfficiency.statValue =  compound.getFloat("EFFICIENCY");
            summedWindUp.modifiedValue = summedWindUp.statValue = compound.getFloat("WINDUP");
            summedMaxWindUp.modifiedValue = summedMaxWindUp.statValue = compound.getFloat("WINDUPMAX");


        }

        @Override
        public ClockworkBaseStat getProgress() {
            return summedProgress;
        }

        @Override
        public ClockworkBaseStat getWindupCost() {
            return summedWindUp;
        }

        @Override
        public ClockworkBaseStat getEfficiency() {
            return summedEfficiency;
        }

        public ClockworkBaseStat getWindUpMaxMod() {
            return summedMaxWindUp;
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
