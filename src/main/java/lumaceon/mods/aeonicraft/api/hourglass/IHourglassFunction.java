package lumaceon.mods.aeonicraft.api.hourglass;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Implement on Item class overrides to allow the temporal hourglass (and possibly other systems) to accept and use it.
 * Each unique implementation of this should be registered via AeonicraftAPIRegistry.
 */
public interface IHourglassFunction
{
    /**
     * Called when a block is right-clicked with the containing hourglass.
     */
    default EnumActionResult onHourglassBlockRightClick(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return EnumActionResult.PASS;
    }

    /**
     * Called on right-click with the containing hourglass. May also call onHourglassBlockRightClick.
     */
    default ActionResult<ItemStack> onHourglassRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        return new ActionResult<>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
    }

    String getRegistryIDString();

    /**
     * If this is implemented on an Item class, this creates an ItemStack to contain or represent this.
     * @return An ItemStack containing this hourglass function.
     */
    @Nullable ItemStack createItemstackContainer();

    /**
     * Given a series of modules in a current hourglass, can this module be added.
     * @param modules An array of current modules; not including this (but possibly including others of its type)
     * @return True if the hourglass should accept this, false otherwise.
     */
    boolean canCoexistWith(IHourglassFunction[] modules);

    //Removed update ticks: feature out of scope for hourglass functions.
    //boolean receivesUpdateTicks();
    //void onUpdate(int ticksSinceLastUpdate);

    /**
     * @return Location for the texture to be rendered over this function.
     */
    ResourceLocation getTextureLocation();
}
