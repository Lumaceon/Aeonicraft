package lumaceon.mods.aeonicraft.client.model.temporal_hourglass;

import com.google.common.collect.ImmutableMap;
import lumaceon.mods.aeonicraft.Aeonicraft;
import lumaceon.mods.aeonicraft.api.hourglass.IHourglassFunction;
import lumaceon.mods.aeonicraft.capability.CapabilityHourglass;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("ALL")
public class TemporalHourglassItemOverrideList extends ItemOverrideList
{
    @CapabilityInject(CapabilityHourglass.IHourglassHandler.class)
    public static final Capability<CapabilityHourglass.IHourglassHandler> HOURGLASS = null;

    public static final HashMap<ResourceLocation, IBakedModel> modelCache = new HashMap<>(20);

    public TemporalHourglassItemOverrideList(List<ItemOverride> overridesIn) {
        super(overridesIn);
    }

    @Override
    public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, @Nullable World world, @Nullable EntityLivingBase entity)
    {
        CapabilityHourglass.IHourglassHandler cap = stack.getCapability(HOURGLASS, null);
        if(cap != null)
        {
            IHourglassFunction hourglassFunction = cap.getActiveFunction();

            if(hourglassFunction == null)
                return originalModel;

            ResourceLocation loc = hourglassFunction.getTextureLocation();
            if(modelCache.containsKey(loc))
                return modelCache.get(loc);
            else
            {
                BakedModelHourglass bmh = (BakedModelHourglass) originalModel;
                ImmutableMap.Builder<String, String> b = ImmutableMap.builder();
                b.put("#ConditionalGold", Aeonicraft.MOD_ID + ":item/more_hourglass_texture");
                b.put("#Module", cap.getActiveFunction().getTextureLocation().toString());

                IModel retexturedCopy = bmh.createRetexturedCopy(b.build());
                IBakedModel gitBaked = retexturedCopy.bake(bmh.state, bmh.format, bmh.bakedTextureGetter);
                modelCache.put(loc, gitBaked);
            }
        }
        return originalModel;
    }
}
