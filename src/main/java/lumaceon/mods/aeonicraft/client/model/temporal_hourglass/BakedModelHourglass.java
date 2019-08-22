package lumaceon.mods.aeonicraft.client.model.temporal_hourglass;

import com.google.common.collect.ImmutableMap;
import lumaceon.mods.aeonicraft.client.model.DummyNullModel;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.common.model.IModelState;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.util.*;
import java.util.function.Function;

/**
 * Effectively a 'default' model for the hourglass. TemporalHourglassItemOverrideList defines the custom models.
 */
@SuppressWarnings("NullableProblems")
public class BakedModelHourglass implements IBakedModel
{
    private static final DummyNullModel NULL_MODEL = new DummyNullModel();

    private final IBakedModel proxyModel;
    private final ModelTemporalHourglass model;

    private TemporalHourglassItemOverrideList overrideList;
    public IModelState state;
    public VertexFormat format;
    public Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter;

    public BakedModelHourglass(IBakedModel proxyModel, ModelTemporalHourglass model, IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter)
    {
        this.proxyModel = proxyModel;
        this.overrideList = new TemporalHourglassItemOverrideList(Collections.emptyList());
        this.model = model;
        this.state = state;
        this.format = format;
        this.bakedTextureGetter = bakedTextureGetter;
    }

    public IModel createRetexturedCopy(ImmutableMap<String, String> textures) {
        return new ModelTemporalHourglass(this.model.getMatLib().makeLibWithReplacements(textures), model.ml);
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        return proxyModel.getQuads(state, side, rand);
    }

    @Override
    public boolean isAmbientOcclusion() {
        return proxyModel.isAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return proxyModel.isGui3d();
    }

    @Override
    public boolean isBuiltInRenderer() {
        return proxyModel.isBuiltInRenderer();
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return proxyModel.getParticleTexture();
    }

    @Override
    public ItemOverrideList getOverrides() {
        return overrideList;
    }

    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
        if(cameraTransformType.equals(ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND)
                || cameraTransformType.equals(ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND)
                || cameraTransformType.equals(ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND)
                || cameraTransformType.equals(ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND))
            return Pair.of(NULL_MODEL, null);
        return proxyModel.handlePerspective(cameraTransformType);
    }
}
