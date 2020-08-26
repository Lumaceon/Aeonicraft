package lumaceon.mods.aeonicraft.api.clockwork.baseStats.modifiers;

import lumaceon.mods.aeonicraft.api.clockwork.IClockworkComponent;
import lumaceon.mods.aeonicraft.api.clockwork.baseStats.ClockworkBaseStat;

/**
 * Class that manages the ModifierParents and Childs. Building and returning them to be used by Components and so on
 * to avoid code clutter.
 */
public class ModifierCollection {

    //general method that defines function behaviour to return progress of a component
    private static ClockworkBaseStat getCompProgress(IClockworkComponent comp){
        return comp.getProgress();
    }

    /**
     * "Plus" modifier that add a static value to a BaseStat in question
     * @param value amount to be added
     * @param range range of the modifier
     * @param origin ClockworkComponent this is attached to TODO: can be deleted?
     * @return returns the ModifierParent that was created
     */
    public static ModifierParent getNewPlusModifier(float value, float range, IClockworkComponent origin){

            //creates a new ModifierParent and sets correct values
            //todo currently just static values, change for modularity
            ModifierParent returnValue = new ModifierParent(5, 1, origin,ModifierCollection::getCompProgress, 1);

            //sets the tooltip of the modifierParent
            returnValue.setGetTooltip((mod) -> {
                return "+" + mod.value +" $"+ mod.getRelevantModifier(origin).getColorCode() + mod.getRelevantModifier(origin).getStatName() +"$r to components in " + mod.getRange() + " tiles range.";
            });

            //sets the calculation for the modifierChild
            returnValue.setCalculation((stat, val) ->{
                stat.modifiedValue += val;
            } );

            //sets the tooltip for the modifierChild
            returnValue.setChildTooltip((mod) -> "  +" + mod.value +" from bonus");


            return returnValue;
        }

    /**
     * a modifierChild applied directly to the matrix to calculate neighbour bonus on the matrix
     * @return
     */
    public static ModifierChild getNeighbourChildBoon(){

                ModifierChild returnValue = new ModifierChild(2,2,ModifierCollection::getCompProgress,(stat, val) -> stat.modifiedValue += stat.statValue * (val-1),(mod) -> "x" + mod.value + " from neighbourbonus");
                return returnValue;
    }
    }
