package lumaceon.mods.aeonicraft.client.tesr;

import lumaceon.mods.aeonicraft.tile.TileTemporalCompressor;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;

public class TESRTemporalCompressor extends TileEntitySpecialRenderer<TileTemporalCompressor>
{
    @Override
    public void render(TileTemporalCompressor te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        for(int x1 = 0; x1 < te.componentMatrix.matrix.length; x1++)
        {
            for(int y1 = 0; y1 < te.componentMatrix.matrix[0].length; y1++)
            {
                //no op
            }
        }
    }

    @Override
    public boolean isGlobalRenderer(TileTemporalCompressor te) {
        return true;
    }
}
