package lumaceon.mods.aeonicraft.api;

import lumaceon.mods.aeonicraft.api.temporalnetwork.TemporalNetwork;
import lumaceon.mods.aeonicraft.api.util.BlockLoc;

import java.util.function.Function;

/**
 * Internal variables set by Aeonicraft to enable the API. This should never be accessed directly.
 */
public class Internal
{
    public static Function<BlockLoc, TemporalNetwork> temporalNetworkRetriever;
    public static Function<TemporalNetwork, Boolean> destroyTemporalNetwork;
}
