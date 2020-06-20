package lumaceon.mods.aeonicraft.api.temporalnetwork;

import lumaceon.mods.aeonicraft.api.Internal;
import lumaceon.mods.aeonicraft.api.TemporalChunks;
import lumaceon.mods.aeonicraft.api.util.BlockLoc;
import lumaceon.mods.aeonicraft.api.util.ChunkLoc;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.Map;

public class TemporalNetwork
{
    public TemporalNetworkGenerationStats generationStats;

    protected long temporalCompression = 0;


    public TemporalNetwork() {
        this.generationStats = new TemporalNetworkGenerationStats(this);
    }

    /**
     * Get the temporal network associated with the current block location.
     * @return The temporal network, or null if none exist at the given location.
     */
    @Nullable
    public static TemporalNetwork getTemporalNetwork(BlockLoc blockLocation) {
        return Internal.getTemporalNetwork.apply(blockLocation);
    }

    /**
     * @return The current TC stored in this network.
     */
    public long getTC() {
        return this.temporalCompression;
    }

    /**
     * Set the current amount of TC in this network.
     * @param temporalCompression New amount of TC.
     */
    public void setTC(long temporalCompression) {
        this.temporalCompression = Math.min(temporalCompression, getTCCapacity());
    }

    /**
     * Add TC to this network.
     * @return The amount of TC which was added successfully.
     */
    public long receiveTC(long inputAmount)
    {
        long amountToActuallyAdd = inputAmount;

        if(this.temporalCompression + amountToActuallyAdd > generationStats.getTCCapacity())
            amountToActuallyAdd = generationStats.getTCCapacity() - this.temporalCompression;

        if(amountToActuallyAdd + this.temporalCompression < 0)
            amountToActuallyAdd = -this.temporalCompression;

        this.temporalCompression += amountToActuallyAdd;
        return amountToActuallyAdd;
    }

    /**
     * @return The TC capacity of this network.
     */
    public long getTCCapacity() {
        return generationStats.getTCCapacity();
    }

    /**
     * Called on the server every 20 update ticks to generate (or lose) TC from unloaded chunks.
     */
    public void onUpdate()
    {
        // Generation TC from internal generators.
        long amountToGain = 0;
        for(Map.Entry<ChunkLoc, TemporalNetworkGenerationStats.TemporalNetworkChunkStats> chunkEntry : generationStats.tcGenPerChunk.entrySet())
        {
            long tcGenIfUnloaded = chunkEntry.getValue().tcGenPerSecond;
            ChunkLoc loc = chunkEntry.getKey();
            World world = DimensionManager.getWorld(loc.getDimensionID());
            if(world == null)
            {
                amountToGain += tcGenIfUnloaded;
            }
            else
            {
                if(TemporalChunks.isChunkFrozen(world, loc.toChunkPos()))
                {
                    amountToGain += tcGenIfUnloaded;
                }
            }
        }
        this.receiveTC(amountToGain);
    }

    /**
     * Called when internal capacity changes to potentially lose time if the new capacity has been reduced below the
     * current amount.
     */
    public void checkCapacityChangeLoss(long newCapacity){
        if(newCapacity < temporalCompression)
            setTC(newCapacity);
    }


    // ******* NBT STUFF ******* \\

    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound networkCompound = new NBTTagCompound();

        networkCompound.setLong("temporal_compression", temporalCompression);
        networkCompound.setTag("locations", generationStats.getNBT());

        return networkCompound;
    }

    public void deserializeNBT(NBTTagCompound nbt)
    {
        generationStats.fromNBT(nbt.getTagList("locations", Constants.NBT.TAG_COMPOUND));
        setTC(nbt.getLong("temporal_compression"));
    }
}
