package lumaceon.mods.aeonicraft.api.clockwork.baseStats.modifiers;

import lumaceon.mods.aeonicraft.api.clockwork.IClockworkComponent;
import lumaceon.mods.aeonicraft.api.clockwork.baseStats.ClockworkBaseStat;

public class ModifierCollection {

    private static ClockworkBaseStat getCompProgress(IClockworkComponent comp){
        return comp.getProgress();
    }

    public static ModifierParent getNewPlusModifier(float value, float range, IClockworkComponent origin){
            ModifierParent returnValue = new ModifierParent(5, 1, origin,ModifierCollection::getCompProgress, 1);
            returnValue.setGetTooltip((mod) -> {
                return "+" + mod.value +" $"+ mod.getRelevantModifier(origin).getColorCode() + mod.getRelevantModifier(origin).getStatName() +"$r to components in " + mod.getRange() + " tiles range.";
            });

            returnValue.setCalculation((stat, val) ->{
                stat.modifiedValue += val;
            } );

            returnValue.setChildTooltip((mod) -> "  +" + mod.value +" from bonus");
            return returnValue;
        }

        public static ModifierChild getNeighbourChildBoon(){

                ModifierChild returnValue = new ModifierChild(2,2,ModifierCollection::getCompProgress,(stat, val) -> stat.modifiedValue += stat.statValue * (val-1),(mod) -> "x" + mod.value + " from neighbourbonus");
                return returnValue;
    }
    }
