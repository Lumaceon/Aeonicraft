package lumaceon.mods.aeonicraft.api.clockwork.baseStats;

import java.util.ArrayList;
import java.util.List;

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

    /*
    get the default/basic description for tooltips of this stat.
     */
    public List<String> getBasicTooltipDescription(){
        List<String> returnValue = new ArrayList<String>();
        returnValue.add("§l" + getColorCode()+ getStatName()+":§r " + StatValue);
        return returnValue;
    }


}
