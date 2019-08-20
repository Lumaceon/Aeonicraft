package lumaceon.mods.aeonicraft.client.gui;

import lumaceon.mods.aeonicraft.Aeonicraft;
import lumaceon.mods.aeonicraft.api.client.HourglassGuiTabs;
import lumaceon.mods.aeonicraft.api.client.IHourglassGuiTab;
import lumaceon.mods.aeonicraft.container.ContainerTemporalHourglass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

public class GuiTemporalHourglass extends GuiContainer
{
    /** The location of the creative inventory tabs texture */
    private static final ResourceLocation HOURGLASS_TABS = new ResourceLocation(Aeonicraft.MOD_ID,  "textures/gui/hourglass_tabs.png");
    public static final ResourceLocation BG_ACTIVES = new ResourceLocation(Aeonicraft.MOD_ID, "textures/gui/gui_hourglass.png");

    private static int subGUISizeX = 290;
    private static int subGUISizeY = 189;

    private boolean resolutionIsDirty = true;
    private ScaledResolution scaledResolution;

    private IHourglassGuiTab[] hourglassTabs;

    private int selectedTabIndex = 0;
    private int tabPage = 0;
    private int iClickedOnDatOne = -1;

    private IHourglassGuiTab currentTab;

    private ItemStack hourglassStack;
    private GuiScreen advancementScreen;

    public GuiTemporalHourglass(ItemStack hourglassStack, GuiScreen advancementScreen) {
        super(new ContainerTemporalHourglass(hourglassStack));
        this.hourglassStack = hourglassStack;
        this.advancementScreen = advancementScreen;
        this.xSize = 300;
        this.ySize = 230;

        hourglassTabs = new IHourglassGuiTab[HourglassGuiTabs.HOURGLASS_GUI_TABS.size()];
        for(int n = 0; n < hourglassTabs.length; n++)
        {
            hourglassTabs[n] = HourglassGuiTabs.HOURGLASS_GUI_TABS.get(n).createNewHourglassGuiTab(this);
            hourglassTabs[n].initGui();
            hourglassTabs[n].setWorldAndResolution(Minecraft.getMinecraft(), subGUISizeX, subGUISizeY);
        }
        currentTab = hourglassTabs[0];
    }

    public void changeTab(int newTabIndex)
    {
        IHourglassGuiTab newTab = hourglassTabs[newTabIndex];
        newTab.initGui();
        newTab.setWorldAndResolution(Minecraft.getMinecraft(), subGUISizeX, subGUISizeY);
        newTab.setParentGuiSize(this.guiLeft, this.guiTop, this.xSize, this.ySize);
        selectedTabIndex = newTabIndex;
        currentTab = newTab;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        if (mouseButton == 0)
        {
            int i = mouseX - (this.guiLeft + 20);
            int j = mouseY - this.guiTop;

            for(int n = 0; n < hourglassTabs.length; n++)
            {
                if (isMouseOverTab(hourglassTabs[n], n, i, j))
                {
                    iClickedOnDatOne = n;
                    return;
                }
            }
            iClickedOnDatOne = -1;
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);

        int subGUITranslationX = this.guiLeft + 5;
        int subGUITranslationY = this.guiTop + 36;
        this.currentTab.mouseClicked(mouseX - subGUITranslationX, mouseY - subGUITranslationY, mouseButton);
    }

