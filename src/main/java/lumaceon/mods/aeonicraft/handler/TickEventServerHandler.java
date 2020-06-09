package lumaceon.mods.aeonicraft.handler;

import lumaceon.mods.aeonicraft.Aeonicraft;
import lumaceon.mods.aeonicraft.worlddata.ExtendedSaveData;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Mod.EventBusSubscriber(modid = Aeonicraft.MOD_ID)
public class TickEventServerHandler
{
    private static WorldServer world = null;

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event)
    {
        if(world == null)
            world = DimensionManager.getWorld(0);
        if(world != null)
            ExtendedSaveData.getInstance(world).onServerTick();
    }
}
