package lumaceon.mods.aeonicraft.handler;

import lumaceon.mods.aeonicraft.Aeonicraft;
import lumaceon.mods.aeonicraft.capability.CapabilityAeonicraftProgression;
import lumaceon.mods.aeonicraft.capability.CapabilityTravelGhost;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = Aeonicraft.MOD_ID)
public class CapabilityAttachHandler
{
    @CapabilityInject(CapabilityTravelGhost.ITravelGhostHandler.class)
    public static final Capability<CapabilityTravelGhost.ITravelGhostHandler> TRAVEL_GHOST = null;

    @SubscribeEvent
    public static void onEntityCapabilityAttach(AttachCapabilitiesEvent<Entity> event)
    {
        Entity entity = event.getObject();
        if(entity instanceof EntityPlayer)
        {
            if(!entity.hasCapability(TRAVEL_GHOST, null))
                event.addCapability(new ResourceLocation(Aeonicraft.MOD_ID + ":travel_ghost"), new CapabilityTravelGhost.Provider());
            if(!entity.hasCapability(CapabilityAeonicraftProgression.AEONICRAFT_PROGRESSION_CAPABILITY, null))
                event.addCapability(new ResourceLocation(Aeonicraft.MOD_ID + ":aeonicraft_progression"), new CapabilityAeonicraftProgression.Provider());
        }
    }
}
