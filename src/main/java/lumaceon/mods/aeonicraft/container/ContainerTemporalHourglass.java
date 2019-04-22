package lumaceon.mods.aeonicraft.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

@SuppressWarnings("NullableProblems")
public class ContainerTemporalHourglass extends Container
{

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }
}
