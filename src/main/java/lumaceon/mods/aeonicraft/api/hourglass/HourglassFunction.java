package lumaceon.mods.aeonicraft.api.hourglass;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

/**
 * Implement on Item class overrides to allow the temporal hourglass (and possibly other systems) to accept and use it.
 * Each unique implementation of this should be registered via AeonicraftAPIRegistry.
 */
public class HourglassFunction extends IForgeRegistryEntry.Impl<HourglassFunction>
{
    public ResourceLocation textureLocation;
    public HourglassUnlockable requiredUnlockable;

    public HourglassFunction(ResourceLocation registryName, HourglassUnlockable requiredUnlockable)
    {
        this.setRegistryName(registryName);
        ResourceLocation temp = this.getRegistryName();
        this.textureLocation = new ResourceLocation(temp.getResourceDomain(), "hourglassfunction/" + temp.getResourcePath());
        this.requiredUnlockable = requiredUnlockable;
    }

    /**
     * Called when a block is right-clicked with the containing hourglass.
     */
    public EnumActionResult onHourglassBlockRightClick(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return EnumActionResult.PASS;
    }

    /**
     * Called on right-click with the containing hourglass. May also call onHourglassBlockRightClick.
     */
    public ActionResult<ItemStack> onHourglassRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        return new ActionResult<>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
    }

    //Removed update ticks: feature out of scope for hourglass functions.
    //void onUpdate(int ticksSinceLastUpdate);

    /**
     * Requires a path to a loaded texture. Resource path should be relative to the textures folder of your mod.
     *
     * For this file path...
     * resources/assets/aeonicraft/textures/hourglassfunction/my_texture.png
     *
     * Return this...
     * new ResourceLocation("aeonicraft", "hourglassfunction/my_texture");
     */
    public ResourceLocation getTextureLocation() {
        return this.textureLocation;
    }

    /**
     *
     * @return The unlockable corresponding to this hourglass function.
     */
    public HourglassUnlockable requiredUnlockable() {
        return requiredUnlockable;
    }
}
