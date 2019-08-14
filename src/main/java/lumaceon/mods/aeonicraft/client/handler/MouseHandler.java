package lumaceon.mods.aeonicraft.client.handler;

import lumaceon.mods.aeonicraft.capability.hourglass.CapabilityHourglass;
import lumaceon.mods.aeonicraft.init.ModItems;
import lumaceon.mods.aeonicraft.network.PacketHandler;
import lumaceon.mods.aeonicraft.network.message.MessageHourglassFunctionChange;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

@Mod.EventBusSubscriber
public class MouseHandler
{
    @SubscribeEvent
    public static void onMouseInput(MouseEvent event)
    {
        if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
        {
            EntityPlayer player = Minecraft.getMinecraft().player;
            if(player != null)
            {
                ItemStack itemInHand = player.inventory.getCurrentItem();
                if(itemInHand.getItem().equals(ModItems.temporal_hourglass))
                {
                    CapabilityHourglass.IHourglassHandler cap = itemInHand.getCapability(CapabilityHourglass.HOURGLASS, null);
                    if(cap != null)
                    {
                        event.setCanceled(true);
                        int change = 0;
                        // TODO -120 is my own mouse's scroll to the right, but this may not be consistent across platforms.
                        int dWheel = event.getDwheel();
                        while(dWheel <= -120)
                        {
                            cap.cycleActiveFunction(true);
                            dWheel += 120;
                            change++;
                        }

                        while(dWheel >= 120)
                        {
                            cap.cycleActiveFunction(false);
                            dWheel -= 120;
                            change++;
                        }

                        if(change > 0)
                            PacketHandler.INSTANCE.sendToServer(new MessageHourglassFunctionChange(cap.getActiveFunction()));
                    }
                }
            }
        }
    }
}
