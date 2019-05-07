package lumaceon.mods.aeonicraft.client.handler;

import lumaceon.mods.aeonicraft.client.particle.ModParticles;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Mod.EventBusSubscriber
public class TickEventClient
{
    @SubscribeEvent
    public static void onClientUpdateTick(TickEvent.ClientTickEvent event)
    {
        ModParticles.updateParticleList();
    }
}
