package lumaceon.mods.aeonicraft.network.message.handler;

import lumaceon.mods.aeonicraft.Aeonicraft;
import lumaceon.mods.aeonicraft.capability.CapabilityHourglass;
import lumaceon.mods.aeonicraft.item.ItemTemporalHourglass;
import lumaceon.mods.aeonicraft.registry.ModItems;
import lumaceon.mods.aeonicraft.network.message.MessageHourglassFunctionChange;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class HandlerHourglassFunctionChange implements IMessageHandler<MessageHourglassFunctionChange, IMessage>
{
    @Override
    public IMessage onMessage(final MessageHourglassFunctionChange message, final MessageContext ctx)
    {
        if(ctx.side != Side.SERVER)
        {
            System.err.println(this.getClass().toString() + " received on wrong side:" + ctx.side);
            return null;
        }

        Aeonicraft.proxy.getThreadListener(ctx).addScheduledTask(() ->
                {
                    EntityPlayer player = ctx.getServerHandler().player;
                    if(player == null)
                        return;

                    ItemStack currentItem = player.inventory.getCurrentItem();

                    if(currentItem.getItem().equals(ModItems.temporal_hourglass))
                        ((ItemTemporalHourglass) currentItem.getItem()).shiftFunction(player, currentItem, message.relativeShift);
                }
        );

        return null;
    }
}
