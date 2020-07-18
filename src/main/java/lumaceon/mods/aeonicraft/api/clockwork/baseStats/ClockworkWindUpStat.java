package lumaceon.mods.aeonicraft.api.clockwork.baseStats;

public class ClockworkWindUpStat extends  ClockworkBaseStat {
    @Override
    public String getStatName() {
        return "Windup";
    }

    @Override
    public String getColorCode() {
        return "§4";
    }

    public ClockworkWindUpStat(float statValue) {
        super(statValue);
    }
}
