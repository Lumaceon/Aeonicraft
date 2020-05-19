package lumaceon.mods.aeonicraft.temporalcompression;

import lumaceon.mods.aeonicraft.api.util.BlockLoc;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

/**
 * Defines the workings of a temporal compressor. Compressors (usually) don't actually do anything via
 * tiles which are meant to be unloaded. Instead, a TemporalCompressor instance is created, stored on a WorldSavedData
 * extension, and updated from a server tick handler.
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

    /**
     * @return True if internal values of have changed, false if not.
     */
    public boolean updateTick(World worldUpdatedFrom)
    {
        boolean ret;
        if(worldUpdatedFrom.provider.getDimension() != location.getDimensionID())
            return false;

        Chunk c = worldUpdatedFrom.getChunkProvider().getLoadedChunk(location.getX() >> 4, location.getZ() >> 4);
        if(c != null && c.isLoaded())
        {
            // Chunk is loaded - destroy all stored time and skip the rest of the update
            ret = timeInMilliseconds != 0;
            timeInMilliseconds = 0;
            return ret;
        }

        ret = timeInMilliseconds == timeCapacityInMilliseconds;
        if(ret) return false; // Already at max capacity - return with no change

        timeInMilliseconds += timeProductionInMillisecondsPerTick;
        if(timeInMilliseconds > timeCapacityInMilliseconds) {
            timeInMilliseconds = timeCapacityInMilliseconds;
        }
        return true;
    }

    public void setTime(long time)
    {
        timeInMilliseconds = time;
    }

    /**
     * @return Amount consumed.
     */
    public long consumeTime(long maxConsume)
    {
        long amt = Math.min(timeInMilliseconds, maxConsume);
        this.setTime(timeInMilliseconds - amt);
        return amt;
    }

    public long getTimeCapacityInMilliseconds()
    {
        return this.timeCapacityInMilliseconds;
    }

    public long getRemainingSpaceInMilliseconds()
    {
        return this.timeCapacityInMilliseconds - this.timeInMilliseconds;
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
