package lumaceon.mods.aeonicraft.client.gui;

import lumaceon.mods.aeonicraft.api.IAssemblable;
import lumaceon.mods.aeonicraft.api.clockwork.IClockwork;
import lumaceon.mods.aeonicraft.capability.CapabilityClockwork;
import lumaceon.mods.aeonicraft.client.gui.util.GuiHelper;
import lumaceon.mods.aeonicraft.container.ContainerAssemblyTable;
import lumaceon.mods.aeonicraft.container.ContainerAssemblyTableClient;
import lumaceon.mods.aeonicraft.inventory.InventoryAssemblyTableComponents;
import lumaceon.mods.aeonicraft.lib.Textures;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class GuiAssemblyTable extends GuiContainer
{
    public World world;
    public int x, y, z;

    public GuiAssemblyTable(InventoryPlayer ip, World world, int x, int y, int z) {
        super(Minecraft.getMinecraft().isIntegratedServerRunning() ? new ContainerAssemblyTable(ip, world) : new ContainerAssemblyTableClient(ip, world));
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.xSize = 300;
        this.ySize = 230;
    }

    @Override
    public void initGui()
    {
        super.initGui();
        this.buttonList.clear();
        if(this.inventorySlots instanceof ContainerAssemblyTable)
        {
            ContainerAssemblyTable container = (ContainerAssemblyTable) this.inventorySlots;
            container.buttonList = this.buttonList;
            container.guiLeft = this.guiLeft;
            container.guiTop = this.guiTop;
            container.onGUIResize();
        }
    }

    @Override
    protected void renderToolTip(ItemStack stack, int x, int y)
    {
        FontRenderer font = stack.getItem().getFontRenderer(stack);
        net.minecraftforge.fml.client.config.GuiUtils.preItemToolTip(stack);
        List<String> tooltip = this.getItemToolTip(stack);

        // Apply additional tooltips if component has them.
        ItemStack assemblyItem = ((ContainerAssemblyTable) this.inventorySlots).anInventoryWithOneSlotForTheAssemblableItem.getStackInSlot(0);
        IClockwork clockwork = assemblyItem.getCapability(CapabilityClockwork.CAP, null);
        if(clockwork != null)
        {
            InventoryAssemblyTableComponents components = ((ContainerAssemblyTable) this.inventorySlots).componentInventory;
            int matrixDiameter = clockwork.getDiameter();

            int index = 0;
            boolean found = false;
            int matrixX = 0;
            int matrixY = 0;
            while(matrixY < matrixDiameter && !found)
            {
                matrixX = 0;
                while(matrixX < matrixDiameter)
                {
                    if(matrixX == matrixDiameter/2 && matrixY == matrixDiameter/2)
                    {
                        // In the case of the center position, the index skips by one in the x.
                        matrixX++;
                        continue;
                    }

                    if(components.getStackInSlot(index) == stack)
                    {
                        found = true;
                        break;
                    }

                    matrixX++;
                    index++;
                }

                if(found) break;
                matrixY++;
            }

            if(found)
            {
                List<String> moreTips = clockwork.getAdditionalTooltipsForMatrixPosition(matrixX, matrixY);
                if(moreTips != null)
                {
                    tooltip.clear();
                    tooltip.addAll(moreTips);
                }
            }
        }

        this.drawHoveringText(tooltip, x, y, (font == null ? fontRenderer : font));
        net.minecraftforge.fml.client.config.GuiUtils.postItemToolTip();
    }

    @Override
    public void actionPerformed(GuiButton button)
    {
        //NOOP
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_)
    {
        if(inventorySlots instanceof ContainerAssemblyTable)
        {
            ContainerAssemblyTable container = (ContainerAssemblyTable)inventorySlots;
            ItemStack item = container.anInventoryWithOneSlotForTheAssemblableItem.getStackInSlot(0);
            if(item != null && item.getItem() instanceof IAssemblable)
            {
                IAssemblable assemblyGUI = (IAssemblable) item.getItem();
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                Minecraft.getMinecraft().renderEngine.bindTexture(assemblyGUI.getGUIBackground(item));
                GuiHelper.drawTexturedModalRectStretched(this.guiLeft, this.guiTop, this.zLevel, this.xSize, this.ySize);
                return;
            }
        }
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft().renderEngine.bindTexture(Textures.GUI.ASSEMBLY_TABLE);
        GuiHelper.drawTexturedModalRectStretched(this.guiLeft, this.guiTop, this.zLevel, this.xSize, this.ySize);
    }
}
