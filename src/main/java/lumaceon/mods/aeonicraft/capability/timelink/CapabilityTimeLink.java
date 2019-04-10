package lumaceon.mods.aeonicraft.capability.timelink;

import lumaceon.mods.aeonicraft.util.BlockLoc;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

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
                        instance.loadFromNBT(base);
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
        long getTime();

        BlockLoc[] getCompressorLocations();

        /**
         * Adds the location to the list of compressor locations only if it doesn't already exist.
         */
        void addCompressorLocationIfUnique(BlockLoc location);

        void loadFromNBT(NBTBase nbt);
        NBTTagCompound saveToNBT();
    }

    public static class TimeLinkHandler implements ITimeLinkHandler
    {
        protected ArrayList<BlockLoc> compressorLocations = new ArrayList<>();

        @Override
        public long getTime() {
            return 0;
        }

        @Override
        public BlockLoc[] getCompressorLocations() {
            return compressorLocations.toArray(new BlockLoc[0]);
        }

        @Override
        public void addCompressorLocationIfUnique(BlockLoc location) {
            if(!compressorLocations.contains(location))
            {
                compressorLocations.add(location);
            }
        }

        @Override
        public void loadFromNBT(NBTBase nbt) {

        }

        @Override
        public NBTTagCompound saveToNBT() {
            return new NBTTagCompound();
        }
    }
}
