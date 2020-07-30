package lumaceon.mods.aeonicraft.api.clockwork.baseStats;

import lumaceon.mods.aeonicraft.Aeonicraft;
import lumaceon.mods.aeonicraft.api.clockwork.baseStats.tooltip.IClockworkTooltip;

import java.util.ArrayList;
import java.util.List;

/*
ClockworkBaseStat class for easier formatting of ingame tooltips.
The children that inherit from this have their getStatName and getColorCode a pre-defined value that is the same across all initialized variables
 */
public class ClockworkBaseStat implements IClockworkTooltip {
    public float StatValue;
    private ClockworkBaseStatDescriptor descriptor;



    public ClockworkBaseStat(float statValue, ClockworkBaseStatDescriptor descriptor){
        StatValue = statValue;
        this.descriptor = descriptor;
    }

    /*
    get the default/basic description for tooltips of this stat.
     */
    @Override
    public List<String> getBasicTooltipDescription(){
        List<String> returnValue = new ArrayList<String>();
        returnValue.add("§l" + getColorCode()+ getStatName()+":§r " + StatValue);
        return returnValue;
    }

    public String getStatName(){
        return descriptor.StatName;
    }

    public String getColorCode(){
        return descriptor.ColorCode;
    }
}
