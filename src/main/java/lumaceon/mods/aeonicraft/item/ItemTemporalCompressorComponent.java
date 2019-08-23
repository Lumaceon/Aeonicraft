package lumaceon.mods.aeonicraft.item;

import lumaceon.mods.aeonicraft.api.temporalcompression.ITemporalCompressorComponentItem;
import lumaceon.mods.aeonicraft.api.temporalcompression.TemporalCompressorComponent;
import net.minecraft.item.ItemStack;

public class ItemTemporalCompressorComponent extends ItemAeonicraft implements ITemporalCompressorComponentItem
{
    protected TemporalCompressorComponent component;
    public ItemTemporalCompressorComponent(int maxStack, int maxDamage, String name) {
        super(maxStack, maxDamage, name);
    }

    public void setCompressorComponent(TemporalCompressorComponent component) {
        this.component = component;
    }

    @Override
    public TemporalCompressorComponent getTemporalCompressorComponent(ItemStack itemStack) {
        return component;
    }
}
