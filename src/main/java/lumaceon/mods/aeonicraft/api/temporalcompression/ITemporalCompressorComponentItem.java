package lumaceon.mods.aeonicraft.api.temporalcompression;

import net.minecraft.item.ItemStack;

public interface ITemporalCompressorComponentItem
{
    TemporalCompressorComponent getTemporalCompressorComponent(ItemStack itemStack);
}
