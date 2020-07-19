package lumaceon.mods.aeonicraft.api.clockwork.baseStats;

import lumaceon.mods.aeonicraft.api.clockwork.IClockworkTooltip;

//Interface that is a collection for the stats of the ClockworkMatrix shenanigans,
//Also has a helper method that returns a collection of these stats back
public interface IClockworkBaseStats {

    ClockworkProgressStat getProgress();
    ClockworkWindUpStat getWindUpCost();
    ClockworkMaxWindUpStat getWindUpMaxMod();
    ClockworkEfficiencyStat getEfficiency();

    default public ClockworkBaseStat[] getClockworkStatCollection(){
        ClockworkBaseStat[] returnValues = {getProgress(),getEfficiency(),getWindUpMaxMod(),getWindUpCost()};
        return returnValues;
    }

}
