package lumaceon.mods.aeonicraft.lib;

import lumaceon.mods.aeonicraft.api.temporal.TC;

public class TimeCosts
{
    public static TC TRAVEL_GHOST = TC.MINUTE;
    public static TC ENDATTRACTOR_SPAWN = TC.MINUTE.multiply(10);
    public static TC AUTO_ADULT_BREED = TC.TICK.multiply(2);
    public static TC INSTANT_FISH_MIN = TC.SECOND.multiply(5);
    public static TC INSTANT_FISH_MAX = TC.SECOND.multiply(30);
}
