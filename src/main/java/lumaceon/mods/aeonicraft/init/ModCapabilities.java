package lumaceon.mods.aeonicraft.init;


import lumaceon.mods.aeonicraft.capability.*;

public class ModCapabilities
{
    public static void init()
    {
        CapabilityAeonicraftProgression.register();
        CapabilityTimeStorage.register();
        CapabilityToggle.register();
        CapabilityTimeLink.register();
        CapabilityHourglass.register();
        CapabilityTravelGhost.register();
        CapabilityClockwork.register();
    }
}
