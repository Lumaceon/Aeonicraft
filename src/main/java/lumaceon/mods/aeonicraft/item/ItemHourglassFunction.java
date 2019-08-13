package lumaceon.mods.aeonicraft.item;

import lumaceon.mods.aeonicraft.Aeonicraft;
import lumaceon.mods.aeonicraft.api.hourglass.IHourglassFunction;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ItemHourglassFunction extends ItemAeonicraft implements IHourglassFunction
{
    protected ResourceLocation texture;

    public ItemHourglassFunction(int maxStack, int maxDamage, String name, String texture) {
        super(maxStack, maxDamage, name);
        this.texture = new ResourceLocation(Aeonicraft.MOD_ID, texture);
    }

    @Override
    public EnumActionResult onHourglassBlockRightClick(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return EnumActionResult.PASS;
    }

    @Override
    public ActionResult<ItemStack> onHourglassRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        return new ActionResult<>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
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
