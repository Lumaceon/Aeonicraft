package lumaceon.mods.aeonicraft.client.gui;

import lumaceon.mods.aeonicraft.Aeonicraft;
import lumaceon.mods.aeonicraft.container.ContainerTemporalHourglass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiTemporalHourglass extends GuiContainer
{
    public static final ResourceLocation BG_ACTIVES = new ResourceLocation(Aeonicraft.MOD_ID, "textures/gui/gui_hourglass_actives.png");

    private ItemStack hourglassStack;

    public GuiTemporalHourglass(ItemStack hourglassStack) {
        super(new ContainerTemporalHourglass(hourglassStack));
        this.hourglassStack = hourglassStack;
        this.xSize = 300;
        this.ySize = 230;
    }

    @Override
    public void initGui()
    {
        super.initGui();
        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;
        buttonList.clear();
        /*int index = 0;
        for(int x = 0; x < 10; x++)
        {
            if(items.length > index && items[index] != null)
            {
                if(items[index].getItem() instanceof IToolUpgrade)
                {
                    buttonList.add(new GuiButtonItem(items[index], index, guiLeft + (x % 10) * 30, guiTop + 15, "", itemRenders, fontRenderer, ((IToolUpgrade) items[index].getItem()).getActive(items[index], this.mc.player.inventory.getCurrentItem())));
                }
            }
            else
            {
                buttonList.add(new GuiButtonItem(null, index, guiLeft + (x % 10) * 30, guiTop + 15, "", itemRenders, fontRenderer, false));
            }
            index++;
        }*/
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

    @Override
    protected void keyTyped(char p_73869_1_, int p_73869_2_) {
        if(p_73869_2_ == 1 || p_73869_2_ == this.mc.gameSettings.keyBindInventory.getKeyCode())
            this.mc.player.closeScreen();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
