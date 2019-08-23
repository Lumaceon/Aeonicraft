package lumaceon.mods.aeonicraft.temporalcompressor;

import lumaceon.mods.aeonicraft.Aeonicraft;
import lumaceon.mods.aeonicraft.api.temporalcompression.TemporalCompressorComponent;
import lumaceon.mods.aeonicraft.util.TimeParser;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Methods for handling the TC generation of Temporal Compressors.
 */
public class TemporalCompressorGeneration
{
    /**
     * Get the stats for the compressor given the matrix of components. Called when changes are made to the matrix.
     * @param componentMatrix
     * @return
     */
    public static TemporalCompressorStats calculateStatsPerTickFromCompressor(TemporalCompressorMatrix componentMatrix)
    {
        // TODO - Make this work gud.
        long gain = 0;

        ArrayList<TemporalCompressorComponent> components = new ArrayList<>();

        //Connect neighbours of matrix
        for (int x = 0; x < componentMatrix.matrix.length; x++) {
            for (int y = 0; y < componentMatrix.matrix[x].length; y++) {
                TemporalCompressorComponent component = componentMatrix.getComponentForCoordinates(x,y);
                if(component != null){
                    components.add(component);
                }
            }
        }

        ArrayList<TemporalCompressorComponentModifier> globalsBefore = getSortedGlobals(components,true);
        ArrayList<TemporalCompressorComponentModifier> globalsAfter = getSortedGlobals(components,false);
        //replace foreach with i iterations for efficency?
        //replace ArrayList with ??? for efficiency?

        for (TemporalCompressorComponent component : components) {
            component.findAndSetNeighbours(componentMatrix); //Add matrix to Component for efficency?
        }

        executeModifierLogics(components,globalsBefore,globalsAfter, TemporalCompressorComponentModifier.ModifyLevel.BASE);
        executeModifierLogics(components,globalsBefore,globalsAfter, TemporalCompressorComponentModifier.ModifyLevel.MODIFIED);
        executeModifierLogics(components,globalsBefore,globalsAfter, TemporalCompressorComponentModifier.ModifyLevel.FINAL);

        for (TemporalCompressorComponent component : components) {
            gain += component.fTCValue;
        }


        return new TemporalCompressorStats(TimeParser.SECOND * gain);
    }

    //Sorts modifiers that affect stuff globally in a modifier list. One is given out to be execute before neighbour logic. The other for after.
    private static ArrayList<TemporalCompressorComponentModifier> getSortedGlobals (ArrayList<TemporalCompressorComponent> components, boolean before) {
        ArrayList<TemporalCompressorComponentModifier> returnValue = new ArrayList<>();

        for (TemporalCompressorComponent component : components) {
            for (TemporalCompressorComponentModifier modifier : component.TCModifiers) {
                if (before && modifier.priority < 0) {
                    returnValue.add(modifier);
                } else if(!before && modifier.priority >= 0){
                        returnValue.add(modifier);
                    }
                }
            }
        Collections.sort(returnValue);
        return returnValue;
    }

    //First executes possible globals before, then neighbour modifiers, then globals after
    public static void executeModifierLogics(ArrayList<TemporalCompressorComponent> components, ArrayList<TemporalCompressorComponentModifier> globalsBefore, ArrayList<TemporalCompressorComponentModifier> globalsAfter, TemporalCompressorComponentModifier.ModifyLevel modifyLevel){
        //replace foreach with i iterations for efficency?
        //replace ArrayList with ??? for efficiency?
        for (TemporalCompressorComponent component : components) {

            if(!component.isModifiable) continue;

            for (TemporalCompressorComponentModifier before : globalsBefore) {component.getGlobified(modifyLevel,before);
            }
        }

        for (TemporalCompressorComponent component : components) {
            if(!component.isModifiable) continue;
            component.doStuff(modifyLevel);

        }

        for (TemporalCompressorComponent component : components) {

            if(!component.isModifiable) continue;

            for (TemporalCompressorComponentModifier before : globalsAfter) {component.getGlobified(modifyLevel,before);
            }

        }
    }

    public static class TemporalCompressorStats
    {
        long temporalCompressionPerTick;
        // etc...

        public TemporalCompressorStats(long temporalCompressionPerTick) {
            this.temporalCompressionPerTick = temporalCompressionPerTick;
        }

        public void printStatsToConsole() {
            Aeonicraft.logger.info(temporalCompressionPerTick);
        }
    }
}
