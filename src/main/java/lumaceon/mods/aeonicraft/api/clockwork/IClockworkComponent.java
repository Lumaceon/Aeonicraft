package lumaceon.mods.aeonicraft.api.clockwork;

import lumaceon.mods.aeonicraft.api.clockwork.baseStats.ClockworkEfficiencyStat;
import lumaceon.mods.aeonicraft.api.clockwork.baseStats.ClockworkMaxWindUpStat;
import lumaceon.mods.aeonicraft.api.clockwork.baseStats.ClockworkProgressStat;
import lumaceon.mods.aeonicraft.api.clockwork.baseStats.ClockworkWindUpStat;

public interface IClockworkComponent
{
        ClockworkProgressStat getProgress();
        ClockworkWindUpStat getWindUpCost();
        ClockworkMaxWindUpStat getWindUpMaxMod();
        ClockworkEfficiencyStat getEfficiency();
        ClockworkComponentTypes getType();
}

