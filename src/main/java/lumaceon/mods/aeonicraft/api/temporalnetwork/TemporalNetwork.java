package lumaceon.mods.aeonicraft.api.temporalnetwork;

import lumaceon.mods.aeonicraft.api.Internal;
import lumaceon.mods.aeonicraft.api.util.BlockLoc;
import lumaceon.mods.aeonicraft.api.util.ChunkLoc;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.Map;

public class TemporalNetwork
{
    public NetworkBlockMap generationStats;

    protected long temporalCompression = 0;


    public TemporalNetwork() {
        this.generationStats = new NetworkBlockMap(this);
    }

    @Nullable
    public static TemporalNetwork getTemporalNetwork(BlockLoc blockLocation) {
        return Internal.getTemporalNetwork.apply(blockLocation);
    }

    public long getTC() {
        return this.temporalCompression;
    }

    public void setTC(long temporalCompression) {
        this.temporalCompression = Math.min(temporalCompression, getTCCapacity());
    }

    /**
     * @return The amount which was added successfully.
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

    public long getTCCapacity() {
        return generationStats.getTCCapacity();
    }

    /**
     * Called on the server each tick to generate (or lose) TC from unloaded chunks.
     */
    public void onUpdate()
    {
        // Generation TC from internal generators.
        long amountToGain = 0;
        for(Map.Entry<ChunkLoc, NetworkBlockMap.TemporalNetworkChunkStats> chunkEntry : generationStats.tcGenPerChunk.entrySet())
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
                Chunk chunk = world.getChunkFromChunkCoords(loc.getX(), loc.getY());
                if(!chunk.isLoaded())
                {
                    amountToGain += tcGenIfUnloaded;
                }
            }
        }
        this.receiveTC(amountToGain);
    }

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
