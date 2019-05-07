package lumaceon.mods.aeonicraft.client.model.temporal_hourglass;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("NullableProblems")
public class FinalBakedModelHourglass implements IBakedModel
{
    private IBakedModel baseModel;
    private ArrayList<BakedQuad> quads;

    public FinalBakedModelHourglass(IBakedModel baseModel, ArrayList<BakedQuad> quads)
    {
        this.baseModel = baseModel;
        this.quads = quads;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        List<BakedQuad> ret = baseModel.getQuads(state, side, rand);
        ret.addAll(quads);
        return ret;
    }

    @Override
    public boolean isAmbientOcclusion() {
        return baseModel.isAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return baseModel.isGui3d();
    }

    @Override
    public boolean isBuiltInRenderer() {
        return baseModel.isBuiltInRenderer();
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return baseModel.getParticleTexture();
    }

    @Override
    public ItemOverrideList getOverrides() {
        throw new UnsupportedOperationException(getClass().toString() + " should not receive override models.");
    }
}
