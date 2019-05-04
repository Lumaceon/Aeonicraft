package lumaceon.mods.aeonicraft.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

@SuppressWarnings("NullableProblems")
public class ContainerTemporalHourglass extends Container
{
    ItemStack hourglassStack;

    public ContainerTemporalHourglass(ItemStack hourglassStack) {
        this.hourglassStack = hourglassStack;
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }
}
