package lumaceon.mods.aeonicraft.client.gui.hourglass;

import lumaceon.mods.aeonicraft.compat.ModCompatProxyRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;

import java.io.IOException;

public class GuiHourglassTabAdvancementsProxy extends GuiHourglassTab
{
    protected GuiScreen advancementsGui;

    public GuiHourglassTabAdvancementsProxy(ItemStack stackToRender, String unlocalizedDisplayName) {
        super(stackToRender, unlocalizedDisplayName, new Container() {
            @Override
            public boolean canInteractWith(EntityPlayer playerIn) {
                return true;
            }
        });

        this.advancementsGui = ModCompatProxyRegistry.betterAdvancementsCompat.getAdvancementsGuiScreen();
    }

    public void initGui() { advancementsGui.initGui(); }
    public void drawScreen(int mouseX, int mouseY, float partialTicks) { advancementsGui.drawScreen(mouseX, mouseY, partialTicks); }
    public void drawHoveringText(String text, int x, int y) { advancementsGui.drawHoveringText(text, x, y); }
    public void setFocused(boolean hasFocusedControlIn) { advancementsGui.setFocused(hasFocusedControlIn); }
    public boolean isFocused() { return advancementsGui.isFocused(); }
    public boolean handleComponentClick(ITextComponent component) { return advancementsGui.handleComponentClick(component); }
    public void sendChatMessage(String msg) { advancementsGui.sendChatMessage(msg); }
    public void sendChatMessage(String msg, boolean addToChat) { advancementsGui.sendChatMessage(msg, addToChat); }
    public void setWorldAndResolution(Minecraft mc, int width, int height) { advancementsGui.setWorldAndResolution(mc, width, height); }
    public void setGuiSize(int w, int h) { advancementsGui.setGuiSize(w, h); }
    public void handleKeyboardInput() throws IOException { advancementsGui.handleKeyboardInput(); }
    public void updateScreen() { advancementsGui.updateScreen(); }
    public void onGuiClosed() { advancementsGui.onGuiClosed(); }
    public void drawDefaultBackground() { advancementsGui.drawDefaultBackground(); }
    public void drawWorldBackground(int tint) { advancementsGui.drawWorldBackground(tint); }
    public void drawBackground(int tint) { advancementsGui.drawBackground(tint); }
    public void confirmClicked(boolean result, int id) { advancementsGui.confirmClicked(result, id); }
    public void onResize(Minecraft mcIn, int w, int h) { advancementsGui.onResize(mcIn, w, h); }
}
