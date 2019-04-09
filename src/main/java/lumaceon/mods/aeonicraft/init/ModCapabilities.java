package lumaceon.mods.aeonicraft.init;


import lumaceon.mods.aeonicraft.capability.timelink.CapabilityTimeLink;

public class ModCapabilities
{
    public static void init()
    {
        CapabilityTimeLink.register();
    }
}
