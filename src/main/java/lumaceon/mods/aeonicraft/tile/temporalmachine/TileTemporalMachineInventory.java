package lumaceon.mods.aeonicraft.tile.temporalmachine;

import lumaceon.mods.aeonicraft.util.SidedInventorySlotConfiguration;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

@SuppressWarnings("NullableProblems")
public abstract class TileTemporalMachineInventory extends TileTemporalMachine implements ISidedInventory
{
    protected NonNullList<ItemStack> inventory;
    protected SidedInventorySlotConfiguration slotConfiguration;

    protected final String name;
    protected final SidedInvWrapper[] sidedInventoryWrappers = {
            new SidedInvWrapper(this, EnumFacing.values()[0]), // DOWN
            new SidedInvWrapper(this, EnumFacing.values()[1]), // UP
            new SidedInvWrapper(this, EnumFacing.values()[2]), // NORTH
            new SidedInvWrapper(this, EnumFacing.values()[3]), // SOUTH
            new SidedInvWrapper(this, EnumFacing.values()[4]), // EAST
            new SidedInvWrapper(this, EnumFacing.values()[5]), // WEST
    };

    public TileTemporalMachineInventory(String name, int inventorySize)
    {
        this.name = name;
        this.inventory = NonNullList.withSize(inventorySize, ItemStack.EMPTY);
        slotConfiguration = new SidedInventorySlotConfiguration();
        this.slotConfiguration.slots = new Slot[inventorySize];
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        side = globalToLocalFacing(side);
        return slotConfiguration.getSlotsForSideIntArray(side);
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction)
    {
        //Confirm we have an array of slots and the index is in-bounds.
        if(slotConfiguration.slots == null || index >= slotConfiguration.slots.length)
            return false;

        //Confirm this index is accessible for the direction.
        boolean foundSlot = false;
        int[] validSlots = getSlotsForFace(direction);
        for(int slot : validSlots)
        {
            if(slot == index)
            {
                foundSlot = true;
                break;
            }
        }

        return foundSlot && slotConfiguration.slots[index].isItemValid(itemStackIn);
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction)
    {
        int[] validSlots = getSlotsForFace(direction);
        for(int slot : validSlots)
            if(slot == index)
                return true;
        return false;
    }

    @Override
    public int getSizeInventory() {
        return inventory.size();
    }

    @Override
    public boolean isEmpty() {
        for(ItemStack is : inventory)
            if(!is.equals(ItemStack.EMPTY))
                return false;
        return true;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return index < inventory.size() ? inventory.get(index) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack decrStackSize(int index, int count)
    {
        ItemStack is = getStackInSlot(index);
        if(!is.isEmpty())
        {
            if(count >= is.getCount())
            {
                setInventorySlotContents(index, ItemStack.EMPTY);
            }
            else
            {
                is = is.splitStack(count);
                if(is.getCount() == 0)
                {
                    setInventorySlotContents(index, ItemStack.EMPTY);
                }
            }
            markDirty();
        }
        return is;
    }

    @Override
    public ItemStack removeStackFromSlot(int index)
    {
        ItemStack ret = inventory.get(index);
        inventory.set(index, ItemStack.EMPTY);
        markDirty();
        return ret;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack)
    {
        inventory.set(index, stack);

        if(!stack.isEmpty() && stack.getCount() > this.getInventoryStackLimit())
            stack.setCount(this.getInventoryStackLimit());
        this.markDirty();

    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return player.getDistanceSqToCenter(this.getPos()) <= 100.0;
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack)
    {
        //Confirm we have an array of slots and the index is in-bounds.
        if(slotConfiguration.slots == null || index >= slotConfiguration.slots.length)
            return false;

        return slotConfiguration.slots[index].isItemValid(stack);
    }

    @Override
    public int getField(int id)
    {
        switch(id)
        {
            case 0: //Progress - 0 (no progress) to 10,000 (complete), though it can go above this in some cases.
                return (int) ((temporalMachine.getCurrentProgress() / temporalMachine.getProgressCostPerAction()) * 10000);
            case 1: //Energy
                return temporalMachine.getEnergyStorage().getEnergyStored();
            case 2: //Max Energy
                return temporalMachine.getEnergyStorage().getMaxEnergyStored();
        }
        return 0;
    }

    @Override
    public void setField(int id, int value)
    {
        switch(id)
        {
            case 0: //Progress Timer
                temporalMachine.setCurrentProgress(value);
                break;
            case 1: //Energy
                temporalMachine.getEnergyStorage().setEnergy(value);
                break;
            case 2: //Max Energy
                temporalMachine.getEnergyStorage().setMaxCapacity(value);
                break;
        }
    }

    @Override
    public int getFieldCount() {
        return 3;
    }

    @Override
    public void clear() {
        for(int i = 0; i < inventory.size(); i++)
            inventory.set(i, ItemStack.EMPTY);
        markDirty();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        nbt = super.writeToNBT(nbt);

        // Save machine inventory (itemstacks)
        if(inventory != null)
        {
            NBTTagList nbtList = new NBTTagList();
            for(int index = 0; index < inventory.size(); index++)
            {
                if(!inventory.get(index).isEmpty())
                {
                    NBTTagCompound tag = new NBTTagCompound();
                    tag.setByte("slot_index", (byte)index);
                    inventory.get(index).writeToNBT(tag);
                    nbtList.appendTag(tag);
                }
            }
            nbt.setTag("machine_inventory", nbtList);
        }

        // Save slot configuration
        nbt.setTag("slot_config", slotConfiguration.serializeNBT());

        return nbt;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);

        // Load machine inventory (itemstacks)
        if(nbt.hasKey("machine_inventory"))
        {
            NBTTagList tagList = nbt.getTagList("machine_inventory", 10);
            inventory = NonNullList.withSize(getSizeInventory(), ItemStack.EMPTY);
            for(int i = 0; i < tagList.tagCount(); ++i)
            {
                NBTTagCompound tagCompound = tagList.getCompoundTagAt(i);
                byte slotIndex = tagCompound.getByte("slot_index");
                if(slotIndex >= 0 && slotIndex < inventory.size())
                    inventory.set(slotIndex, new ItemStack(tagCompound));
            }
        }

        // Load slot configurations
        if(nbt.hasKey("slot_config"))
            slotConfiguration.deserializeNBT(nbt.getCompoundTag("slot_config"));
    }


    @Override public String getName() { return this.name; }
    @Override public boolean hasCustomName() { return true; }
    @Override public void openInventory(EntityPlayer player) {}
    @Override public void closeInventory(EntityPlayer player) {}
}
