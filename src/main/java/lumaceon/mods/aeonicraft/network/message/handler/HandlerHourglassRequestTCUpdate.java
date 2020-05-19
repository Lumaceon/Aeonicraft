package lumaceon.mods.aeonicraft.network.message.handler;

import lumaceon.mods.aeonicraft.Aeonicraft;
import lumaceon.mods.aeonicraft.capability.CapabilityTimeLink;
import lumaceon.mods.aeonicraft.registry.ModItems;
import lumaceon.mods.aeonicraft.network.PacketHandler;
import lumaceon.mods.aeonicraft.network.message.MessageHourglassRequestTCUpdate;
import lumaceon.mods.aeonicraft.network.message.MessageHourglassTCUpdate;
import lumaceon.mods.aeonicraft.temporalcompression.TemporalCompressor;
import lumaceon.mods.aeonicraft.api.util.BlockLoc;
import lumaceon.mods.aeonicraft.worlddata.ExtendedWorldData;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;

public class HandlerHourglassRequestTCUpdate implements IMessageHandler<MessageHourglassRequestTCUpdate, IMessage>
{
    @Override
    public IMessage onMessage(final MessageHourglassRequestTCUpdate message, final MessageContext ctx)
    {
        if(ctx.side != Side.SERVER)
        {
            System.err.println(this.getClass().toString() + " received on wrong side:" + ctx.side);
            return null;
        }

        Aeonicraft.proxy.getThreadListener(ctx).addScheduledTask(() ->
                {
                    EntityPlayerMP player = ctx.getServerHandler().player;
                    if(player == null || player.world == null)
                        return;

                    ItemStack targetItem = player.inventory.getStackInSlot(message.stackIndex);
                    if(targetItem.getItem().equals(ModItems.temporal_hourglass))
                    {
                        CapabilityTimeLink.ITimeLinkHandler cap = targetItem.getCapability(CapabilityTimeLink.TIME_LINK, null);
                        if(cap != null)
                        {
                            ExtendedWorldData worldData = ExtendedWorldData.getInstance(player.world);
                            ArrayList<TemporalCompressor> tcs = new ArrayList<>();
                            for(BlockLoc loc : cap.getCompressorLocations())
                            {
                                TemporalCompressor tc = worldData.temporalCompressorProcesses.get(loc);
                                if(tc != null)
                                    tcs.add(tc);
                            }
                            PacketHandler.INSTANCE.sendTo(new MessageHourglassTCUpdate(tcs.toArray(new TemporalCompressor[0]), message.stackIndex), player);
                        }
                    }
                }
        );

        return null;
    }
}
