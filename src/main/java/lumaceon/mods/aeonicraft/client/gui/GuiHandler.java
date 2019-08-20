package lumaceon.mods.aeonicraft.client.gui;

import lumaceon.mods.aeonicraft.Aeonicraft;
import lumaceon.mods.aeonicraft.compat.ModCompatProxyRegistry;
import lumaceon.mods.aeonicraft.container.ContainerTemporalHourglass;
import lumaceon.mods.aeonicraft.registry.ModItems;
import lumaceon.mods.aeonicraft.lib.GUIs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class GuiHandler implements IGuiHandler
{
    public GuiHandler() {
        NetworkRegistry.INSTANCE.registerGuiHandler(Aeonicraft.instance, this);
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        if(ID == GUIs.TEMPORAL_HOURGLASS.ordinal())
        {
            ItemStack hg = player.inventory.getCurrentItem();
            if(hg.getItem().equals(ModItems.temporal_hourglass))
                return new ContainerTemporalHourglass(hg);
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        if(ID == GUIs.TEMPORAL_HOURGLASS.ordinal())
        {
            ItemStack hg = player.inventory.getCurrentItem();
            if(hg.getItem().equals(ModItems.temporal_hourglass))
                return new GuiTemporalHourglass(hg, ModCompatProxyRegistry.betterAdvancementsCompat.getAdvancementsGuiScreen());
        }
        return null;
    }
}
