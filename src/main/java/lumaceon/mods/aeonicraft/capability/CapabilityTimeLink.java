package lumaceon.mods.aeonicraft.capability;

import lumaceon.mods.aeonicraft.temporalcompression.TemporalCompressor;
import lumaceon.mods.aeonicraft.util.BlockLoc;
import lumaceon.mods.aeonicraft.worlddata.ExtendedWorldData;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;

public class CapabilityTimeLink
{
    @CapabilityInject(ITimeLinkHandler.class)
    public static final Capability<ITimeLinkHandler> TIME_LINK = null;

    public static void register()
    {
        CapabilityManager.INSTANCE.register(ITimeLinkHandler.class, new Capability.IStorage<ITimeLinkHandler>()
                {
                    @Override
                    public NBTBase writeNBT(Capability<ITimeLinkHandler> capability, ITimeLinkHandler instance, EnumFacing side) {
                        return instance.saveToNBT();
                    }

                    @Override
                    public void readNBT(Capability<ITimeLinkHandler> capability, ITimeLinkHandler instance, EnumFacing side, NBTBase base) {
                        instance.loadFromNBT((NBTTagCompound) base);
                    }
                },
                () -> new TimeLinkHandler()
        );
    }

    public interface ITimeLinkHandler
    {
        /**
         * Gets the time available from all sources.
         * @return Time available (in milliseconds)
         */
        long getTimeServer(World world);

        long getTimeClient();

        BlockLoc[] getCompressorLocations();

        void updateClientInformation(TemporalCompressor[] tcs);

        /**
         * Adds the location to the list of compressor locations only if it doesn't already exist.
         */
        void addCompressorLocationIfUnique(BlockLoc location);

        void loadFromNBT(NBTTagCompound nbt);
        NBTTagCompound saveToNBT();
    }

    public static class TimeLinkHandler implements ITimeLinkHandler
    {
        protected ArrayList<BlockLoc> compressorLocations = new ArrayList<>();
        private TemporalCompressor[] compressorsForClient = new TemporalCompressor[0];

        @Override
        public long getTimeServer(World world)
        {
            long ret = 0;

            ExtendedWorldData worldData = ExtendedWorldData.getInstance(world);
            for(BlockLoc loc : compressorLocations)
            {
                TemporalCompressor tc = worldData.temporalCompressorProcesses.get(loc);
                if(tc != null)
                {
                    ret += tc.getTime();
                }
            }

            return ret;
        }

        @Override
        public long getTimeClient()
        {
            long time = 0;

            for(TemporalCompressor tc : compressorsForClient)
            {
                time += tc.getTime();
            }

            return time;
        }

        @Override
        public BlockLoc[] getCompressorLocations() {
            return compressorLocations.toArray(new BlockLoc[0]);
        }

        @Override
        public void updateClientInformation(TemporalCompressor[] tcs) {
            this.compressorsForClient = tcs;
        }

        @Override
        public void addCompressorLocationIfUnique(BlockLoc location) {
            if(!compressorLocations.contains(location))
            {
                compressorLocations.add(location);
            }
        }

        @Override
        public void loadFromNBT(NBTTagCompound nbt)
        {
            compressorLocations.clear();
            if(nbt.hasKey("locs"))
            {
                NBTTagList list = nbt.getTagList("locs", Constants.NBT.TAG_INT_ARRAY);
                for(int i = 0; i < list.tagCount(); i++)
                {
                    int[] tag = list.getIntArrayAt(i);
                    compressorLocations.add(new BlockLoc(tag[0], tag[1], tag[2], tag[3]));
                }
            }
        }

        @Override
        public NBTTagCompound saveToNBT()
        {
            NBTTagCompound nbt = new NBTTagCompound();
            NBTTagList list = new NBTTagList();

            for(BlockLoc loc : compressorLocations){
                list.appendTag(new NBTTagIntArray(new int[] { loc.getX(), loc.getY(), loc.getZ(), loc.getDimensionID() }));
            }

            nbt.setTag("locs", list);
            return nbt;
        }
    }
}
