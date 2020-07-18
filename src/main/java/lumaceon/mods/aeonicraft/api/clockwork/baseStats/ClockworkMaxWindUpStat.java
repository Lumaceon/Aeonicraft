package lumaceon.mods.aeonicraft.api.clockwork.baseStats;

public class ClockworkMaxWindUpStat extends  ClockworkBaseStat {
    @Override
    public String getStatName() {
        return "Max Windup";
    }

    @Override
    public String getColorCode() {
        return "§2";
    }

    public ClockworkMaxWindUpStat(float statValue) {
        super(statValue);
    }
}
