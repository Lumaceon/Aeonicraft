package lumaceon.mods.aeonicraft.network.message.handler;

import lumaceon.mods.aeonicraft.Aeonicraft;
import lumaceon.mods.aeonicraft.capability.CapabilityAeonicraftProgression;
import lumaceon.mods.aeonicraft.network.message.MessageGainUnlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class HandlerGainUnlock implements IMessageHandler<MessageGainUnlock, IMessage>
{
    @Override
    public IMessage onMessage(MessageGainUnlock message, MessageContext ctx)
    {
        if(ctx.side != Side.SERVER)
        {
            System.err.println(this.getClass().toString() + " received on wrong side: " + ctx.side);
            return null;
        }

        Aeonicraft.proxy.getThreadListener(ctx).addScheduledTask(() ->
                {
                    if(message.unlockable == null)
                        return;

                    EntityPlayer player = ctx.getServerHandler().player;
                    if(player == null)
                        return;

                    CapabilityAeonicraftProgression.IAeonicraftProgressionHandler cap = player.getCapability(CapabilityAeonicraftProgression.AEONICRAFT_PROGRESSION_CAPABILITY, null);
                    if(cap != null)
                        cap.unlock(message.unlockable);
                }
        );

        return null;
    }
}
