package lumaceon.mods.aeonicraft.compat.betteradvancements;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BetterAdvancementsCompatActive implements IBetterAdvancementsCompat
{
    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getAdvancementsGuiScreen() {
      //  return new GuiScreenBetterAdvancements(Minecraft.getMinecraft().player.connection.getAdvancementManager());
        return null;
    }
}
