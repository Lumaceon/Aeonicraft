package lumaceon.mods.aeonicraft.api;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

/**
 * Implement on Item class overrides to allow the temporal hourglass (and possibly other systems) to accept and use it.
 * Each unique implementation of this should be registered via AeonicraftAPIRegistry.
 */
public interface IHourglassFunction
{
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
