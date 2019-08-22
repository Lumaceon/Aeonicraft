package lumaceon.mods.aeonicraft.temporalcompressor;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class TemporalCompressorComponentUpdatable extends TemporalCompressorComponent
{
    public TemporalCompressorComponentUpdatable(ResourceLocation registryName) {
        super(registryName);
    }

    public void onUpdateTick(World world) {}
}
