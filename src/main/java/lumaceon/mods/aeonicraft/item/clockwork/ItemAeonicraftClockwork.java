package lumaceon.mods.aeonicraft.item.clockwork;

import lumaceon.mods.aeonicraft.Aeonicraft;
import lumaceon.mods.aeonicraft.api.clockwork.IClockwork;
import lumaceon.mods.aeonicraft.api.clockwork.ItemClockwork;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemAeonicraftClockwork extends ItemClockwork
{
    public ItemAeonicraftClockwork(int matrixSize, int maxStack, int maxDamage, String name) {
        super(matrixSize, maxStack, maxDamage, Aeonicraft.MOD_ID, Aeonicraft.instance.CREATIVE_TAB, name);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack itemStack = playerIn.getHeldItem(handIn);
        IClockwork work = itemStack.getCapability(CLOCKWORK_CAPABILITY,null);
        Aeonicraft.logger.info(work.getSummedTestInt());
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

}
