package lumaceon.mods.aeonicraft.api.clockwork;

import lumaceon.mods.aeonicraft.api.clockwork.baseStats.IClockworkBaseStats;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;

/**
 * Interface for clockwork stats.
 */
public interface IClockwork extends IClockworkBaseStats
{
    int getDiameter();
    void buildFromStacks(IClockworkComponent[][] components);
    List<String> getAdditionalTooltipsForMatrixPosition(int x, int y);

    NBTTagCompound serializeNBT();
    void deserializeNBT(NBTTagCompound compound);
}
