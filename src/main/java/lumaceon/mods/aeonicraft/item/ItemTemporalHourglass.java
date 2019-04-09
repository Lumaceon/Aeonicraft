package lumaceon.mods.aeonicraft.item;

import lumaceon.mods.aeonicraft.capability.timelink.CapabilityTimeLink;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemTemporalHourglass extends ItemAeonicraft
{
    @CapabilityInject(CapabilityTimeLink.ITimeLinkHandler.class)
    private static final Capability<CapabilityTimeLink.ITimeLinkHandler> TIME_LINK = null;

    public ItemTemporalHourglass(int maxStack, int maxDamage, String name) {
        super(maxStack, maxDamage, name);
    }

    @Override
    public net.minecraftforge.common.capabilities.ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
        return new Provider();
    }

    private static class Provider implements ICapabilitySerializable<NBTTagCompound>
    {
        CapabilityTimeLink.ITimeLinkHandler timeLinkHandler;

        private Provider()
        {
            timeLinkHandler = new CapabilityTimeLink.TimeLinkHandler();
            //Init stuff if necessary...
        }

        @Override
        public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
            return capability == TIME_LINK;
        }

        @Override
        public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing)
        {
            if(capability == TIME_LINK)
            {
                return TIME_LINK.cast(timeLinkHandler);
            }
            return null;
        }

        @Override
        public NBTTagCompound serializeNBT()
        {
            return timeLinkHandler.saveToNBT();
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt)
        {
            timeLinkHandler.loadFromNBT(nbt);
        }
    }
}
