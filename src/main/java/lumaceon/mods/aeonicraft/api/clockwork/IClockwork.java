package lumaceon.mods.aeonicraft.api.clockwork;

import net.minecraft.nbt.NBTTagCompound;

/**
 * Interface for clockwork stats.
 */
public interface IClockwork
{
    void buildFromStacks(IClockworkComponent[][] components);

    NBTTagCompound serializeNBT();
    void deserializeNBT(NBTTagCompound compound);

    float getSummedProgress();
}
