package lumaceon.mods.aeonicraft.capability;

import lumaceon.mods.aeonicraft.api.util.BlockLoc;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

import javax.annotation.Nullable;

public class CapabilityTemporalNetworkLinker
{
    @CapabilityInject(ITemporalNetworkLinker.class)
    public static final Capability<ITemporalNetworkLinker> NETWORK_LINKER = null;

    public static void register()
    {
        CapabilityManager.INSTANCE.register(ITemporalNetworkLinker.class, new Capability.IStorage<ITemporalNetworkLinker>()
                {
                    @Override
                    public NBTBase writeNBT(Capability<ITemporalNetworkLinker> capability, ITemporalNetworkLinker instance, EnumFacing side)
                    {
                        BlockLoc loc = instance.getRelayLocation();
                        if(loc != null)
                            return loc.serializeToNBT();

                        return new NBTTagCompound();
                    }

                    @Override
                    public void readNBT(Capability<ITemporalNetworkLinker> capability, ITemporalNetworkLinker instance, EnumFacing side, NBTBase base) {
                        if(!base.hasNoTags())
                            instance.setRelayLocation(new BlockLoc((NBTTagCompound)  base));
                    }
                },
                TemporalNetworkLinker::new
        );
    }

    public interface ITemporalNetworkLinker
    {
        void setRelayLocation(@Nullable BlockLoc loc);

        @Nullable
        BlockLoc getRelayLocation();
    }

    public static class TemporalNetworkLinker implements ITemporalNetworkLinker
    {
        BlockLoc loc = null;

        @Override
        public void setRelayLocation(@Nullable BlockLoc loc) {
            this.loc = loc;
        }

        @Nullable
        @Override
        public BlockLoc getRelayLocation() {
            return this.loc;
        }
    }
}
