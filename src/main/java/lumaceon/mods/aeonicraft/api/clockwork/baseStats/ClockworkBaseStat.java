package lumaceon.mods.aeonicraft.api.clockwork.baseStats;

import lumaceon.mods.aeonicraft.api.clockwork.baseStats.tooltip.IClockworkTooltip;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/*
ClockworkBaseStat class for easier formatting of ingame tooltips.
The children that inherit from this have their getStatName and getColorCode a pre-defined value that is the same across all initialized variables
 */
public class ClockworkBaseStat implements IClockworkTooltip {
    public float statValue;
    public float modifiedValue;
    private List<String> modifiers = new ArrayList<String>();
    private ClockworkBaseStatDescriptor descriptor;




    public ClockworkBaseStat(float statValue, ClockworkBaseStatDescriptor descriptor){
        this.statValue = statValue;
        this.descriptor = descriptor;
        modifiedValue = statValue;
    }

    /*
    get the default/basic description for tooltips of this stat.
     */
    @Override
    public List<String> getBasicTooltipDescription(){
        List<String> returnValue = new ArrayList<String>();
        String formatedValue = Float.toString(statValue);

        if(statValue != modifiedValue){
            formatedValue=formatedValue +"(";
            float difference = Math.abs(statValue - modifiedValue);
            if(statValue > modifiedValue){
                formatedValue=formatedValue+"-";
            }else{
                formatedValue=formatedValue+"+";
            }
            formatedValue=formatedValue+difference+")";
        }

        returnValue.add("§l" + getColorCode()+ getStatName()+":§r " + formatedValue);
        for (String modifier : modifiers
             ) {
            returnValue.add(modifier);
        }
        return returnValue;
    }

    public String getStatName(){
        return descriptor.StatName;
    }

    public String getColorCode(){
        return descriptor.ColorCode;
    }

    public List<String> getModifiers() {
        return modifiers;
    }
}
