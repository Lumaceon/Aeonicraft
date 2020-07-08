package lumaceon.mods.aeonicraft.api.temporal.temporalnetwork;

import lumaceon.mods.aeonicraft.api.Internal;
import lumaceon.mods.aeonicraft.api.TemporalChunks;
import lumaceon.mods.aeonicraft.api.temporal.TC;
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

    protected TC temporalCompression = TC.NONE;


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
    public TC getTC() {
        return this.temporalCompression;
    }

    /**
     * Set the current amount of TC in this network.
     * @param temporalCompression New amount of TC.
     */
    public void setTC(TC temporalCompression) {
        this.temporalCompression = new TC(Math.min(temporalCompression.getVal(), getTCCapacity().getVal()));
    }

    /**
     * Add TC to this network.
     * @return The amount of TC which was added successfully.
     */
    public TC receiveTC(TC inputAmount)
    {
        TC amountToActuallyAdd = inputAmount;

        if(this.temporalCompression.getVal() + amountToActuallyAdd.getVal() > generationStats.getTCCapacity().getVal())
            amountToActuallyAdd = generationStats.getTCCapacity().subtract(this.temporalCompression);

        if(amountToActuallyAdd.getVal() + this.temporalCompression.getVal() < 0)
            amountToActuallyAdd = TC.NONE.subtract(this.temporalCompression);

        this.temporalCompression = this.temporalCompression.add(amountToActuallyAdd);
        return amountToActuallyAdd;
    }

    /**
     * @return The TC capacity of this network.
     */
    public TC getTCCapacity() {
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
            long tcGenIfUnloaded = chunkEntry.getValue().tcGenPerSecond.getVal();
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
        this.receiveTC(new TC(amountToGain));
    }

    /**
     * Called when internal capacity changes to potentially lose time if the new capacity has been reduced below the
     * current amount.
     */
    public void checkCapacityChangeLoss(TC newCapacity){
        if(newCapacity.getVal() < temporalCompression.getVal())
            setTC(newCapacity);
    }


    // ******* NBT STUFF ******* \\

    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound networkCompound = new NBTTagCompound();

        networkCompound.setLong("temporal_compression", temporalCompression.getVal());
        networkCompound.setTag("locations", generationStats.getNBT());

        return networkCompound;
    }

    public void deserializeNBT(NBTTagCompound nbt)
    {
        generationStats.fromNBT(nbt.getTagList("locations", Constants.NBT.TAG_COMPOUND));
        setTC(new TC(nbt.getLong("temporal_compression")));
    }
}
