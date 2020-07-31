package lumaceon.mods.aeonicraft.api.clockwork.baseStats.modifiers;

import lumaceon.mods.aeonicraft.api.clockwork.IClockworkComponent;
import lumaceon.mods.aeonicraft.api.clockwork.baseStats.ClockworkBaseStat;
import lumaceon.mods.aeonicraft.api.clockwork.baseStats.IClockworkBaseStats;
import lumaceon.mods.aeonicraft.api.clockwork.baseStats.tooltip.IClockworkTooltip;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class ModifierChild implements IClockworkTooltip {

    int ID;
    Function<IClockworkComponent, ClockworkBaseStat> relevantModifier;
    Function<ModifierChild, String> getTooltip;
    float value;
    BiConsumer<ClockworkBaseStat, Float> calculation;

    public void stuff2(IClockworkComponent target){
        if(relevantModifier.apply(target).statValue == 0) return;
        relevantModifier.apply(target).getModifiers().add(getTooltip.apply(this));
        calculation.accept(relevantModifier.apply(target), value);
    }

   public ModifierChild(int ID, float value, Function<IClockworkComponent, ClockworkBaseStat>  relevantModifier, BiConsumer<ClockworkBaseStat, Float> calculation, Function <ModifierChild, String> getTooltip){
        this.ID = ID;
        this.value = value;
        this.relevantModifier = relevantModifier;
        this.calculation = calculation;
        this.getTooltip = getTooltip;
        getTooltip = (mod) -> "Tooltip not set";
   }

    public void setGetTooltip(Function<ModifierChild, String> tooltipFunction){
        getTooltip = tooltipFunction;
    }

    public float getValue(){
        return value;
    }

    @Override
    public List<String> getBasicTooltipDescription() {
        List<String> returnValue = new ArrayList<String>();
        returnValue.add(getTooltip.apply(this));
        return returnValue;
    }

    public boolean mergeModChilds(ModifierChild other){
        if(other.ID == ID){
            value += other.getValue();
            return true;
        }
        return false;
    }
}
