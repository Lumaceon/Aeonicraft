package lumaceon.mods.aeonicraft.worlddata;

import lumaceon.mods.aeonicraft.Aeonicraft;
import lumaceon.mods.aeonicraft.api.Internal;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;

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
        Internal.mergeTemporalNetworks =
                (locPair) -> temporalNetworkData.mergeNetworks(locPair);
        Internal.separateNetworksIfNecessary =
                (locPair) -> temporalNetworkData.checkSeparation(locPair);
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

    private long updateTimer = 0;
    public void onServerTick()
    {
        if(updateTimer % 20 == 0)
        {
            temporalNetworkData.update();
        }
        markDirty(); // We perform our own NBT caching for efficiency.
        updateTimer++;
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
        if(nbt.hasKey("ac_temporal_networks"))
            temporalNetworkData.deserializeNBT(nbt.getCompoundTag("ac_temporal_networks"));
    }
}
