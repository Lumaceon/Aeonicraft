package lumaceon.mods.aeonicraft.client.gui.hourglass;

import lumaceon.mods.aeonicraft.api.hourglass.HourglassUnlockable;
import lumaceon.mods.aeonicraft.api.hourglass.HourglassUnlockableCategory;
import lumaceon.mods.aeonicraft.api.util.Icon;
import lumaceon.mods.aeonicraft.client.gui.util.GuiHelper;
import lumaceon.mods.aeonicraft.client.gui.util.UnlockableGUIDefinition;
import lumaceon.mods.aeonicraft.lib.Textures;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class GuiHourglassTabUnlocks extends GuiHourglassTab
{
    private HashMap<HourglassUnlockableCategory, Integer> categoryIndecies;
    private EntityPlayer player;

    private Collection<HourglassUnlockableCategory> categories; // All categories and lists for them are bottom to top
    private Collection<HourglassUnlockable> unlockables;
    private boolean resolutionIsDirty = true;

    protected CategoryGUIDefinition[] categoryDefinitions;
    protected HashMap<HourglassUnlockable, UnlockableGUIDefinition> unlockableDefinitions;

    protected float scrollX;
    protected float scrollY;
    protected float maxX;
    protected float maxY;
    private int totalWidth;
    private int totalHeight;

    public GuiHourglassTabUnlocks(ItemStack stackToRender, String unlocalizedDisplayName)
    {
        super(stackToRender, unlocalizedDisplayName, new Container() {
            @Override
            public boolean canInteractWith(EntityPlayer playerIn) {
                return true;
            }
        });


        // Some basic field updates...
        this.width = 290;
        this.height = 189;
        this.mc = Minecraft.getMinecraft();
        this.player = this.mc.player;
        this.scrollAtLastClickX = this.scrollX;
        this.scrollAtLastClickY = this.scrollY;
        categories = GameRegistry.findRegistry(HourglassUnlockableCategory.class).getValuesCollection();
        categoryDefinitions = new CategoryGUIDefinition[categories.size()];
        categoryIndecies = new HashMap<>(categories.size());
        unlockables = GameRegistry.findRegistry(HourglassUnlockable.class).getValuesCollection();
        unlockableDefinitions = new HashMap<>(unlockables.size());
        int i;


        // Set up height from the available categories and other initial set ups for each category...
        i = 0;
        maxY = 2F - this.height;
        for(HourglassUnlockableCategory category : categories)
        {
            maxY += category.textureHeight() + 2;
            categoryDefinitions[i] = new CategoryGUIDefinition();
            categoryIndecies.put(category, i);
            i++;
        }

        if(maxY < 0)
            maxY = 0;

        this.scrollY = maxY;


        // Organize unlockables and set up width based on the longest category...
        int catIndex;
        HourglassUnlockableCategory cat;
        UnlockableGUIDefinition definition;
        for(HourglassUnlockable unlockable : unlockables)
        {
            definition = new UnlockableGUIDefinition(unlockable, player);
            cat = unlockable.getCategory();
            if(cat != null)
            {
                catIndex = categoryIndecies.getOrDefault(cat, -1);
                if(catIndex != -1)
                {
                    CategoryGUIDefinition catDef = categoryDefinitions[catIndex];
                    catDef.addUnlockable(unlockable);
                    definition.setBaseX(catDef.unlockables.size()*32 + 4);
                    unlockableDefinitions.put(unlockable, definition);
                }
            }
        }

        int greatestCategoryWidth = 0;
        for(CategoryGUIDefinition c : categoryDefinitions)
            greatestCategoryWidth = Math.max(greatestCategoryWidth, c.width);

        this.maxX = greatestCategoryWidth - this.width;
        if(this.maxX < 0)
            this.maxX = 0;

        this.scrollX = 0;


        // Some final set up...
        this.totalWidth = (int) this.maxX + this.width;
        this.totalHeight = (int) this.maxY + this.height;

        int lastHeight = totalHeight;
        i = 0;
        for(HourglassUnlockableCategory category : categories)
        {
            lastHeight = categoryDefinitions[i].topPos = lastHeight - (category.textureHeight() + 2);
            for(HourglassUnlockable unlockable : categoryDefinitions[i].unlockables)
            {
                UnlockableGUIDefinition ulDef = unlockableDefinitions.get(unlockable);
                ulDef.setBaseY(lastHeight + 4);
            }
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

        this.renderUnlockables(mouseX, mouseY);

        // Re-translate x back to normal position for category bar
        GlStateManager.translate(scrollX, 0, 0);

        this.renderCategoryBar(mouseX, mouseY);

        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GlStateManager.popMatrix();

        this.renderCategoryTooltips(mouseX, mouseY);
        this.renderUnlockTooltips(mouseX, mouseY);
    }

    private void renderCategoryBar(int mouseX, int mouseY)
    {
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
    }

    private void renderUnlockables(int mouseX, int mouseY)
    {
        for(UnlockableGUIDefinition def : unlockableDefinitions.values())
        {
            float brightness = (isOverUnlockable(def, mouseX, mouseY) ?  0.85F : 0.5F);
            if(def.isUnlocked) brightness = 1;
            GlStateManager.color(brightness, brightness, brightness, 1.0F);
            Minecraft.getMinecraft().renderEngine.bindTexture(Textures.HOURGLASS_UL_CAT_BG);
            GuiHelper.drawTexturedModalRectWithUVs(def.getLeft(), def.getTop(), this.zLevel, 24, 24, 0, 0, 0.5F, 1);
            Icon icon = def.unlockable.getIcon();
            if(icon != null)
            {
                ItemStack stack = icon.getStackToRender();
                if(stack != null)
                {
                    drawItemStack(icon.getStackToRender(), def.getLeft() + 4, def.getTop() + 4, "");
                }
                else
                {
                    GuiHelper.bindAndDrawRegisteredTexture(def.getLeft() + 4, def.getTop() + 4, this.zLevel, 16, 16, 0, 0, 1, 1, 0, mc, icon.getTexture());
                }
            }
        }
    }

    protected void renderCategoryTooltips(int mouseX, int mouseY)
    {
        int i = 0;
        for(HourglassUnlockableCategory category : categories)
        {
            if(isOverCategory(category, i, mouseX, mouseY))
            {
                this.drawHoveringText(I18n.format(category.getRegistryName().toString()), mouseX, mouseY);
            }
            i++;
        }
    }

    protected void renderUnlockTooltips(int mouseX, int mouseY)
    {
        for(HourglassUnlockable unlockable : unlockables)
        {
            UnlockableGUIDefinition def = unlockableDefinitions.get(unlockable);
            if(isOverUnlockable(def, mouseX, mouseY))
            {
                ArrayList<String> textLines  = new ArrayList<>(1);
                String toolTip = I18n.format(unlockable.getRegistryName().toString());
                textLines.add(toolTip);
                textLines.add("AP cost: " + unlockable.getAdvancementCostWeight());
                textLines.add("TC cost: " + unlockable.getTimeCostWeight());
                int w = mc.fontRenderer.getStringWidth(toolTip) + 16;
                int x = (int) (def.getLeft() + 12 - w*0.5F);
                net.minecraftforge.fml.client.config.GuiUtils.drawHoveringText(textLines, x, def.getTop() - 25, width, height, -1, this.fontRenderer);
                //this.drawHoveringText(I18n.format(unlockable.getRegistryName().toString()), mouseX, mouseY);
            }
        }
    }

    private boolean isOverCategory(HourglassUnlockableCategory category, int categoryIndex, int mouseX, int mouseY)
    {
        if(mouseX < this.guiLeft || mouseX > this.guiLeft + 32 || mouseY < this.guiTop || mouseY > this.guiTop + this.ySize)
            return false;

        int localY = (int) (categoryDefinitions[categoryIndex].topPos - scrollY);

        return mouseY >= localY && mouseY < localY + category.textureHeight();
    }

    private boolean isOverUnlockable(UnlockableGUIDefinition unlockable, int mouseX, int mouseY)
    {
        if(mouseX < this.guiLeft + 32 || mouseX > this.guiLeft + this.xSize || mouseY < this.guiTop || mouseY > this.guiTop + this.ySize)
            return false;

        int localX = (int) (unlockable.getLeft() - scrollY);
        int localY = (int) (unlockable.getTop() - scrollY);

        if(mouseY < localY + 4 || mouseY >= localY + 24)
            return false;

        return mouseX > localX && mouseX < localX + 24;
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
    private HourglassUnlockable unlockableLastClicked = null;
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
            for(UnlockableGUIDefinition def : unlockableDefinitions.values())
                if(isOverUnlockable(def, mouseX, mouseY))
                    this.unlockableLastClicked = def.unlockable;
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
        if(this.unlockableLastClicked != null)
        {
            UnlockableGUIDefinition def = this.unlockableDefinitions.get(unlockableLastClicked);
            if(isOverUnlockable(def, mouseX, mouseY) && Math.sqrt(Math.pow(mouseX - mouseLastClickedX, 2) + Math.pow(mouseY - mouseLastClickedY, 2)) < 12)
            {
                def.onClicked(this.mc);
            }
        }
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

    public class CategoryGUIDefinition
    {
        int topPos = 0;
        int width = 32;
        final ArrayList<HourglassUnlockable> unlockables;

        public CategoryGUIDefinition()
        {
            unlockables = new ArrayList<>();
        }

        public void addUnlockable(HourglassUnlockable unlockable)
        {
            unlockables.add(unlockable);
            width += 32;
        }
    }
}
