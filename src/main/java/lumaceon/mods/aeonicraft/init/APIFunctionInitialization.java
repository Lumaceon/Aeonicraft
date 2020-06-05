package lumaceon.mods.aeonicraft.init;

import lumaceon.mods.aeonicraft.api.Internal;
import lumaceon.mods.aeonicraft.capability.CapabilityClockwork;
import lumaceon.mods.aeonicraft.lib.Textures;

public class APIFunctionInitialization
{
    public static void initFunctions()
    {
        Internal.createDefaultClockworkImplementation =
                (matrixSize) -> new CapabilityClockwork.Clockwork(matrixSize);
        Internal.defaultAssemblyTableTexture = Textures.GUI.ASSEMBLY_TABLE;
        //TODO Internal.temporalNetworkRetriever = somesuch
    }
}
