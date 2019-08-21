package lumaceon.mods.aeonicraft.util;

import lumaceon.mods.aeonicraft.capability.CapabilityTimeStorage;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TimeHelper
{
    public static long getTime(Entity entity)
    {
        CapabilityTimeStorage.ITimeStorage cap = entity.getCapability(CapabilityTimeStorage.TIME_STORAGE_CAPABILITY, null);
        if(cap != null)
        {
            return cap.getTimeInTicks();
        }
        return 0;
    }

    /**
     * @return Time consumed.
     */
    public static long consumeTime(Entity entity, long amountToConsume)
    {
        CapabilityTimeStorage.ITimeStorage cap = entity.getCapability(CapabilityTimeStorage.TIME_STORAGE_CAPABILITY, null);
        if(cap != null)
        {
            return cap.extractTime(amountToConsume);
        }
        return 0;
    }

    /**
     * Gets the time to break the block in milliseconds.
     * @param state The state of the block to break.
     * @param player The player trying to break the block.
     * @param tool The tools used.
     * @return The time, in milliseconds, it takes to break the block.
     */
    public static long getTimeToBreakBlock(World world, BlockPos pos, IBlockState state, EntityLivingBase player, ItemStack tool)
    {
        if(!(player instanceof EntityPlayer) || !state.getBlock().canHarvestBlock(world, pos, (EntityPlayer) player))
            return Long.MAX_VALUE;

        float strength = tool.getItem().getDestroySpeed(tool, state);
        float timeCostInTicks = state.getBlockHardness(world, pos) * 30F / strength;
        if(player.isInWater())
            timeCostInTicks *= 5.0F;
        if(player.isAirBorne)
            timeCostInTicks *= 5.0F;
        return (long) (timeCostInTicks * 50.0F);
    }
}
