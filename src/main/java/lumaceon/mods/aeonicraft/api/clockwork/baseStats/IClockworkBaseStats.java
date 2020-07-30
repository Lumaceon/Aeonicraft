package lumaceon.mods.aeonicraft.api.clockwork.baseStats;

//Interface that is a collection for the stats of the ClockworkMatrix shenanigans,
//Also has a helper method that returns a collection of these stats back
public interface IClockworkBaseStats {


    ClockworkBaseStat getProgress();
    ClockworkBaseStat getWindupCost();
    ClockworkBaseStat getEfficiency();

    default public ClockworkBaseStat[] getClockworkStatCollection(){
        ClockworkBaseStat[] returnValues = {getProgress(),getEfficiency(), getWindupCost()};
        return returnValues;
    }

}
