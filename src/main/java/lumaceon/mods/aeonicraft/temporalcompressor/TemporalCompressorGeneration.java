package lumaceon.mods.aeonicraft.temporalcompressor;

import lumaceon.mods.aeonicraft.util.TimeParser;

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
        return new TemporalCompressorStats(TimeParser.SECOND * 0);
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
