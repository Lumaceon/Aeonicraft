package lumaceon.mods.aeonicraft.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilityToggle
{
    @CapabilityInject(IToggleHandler.class)
    public static final Capability<IToggleHandler> TOGGLE = null;

    public static void register()
    {
        CapabilityManager.INSTANCE.register(IToggleHandler.class, new Capability.IStorage<IToggleHandler>()
            {
                @Override
                public NBTBase writeNBT(Capability<IToggleHandler> capability, IToggleHandler instance, EnumFacing side)
                {
                    NBTTagCompound nbt = new NBTTagCompound();
                    nbt.setBoolean("is_active", instance.isActive());
                    return nbt;
                }

                @Override
                public void readNBT(Capability<IToggleHandler> capability, IToggleHandler instance, EnumFacing side, NBTBase base) {
                    instance.setActive(((NBTTagCompound) base).getBoolean("is_active"));
                }
            },
            () -> new CapabilityToggle.ToggleHandler()
        );
    }

    public interface IToggleHandler
    {
        void setActive(boolean active);
        boolean isActive();
    }

    public static class ToggleHandler implements IToggleHandler
    {
        boolean isActive = false;

        @Override
        public void setActive(boolean active) {
            this.isActive = active;
        }

        @Override
        public boolean isActive() {
            return this.isActive;
        }
    }
}
