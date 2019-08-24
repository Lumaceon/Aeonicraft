package lumaceon.mods.aeonicraft.client.tesr;

import lumaceon.mods.aeonicraft.api.temporalcompression.TemporalCompressorComponent;
import lumaceon.mods.aeonicraft.registry.ModItems;
import lumaceon.mods.aeonicraft.tile.TileTemporalCompressor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;

import javax.vecmath.Point2i;

public class TESRTemporalCompressor extends TileEntitySpecialRenderer<TileTemporalCompressor>
{
    private static Minecraft MC;
    private static ItemStack TEMP_DEFUALT = new ItemStack(ModItems.temporal_hourglass);

    public TESRTemporalCompressor() {
        MC = Minecraft.getMinecraft();
    }

    @Override
    public void render(TileTemporalCompressor te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();
        GlStateManager.translate(x + 0.5F, y + 1.5F, z + 0.5F);
        GlStateManager.disableRescaleNormal();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enableLighting();
        for(int x1 = 0; x1 < te.componentMatrix.matrix.length; x1++)
        {
            for(int y1 = 0; y1 < te.componentMatrix.matrix[0].length; y1++)
            {
                TemporalCompressorComponent component = te.componentMatrix.getComponentForCoordinates(x1, y1);
                if(component != null)
                {
                    Point2i center = te.componentMatrix.getCenter();
                    GlStateManager.translate(center.x - x1, 0, center.y - y1);
                    ItemStack stack = component.itemToRender;
                    MC.getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.NONE);
                    GlStateManager.translate(-(center.x - x1), 0, -(center.y - y1));
                }
            }
        }
        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
    }

    @Override
    public boolean isGlobalRenderer(TileTemporalCompressor te) {
        return true;
    }
}
