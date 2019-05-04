package lumaceon.mods.aeonicraft.handler;

import lumaceon.mods.aeonicraft.api.IHourglassFunction;
import lumaceon.mods.aeonicraft.init.ModItems;
import lumaceon.mods.aeonicraft.item.ItemTemporalHourglass;
import lumaceon.mods.aeonicraft.util.InventoryHelper;
import lumaceon.mods.aeonicraft.util.TimeCosts;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber
public class PlayerEventHandler
{
    @SubscribeEvent
    public static void onBlockAboutToBeBroken(PlayerEvent.BreakSpeed event)
    {
        EntityPlayer player = event.getEntityPlayer();
        if(player != null)
        {
            ItemStack firstHourglass = InventoryHelper.getFirstStackOfTypeInInventory(player.inventory, ModItems.temporalHourglass);
            if(firstHourglass != null)
            {
                ItemTemporalHourglass hourglass = (ItemTemporalHourglass) firstHourglass.getItem();
                IHourglassFunction hourglassFunction = hourglass.getActiveHourglassFunction(firstHourglass);
                if(hourglassFunction != null && hourglassFunction.equals(ModItems.hgFuncExcavationOC))
                {
                    long timeToBreakBlock = TimeCosts.getTimeToBreakBlock(player.world, event.getPos(), event.getState(), player, player.inventory.getCurrentItem());
                    if(hourglass.availableTime(firstHourglass, player.world, player.world.isRemote ? Side.CLIENT : Side.SERVER) >= timeToBreakBlock)
                    {
                        int hourglassSlotIndex = -1;
                        for(int i = 0; i < player.inventory.getSizeInventory(); i++)
                        {
                            if(player.inventory.getStackInSlot(i) == firstHourglass)
                            {
                                hourglassSlotIndex = i;
                                break;
                            }
                        }
                        long amtConsumed = hourglass.consumeTime(player, firstHourglass, player.world, hourglassSlotIndex, timeToBreakBlock);
                        if(amtConsumed >= timeToBreakBlock)
                        {
                            event.setNewSpeed(Float.MAX_VALUE);
                        }
                    }
                    else
                    {
                        event.setNewSpeed(0.0F);
                    }
                }
            }
        }
    }
}
