package lumaceon.mods.aeonicraft.api.clockwork;

import lumaceon.mods.aeonicraft.api.IAssemblable;
import lumaceon.mods.aeonicraft.api.Internal;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.Objects;

public class ItemClockwork extends Item implements IAssemblable
{
    @CapabilityInject(IClockwork.class)
    public static final Capability<IClockwork> CLOCKWORK_CAPABILITY = null;

    protected int matrixSize;

    public ItemClockwork(int matrixSize, int maxStack, int maxDamage, String modID, CreativeTabs tab, String name)
    {
        super();
        this.setMaxStackSize(maxStack);
        this.setMaxDamage(maxDamage);
        this.setNoRepair();
        this.setCreativeTab(tab);
        this.setRegistryName(modID, name);
        this.setUnlocalizedName(Objects.requireNonNull(this.getRegistryName()).toString());
        assert matrixSize > 2 && matrixSize % 2 == 1; // Size must be odd and at least 3.
        this.matrixSize = matrixSize;
    }

    @Override
    public net.minecraftforge.common.capabilities.ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
        return new ClockworkCapabilityProvider(stack, matrixSize);
    }

    @Override
    public ResourceLocation getGUIBackground(ItemStack stack) {
        return Internal.defaultAssemblyTableTexture;
    }

    @Override
    public Slot[] getContainerSlots(IInventory inventory, int centerSlotX, int centerSlotY)
    {
        Slot[] ret = new Slot[(matrixSize * matrixSize) - 1];
        int matrixRadius = matrixSize / 2;

        int index;
        for(int x = 0; x < matrixSize; x++)
        {
            for(int y = 0; y < matrixSize; y++)
            {
                if(y == matrixRadius && x == matrixRadius)
                    continue;

                index = x + (y * matrixSize);
                if(y > matrixRadius || (y == matrixRadius && x > matrixRadius))
                    index--;

                ret[index] = new SlotClockworkComponent(inventory, index, x*18 + centerSlotX - (matrixRadius * 18), y*18 + centerSlotY - (matrixRadius * 18));
            }
        }
        return ret;
    }

    protected static class ClockworkCapabilityProvider implements ICapabilitySerializable<NBTTagCompound>
    {
        @CapabilityInject(IEnergyStorage.class)
        static Capability<IEnergyStorage> ENERGY_STORAGE_CAPABILITY = null;
        @CapabilityInject(IItemHandler.class)
        static Capability<IItemHandler> ITEM_HANDLER_CAPABILITY = null;

        EnergyStorageModular energyStorage;
        ItemStackHandlerClockwork inventory;
        IClockwork clockwork;

        public ClockworkCapabilityProvider(ItemStack stack, int matrixSize) {
            clockwork = Internal.createDefaultClockworkImplementation.apply(matrixSize);
            energyStorage = new EnergyStorageModular(1, stack);
            inventory = new ItemStackHandlerClockwork(matrixSize, matrixSize, clockwork, energyStorage);
        }

        @Override
        public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
            return capability != null &&
                    capability == ENERGY_STORAGE_CAPABILITY ||
                    capability == ITEM_HANDLER_CAPABILITY ||
                    capability == CLOCKWORK_CAPABILITY;
        }

        @Override
        public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
        {
            if(capability == null)
                return null;

            if(capability == ENERGY_STORAGE_CAPABILITY)
                return ENERGY_STORAGE_CAPABILITY.cast(energyStorage);
            else if(capability == ITEM_HANDLER_CAPABILITY)
                return ITEM_HANDLER_CAPABILITY.cast(inventory);
            else if(capability == CLOCKWORK_CAPABILITY)
                return CLOCKWORK_CAPABILITY.cast(clockwork);

            return null;
        }

        @Override
        public NBTTagCompound serializeNBT()
        {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setTag("clockwork_stats", clockwork.serializeNBT());
            tag.setTag("inventory", inventory.serializeNBT());
            tag.setInteger("energy", energyStorage.getEnergyStored());
            tag.setInteger("max_capacity", energyStorage.getMaxEnergyStored());
            return tag;
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt)
        {
            energyStorage.setMaxCapacity(nbt.getInteger("max_capacity"));
            energyStorage.receiveEnergy(nbt.getInteger("energy"), false);
            inventory.deserializeNBT((NBTTagCompound) nbt.getTag("inventory"));
            clockwork.deserializeNBT(nbt);
        }
    }
}
