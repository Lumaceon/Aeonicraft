package lumaceon.mods.aeonicraft.api.util;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * An icon for situations where either an item or a texture can be rendered to represent it.
 */
public class Icon
{
    private ResourceLocation texture = null;
    private ItemStack stackToRender = null;

    public Icon(ResourceLocation textureLocation) {
        this.texture = textureLocation;
    }

    public Icon(ItemStack stackToRender) {
        this.stackToRender = stackToRender;
    }

    public ResourceLocation getTexture() {
        return this.texture;
    }

    public ItemStack getStackToRender() {
        return this.stackToRender;
    }
}
