package lumaceon.mods.aeonicraft.client.gui.hourglass;

import lumaceon.mods.aeonicraft.api.client.IHourglassGuiTab;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

import java.io.IOException;

public class GuiHourglassTab extends GuiContainer implements IHourglassGuiTab
{
    private ItemStack stackToRender;
    private String unlocalizedDisplayName;

    protected int parentGuiLeft = 0;
    protected int parentGuiTop = 0;
    protected int parentGuiSizeX = 0;
    protected int parentGuiSizeY = 0;

    public GuiHourglassTab(ItemStack stackToRender, String unlocalizedDisplayName, Container inventorySlotsIn) {
        super(inventorySlotsIn);
        this.stackToRender = stackToRender;
        this.unlocalizedDisplayName = unlocalizedDisplayName;
    }

    @Override
    public String getGuiTabLabel() {
        return this.unlocalizedDisplayName;
    }

    @Override
    public ItemStack stackToRender() {
        return stackToRender;
    }

    @Override
    public void setParentGuiSize(int x, int y, int xSize, int ySize) {
        parentGuiLeft = x;
        parentGuiTop = y;
        parentGuiSizeX = xSize;
        parentGuiSizeY = ySize;
    }

    @Override
    public void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {

    }

    @Override
    public void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {

    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {

    }

    @Override
    public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {

    }
}
