package lumaceon.mods.aeonicraft.api.clockwork.baseStats;

/*
ClockworkBaseStat class for easier formatting of ingame tooltips.
The children that inherit from this have their getStatName and getColorCode a pre-defined value that is the same across all initialized variables
 */
public abstract class ClockworkBaseStat {
    public abstract String getStatName();
    public abstract String getColorCode();
    public float StatValue;

    public ClockworkBaseStat(float statValue){
        StatValue = statValue;
    }


}
