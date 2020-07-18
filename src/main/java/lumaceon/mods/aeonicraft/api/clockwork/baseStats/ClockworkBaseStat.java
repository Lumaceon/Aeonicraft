package lumaceon.mods.aeonicraft.api.clockwork.baseStats;

public abstract class ClockworkBaseStat {
    public abstract String getStatName();
    public abstract String getColorCode();
    public float StatValue;

    public ClockworkBaseStat(float statValue){
        StatValue = statValue;
    }


}
