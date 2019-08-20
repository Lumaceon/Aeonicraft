package lumaceon.mods.aeonicraft.api.client;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;

import java.io.IOException;

/**
 * Easiest to implement by extending GuiScreen or GuiContainer.
 *
 * Parent GUI is a GuiContainer, therefore several function for both Screen and Container
 * are provided and are usually called before the parent container's respective function.
 * Most functions can simply act as proxies for the superclass functions with the same signature.
 *
 * Note that all variables for height, width, and x/y position are relative to THIS gui:
 * not the entire screen or the hourglass gui.
 */
public interface IHourglassGuiTab
{
    String getGuiTabLabel(); // For hovering over the tab
    ItemStack stackToRender();

    void initGui();
    void setParentGuiSize(int x, int y, int xSize, int ySize);
    void drawScreen(int mouseX, int mouseY, float partialTicks);
    void drawGuiContainerForegroundLayer(int mouseX, int mouseY);
    void setFocused(boolean hasFocusedControlIn);
    boolean isFocused();
    boolean handleComponentClick(ITextComponent component);
    void sendChatMessage(String msg);
    void sendChatMessage(String msg, boolean addToChat);
    void setWorldAndResolution(Minecraft mc, int width, int height);
    void setGuiSize(int w, int h);
    void handleKeyboardInput() throws IOException;
    void updateScreen();
    void onGuiClosed();
    void drawDefaultBackground();
    void drawWorldBackground(int tint);
    void drawBackground(int tint);
    void confirmClicked(boolean result, int id);
    void onResize(Minecraft mcIn, int w, int h);
    void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException;
    void mouseReleased(int mouseX, int mouseY, int state);
    void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick);
}
