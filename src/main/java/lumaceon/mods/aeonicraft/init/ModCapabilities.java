package lumaceon.mods.aeonicraft.init;


import lumaceon.mods.aeonicraft.capability.CapabilityHourglass;
import lumaceon.mods.aeonicraft.capability.CapabilityTimeLink;
import lumaceon.mods.aeonicraft.capability.CapabilityTimeStorage;
import lumaceon.mods.aeonicraft.capability.CapabilityToggle;
import lumaceon.mods.aeonicraft.capability.CapabilityTravelGhost;

public class ModCapabilities
{
    public static void init()
    {
        CapabilityTimeStorage.register();
        CapabilityToggle.register();
        CapabilityTimeLink.register();
        CapabilityHourglass.register();
        CapabilityTravelGhost.register();
    }
}
