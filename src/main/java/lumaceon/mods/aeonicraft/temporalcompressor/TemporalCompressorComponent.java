package lumaceon.mods.aeonicraft.temporalcompressor;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;

/**
 * Component for temporal compressor system, which can be attached to a world chunk.
 */
public class TemporalCompressorComponent extends IForgeRegistryEntry.Impl<TemporalCompressorComponent>
{
    public final float TCGen;

    public TemporalCompressorComponent(ResourceLocation registryName, float amount) {
        TCGen = amount;
        this.setRegistryName(registryName);
    }
}
