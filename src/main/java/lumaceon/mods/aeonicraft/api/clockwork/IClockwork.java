package lumaceon.mods.aeonicraft.api.clockwork;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Capability interface for clockwork stats.
 */
public interface IClockwork
{
    void buildFromStacks(ItemStack[] stacks);

    NBTTagCompound serializeNBT();
    void deserializeNBT(NBTTagCompound compound);
}
