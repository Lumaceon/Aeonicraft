package lumaceon.mods.aeonicraft.client.model.temporal_hourglass;

import lumaceon.mods.aeonicraft.Aeonicraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.common.model.IModelState;

import java.util.Collection;
import java.util.function.Function;

@SuppressWarnings("NullableProblems")
public class ModelTemporalHourglass extends OBJModel
{
    public ResourceLocation ml;

    public ModelTemporalHourglass(MaterialLibrary matLib, ResourceLocation modelLocation) {
        super(matLib, modelLocation);
        this.ml = modelLocation;
    }

    @Override
    public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        IBakedModel bakedModel = super.bake(state, format, bakedTextureGetter);
        return new BakedModelHourglass(bakedModel, this, state, format, bakedTextureGetter);
    }

    @Override
    public Collection<ResourceLocation> getTextures()
    {
        Collection<ResourceLocation> c = super.getTextures();
        c.add(new ResourceLocation(Aeonicraft.MOD_ID, "item/more_hourglass_texture"));
        return c;
    }
}
