package lumaceon.mods.aeonicraft.network.message.handler;

import lumaceon.mods.aeonicraft.Aeonicraft;
import lumaceon.mods.aeonicraft.capability.CapabilityTimeLink;
import lumaceon.mods.aeonicraft.network.message.MessageHourglassTCUpdate;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class HandlerHourglassTCUpdate implements IMessageHandler<MessageHourglassTCUpdate, IMessage>
{
    @CapabilityInject(CapabilityTimeLink.ITimeLinkHandler.class)
    private static final Capability<CapabilityTimeLink.ITimeLinkHandler> TIME_LINK = null;

    @Override
    public IMessage onMessage(final MessageHourglassTCUpdate message, final MessageContext ctx)
    {
        if(ctx.side != Side.CLIENT)
        {
            System.err.println(this.getClass().toString() + " received on wrong side:" + ctx.side);
            return null;
        }

        Aeonicraft.proxy.getThreadListener(ctx).addScheduledTask(() ->
                {
                    World world = Aeonicraft.proxy.getClientWorld();
                    EntityPlayer player = Aeonicraft.proxy.getClientPlayer();
                    Slot targetSlot = player.inventoryContainer.getSlotFromInventory(player.inventory, message.targetInventorySlotOfHourglass);
                    ItemStack is = targetSlot.getStack();
                    CapabilityTimeLink.ITimeLinkHandler cap = is.getCapability(TIME_LINK, null);
                    if(cap != null)
                    {
                        cap.updateClientInformation(message.tcs);
                    }
                }
        );

        return null;
    }
}
