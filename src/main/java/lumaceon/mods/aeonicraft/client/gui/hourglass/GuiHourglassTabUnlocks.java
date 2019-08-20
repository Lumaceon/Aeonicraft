package lumaceon.mods.aeonicraft.client.gui.hourglass;

import lumaceon.mods.aeonicraft.api.hourglass.HourglassUnlockable;
import lumaceon.mods.aeonicraft.api.hourglass.HourglassUnlockableCategory;
import lumaceon.mods.aeonicraft.client.gui.GuiHelper;
import lumaceon.mods.aeonicraft.lib.Textures;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.Collection;

public class GuiHourglassTabUnlocks extends GuiHourglassTab
{
    private Collection<HourglassUnlockableCategory> categories;
    private boolean resolutionIsDirty = true;

    /** The Y coordinate of the top of each category, listed from bottom to top (same order as categories) **/
    protected int[] categoryYLevels;

    protected float scrollX = 0;
    protected float scrollY = 0;
    protected float maxX = 1000F;
    protected float maxY = 0F;
    private int totalWidth;
    private int totalHeight;

    public GuiHourglassTabUnlocks(ItemStack stackToRender, String unlocalizedDisplayName) {
        super(stackToRender, unlocalizedDisplayName, new Container() {
            @Override
            public boolean canInteractWith(EntityPlayer playerIn) {
                return true;
            }
        });

        this.width = 290;
        this.height = 189;
        this.mc = Minecraft.getMinecraft();
        this.scrollAtLastClickX = this.scrollX;
        this.scrollAtLastClickY = this.scrollY;

        maxY = 2F - this.height;
        categories = GameRegistry.findRegistry(HourglassUnlockableCategory.class).getValuesCollection();
        for(HourglassUnlockableCategory category : categories)
        {
            maxY += category.textureHeight() + 2;
        }

        if(maxY < 0)
            maxY = 0;

        this.scrollY = maxY;

        this.totalWidth = (int) this.maxX + this.width;
        this.totalHeight = (int) this.maxY + this.height;

        categoryYLevels = new int[categories.size()];
        int i = 0;
        int lastHeight = totalHeight;
        for(HourglassUnlockableCategory category : categories)
        {
            lastHeight = categoryYLevels[i] = lastHeight - (category.textureHeight() + 2);
            i++;
        }
    }

    @Override
    public void initGui()
    {
        this.xSize = this.width;
        this.ySize = this.height;
        resolutionIsDirty = true;
    }

    private float x1, x2, y1, y2 = 0F;
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        if(resolutionIsDirty)
        {
            x1 = this.guiLeft + this.parentGuiLeft + 5;
            x2 = this.guiLeft + this.parentGuiLeft + 5 + xSize;
            y1 = this.guiTop + this.parentGuiTop + 36;
            y2 = this.guiTop + this.parentGuiTop + 36 + ySize;
            ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
            x1 *= sr.getScaleFactor();
            y1 *= sr.getScaleFactor();
            x2 *= sr.getScaleFactor();
            y2 *= sr.getScaleFactor();
            resolutionIsDirty = false;
        }

        GlStateManager.pushMatrix();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor((int)x1, (int)(Display.getHeight()-y2), (int)(x2-x1), (int)(y2-y1));

        GlStateManager.translate(-scrollX, -scrollY, 0);

        GlStateManager.pushAttrib();
        super.drawScreen(mouseX, mouseY, partialTicks);
        GlStateManager.popAttrib();

        // ==Draw a category bar that doesn't scroll horizontally but only vertical==

        // Re-translate x back to normal position
        GlStateManager.translate(scrollX, 0, 0);

        int currentHeight = this.totalHeight;
        for(HourglassUnlockableCategory category : categories)
        {
            currentHeight -= (category.textureHeight() + 2);

            // Draw center
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1F);
            Minecraft.getMinecraft().renderEngine.bindTexture(Textures.HOURGLASS_UL_CAT_BG);
            GuiHelper.drawTexturedModalRectWithUVs(0, currentHeight + 2, this.zLevel, 32, category.textureHeight() - 4, 0.5F, 0, 1, (category.textureHeight() - 4) / 32F);

            // Draw top and bottom of the category border on top of the version with just the sides
            GuiHelper.drawTexturedModalRectWithUVs(0, currentHeight, this.zLevel, 32, 2, 0, 0, 0.5F, 2F/32F); // top
            GuiHelper.drawTexturedModalRectWithUVs(0, currentHeight + category.textureHeight() - 2, this.zLevel, 32, 2, 0, 30F/32F, 0.5F, 1); // bottom

