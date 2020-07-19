package lumaceon.mods.aeonicraft.api.clockwork;

import lumaceon.mods.aeonicraft.api.clockwork.baseStats.*;

//Dummy class to hold data to populate modified tooltip matrix on Capability ClockworkJava
public class ClockworkTooltipDummy implements IClockworkBaseStats {

    ClockworkEfficiencyStat compEfficiency = new ClockworkEfficiencyStat(0);
    ClockworkMaxWindUpStat compMaxWindUp = new ClockworkMaxWindUpStat(0);
    ClockworkProgressStat compProgress = new ClockworkProgressStat(0);
    ClockworkWindUpStat compWindUp = new ClockworkWindUpStat(0);

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
