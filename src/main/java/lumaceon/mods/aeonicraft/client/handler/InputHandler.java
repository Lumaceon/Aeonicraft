package lumaceon.mods.aeonicraft.client.handler;

import lumaceon.mods.aeonicraft.Aeonicraft;
import lumaceon.mods.aeonicraft.api.hourglass.IHourglassFunction;
import lumaceon.mods.aeonicraft.capability.travelghost.CapabilityTravelGhost;
import lumaceon.mods.aeonicraft.entity.EntityTravelGhost;
import lumaceon.mods.aeonicraft.init.ModItems;
import lumaceon.mods.aeonicraft.item.ItemTemporalHourglass;
import lumaceon.mods.aeonicraft.util.InventoryHelper;
import lumaceon.mods.aeonicraft.util.TimeParser;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovementInput;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber
public class InputHandler
{
    @SubscribeEvent
    public static void onMovementInputUpdate(InputUpdateEvent event)
    {
        EntityPlayer player = Aeonicraft.proxy.getClientPlayer();
        CapabilityTravelGhost.ITravelGhostHandler cap = player.getCapability(CapabilityTravelGhost.TRAVEL_GHOST, null);
        if(cap != null)
        {
            EntityTravelGhost travelGhost = cap.getTravelGhost();
            if(travelGhost != null && travelGhost.isAddedToWorld())
            {
                // nullify movement in favor of travel ghost...
                MovementInput input = event.getMovementInput();
                input.moveForward = 0.0F;
                input.moveStrafe = 0.0F;

                if(input.forwardKeyDown)
                {
                    ItemStack hourglass = InventoryHelper.getFirstStackOfTypeInInventory(player.inventory, ModItems.temporal_hourglass);
                    IHourglassFunction func = InventoryHelper.getHourglassFunctionFromHourglass(hourglass);

                    if(func != null && func.equals(ModItems.hourglass_function_travel) && hourglass.getItem() instanceof ItemTemporalHourglass)
                    {
                        ItemTemporalHourglass hg = (ItemTemporalHourglass) hourglass.getItem();
                        if(hg.availableTime(hourglass, player.world, Side.CLIENT) >= TimeParser.SECOND * 6)
                        {

                        }
                    }
                }
            }
        }

    }
}
