package lumaceon.mods.aeonicraft.client.gui.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class GuiHelper
{
    /**
     * Renders similar to the vanilla drawTexturedModalRect from the Gui class, except this allows you to specify the
     * size of the texture file, so you aren't forced to make massive 256x256 files when you only need 64x64.
     *
     * @param x X location of rectangle to draw.
     * @param y Y location of rectangle to draw.
     * @param textureX X coordinate in the texture.
     * @param textureY Y coordinate in the texture.
     * @param width Width of the rectangle in pixels (also applies to the texture).
     * @param height Height of the tectangle in pixels (also applies to the texture).
     * @param textureFileSize The size of the entire texture file we have bound to the renderer (must be square).
     */
    public static void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height, int textureFileSize, double zLevel)
    {
        float f = 1.0F / textureFileSize;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vertexbuffer = tessellator.getBuffer();
        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        vertexbuffer.pos(x + 0, y + height, zLevel).tex((float)(textureX + 0) * f, (float)(textureY + height) * f).endVertex();
        vertexbuffer.pos(x + width, y + height, zLevel).tex((float)(textureX + width) * f, (float)(textureY + height) * f).endVertex();
        vertexbuffer.pos(x + width, y + 0, zLevel).tex((float)(textureX + width) * f, (float)(textureY + 0) * f).endVertex();
        vertexbuffer.pos(x + 0, y + 0, zLevel).tex((float)(textureX + 0) * f, (float)(textureY + 0) * f).endVertex();
        tessellator.draw();
    }

    /**
     * Draws the bound texture by stretching it over the specified width and height.
     */
    public static void drawTexturedModalRectStretched(int x, int y, double zLevel, int width, int height)
    {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder renderer = tessellator.getBuffer();
        renderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        renderer.pos(x, y + height, zLevel).tex(0, 1).endVertex();
        renderer.pos(x + width, y + height, zLevel).tex(1, 1).endVertex();
        renderer.pos(x + width, y, zLevel).tex(1, 0).endVertex();
        renderer.pos(x, y, zLevel).tex(0, 0).endVertex();
        tessellator.draw();
    }

    /**
     * Draws the bound texture by stretching it over the specified width and height.
     */
    public static void drawTexturedModalRectStretched(double x, double y, double zLevel, double width, double height)
    {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder renderer = tessellator.getBuffer();
        renderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        renderer.pos(x, y + height, zLevel).tex(0, 1).endVertex();
        renderer.pos(x + width, y + height, zLevel).tex(1, 1).endVertex();
        renderer.pos(x + width, y, zLevel).tex(1, 0).endVertex();
        renderer.pos(x, y, zLevel).tex(0, 0).endVertex();
        tessellator.draw();
    }

    /**
     * Draws the bound texture by stretching it over the specified width and height.
     */
    public static void drawTexturedModalRectWithUVs(int x, int y, double zLevel, int width, int height, float minU, float minV, float maxU, float maxV)
    {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder renderer = tessellator.getBuffer();
        renderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        renderer.pos(x, y + height, zLevel).tex(minU, maxV).endVertex();
        renderer.pos(x + width, y + height, zLevel).tex(maxU, maxV).endVertex();
        renderer.pos(x + width, y, zLevel).tex(maxU, minV).endVertex();
        renderer.pos(x, y, zLevel).tex(minU, minV).endVertex();
        tessellator.draw();
    }

    /**
     * Draws the bound texture by stretching it over the specified width and height.
     */
    public static void drawTexturedModalRectWithRotatedUVs(int x, int y, double zLevel, int width, int height, float minU, float minV, float maxU, float maxV, int timesRotated)
    {
        float u1, u2, u3, u4;
        float v1, v2, v3, v4;

        switch(timesRotated % 4)
        {
            default:
                u1 = minU; u2 = maxU; u3 = maxU; u4 = minU;
                v1 = maxV; v2 = maxV; v3 = minV; v4 = minV;
                break;
            case 1:
                u1 = minU; u2 = minU; u3 = maxU; u4 = maxU;
                v1 = minV; v2 = maxV; v3 = maxV; v4 = minV;
                break;
            case 2:
                u1 = maxU; u2 = minU; u3 = minU; u4 = maxU;
                v1 = minV; v2 = minV; v3 = maxV; v4 = maxV;
                break;
            case 3:
                u1 = maxU; u2 = maxU; u3 = minU; u4 = minU;
                v1 = maxV; v2 = minV; v3 = minV; v4 = maxV;
                break;
        }

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder renderer = tessellator.getBuffer();
        renderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        renderer.pos(x, y + height, zLevel).tex(u1, v1).endVertex();
        renderer.pos(x + width, y + height, zLevel).tex(u2, v2).endVertex();
        renderer.pos(x + width, y, zLevel).tex(u3, v3).endVertex();
        renderer.pos(x, y, zLevel).tex(u4, v4).endVertex();
        tessellator.draw();
    }

    public static void drawTexturedModalRectCutTop(int x, int y, double zLevel, int width, int fullHeight, int currentHeight)
    {
        float height = fullHeight - (currentHeight);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vertexbuffer = tessellator.getBuffer();
        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        vertexbuffer.pos(x, y+fullHeight, zLevel).tex(0, 0).endVertex();
        vertexbuffer.pos(x+width, y+fullHeight, zLevel).tex(1, 0).endVertex();
        vertexbuffer.pos(x+width, y+height, zLevel).tex(1, (float) -(1 +(currentHeight))/fullHeight).endVertex();
        vertexbuffer.pos(x, y+height, zLevel).tex(0, (float) -(1 +(currentHeight))/fullHeight).endVertex();
        tessellator.draw();
    }

    public static void drawTexturedModalRectStretchedWithUVOffset(int x, int y, double zLevel, int width, int height, float uOffset, float vOffset)
    {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder renderer = tessellator.getBuffer();
        renderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        renderer.pos(x, y + height, zLevel).tex(0 + uOffset, 1 + vOffset).endVertex();
        renderer.pos(x + width, y + height, zLevel).tex(1 + uOffset, 1 + vOffset).endVertex();
        renderer.pos(x + width, y, zLevel).tex(1 + uOffset, 0 + vOffset).endVertex();
        renderer.pos(x, y, zLevel).tex(0 + uOffset, 0 + vOffset).endVertex();
        tessellator.draw();
    }

    /**
     * Draws fluid top to bottom, based on the current amount of fluid vs the given max capacity.
     * Width also determines the effective size of the fluid (as fluid pixels are drawn square).
     */
    public static void drawFluidBar(int x, int y, double zLevel, int width, int height, int maxCapacity, int currentAmount, FluidStack fluidStack, Minecraft mc)
    {
        Fluid fluid = fluidStack.getFluid();
        TextureAtlasSprite textureUV = mc.getTextureMapBlocks().getTextureExtry(fluid.getStill(fluidStack).toString());
        mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

        if(maxCapacity <= 0)
            return;

        float fluidPercentage = (float) currentAmount / (float) maxCapacity;
        int actualHeight = (int) (height * fluidPercentage);

        if(actualHeight <= 0)
            return;

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vertexbuffer = tessellator.getBuffer();
        int pass = 0;
        int heightToRender;
        float maxV;
        while(actualHeight > 0)
        {
            heightToRender = actualHeight > width ? width : actualHeight;
            maxV = ((textureUV.getMaxV() - textureUV.getMinV()) * (float) heightToRender / (float) width) + textureUV.getMinV();
            vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
            vertexbuffer.pos(x, y+height-(pass*width), zLevel).tex(textureUV.getMinU(), textureUV.getMinV()).endVertex(); //Bottom-left
            vertexbuffer.pos(x+width, y+height-(pass*width), zLevel).tex(textureUV.getMaxU(), textureUV.getMinV()).endVertex(); //Bottom-right
            vertexbuffer.pos(x+width, ((y+height)-heightToRender)-(pass*width), zLevel).tex(textureUV.getMaxU(), maxV ).endVertex(); //Top-right
            vertexbuffer.pos(x, ((y+height)-heightToRender)-(pass*width), zLevel).tex(textureUV.getMinU(), maxV ).endVertex(); //Top-left
            tessellator.draw();

            actualHeight = actualHeight - width;
            ++pass;
        }
    }

    /**
     * Draws the registered texture. Useful for cases such as animated sprites and fluids.
     * Doesn't loop with UVs, unlike most of these other methods.
     */
    public static void bindAndDrawRegisteredTexture(int x, int y, double zLevel, int width, int height, float minU, float minV, float maxU, float maxV, int timesRotated, Minecraft mc, ResourceLocation textureLocation)
    {
        TextureAtlasSprite textureUV = mc.getTextureMapBlocks().getTextureExtry(textureLocation.toString());
        if(textureUV == null)
            return;

        mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        float uDifference = textureUV.getMaxU() - textureUV.getMinU();
        float vDifference = textureUV.getMaxU() - textureUV.getMinU();
        minU = textureUV.getMinU() + uDifference * minU;
        maxU = textureUV.getMaxU() - uDifference * (1F-maxU);
        minV = textureUV.getMinV() + vDifference * minV;
        maxV = textureUV.getMaxV() - vDifference * (1F-maxV);

        float u1, u2, u3, u4;
        float v1, v2, v3, v4;

        switch(timesRotated % 4)
        {
            default:
                u1 = minU; u2 = maxU; u3 = maxU; u4 = minU;
                v1 = maxV; v2 = maxV; v3 = minV; v4 = minV;
                break;
            case 1:
                u1 = minU; u2 = minU; u3 = maxU; u4 = maxU;
                v1 = minV; v2 = maxV; v3 = maxV; v4 = minV;
                break;
            case 2:
                u1 = maxU; u2 = minU; u3 = minU; u4 = maxU;
                v1 = minV; v2 = minV; v3 = maxV; v4 = maxV;
                break;
            case 3:
                u1 = maxU; u2 = maxU; u3 = minU; u4 = minU;
                v1 = maxV; v2 = minV; v3 = minV; v4 = maxV;
                break;
        }

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder renderer = tessellator.getBuffer();
        renderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        renderer.pos(x, y + height, zLevel).tex(u1, v1).endVertex();
        renderer.pos(x + width, y + height, zLevel).tex(u2, v2).endVertex();
        renderer.pos(x + width, y, zLevel).tex(u3, v3).endVertex();
        renderer.pos(x, y, zLevel).tex(u4, v4).endVertex();
        tessellator.draw();
    }
}
