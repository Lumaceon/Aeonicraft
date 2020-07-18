package lumaceon.mods.aeonicraft.api.clockwork.baseStats;

public class ClockworkProgressStat extends  ClockworkBaseStat {
    @Override
    public String getStatName() {
        return "Progress";
    }

    @Override
    public String getColorCode() {
        return "§3";
    }

    public ClockworkProgressStat(float statValue) {
        super(statValue);
    }
}
