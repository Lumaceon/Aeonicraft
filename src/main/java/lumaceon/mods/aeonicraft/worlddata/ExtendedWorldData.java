package lumaceon.mods.aeonicraft.worlddata;

import lumaceon.mods.aeonicraft.Aeonicraft;
import lumaceon.mods.aeonicraft.temporalcompression.TemporalCompressor;
import lumaceon.mods.aeonicraft.util.BlockLoc;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;

import java.util.HashMap;
import java.util.Iterator;

@SuppressWarnings("NullableProblems")
public class ExtendedWorldData extends WorldSavedData
{
    private static final String ID = Aeonicraft.MOD_ID + "_world_data";
    public ExtendedWorldData() { super(ID); }
    public ExtendedWorldData(String id) { super(id); }

    public static ExtendedWorldData getInstance(World world)
    {
        // world.getMapStorage for save-level storage - may also need to be null-checked
        MapStorage ms = world.getPerWorldStorage();
        ExtendedWorldData dataHandler = (ExtendedWorldData) ms.getOrLoadData(ExtendedWorldData.class, ID);
        if(dataHandler == null)
        {
            dataHandler = new ExtendedWorldData();
            ms.setData(ID, dataHandler);
        }
        return dataHandler;
    }

    public HashMap<BlockLoc, TemporalCompressor> temporalCompressorProcesses = new HashMap<>();

    /**
     * Registers a new TemporalCompressor. If the internal BlockLoc is already registered, it will be ignored.
     * @param tcpToAdd The TemporalCompressor currently active after the registry attempt (same as passed in if successful).
     */
    public TemporalCompressor registerTemporalCompressor(TemporalCompressor tcpToAdd)
    {
        TemporalCompressor tcp = temporalCompressorProcesses.get(tcpToAdd.getLocation());
        if(tcp != null)
        {
            return tcp;
        }
        else
        {
            temporalCompressorProcesses.put(tcpToAdd.getLocation(), tcpToAdd);
            markDirty();
            return tcpToAdd;
        }
    }

    public void removeTemporalCompressorAt(BlockLoc location) {
        temporalCompressorProcesses.remove(location);
        markDirty();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        if(nbt.hasKey("temporal_compressors"))
        {
            NBTTagList list = nbt.getTagList("temporal_compressors", Constants.NBT.TAG_COMPOUND);
            for(int i = 0; i < list.tagCount(); i++)
            {
                NBTTagCompound tag = list.getCompoundTagAt(i);
                TemporalCompressor tc = new TemporalCompressor(tag);
                registerTemporalCompressor(tc);
                Aeonicraft.logger.info("Loaded Temporal Compressor at (" + tc.getLocation().toString() + ")");
                Aeonicraft.logger.info("Cap: " + tc.getTimeCapacityInMilliseconds() + ", Prod: " + tc.getTimeProductionInMillisecondsPerTick() + ", Time: " + tc.getTime());
            }
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        NBTTagList list = new NBTTagList();
        Iterator<TemporalCompressor> i = temporalCompressorProcesses.values().iterator();
        while(i.hasNext())
        {
            TemporalCompressor tc = i.next();
            NBTTagCompound tcTag = new NBTTagCompound();
            tc.writeToNBT(tcTag);
            list.appendTag(tcTag);
        }
        nbt.setTag("temporal_compressors", list);
        return nbt;
    }
}
