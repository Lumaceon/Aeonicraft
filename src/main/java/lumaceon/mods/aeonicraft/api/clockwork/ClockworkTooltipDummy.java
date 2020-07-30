package lumaceon.mods.aeonicraft.api.clockwork;

import lumaceon.mods.aeonicraft.api.clockwork.baseStats.*;

//Dummy class to hold data to populate modified tooltip matrix on Capability ClockworkJava
public class ClockworkTooltipDummy implements IClockworkComponent {

    public ClockworkTooltipDummy(){
        this(0,0,0);
    }
    public ClockworkTooltipDummy(float efficiency, float progress, float windUp){
        compEfficiency =  BaseStatBuilder.getNewEfficiencyStatInstance(0);
        compProgress =  BaseStatBuilder.getNewProgressStatInstance(0);
        compWindUp =  BaseStatBuilder.getNewWindupStatInstance(0);
    }

    public ClockworkTooltipDummy(IClockworkComponent componentToCopyFrom){
        this(componentToCopyFrom.getEfficiency().StatValue, componentToCopyFrom.getProgress().StatValue,componentToCopyFrom.getWindupCost().StatValue);
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
}
