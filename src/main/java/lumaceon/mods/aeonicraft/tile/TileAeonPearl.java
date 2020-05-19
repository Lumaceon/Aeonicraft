package lumaceon.mods.aeonicraft.tile;

import lumaceon.mods.aeonicraft.block.BlockAeonPearl;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;

public class TileAeonPearl extends TileAeonicraft implements ITickable
{
    private static final String NBT_KEY_TIME_LAST_SAVED = "last_time_saved";
    private static final String NBT_KEY_PROGRESS = "ap_progress_timer";

    public int progressTimer = 0;
    public long timeOfLastSave = -1;

    public TileAeonPearl() {
        super();
    }

    @Override
    public void update() {
        if(world.getTotalWorldTime() % 33 == 0 && !world.isRemote)
        {
            markDirty();  // If anyone reading this has any better solutions, lemme know please...
            if(timeOfLastSave != -1)
            {
                progressTimer += getWorld().getTotalWorldTime() - timeOfLastSave;
                IBlockState state = getWorld().getBlockState(getPos());
                if(state.getBlock() instanceof BlockAeonPearl)
                {
                    ((BlockAeonPearl) state.getBlock()).applyUnloadedTickTime(progressTimer, getWorld(), getPos(), state);
                }
                timeOfLastSave = -1;
            }
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        compound = super.writeToNBT(compound);
        compound.setLong(NBT_KEY_TIME_LAST_SAVED, getWorld().getTotalWorldTime());
        compound.setInteger(NBT_KEY_PROGRESS, progressTimer);
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);

        if(compound.hasKey(NBT_KEY_PROGRESS))
            progressTimer = compound.getInteger(NBT_KEY_PROGRESS);
        if(compound.hasKey(NBT_KEY_TIME_LAST_SAVED))
            timeOfLastSave = compound.getLong(NBT_KEY_TIME_LAST_SAVED);
    }
}
