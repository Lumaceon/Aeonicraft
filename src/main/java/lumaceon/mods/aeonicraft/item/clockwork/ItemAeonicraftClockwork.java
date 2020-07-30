package lumaceon.mods.aeonicraft.item.clockwork;

import lumaceon.mods.aeonicraft.Aeonicraft;
import lumaceon.mods.aeonicraft.api.clockwork.IClockwork;
import lumaceon.mods.aeonicraft.api.clockwork.baseStats.tooltip.IClockworkTooltips;
import lumaceon.mods.aeonicraft.api.clockwork.ItemClockwork;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemAeonicraftClockwork extends ItemClockwork implements IClockworkTooltips
{
    public ItemAeonicraftClockwork(int matrixSize, int maxStack, int maxDamage, String name) {
        super(matrixSize, maxStack, maxDamage, Aeonicraft.MOD_ID, Aeonicraft.instance.CREATIVE_TAB, name);
    }

  /*  @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack itemStack = playerIn.getHeldItem(handIn);
        IClockwork work = itemStack.getCapability(CLOCKWORK_CAPABILITY,null);
        Aeonicraft.logger.info(work.getSummedProgress());
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }*/

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        IClockwork work = stack.getCapability(CLOCKWORK_CAPABILITY,null);
        tooltip.addAll(getTooltip(work.getClockworkStatCollection()));
    }
}
