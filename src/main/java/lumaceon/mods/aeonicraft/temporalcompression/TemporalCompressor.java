package lumaceon.mods.aeonicraft.temporalcompression;

import lumaceon.mods.aeonicraft.util.BlockLoc;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

/**
 * Defines the workings of a temporal compressor. Compressors (usually) don't actually do anything via
 * tiles which are meant to be unloaded. Instead, a TemporalCompressor instance is created, stored on a WorldSavedData
 * extension, and updated from a server tick event.
 */
public class TemporalCompressor
{
    private static String TCP_POSITION_TAG = "TCP_position";
    private static String TCP_CAP_TAG = "TCP_cap";
    private static String TCP_PRODUCT_TAG = "TCP_product";
    private static String TCP_TIME_TAG = "TCP_time";
    private BlockLoc location;

    private long timeCapacityInMilliseconds;
    private long timeProductionInMillisecondsPerTick;
    private long timeInMilliseconds = 0;

    public TemporalCompressor(NBTTagCompound nbt)
    {
        int[] pos = nbt.getIntArray(TCP_POSITION_TAG);
        this.location = new BlockLoc(pos[0], pos[1], pos[2], pos[3]);
        this.timeCapacityInMilliseconds = nbt.getLong(TCP_CAP_TAG);
        this.timeProductionInMillisecondsPerTick = nbt.getLong(TCP_PRODUCT_TAG);
        this.timeInMilliseconds = nbt.getLong(TCP_TIME_TAG);
    }

    public TemporalCompressor(long timeCapacityInMilliseconds, long timeProductionInMillisecondsPerTick, BlockLoc location)
    {
        this.timeCapacityInMilliseconds = timeCapacityInMilliseconds;
        this.timeProductionInMillisecondsPerTick = timeProductionInMillisecondsPerTick;
        this.location = location;
    }

    public void updateTick(World worldUpdatedFrom)
    {
        if(worldUpdatedFrom.provider.getDimension() != location.getDimensionID())
            return;

        if(worldUpdatedFrom.getChunkProvider().getLoadedChunk(location.getX() >> 4, location.getZ() >> 4) != null)
        {
            // Chunk is loaded - destroy all stored time and skip the rest of the update
            timeInMilliseconds = 0;
            return;
        }

        timeInMilliseconds += timeProductionInMillisecondsPerTick;
        if(timeInMilliseconds > timeCapacityInMilliseconds)
        {
            timeInMilliseconds = timeCapacityInMilliseconds;
        }
    }

    public long getTimeCapacityInMilliseconds()
    {
        return this.timeCapacityInMilliseconds;
    }

    public long getTimeProductionInMillisecondsPerTick()
    {
        return this.timeProductionInMillisecondsPerTick;
    }

    public long getTime()
    {
        return timeInMilliseconds;
    }

    public BlockLoc getLocation() {
        return location;
    }

    public void writeToNBT(NBTTagCompound nbt) {
        nbt.setIntArray(TCP_POSITION_TAG, new int[] { location.getX(), location.getY(), location.getZ(), location.getDimensionID() });
        nbt.setLong(TCP_CAP_TAG, this.timeCapacityInMilliseconds);
        nbt.setLong(TCP_PRODUCT_TAG, this.timeProductionInMillisecondsPerTick);
        nbt.setLong(TCP_TIME_TAG, this.timeInMilliseconds);
    }
}