            // Draw category icon
            GlStateManager.pushAttrib();
            drawItemStack(category.getIcon().getStackToRender(), 8, currentHeight + (category.textureHeight() / 2) - 8, "");
            GlStateManager.popAttrib();
        }

        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GlStateManager.popMatrix();

        this.renderCategoryTooltips(mouseX, mouseY);
        this.renderUnlockTooltips(mouseX, mouseY);
    }

    protected void renderCategoryTooltips(int mouseX, int mouseY)
    {
        int i = 0;
        for(HourglassUnlockableCategory category : categories)
        {
            if(isOverCategory(category, i, mouseX, mouseY))
            {
                this.drawHoveringText(category.getRegistryName().toString(), mouseX, mouseY);
            }
            i++;
        }
    }

    protected void renderUnlockTooltips(int mouseX, int mouseY)
    {

    }

    private boolean isOverCategory(HourglassUnlockableCategory category, int categoryIndex, int mouseX, int mouseY)
    {
        if(mouseX < this.guiLeft || mouseX > this.guiLeft + 32 || mouseY < this.guiTop | mouseY > this.guiTop + this.ySize)
            return false;

        int localY = (int) (categoryYLevels[categoryIndex] - scrollY);

        if(mouseY < localY || mouseY >= localY + category.textureHeight())
            return false;

        return true;
    }

    private boolean isOverUnlockable(HourglassUnlockable unlockable, int unlockableIndex, int mouseX, int mouseY)
    {
        return false;
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
    public void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        long time = System.currentTimeMillis();

        int currentHeight = this.totalHeight;
        float offsetPercentage = time % 5000 / 5000F;
        float offsetPercentage2 = time % 8300 / 8300F;
        float offsetPercentage3 = time % 12050 / 12050F;
        float offsetPercentage4 = time % 10490 / 10490F;
        float offsetPercentage5 = time % 6235 / 6235F;

        int index = 1;
        float speedMulti;
        for(HourglassUnlockableCategory category : this.categories)
        {
            if(category != null)
            {
                speedMulti = 1+index*0.1295F;

                // Render 2-pixel tall border base
                GlStateManager.color(1.0F, 1.0F, 1.0F, 0.75F);
                Minecraft.getMinecraft().renderEngine.bindTexture(Textures.TEMPORAL_BASE);
                GuiHelper.drawTexturedModalRectWithUVs(0, currentHeight - 2, this.zLevel, totalWidth, 2, 0, 0, (float) totalWidth / 16, 1F/8F);

                // Render 2-pixel border shiny dot thingies
                GlStateManager.color(1.0F, 1.0F, 1.0F, 0.75F);
                Minecraft.getMinecraft().renderEngine.bindTexture(Textures.TEMPORAL_DOTS);
                offsetPercentage = time % 5000*speedMulti / (5000F*speedMulti);
                offsetPercentage2 = time % 8300*speedMulti / (8300F*speedMulti);
                offsetPercentage3 = time % 12050*speedMulti / (12050F*speedMulti);
                offsetPercentage4 = time % 10490*speedMulti / (10490F*speedMulti);
                offsetPercentage5 = time % 6235*speedMulti / (6235F*speedMulti);
                GuiHelper.drawTexturedModalRectWithRotatedUVs(0, currentHeight - 2, this.zLevel, totalWidth, 2, 1F/8F, offsetPercentage, 1F/6F, totalWidth/256F + offsetPercentage, 3);
                GuiHelper.drawTexturedModalRectWithRotatedUVs(0, currentHeight - 2, this.zLevel, totalWidth, 2, 1F/8F, offsetPercentage2, 1F/6F, totalWidth/256F + offsetPercentage2, 3);
                GuiHelper.drawTexturedModalRectWithRotatedUVs(0, currentHeight - 2, this.zLevel, totalWidth, 2, 1F/8F, offsetPercentage3, 1F/6F, totalWidth/256F + offsetPercentage3, 3);
                GuiHelper.drawTexturedModalRectWithRotatedUVs(0, currentHeight - 2, this.zLevel, totalWidth, 2, 1F/8F, offsetPercentage4, 1F/6F, totalWidth/256F + offsetPercentage4, 3);
                GuiHelper.drawTexturedModalRectWithRotatedUVs(0, currentHeight - 2, this.zLevel, totalWidth, 2, 1F/8F, offsetPercentage5, 1F/6F, totalWidth/256F + offsetPercentage5, 3);

                // Render category background
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                ResourceLocation loc = category.backgroundTexture();
                Minecraft.getMinecraft().renderEngine.bindTexture(loc);
                currentHeight -= (category.textureHeight() + 2);
                GuiHelper.drawTexturedModalRectWithUVs(0, currentHeight, this.zLevel, totalWidth, category.textureHeight(), 0, 0, (float) totalWidth / category.textureWidth(), 1);

                index++;
            }
        }
        GlStateManager.color(1.0F, 1.0F, 1.0F, 0.75F);
        Minecraft.getMinecraft().renderEngine.bindTexture(Textures.TEMPORAL_BASE);
        GuiHelper.drawTexturedModalRectWithUVs(0, currentHeight - 2, this.zLevel, totalWidth, 2, 0, 0, (float) totalWidth / 16, 1F/8F);

        GlStateManager.color(1.0F, 1.0F, 1.0F, 0.75F);
        Minecraft.getMinecraft().renderEngine.bindTexture(Textures.TEMPORAL_DOTS);
        GuiHelper.drawTexturedModalRectWithRotatedUVs(0, currentHeight - 2, this.zLevel, totalWidth, 2, 1F/8F, offsetPercentage, 1F/6F, totalWidth/256F + offsetPercentage, 3);
        GuiHelper.drawTexturedModalRectWithRotatedUVs(0, currentHeight - 2, this.zLevel, totalWidth, 2, 1F/8F, offsetPercentage2, 1F/6F, totalWidth/256F + offsetPercentage2, 3);
        GuiHelper.drawTexturedModalRectWithRotatedUVs(0, currentHeight - 2, this.zLevel, totalWidth, 2, 1F/8F, offsetPercentage3, 1F/6F, totalWidth/256F + offsetPercentage3, 3);
        GuiHelper.drawTexturedModalRectWithRotatedUVs(0, currentHeight - 2, this.zLevel, totalWidth, 2, 1F/8F, offsetPercentage4, 1F/6F, totalWidth/256F + offsetPercentage4, 3);
        GuiHelper.drawTexturedModalRectWithRotatedUVs(0, currentHeight - 2, this.zLevel, totalWidth, 2, 1F/8F, offsetPercentage5, 1F/6F, totalWidth/256F + offsetPercentage5, 3);
    }

    private int mouseLastClickedX, mouseLastClickedY = 0;
    private float scrollAtLastClickX;
    private float scrollAtLastClickY;
    private boolean scrollingVertically = false;
    private boolean scrollingHorizontally = false;
    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.mouseLastClickedX = mouseX;
        this.mouseLastClickedY = mouseY;
        this.scrollAtLastClickX = this.scrollX;
        this.scrollAtLastClickY = this.scrollY;

        int i = 0;
        boolean found = false;
        for(HourglassUnlockableCategory category : categories)
        {
            if(isOverCategory(category, i, mouseX, mouseY))
            {
                found = true;
                break;
            }
            i++;
        }

        if(found)
        {
            scrollingHorizontally = false;
            scrollingVertically = true;
        }
        else if(mouseX >= this.guiLeft && mouseX <= this.guiLeft + this.xSize && mouseY >= this.guiTop && mouseY <= this.guiTop + this.ySize)
        {
            scrollingHorizontally = true;
            scrollingVertically = true;
        }
        else
        {
            scrollingHorizontally = false;
            scrollingVertically = false;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        if(timeSinceLastClick > 20)
        {
            if(scrollingHorizontally)
                scrollX = Math.min(maxX, Math.max(0, this.scrollAtLastClickX + mouseLastClickedX - mouseX));
            if(scrollingVertically)
                scrollY = Math.min(maxY, Math.max(0, this.scrollAtLastClickY + mouseLastClickedY - mouseY));
        }
    }

    @Override
    public void setWorldAndResolution(Minecraft mc, int width, int height)
    {
        super.setWorldAndResolution(mc, width, height);
        this.resolutionIsDirty = true;
    }

    private void legacyFancies()
    {
        /*GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft().renderEngine.bindTexture(Textures.HOURGLASS_BASE_GUI);
        GuiHelper.drawTexturedModalRectStretched(this.guiLeft, this.guiTop, this.zLevel, this.xSize, this.ySize);

        Minecraft.getMinecraft().renderEngine.bindTexture(Textures.TEMPORAL_CLOUD_RING);
        GlStateManager.pushAttrib();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.enableBlend();

        int[] cycleTimes = {
                10133, 5390, 7696, 3105, 2965, 5964, 4109, 8387
        };

        float offset = 1.0F;
        float alpha = 0.25F;

        float cyclePercent;
        for(int i = 0; i < cycleTimes.length; i++)
        {
            cyclePercent = System.currentTimeMillis() % cycleTimes[i] / (float) cycleTimes[i];
            GlStateManager.color(1.0F, 1.0F, 1.0F, cyclePercent < 0.5F ? alpha * cyclePercent * 2F : alpha);//(cyclePercent > 0.6F ? alpha*(1-(cyclePercent-0.6F)/0.4F) : alpha));
            GuiHelper.drawTexturedModalRectWithUVs(0, 0, this.zLevel, this.xSize, ySize, cyclePercent*0.5F, cyclePercent*0.5F, offset - (0.5F*cyclePercent), offset - (0.5F*cyclePercent));
        }
        GlStateManager.color(1.0F, 1.0F, 1.0F, alpha);

        GlStateManager.popAttrib();
        Minecraft.getMinecraft().renderEngine.bindTexture(Textures.TEMPORAL_ELECTRIC_BASE2);
        GlStateManager.color(0.5F, 0.5F, 0.5F, 1.0F);
        GlStateManager.blendFunc(GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GuiHelper.drawTexturedModalRectStretched(this.guiLeft, this.guiTop, this.zLevel, this.xSize, this.ySize);
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);*/
    }
}
