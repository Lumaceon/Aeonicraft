package lumaceon.mods.aeonicraft.temporalcompressor;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;

/**
 * Component for temporal compressor system, which can be attached to a world chunk.
 */
public class TemporalCompressorComponent extends IForgeRegistryEntry.Impl<TemporalCompressorComponent>
{
    public TemporalCompressorComponent(ResourceLocation registryName) {
        this.setRegistryName(registryName);
    }
}
