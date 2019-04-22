package lumaceon.mods.aeonicraft.init;


import lumaceon.mods.aeonicraft.capability.hourglass.CapabilityHourglass;
import lumaceon.mods.aeonicraft.capability.timelink.CapabilityTimeLink;
import lumaceon.mods.aeonicraft.capability.toggle.CapabilityToggle;

public class ModCapabilities
{
    public static void init()
    {
        CapabilityToggle.register();
        CapabilityTimeLink.register();
        CapabilityHourglass.register();
    }
}
