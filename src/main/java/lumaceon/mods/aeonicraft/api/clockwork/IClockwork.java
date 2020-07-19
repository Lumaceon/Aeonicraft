package lumaceon.mods.aeonicraft.api.clockwork;

import lumaceon.mods.aeonicraft.api.clockwork.baseStats.ClockworkEfficiencyStat;
import lumaceon.mods.aeonicraft.api.clockwork.baseStats.ClockworkMaxWindUpStat;
import lumaceon.mods.aeonicraft.api.clockwork.baseStats.IClockworkBaseStats;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Interface for clockwork stats.
 */
public interface IClockwork extends IClockworkBaseStats
{
    void buildFromStacks(IClockworkComponent[][] components);

    NBTTagCompound serializeNBT();
    void deserializeNBT(NBTTagCompound compound);
}
