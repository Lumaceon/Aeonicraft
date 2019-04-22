package lumaceon.mods.aeonicraft.client.gui;

import lumaceon.mods.aeonicraft.Aeonicraft;
import lumaceon.mods.aeonicraft.container.ContainerTemporalHourglass;
import lumaceon.mods.aeonicraft.lib.GUIs;
import net.minecraft.entity.player.EntityPlayer;
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
        if(ID == GUIs.TEMPORAL_HOuRGLASS.ordinal())
        {
            return new ContainerTemporalHourglass();
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        if(ID == GUIs.TEMPORAL_HOuRGLASS.ordinal())
        {
            return new GuiTemporalHourglass();
        }
        return null;
    }
}
