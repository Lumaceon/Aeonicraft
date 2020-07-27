package lumaceon.mods.aeonicraft.api.clockwork;

import lumaceon.mods.aeonicraft.api.clockwork.baseStats.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

//Dummy class to hold data to populate modified tooltip matrix on Capability ClockworkJava
public class ClockworkTooltipDummy implements IClockworkBaseStats {

    //todo do more thonks
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClockworkTooltipDummy that = (ClockworkTooltipDummy) o;
        return Objects.equals(compEfficiency, that.compEfficiency) &&
                Objects.equals(compMaxWindUp, that.compMaxWindUp) &&
                Objects.equals(compProgress, that.compProgress) &&
                Objects.equals(compWindUp, that.compWindUp) &&
                Objects.equals(clockworkModTooltipCollection, that.clockworkModTooltipCollection);
    }

    @Override
    public int hashCode() {
        return Objects.hash(compEfficiency, compMaxWindUp, compProgress, compWindUp);
    }

    public ClockworkEfficiencyStat compEfficiency = new ClockworkEfficiencyStat(0);
    ClockworkMaxWindUpStat compMaxWindUp = new ClockworkMaxWindUpStat(0);
    ClockworkProgressStat compProgress = new ClockworkProgressStat(0);
    ClockworkWindUpStat compWindUp = new ClockworkWindUpStat(0);
    HashMap<ClockworkBaseStat, List<String>> clockworkModTooltipCollection = new HashMap<ClockworkBaseStat,List <String>>();

    public HashMap<ClockworkBaseStat, List<String>> getTooltipCollection(){
        return clockworkModTooltipCollection;
    }

    public List<String> getTooltipCollection(ClockworkBaseStat key){
        if(!clockworkModTooltipCollection.containsKey(key)) {
            clockworkModTooltipCollection.put(key,new ArrayList<String>());
        }

        return clockworkModTooltipCollection.get(key);
    }

    @Override
    public ClockworkProgressStat getProgress() {
        return compProgress;
    }

    @Override
    public ClockworkWindUpStat getWindUpCost() {
        return compWindUp;
    }

    @Override
    public ClockworkMaxWindUpStat getWindUpMaxMod() {
        return compMaxWindUp;
    }

    @Override
    public ClockworkEfficiencyStat getEfficiency() {
        return compEfficiency;
    }
}
