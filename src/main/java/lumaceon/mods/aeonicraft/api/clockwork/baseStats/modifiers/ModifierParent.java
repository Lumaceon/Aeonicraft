package lumaceon.mods.aeonicraft.api.clockwork.baseStats.modifiers;
import lumaceon.mods.aeonicraft.api.clockwork.IClockworkComponent;
import lumaceon.mods.aeonicraft.api.clockwork.baseStats.ClockworkBaseStat;
import lumaceon.mods.aeonicraft.api.clockwork.baseStats.tooltip.IClockworkTooltip;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;


public class ModifierParent implements  IClockworkTooltip {

    float value;
    IClockworkComponent parentComp;
    Function<ModifierParent, String> getTooltip;
    private Function<ModifierChild, String> childTooltip;
    Function<IClockworkComponent, ClockworkBaseStat> relevantModifier;
    private int range;
    int ID;
    private BiConsumer<ClockworkBaseStat, Float> calculation;


    public ModifierParent(float value, int range, IClockworkComponent parentComponent, Function<IClockworkComponent, ClockworkBaseStat> baseStatFunction, int ID){
        this.value = value;
        this.parentComp = parentComponent;
        this.range = range;
        relevantModifier = baseStatFunction;
        getTooltip = (mod) -> "Tooltip not set";
        this.ID = ID;
    }

    public void setGetTooltip(Function<ModifierParent, String> tooltipFunction){
        getTooltip = tooltipFunction;
    }

    public ClockworkBaseStat getRelevantModifier(IClockworkComponent comp){
        return relevantModifier.apply(comp);
    }

    public ModifierChild spawnChild(){
        ModifierChild returnValue = new ModifierChild(ID,value,relevantModifier, calculation,childTooltip);
        return returnValue;
    }

    public void doNothing(){

    }

    @Override
    public List<String> getBasicTooltipDescription() {
        List<String> returnValue = new ArrayList<String>();
        returnValue.add(getTooltip.apply(this));
        return  returnValue;
    }

    public void setCalculation(BiConsumer<ClockworkBaseStat, Float> calculation) {
        this.calculation = calculation;
    }

    public void setChildTooltip(Function<ModifierChild, String> childTooltip) {
        this.childTooltip = childTooltip;
    }

    public int getRange() {
        return range;
    }
}
