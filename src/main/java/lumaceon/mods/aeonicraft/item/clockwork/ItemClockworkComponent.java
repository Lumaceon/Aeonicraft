package lumaceon.mods.aeonicraft.item.clockwork;

import lumaceon.mods.aeonicraft.api.clockwork.IClockworkComponentItem;
import lumaceon.mods.aeonicraft.item.ItemAeonicraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemClockworkComponent extends ItemAeonicraft implements IClockworkComponentItem
{
    private int thingy;
    public ItemClockworkComponent(int maxStack, int maxDamage, String name, int thingy) {
        super(maxStack, maxDamage, name);
        this.thingy = thingy;
    }

    @Override
    public int getTestInt() {
        return thingy;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add("Lea hi!");
        tooltip.add("Thingy: " + thingy);
    }
}
