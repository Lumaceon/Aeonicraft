package lumaceon.mods.aeonicraft.registry;

import lumaceon.mods.aeonicraft.Aeonicraft;
import lumaceon.mods.aeonicraft.entity.EntityTravelGhost;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class ModEntities
{
    public static void init()
    {
        EntityRegistry.registerModEntity(new ResourceLocation(Aeonicraft.MOD_ID, "travel_ghost"), EntityTravelGhost.class, "travel_ghost", 0, Aeonicraft.instance, 128, 1, true);
    }
}
