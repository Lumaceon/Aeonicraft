package lumaceon.mods.aeonicraft.api;

import lumaceon.mods.aeonicraft.api.clockwork.IClockwork;
import lumaceon.mods.aeonicraft.api.temporalnetwork.TemporalNetwork;
import lumaceon.mods.aeonicraft.api.util.BlockLoc;
import net.minecraft.util.ResourceLocation;

import java.util.function.Function;

/**
 * Internal variables set by Aeonicraft to enable the API. This should never be accessed directly.
 */
public class Internal
{
    public static Function<BlockLoc, TemporalNetwork> getTemporalNetwork;
    public static Function<BlockLoc, TemporalNetwork> addTemporalNetworkLocation;
    public static Function<BlockLoc, Integer> removeTemporalNetworkLocation;
    public static Function<Integer, IClockwork> createDefaultClockworkImplementation;

    public static ResourceLocation defaultAssemblyTableTexture;
}
