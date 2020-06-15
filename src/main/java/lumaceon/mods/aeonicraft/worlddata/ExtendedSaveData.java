package lumaceon.mods.aeonicraft.worlddata;

import lumaceon.mods.aeonicraft.Aeonicraft;
import lumaceon.mods.aeonicraft.api.Internal;
import lumaceon.mods.aeonicraft.temporalcompression.TemporalCompressor;
import lumaceon.mods.aeonicraft.api.util.BlockLoc;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;

import java.util.Iterator;

@SuppressWarnings("NullableProblems")
public class ExtendedSaveData extends WorldSavedData
{
    private static final String ID = Aeonicraft.MOD_ID + "_save_data";

    public ExtendedSaveData() {
        super(ID);
        setup();
    }
    public ExtendedSaveData(String id) {
        super(id);
        setup();
    }

    private void setup() {
        temporalNetworkData = new TemporalNetworkData(this);
        Internal.getTemporalNetwork =
                (loc) -> temporalNetworkData.get(loc);
        Internal.addTemporalNetworkLocation =
                (loc) -> temporalNetworkData.setupLocationForNetworkBlockMap(loc);
        Internal.removeTemporalNetworkLocation =
                (loc) -> temporalNetworkData.removeNetworkFromLocation(loc);
    }

    public static ExtendedSaveData getInstance(World world)
    {
        // world.getPerMapStorage for world-level storage - may also need to be null-checked
        MapStorage ms = world.getMapStorage();
        ExtendedSaveData dataHandler = (ExtendedSaveData) ms.getOrLoadData(ExtendedSaveData.class, ID);
        if(dataHandler == null)
        {
            dataHandler = new ExtendedSaveData();
            ms.setData(ID, dataHandler);
        }
        return dataHandler;
    }


    private TemporalNetworkData temporalNetworkData;

    public void onServerTick()
    {
        temporalNetworkData.update();
        markDirty(); // Mark dirty constantly so we don't miss stuff. We perform our own NBT caching for efficiency.
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        nbt.setTag("ac_temporal_networks", temporalNetworkData.serializeNBT());
        return nbt;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        //TODO WHY ISN'T THIS FREAKIN' THING BEING CALLED!?!?!?!?
        if(nbt.hasKey("ac_temporal_networks"))
            temporalNetworkData.deserializeNBT(nbt.getCompoundTag("ac_temporal_networks"));
    }
}
