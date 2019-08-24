package lumaceon.mods.aeonicraft.client.tesr;

import lumaceon.mods.aeonicraft.api.temporalcompression.TemporalCompressorComponent;
import lumaceon.mods.aeonicraft.tile.TileTemporalCompressor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;

public class TESRTemporalCompressor extends TileEntitySpecialRenderer<TileTemporalCompressor>
{
    private static Minecraft MC;

    public TESRTemporalCompressor() {
        MC = Minecraft.getMinecraft();
    }

    @Override
    public void render(TileTemporalCompressor te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        for(int x1 = 0; x1 < te.componentMatrix.matrix.length; x1++)
        {
            for(int y1 = 0; y1 < te.componentMatrix.matrix[0].length; y1++)
            {
                TemporalCompressorComponent component = te.componentMatrix.getComponentForCoordinates(x1, y1);
                if(component != null)
                {
                    ItemStack stack = component.itemToRender;
                    MC.getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.FIXED);
                }
            }
        }
    }

    @Override
    public boolean isGlobalRenderer(TileTemporalCompressor te) {
        return true;
    }
}
