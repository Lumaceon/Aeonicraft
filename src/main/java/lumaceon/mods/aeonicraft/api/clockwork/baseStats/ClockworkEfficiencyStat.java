package lumaceon.mods.aeonicraft.api.clockwork.baseStats;

public class ClockworkEfficiencyStat extends  ClockworkBaseStat{
    @Override
    public String getStatName() {
        return "Efficiency";
    }

    @Override
    public String getColorCode() {
        return "§1";
    }

    public ClockworkEfficiencyStat(float statValue) {
        super(statValue);
    }
}