    /**
     * Called when a mouse button is released.
     */
    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state)
    {
        if (state == 0)
        {
            int i = mouseX - (this.guiLeft + 20);
            int j = mouseY - this.guiTop;

            for(int n = 0; n < hourglassTabs.length; n++)
            {
                if (n != this.selectedTabIndex && iClickedOnDatOne == n && isMouseOverTab(hourglassTabs[n], n, i, j))
                {
                    this.changeTab(n);
                    return;
                }
            }
        }

        super.mouseReleased(mouseX, mouseY, state);

        int subGUITranslationX = this.guiLeft + 5;
        int subGUITranslationY = this.guiTop + 36;
        this.currentTab.mouseReleased(mouseX - subGUITranslationX, mouseY - subGUITranslationY, state);
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick)
    {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);

        int subGUITranslationX = this.guiLeft + 5;
        int subGUITranslationY = this.guiTop + 36;
        this.currentTab.mouseClickMove(mouseX - subGUITranslationX, mouseY - subGUITranslationY, clickedMouseButton, timeSinceLastClick);
    }

    private boolean isMouseOverTab(IHourglassGuiTab tab, int tabIndex, int mouseX, int mouseY)
    {
        if (tabIndex / 10 != tabPage)
            return false; // Tab index is not a part of this tab page...

        int j = 27 * tabIndex;
        int k = 0;

        if (tabIndex > 0)
        {
            j += tabIndex;
        }

        return mouseX >= j && mouseX <= j + 27 && mouseY >= k && mouseY <= k + 32;
    }

    private void renderHourglassTabTooltips(int mouseX, int mouseY)
    {
        int tabStartX = guiLeft + 20;
        int tabStartY = guiTop;
        for(int n = 0; n < hourglassTabs.length; n++)
        {
            if(isMouseOverTab(hourglassTabs[n], n, mouseX - tabStartX, mouseY - tabStartY))
            {
                this.drawHoveringText(I18n.format(hourglassTabs[n].getGuiTabLabel()), mouseX, mouseY);
            }
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void initGui()
    {
        super.initGui();
        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;
        this.currentTab.setParentGuiSize(this.guiLeft, this.guiTop, this.xSize, this.ySize);
        this.currentTab.initGui();
        buttonList.clear();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        if(resolutionIsDirty)
        {
            this.scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
            resolutionIsDirty = false;
        }

        int subGUITranslationX = this.guiLeft + 5;
        int subGUITranslationY = this.guiTop + 36;

        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();
        GlStateManager.translate(subGUITranslationX, subGUITranslationY, 0);
        this.currentTab.drawScreen(mouseX - subGUITranslationX, mouseY - subGUITranslationY, partialTicks);
        GlStateManager.popAttrib();
        GlStateManager.popMatrix();

        GlStateManager.enableBlend();
        GlStateManager.pushAttrib();
        super.drawScreen(mouseX, mouseY, partialTicks);
        GlStateManager.popAttrib();

        int tabStartX = guiLeft + 20;
        int tabStartY = guiTop;

        for(int i = 10 * tabPage; i < hourglassTabs.length; i++)
        {
            GlStateManager.pushAttrib();
            IHourglassGuiTab tab = hourglassTabs[i];
            drawItemStack(tab.stackToRender(), tabStartX+6 + i*28, tabStartY + (i == selectedTabIndex ? 8 : 10), "");
            GlStateManager.popAttrib();
        }

        //int scaledMouseX = mouseX * scaledResolution.getScaleFactor();
        //int scaledMouseY = mouseY * scaledResolution.getScaleFactor();
        this.renderHoveredToolTip(mouseX, mouseY);
        this.renderHourglassTabTooltips(mouseX, mouseY);
    }

    private void drawItemStack(ItemStack stack, int x, int y, String altText)
    {
        GlStateManager.translate(0.0F, 0.0F, 32.0F);
        this.zLevel = 200.0F;
        this.itemRender.zLevel = 200.0F;
        net.minecraft.client.gui.FontRenderer font = stack.getItem().getFontRenderer(stack);
        if (font == null) font = fontRenderer;
        this.itemRender.renderItemAndEffectIntoGUI(stack, x, y);
        this.itemRender.renderItemOverlayIntoGUI(font, stack, x, y, altText);
        this.zLevel = 0.0F;
        this.itemRender.zLevel = 0.0F;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft().renderEngine.bindTexture(BG_ACTIVES);
        GuiHelper.drawTexturedModalRectStretched(this.guiLeft, this.guiTop, this.zLevel, this.xSize, this.ySize);

        int tabWidth = 28;
        int tabHeight = 32;
        int textureCoordX;
        int textureCoordY;
        Minecraft.getMinecraft().renderEngine.bindTexture(HOURGLASS_TABS);
        RenderHelper.disableStandardItemLighting();

        for(int i = tabPage*10; i < hourglassTabs.length; i++)
        {
            if(i%10 == 0)
                textureCoordX = 0; // First tab
            else if(i == hourglassTabs.length - 1)
                textureCoordX = tabWidth*2; // Last tab
            else
                textureCoordX = tabWidth;

            if(i == selectedTabIndex)
                textureCoordY = 0; // Currently selected tab
            else
                textureCoordY = 32;

            GuiHelper.drawTexturedModalRect(this.guiLeft + 20 + tabWidth*i, i == selectedTabIndex ? this.guiTop: this.guiTop + 2, textureCoordX, textureCoordY, tabWidth, tabHeight, 128, this.zLevel);
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        int subGUITranslationX = this.guiLeft + 5;
        int subGUITranslationY = this.guiTop + 36;
        currentTab.drawGuiContainerForegroundLayer(mouseX - subGUITranslationX, mouseY - subGUITranslationY);
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }

    @Override
    protected void keyTyped(char p_73869_1_, int p_73869_2_) {
        if(p_73869_2_ == 1 || p_73869_2_ == this.mc.gameSettings.keyBindInventory.getKeyCode())
            this.mc.player.closeScreen();
    }

    @Override
    public void drawHoveringText(String text, int x, int y) {
        super.drawHoveringText(text, x, y);
    }

    @Override
    public void setFocused(boolean hasFocusedControlIn) {
        this.currentTab.setFocused(hasFocusedControlIn);
        super.setFocused(hasFocusedControlIn);
    }

    @Override
    public boolean isFocused() {
        return super.isFocused() || this.currentTab.isFocused();
    }

    @Override
    public boolean handleComponentClick(ITextComponent component) {
        return this.currentTab.handleComponentClick(component) || super.handleComponentClick(component);
    }

    @Override
    public void sendChatMessage(String msg)
    {
        this.currentTab.sendChatMessage(msg);
        super.sendChatMessage(msg);
    }

    @Override
    public void sendChatMessage(String msg, boolean addToChat)
    {
        this.currentTab.sendChatMessage(msg, addToChat);
        super.sendChatMessage(msg, addToChat);
    }

    @Override
    public void setWorldAndResolution(Minecraft mc, int width, int height)
    {
        super.setWorldAndResolution(mc, width, height);
        this.currentTab.setWorldAndResolution(mc, subGUISizeX, subGUISizeY);
        this.currentTab.setParentGuiSize(this.guiLeft, this.guiTop, this.xSize, this.ySize);
        this.resolutionIsDirty = true;
    }

    @Override
    public void setGuiSize(int w, int h)
    {
        super.setGuiSize(w, h);
        this.currentTab.setGuiSize(subGUISizeX, subGUISizeY);
        this.currentTab.setParentGuiSize(this.guiLeft, this.guiTop, this.xSize, this.ySize);
    }

    @Override
    public void handleKeyboardInput() throws IOException
    {
        this.currentTab.handleKeyboardInput();
        super.handleKeyboardInput();
    }

    @Override
    public void updateScreen()
    {
        this.currentTab.updateScreen();
        super.updateScreen();
    }

    @Override
    public void onGuiClosed()
    {
        this.currentTab.onGuiClosed();
        super.onGuiClosed();
    }

    @Override
    public void drawDefaultBackground()
    {
        this.currentTab.drawDefaultBackground();
        super.drawDefaultBackground();
    }

    @Override
    public void drawWorldBackground(int tint)
    {
        this.currentTab.drawWorldBackground(tint);
        super.drawWorldBackground(tint);
    }

    @Override
    public void drawBackground(int tint)
    {
        this.currentTab.drawBackground(tint);
        super.drawBackground(tint);
    }

    @Override
    public void confirmClicked(boolean result, int id)
    {
        this.currentTab.confirmClicked(result, id);
        super.confirmClicked(result, id);
    }

    @Override
    public void onResize(Minecraft mcIn, int w, int h)
    {
        super.onResize(mcIn, w, h);
        this.currentTab.onResize(mcIn, subGUISizeX, subGUISizeY);
        this.currentTab.setParentGuiSize(this.guiLeft, this.guiTop, this.xSize, this.ySize);
    }
}
