package lumaceon.mods.aeonicraft.registry.custom;

import lumaceon.mods.aeonicraft.Aeonicraft;
import lumaceon.mods.aeonicraft.api.hourglass.HourglassUnlockable;
import lumaceon.mods.aeonicraft.api.hourglass.HourglassUnlockableCategory;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

@Mod.EventBusSubscriber(modid = Aeonicraft.MOD_ID)
public class HandlerNewRegistry
{
    @SubscribeEvent
    public static void registerception(RegistryEvent.NewRegistry event)
    {
        // Hourglass Unlockable Categories
        RegistryBuilder<HourglassUnlockableCategory> b1 = new RegistryBuilder<>();
        b1.setName(new ResourceLocation(Aeonicraft.MOD_ID, "hourglass_unlockable_category_registry"));
        b1.setType(HourglassUnlockableCategory.class);
        b1.create();

        // Hourglass Unlockables
        RegistryBuilder<HourglassUnlockable> b2 = new RegistryBuilder<>();
        b2.setName(new ResourceLocation(Aeonicraft.MOD_ID, "hourglass_unlockable_registry"));
        b2.setType(HourglassUnlockable.class);
        IForgeRegistry<HourglassUnlockable> registry = b2.create();
    }
}
