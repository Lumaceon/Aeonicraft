package lumaceon.mods.aeonicraft.registry;


import lumaceon.mods.aeonicraft.capability.*;

public class ModCapabilities
{
    public static void init()
    {
        CapabilityAeonicraftProgression.register();
        CapabilityToggle.register();
        CapabilityHourglass.register();
        CapabilityTravelGhost.register();
        CapabilityClockwork.register();
        CapabilityTemporalNetworkLinker.register();
    }
}
