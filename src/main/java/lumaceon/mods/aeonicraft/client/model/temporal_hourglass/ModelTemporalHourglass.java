package lumaceon.mods.aeonicraft.client.model.temporal_hourglass;

import com.google.common.collect.ImmutableList;
import lumaceon.mods.aeonicraft.Aeonicraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ItemLayerModel;
import net.minecraftforge.common.model.IModelState;

import java.util.Collection;
import java.util.function.Function;

@SuppressWarnings("NullableProblems")
public class ModelTemporalHourglass implements IModel
{
    public static final ResourceLocation baseLocation = new ResourceLocation(Aeonicraft.MOD_ID, "item/temporal_hourglass");

    private ItemLayerModel itemLayerModel = new ItemLayerModel(ImmutableList.of(baseLocation));

    @Override
    public Collection<ResourceLocation> getTextures() {
        return itemLayerModel.getTextures();
    }

    @Override
    public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        return new BakedModelHourglass(itemLayerModel.bake(state, format, bakedTextureGetter), state, format, bakedTextureGetter);
    }
}
