package lumaceon.mods.aeonicraft.network.message.handler;

import lumaceon.mods.aeonicraft.Aeonicraft;
import lumaceon.mods.aeonicraft.capability.CapabilityAeonicraftProgression;
import lumaceon.mods.aeonicraft.network.message.MessageLoginDataSync;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class HandlerLoginDataSync implements IMessageHandler<MessageLoginDataSync, IMessage>
{
    @CapabilityInject(CapabilityAeonicraftProgression.IAeonicraftProgressionHandler.class)
    public static final Capability<CapabilityAeonicraftProgression.IAeonicraftProgressionHandler> AEONICRAFT_PROGRESSION_CAPABILITY = null;

    @Override
    public IMessage onMessage(MessageLoginDataSync message, MessageContext ctx)
    {
        if(ctx.side != Side.CLIENT)
        {
            System.err.println(this.getClass().toString() + " received on wrong side: " + ctx.side);
            return null;
        }

        Aeonicraft.proxy.getThreadListener(ctx).addScheduledTask(() ->
                {
                    EntityPlayer player = Aeonicraft.proxy.getClientPlayer();

                    CapabilityAeonicraftProgression.IAeonicraftProgressionHandler cap = player.getCapability(AEONICRAFT_PROGRESSION_CAPABILITY, null);
                    if(cap != null)
                        cap.loadFromNBT(message.progressionHandlerNBT);
                }
        );

        return null;
    }
}
