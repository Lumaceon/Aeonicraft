package lumaceon.mods.aeonicraft.temporalcompressor;

import lumaceon.mods.aeonicraft.Aeonicraft;
import lumaceon.mods.aeonicraft.util.TimeParser;

import java.util.ArrayList;

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
                    component.findAndSetNeighbours(componentMatrix,x,y);
                    components.add(component);
                }
            }
        }

        for (TemporalCompressorComponent component : components) {
            component.doStuff(TemporalCompressorComponentVOP.ModifyLevel.BASE);
        }

        for (TemporalCompressorComponent component : components) {
            component.doStuff(TemporalCompressorComponentVOP.ModifyLevel.MODIFIED);
        }

        for (TemporalCompressorComponent component : components) {
            component.doStuff(TemporalCompressorComponentVOP.ModifyLevel.FINAL);
        }

        for (TemporalCompressorComponent component : components) {
            gain += component.fTCValue;
        }


        return new TemporalCompressorStats(TimeParser.SECOND * gain);
    }

    public static class TemporalCompressorStats
    {
        long temporalCompressionPerTick;
        // etc...

        public TemporalCompressorStats(long temporalCompressionPerTick) {
            this.temporalCompressionPerTick = temporalCompressionPerTick;
        }
    }
}
