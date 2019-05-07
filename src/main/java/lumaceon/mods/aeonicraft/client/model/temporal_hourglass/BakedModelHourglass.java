package lumaceon.mods.aeonicraft.client.model.temporal_hourglass;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.model.IModelState;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/**
 * Effectively a 'default' model for the hourglass. TemporalHourglassItemOverrideList defines the custom models.
 */
@SuppressWarnings("NullableProblems")
public class BakedModelHourglass implements IBakedModel
{
    private IBakedModel baseModel;
    private TemporalHourglassItemOverrideList overrideList;
    public IModelState state;
    public VertexFormat format;
    public Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter;

    public BakedModelHourglass(IBakedModel model, IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter){
        this.baseModel = model;
        this.overrideList = new TemporalHourglassItemOverrideList(Collections.emptyList());
        this.state = state;
        this.format = format;
        this.bakedTextureGetter = bakedTextureGetter;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        return baseModel.getQuads(state, side, rand);
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
        return overrideList;
    }

    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
        Matrix4f matrix4f = baseModel.handlePerspective(cameraTransformType).getRight();
        return Pair.of(this, matrix4f);
    }
}
