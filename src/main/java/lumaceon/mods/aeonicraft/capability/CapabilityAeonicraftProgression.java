package lumaceon.mods.aeonicraft.capability;

import lumaceon.mods.aeonicraft.api.hourglass.HourglassUnlockable;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class CapabilityAeonicraftProgression
{
    @CapabilityInject(IAeonicraftProgressionHandler.class)
    public static final Capability<IAeonicraftProgressionHandler> AEONICRAFT_PROGRESSION_CAPABILITY = null;

    public static void register()
    {
        CapabilityManager.INSTANCE.register(IAeonicraftProgressionHandler.class, new Capability.IStorage<IAeonicraftProgressionHandler>()
                {
                    @Override
                    public NBTBase writeNBT(Capability<IAeonicraftProgressionHandler> capability, IAeonicraftProgressionHandler instance, EnumFacing side) {
                        return instance.saveToNBT();
                    }

                    @Override
                    public void readNBT(Capability<IAeonicraftProgressionHandler> capability, IAeonicraftProgressionHandler instance, EnumFacing side, NBTBase base) {
                        instance.loadFromNBT((NBTTagCompound) base);
                    }
                },
                () -> new AeonicraftProgressionHandler()
        );
    }

    public interface IAeonicraftProgressionHandler
    {
        void unlock(HourglassUnlockable unlockable);

        boolean isUnlocked(HourglassUnlockable unlockable);

        void loadFromNBT(NBTTagCompound nbt);
        NBTTagCompound saveToNBT();
    }

    public static class AeonicraftProgressionHandler implements IAeonicraftProgressionHandler
    {
        protected String unlocksStateString = "";

        /**
         * Used mainly on the client to sync this up with the server.
         * @param unlockState The full internal state of the unlocks.
         */
        public void setUnlockState(String unlockState)
        {
            this.unlocksStateString = unlockState;
        }

        @Override
        public void unlock(HourglassUnlockable unlockable) {
            if(!isUnlocked(unlockable))
                unlocksStateString += "|" + unlockable.getRegistryName().toString() + ";";
        }

        @Override
        public boolean isUnlocked(HourglassUnlockable unlockable) {
            return unlocksStateString.contains("|" + unlockable.getRegistryName().toString() + ";");
        }

        @Override
        public void loadFromNBT(NBTTagCompound nbt)
        {
            if(nbt.hasKey("unlocksStateString"))
            {
                unlocksStateString = nbt.getString("unlocksStateString");
            }
        }

        @Override
        public NBTTagCompound saveToNBT()
        {
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setString("unlocksStateString", unlocksStateString);
            return nbt;
        }
    }

    // Default provider
    public static class Provider implements ICapabilitySerializable<NBTTagCompound>
    {
        AeonicraftProgressionHandler implementation;

        public Provider() {
            implementation = new AeonicraftProgressionHandler();
        }

        @Override
        public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
            return AEONICRAFT_PROGRESSION_CAPABILITY == capability;
        }

        @Override
        public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
            if(hasCapability(capability, facing))
                return AEONICRAFT_PROGRESSION_CAPABILITY.cast(implementation);
            return null;
        }

        @Override
        public NBTTagCompound serializeNBT() {
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setTag("aeonicraft_progression_cap_data", implementation.saveToNBT());
            return nbt;
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt) {
            if(nbt.hasKey("aeonicraft_progression_cap_data"))
                implementation.loadFromNBT(nbt.getCompoundTag("aeonicraft_progression_cap_data"));
        }
    }
}
