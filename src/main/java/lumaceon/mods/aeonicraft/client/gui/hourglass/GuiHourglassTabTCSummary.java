package lumaceon.mods.aeonicraft.client.gui.hourglass;

import lumaceon.mods.aeonicraft.Aeonicraft;
import lumaceon.mods.aeonicraft.client.gui.util.GuiHelper;
import net.minecraft.client.Minecraft;
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
    public void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        Minecraft.getMinecraft().renderEngine.bindTexture(BG);
        GuiHelper.drawTexturedModalRectStretched(0, 0, this.zLevel, width, height);
    }
}
