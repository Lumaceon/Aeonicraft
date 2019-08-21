package lumaceon.mods.aeonicraft.handler;

import lumaceon.mods.aeonicraft.Aeonicraft;
import lumaceon.mods.aeonicraft.capability.CapabilityAeonicraftProgression;
import lumaceon.mods.aeonicraft.network.PacketHandler;
import lumaceon.mods.aeonicraft.network.message.MessageLoginDataSync;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = Aeonicraft.MOD_ID)
public class EntityEventHandler
{
    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinWorldEvent event)
    {
        Entity entity = event.getEntity();

        //Update the client of the player with relevant data the player should know.
        if (entity instanceof EntityPlayer)
        {
            if (!event.getWorld().isRemote)
            {
                CapabilityAeonicraftProgression.IAeonicraftProgressionHandler cap = entity.getCapability(CapabilityAeonicraftProgression.AEONICRAFT_PROGRESSION_CAPABILITY, null);
                if (cap != null) {
                    PacketHandler.INSTANCE.sendTo(new MessageLoginDataSync(cap), (EntityPlayerMP) entity);
                }
            }
        }
    }
}
