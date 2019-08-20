package lumaceon.mods.aeonicraft.api.hourglass;

import lumaceon.mods.aeonicraft.api.util.Icon;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class HourglassUnlockableCategory extends IForgeRegistryEntry.Impl<HourglassUnlockableCategory>
{
    public ResourceLocation background;
    protected Icon icon;
    protected String unlocalizedDescription;
    protected int textureX;
    protected int textureY;

    public HourglassUnlockableCategory(ResourceLocation registryName, Icon icon, String unlocalizedDescription, ResourceLocation background,
                                       int textureX, int textureY)
    {
        this.setRegistryName(registryName);
        this.icon = icon;
        this.unlocalizedDescription = unlocalizedDescription;
        this.background = background;
        this.textureX = textureX;
        this.textureY = textureY;
    }

    public Icon getIcon() {
        return this.icon;
    }

    public String getShortDescription() {
        return I18n.format(unlocalizedDescription);
    }

    /**
     * Background texture is stretched vertically, but should loop horizontally as it will expand to match the longest
     * category shown.
     */
    public ResourceLocation backgroundTexture() {
        return background;
    }

    // Simple getters for texture height and width...
    public int textureWidth() {
        return textureX;
    }
    public int textureHeight() {
        return textureY;
    }
}
