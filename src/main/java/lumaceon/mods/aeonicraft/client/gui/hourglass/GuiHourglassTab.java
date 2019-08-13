package lumaceon.mods.aeonicraft.client.gui.hourglass;

import lumaceon.mods.aeonicraft.api.client.IHourglassGuiTab;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

public class GuiHourglassTab extends GuiContainer implements IHourglassGuiTab
{
    private ItemStack stackToRender;
    private String unlocalizedDisplayName;

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
    public void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {

    }

    @Override
    public void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {

    }
}
