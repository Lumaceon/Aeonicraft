package lumaceon.mods.aeonicraft.client.model.temporal_hourglass;

import com.google.common.collect.ImmutableList;
import lumaceon.mods.aeonicraft.api.IHourglassFunction;
import lumaceon.mods.aeonicraft.capability.hourglass.CapabilityHourglass;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ItemLayerModel;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

import javax.annotation.Nullable;
import java.util.List;

@SuppressWarnings("ALL")
public class TemporalHourglassItemOverrideList extends ItemOverrideList
{
    @CapabilityInject(CapabilityHourglass.IHourglassHandler.class)
    public static final Capability<CapabilityHourglass.IHourglassHandler> HOURGLASS = null;

    public TemporalHourglassItemOverrideList(List<ItemOverride> overridesIn) {
        super(overridesIn);
    }

    @Override
    public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, @Nullable World world, @Nullable EntityLivingBase entity)
    {
        CapabilityHourglass.IHourglassHandler cap = stack.getCapability(HOURGLASS, null);
        if(cap != null && originalModel instanceof BakedModelHourglass)
        {
            BakedModelHourglass bmh = (BakedModelHourglass) originalModel;
            IHourglassFunction hourglassFunction = cap.getActiveFunction();
            if(hourglassFunction == null)
            {
                return originalModel;
            }
            ItemLayerModel ilm = new ItemLayerModel(ImmutableList.of(ModelTemporalHourglass.baseLocation, hourglassFunction.getTextureLocation()));
            return ilm.bake(bmh.state, bmh.format, bmh.bakedTextureGetter);
        }
        return originalModel;
    }
}
