package lumaceon.mods.aeonicraft.compat.betteradvancements;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IBetterAdvancementsCompat
{
    @SideOnly(Side.CLIENT)
    GuiScreen getAdvancementsGuiScreen();
}
