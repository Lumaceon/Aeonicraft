package lumaceon.mods.aeonicraft.api.client;

import net.minecraft.client.gui.GuiScreen;

public abstract class HourglassGuiTabFactory
{
    public abstract IHourglassGuiTab createNewHourglassGuiTab(GuiScreen hourglassGui);
}
