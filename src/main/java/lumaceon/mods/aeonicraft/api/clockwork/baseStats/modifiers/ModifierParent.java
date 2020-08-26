package lumaceon.mods.aeonicraft.api.clockwork.baseStats.modifiers;
import lumaceon.mods.aeonicraft.api.clockwork.IClockworkComponent;
import lumaceon.mods.aeonicraft.api.clockwork.baseStats.ClockworkBaseStat;
import lumaceon.mods.aeonicraft.api.clockwork.baseStats.tooltip.IClockworkTooltip;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;


/**
 * ModifierParent class that can be attached to ClockworkComponents.
 * Used in matrix calculation to spawn ModifierChild classes in MatrixCells to modify the values of ClockworkComponents
 * Holds a bunch of values for ModifierChild since it is spawning them
 */
public class ModifierParent implements  IClockworkTooltip {

    //value for the ModifierChild
    float value;

    //reference to the component the modifier is attached to
    //TODO: delete?
    IClockworkComponent parentComp;

    //tooltip of the ModifierParent
    Function<ModifierParent, String> getTooltip;

    //tooltip of the ModifierChild
    private Function<ModifierChild, String> childTooltip;

    //function that returns the relevant stat to make the Class more modular for the child.
    Function<IClockworkComponent, ClockworkBaseStat> relevantClockworkStat;

    //range that the ModifierParent is spawning childs in(1= neighbours, etc.)
    private int range;

    //unique ID for the ModifierChild
    int ID;

    //calculation the ModifierChild does to the ClockworkBaseStat
    private BiConsumer<ClockworkBaseStat, Float> calculation;

    /**
     * Constructor for ModifierParent
     * @param value value for the child to calculate with
     * @param range range that children might be spawned in
     * @param parentComponent parentComponent that the Modifier is attached to
     * @param baseStatFunction function that returns the relevant stat to make the class more modular
     * @param ID unique ID for the child
     */
    public ModifierParent(float value, int range, IClockworkComponent parentComponent, Function<IClockworkComponent, ClockworkBaseStat> baseStatFunction, int ID){
        this.value = value;
        this.parentComp = parentComponent;
        this.range = range;
        relevantClockworkStat = baseStatFunction;
        getTooltip = (mod) -> "Tooltip not set";
        this.ID = ID;
    }

    /**
     * sets tooltip of the ModifierParent
     * @param tooltipFunction
     */
    public void setGetTooltip(Function<ModifierParent, String> tooltipFunction){
        getTooltip = tooltipFunction;
    }

    /* todo can be deleted? Commented out for now
    public ClockworkBaseStat getRelevantModifier(IClockworkComponent comp){
        return relevantClockworkStat.apply(comp);
    }*/

    /**
     * creates a new ModifierChild
     * @return returns newly created ModifierChild
     */
    public ModifierChild spawnChild(){
        ModifierChild returnValue = new ModifierChild(ID,value, relevantClockworkStat, calculation,childTooltip);
        return returnValue;
    }

    /* todo can be deleted? Commented out for now
    public void doNothing(){

    }*/

    /**
     * returns tooltip as defined by getTooltip
     * @return returns the tooltip in List<String> form
     */
    @Override
    public List<String> getBasicTooltipDescription() {
        List<String> returnValue = new ArrayList<String>();
        returnValue.add(getTooltip.apply(this));
        return  returnValue;
    }

    /**
     * sets the calculation shenanigans for the child
     * @param calculation Function that calculation is to be set to
     */
    public void setCalculation(BiConsumer<ClockworkBaseStat, Float> calculation) {
        this.calculation = calculation;
    }

    /**
     * sets tooltip of the child
     * @param tooltipFunction
     */
    public void setChildTooltip(Function<ModifierChild, String> childTooltip) {
        this.childTooltip = childTooltip;
    }

    /**
     * returns range variable of the parent
     * @return returns range
     */
    public int getRange() {
        return range;
    }
}
