package lumaceon.mods.aeonicraft.item;

import lumaceon.mods.aeonicraft.Aeonicraft;
import lumaceon.mods.aeonicraft.api.IHourglassFunction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class ItemHourglassFunction extends ItemAeonicraft implements IHourglassFunction
{
    protected ResourceLocation texture;

    public ItemHourglassFunction(int maxStack, int maxDamage, String name, String texture) {
        super(maxStack, maxDamage, name);
        this.texture = new ResourceLocation(Aeonicraft.MOD_ID, texture);
    }

    @Override
    public String getRegistryIDString() {
        return this.getRegistryName() == null ? this.getUnlocalizedName() : this.getRegistryName().toString();
    }

    @Nullable
    @Override
    public ItemStack createItemstackContainer() {
        return new ItemStack(this);
    }

    @Override
    public boolean canCoexistWith(IHourglassFunction[] modules)
    {
        // Disallows this module of one of the same class already exists.
        for(IHourglassFunction f : modules)
            if(f.equals(this))
                return false;
        return true;
    }

    @Override
    public ResourceLocation getTextureLocation() {
        return texture;
    }
}
