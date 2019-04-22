package lumaceon.mods.aeonicraft.client.gui;

import lumaceon.mods.aeonicraft.Aeonicraft;
import lumaceon.mods.aeonicraft.container.ContainerTemporalHourglass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiTemporalHourglass extends GuiContainer
{
    public static final ResourceLocation BG_ACTIVES = new ResourceLocation(Aeonicraft.MOD_ID, "textures/gui/gui_hourglass_actives.png");

    public GuiTemporalHourglass() {
        super(new ContainerTemporalHourglass());
        this.xSize = 300;
        this.ySize = 230;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft().renderEngine.bindTexture(BG_ACTIVES);
        GuiHelper.drawTexturedModalRectStretched(this.guiLeft, this.guiTop, this.zLevel, this.xSize, this.ySize);
    }
}
