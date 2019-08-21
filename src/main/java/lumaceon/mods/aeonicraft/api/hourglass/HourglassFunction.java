package lumaceon.mods.aeonicraft.api.hourglass;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

/**
 * Class for hourglass functions, which can be cycled through to give a variety of passive/active effects once unlocked.
 *
 * If the same ResourceLocation (or two, equal instances) are used as a registry name for both a registered
 * HourglassFunction and HourglassUnlockable, Aeonicraft automatically associates them during the post-init phase by
 * setting the requiredUnlockable field.
 */
public class HourglassFunction extends IForgeRegistryEntry.Impl<HourglassFunction>
{
    public ResourceLocation textureLocation;
    public HourglassUnlockable requiredUnlockable;

    public HourglassFunction(ResourceLocation registryName)
    {
        this.setRegistryName(registryName);
        ResourceLocation temp = this.getRegistryName();
        this.textureLocation = new ResourceLocation(temp.getResourceDomain(), "hourglassfunction/" + temp.getResourcePath());
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

    /**
     * Called each tick from Item::onUpdate if this is the active hourglass function.
     *
     * @param stack ItemStack to represent the hourglass.
     * @param world The relevant world.
     * @param entity The relevant entity (usually the player).
     * @param itemSlot The slot of the entity's inventory this is in.
     * @param isSelected True if and only if the hourglass is currently being held in a selected slot.
     */
    public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {}

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
