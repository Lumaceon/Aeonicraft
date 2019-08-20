package lumaceon.mods.aeonicraft.api;

import lumaceon.mods.aeonicraft.api.hourglass.HourglassUnlockable;
import lumaceon.mods.aeonicraft.api.hourglass.HourglassUnlockableCategory;

/**
 * Provides a reference to the vanilla Aeonicraft categories as well as methods to modify how much it takes to actually
 * unlock stuff.
 */
public class HourglassUnlocks
{
    private static long totalTimeToUnlockALl = 1000000000000L;
    private static int totalAdvancementToUnlockALl = 1000;

    public static HourglassUnlockableCategory categoryProgression = null;
    public static HourglassUnlockableCategory categoryHourglassFunction = null;
    public static HourglassUnlockableCategory categoryTemporalMachination = null;

    public static HourglassUnlockable hourglassFunctionExcavation = null;
    public static HourglassUnlockable hourglassFunctionLivestock = null;
    public static HourglassUnlockable hourglassFunctionAquaticLure = null;
    public static HourglassUnlockable hourglassFunctionTraveller = null;


    public static void increaseTotalTimeRequiredToAllUnlocks(long increase) {
        totalTimeToUnlockALl += increase;
    }

    public static long getTotalTimeToAllUnlocks() {
        return totalTimeToUnlockALl;
    }

    public static int getTotalAdvancementToAllUnlocks() {
        return totalAdvancementToUnlockALl;
    }
}
