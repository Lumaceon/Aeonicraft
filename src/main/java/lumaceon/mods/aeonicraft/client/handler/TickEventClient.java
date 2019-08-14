package lumaceon.mods.aeonicraft.client.handler;

import lumaceon.mods.aeonicraft.Aeonicraft;
import lumaceon.mods.aeonicraft.capability.timestorage.CapabilityTimeStorage;
import lumaceon.mods.aeonicraft.client.particle.ModParticles;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Mod.EventBusSubscriber(modid = Aeonicraft.MOD_ID)
public class TickEventClient
{
    @CapabilityInject(CapabilityTimeStorage.ITimeStorage.class)
    private static final Capability<CapabilityTimeStorage.ITimeStorage> TIME_STORAGE = null;

    @SubscribeEvent
    public static void onClientUpdateTick(TickEvent.ClientTickEvent event)
    {
        ModParticles.updateParticleList();
    }

    @SubscribeEvent
    public static void onRenderTick(TickEvent.RenderTickEvent event)
    {
        EntityPlayer player = Aeonicraft.proxy.getClientPlayer();
        if(player != null)
        {
            CapabilityTimeStorage.ITimeStorage cap = player.getCapability(TIME_STORAGE, null);
            if(cap != null)
            {
                cap.renderUpdateTick(event.renderTickTime);
            }
        }
    }
}
