package lumaceon.mods.aeonicraft.api.clockwork;

import lumaceon.mods.aeonicraft.api.clockwork.baseStats.*;
import lumaceon.mods.aeonicraft.api.clockwork.baseStats.tooltip.IClockworkTooltip;
import lumaceon.mods.aeonicraft.api.clockwork.baseStats.tooltip.IClockworkTooltips;

import java.util.List;

//Dummy class to hold data to populate modified tooltip matrix on Capability ClockworkJava
public class ClockworkTooltipDummy implements IClockworkComponent, IClockworkTooltips {

    public ClockworkTooltipDummy(){
        this(0,0,0);
    }

    public ClockworkTooltipDummy(float efficiency, float progress, float windUp){
        compEfficiency =  BaseStatBuilder.getNewEfficiencyStatInstance(efficiency);
        compProgress =  BaseStatBuilder.getNewProgressStatInstance(progress);
        compWindUp =  BaseStatBuilder.getNewWindupStatInstance(windUp);
    }

    public ClockworkTooltipDummy(IClockworkComponent componentToCopyFrom){
        this(componentToCopyFrom.getEfficiency().statValue, componentToCopyFrom.getProgress().statValue, componentToCopyFrom.getWindupCost().statValue);
    }

    ClockworkBaseStat    compEfficiency;
   ClockworkBaseStat    compProgress;
   ClockworkBaseStat    compWindUp;

    @Override
    public ClockworkBaseStat getProgress() {
        return compProgress;
    }
    @Override
    public ClockworkBaseStat getWindupCost() {
        return compWindUp;
    }
    @Override
    public ClockworkBaseStat getEfficiency() {
        return compEfficiency;
    }
    @Override
    public ClockworkComponentTypes getType() {
        return null;
    }

 /*   @Override
    public List<String> getBasicTooltipDescription() {
        return null;
    }*/
}
