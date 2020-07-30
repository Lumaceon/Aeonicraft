package lumaceon.mods.aeonicraft.api.clockwork.baseStats.tooltip;

import lumaceon.mods.aeonicraft.api.clockwork.baseStats.ClockworkBaseStat;

import java.util.ArrayList;
import java.util.List;

//Tooltip helper interface that individual Clockwork items populate their Tooltips with
public interface IClockworkTooltips {

    default public List<String> getTooltip(ClockworkBaseStat[] stats){
        List<String> returnValue = new ArrayList<String>();
        for(ClockworkBaseStat stat : stats){
            if(stat.StatValue != 0){
                //Format of the tooltips, bold statName with a color followed by the number.
                returnValue.addAll(stat.getBasicTooltipDescription());
            }
        }
        return returnValue;
    }

}
