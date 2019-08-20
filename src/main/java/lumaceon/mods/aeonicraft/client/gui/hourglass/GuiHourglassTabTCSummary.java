package lumaceon.mods.aeonicraft.client.gui.hourglass;

import lumaceon.mods.aeonicraft.Aeonicraft;
import lumaceon.mods.aeonicraft.capability.CapabilityTimeStorage;
import lumaceon.mods.aeonicraft.client.gui.GuiHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GuiHourglassTabTCSummary extends GuiHourglassTab
{
    public static final ResourceLocation BG = new ResourceLocation(Aeonicraft.MOD_ID, "textures/gui/gui_hourglass_tc_summary.png");

    public GuiHourglassTabTCSummary(ItemStack stackToRender, String unlocalizedDisplayName) {
        super(stackToRender, unlocalizedDisplayName, new Container() {
            @Override
            public boolean canInteractWith(EntityPlayer playerIn) {
                return true;
            }
        });

        this.width = 290;
        this.height = 189;
    }

    @Override
    public void initGui()
    {

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        super.drawScreen(mouseX, mouseY, partialTicks);

        // tooltips
        int clockX = 144;
        int clockY = 35;
        int radius = 30;

        if(Math.sqrt((mouseX - clockX) * (mouseX - clockX) + (mouseY - clockY) * (mouseY - clockY)) < radius)
        {
            CapabilityTimeStorage.ITimeStorage cap = Aeonicraft.proxy.getClientPlayer().getCapability(CapabilityTimeStorage.TIME_STORAGE_CAPABILITY, null);
            if(cap instanceof CapabilityTimeStorage.TimeStorage)
            {
                CapabilityTimeStorage.TimeStorage ts = (CapabilityTimeStorage.TimeStorage) cap;
                this.drawHoveringText(I18n.format(Long.toString(ts.getTimeInTicksForDisplay())), mouseX, mouseY);
            }
        }
    }

    @Override
    public void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        Minecraft.getMinecraft().renderEngine.bindTexture(BG);
        GuiHelper.drawTexturedModalRectStretched(0, 0, this.zLevel, width, height);
    }
}
