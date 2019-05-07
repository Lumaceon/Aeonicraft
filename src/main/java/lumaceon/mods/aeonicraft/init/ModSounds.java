package lumaceon.mods.aeonicraft.init;

import lumaceon.mods.aeonicraft.Aeonicraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@GameRegistry.ObjectHolder(Aeonicraft.MOD_ID)
public class ModSounds
{
    public static final SoundEvent time_ding_short = null;
    public static final SoundEvent time_ding_medium = null;
    public static final SoundEvent time_ding_long = null;

    public static SoundEvent create(ResourceLocation registryName) {
        SoundEvent ret = new SoundEvent(registryName);
        ret.setRegistryName(registryName);
        return ret;
    }
}
