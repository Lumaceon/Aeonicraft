package lumaceon.mods.aeonicraft.api.clockwork;

import lumaceon.mods.aeonicraft.api.clockwork.baseStats.ClockworkBaseStat;

import java.util.List;

//Tooltip helper interface for the Clockwork stuff
public interface IClockworkTooltip {

    default public List<String> getTooltip(ClockworkBaseStat[] stats, List<String> returnValue){

        for(ClockworkBaseStat stat : stats){
            if(stat.StatValue != 0){
                //Format of the tooltips, bold statName with a color followed by the number.
                returnValue.add("§l"+stat.getColorCode()+stat.getStatName()+":§r " + stat.StatValue);
            }
        }
        return returnValue;
    }
}
