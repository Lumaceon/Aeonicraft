package lumaceon.mods.aeonicraft.temporalcompressor;

import lumaceon.mods.aeonicraft.api.temporalcompression.TemporalCompressorComponent;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class TemporalCompressorComponentUpdatable extends TemporalCompressorComponent
{
    public TemporalCompressorComponentUpdatable(ResourceLocation registryName) {
        super(registryName);
    }

    public void onUpdateTick(World world) {}
}
