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


/**
 * Class that populates the ClockworkMatrix with modifiers and applies its calculations (and tooltip) to a
 * ClockworkComponent that lies in the same Matrix cell.
 */
public class ModifierChild implements IClockworkTooltip {

    //unique ID that is used to merge multiple modifiers of the same kind on the same cell so tooltips don't repeat
    int ID;

    //function to define that returns the relevant stat to make the Class more modular.
    // e.g. something like return IClockworkComponent.getSpeedStat
    Function<IClockworkComponent, ClockworkBaseStat> relevantClockworkStat;

    //function to define that returns properly formated tooltip of the
    // child as modifier tooltips can be rather unique
    Function<ModifierChild, String> getTooltip;

    //value of the modifierChild to work/calculate with
    float value;

    //function to define that does the calculation and modifies the ClockworkBaseStat class
    BiConsumer<ClockworkBaseStat, Float> calculation;

    /**
     * Method that modifies the ClockworkBaseStat of the target defined by relevantClockworkStat with the calculations
     * defined by calculation.
     * @param target ClockworkComponent that should be modified
     */
    public void modifyClockworkComp(IClockworkComponent target){

        //return if the relevantStat is 0
        if(relevantClockworkStat.apply(target).statValue == 0) return;

        //add the tooltip of the child to the ClockworkStat
        relevantClockworkStat.apply(target).getModifiers().add(getTooltip.apply(this));

        //execute the actual calculation
        calculation.accept(relevantClockworkStat.apply(target), value);
    }


    /**
     * Constructor of the ModifierChild class
     * @param ID semi-unique ID to identify and merge equal modifierChild classes
     * @param value value that the modifierChild works with
     * @param relevantClockworkStat function to define that returns the relevant stat to make the Class more modular. e.g. something like return IClockworkComponent.getSpeedStat
     * @param calculation function to define that does the calculation and modifies the ClockworkBaseStat class
     */
   public ModifierChild(int ID, float value, Function<IClockworkComponent, ClockworkBaseStat> relevantClockworkStat, BiConsumer<ClockworkBaseStat, Float> calculation,Function <ModifierChild, String> getTooltip){
        this.ID = ID;
        this.value = value;
        this.relevantClockworkStat = relevantClockworkStat;
        this.calculation = calculation;
        this.getTooltip = getTooltip;

        //todo: can be deleted? Can't check if it still runs atm, commented out for the time being
        // getTooltip = (mod) -> "Tooltip not set";
   }


    /**
     * Sets the getTooltip function
     * @param tooltipFunction function that getTooltip should be set to
     */
    public void setGetTooltip(Function<ModifierChild, String> tooltipFunction){
        getTooltip = tooltipFunction;
    }

    public float getValue(){
        return value;
    }

    /**
     * returns tooltip of the class as defined in GetTooltip
     * @return returnValue in List<String> format
     */
    @Override
    public List<String> getBasicTooltipDescription() {
        List<String> returnValue = new ArrayList<String>();
        returnValue.add(getTooltip.apply(this));
        return returnValue;
    }

    /**
     * Checks if another ModifierChild is of the same class as this one and merges their value if true
     * @param other other ModifierChild to check against
     * @return returns true if the "merge" was succesful, false if not
     */
    public boolean mergeModChildValues(ModifierChild other){
        if(other.ID == ID){
            value += other.getValue();
            return true;
        }
        return false;
    }
}
