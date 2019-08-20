package lumaceon.mods.aeonicraft.network.message.handler;

import lumaceon.mods.aeonicraft.Aeonicraft;
import lumaceon.mods.aeonicraft.capability.CapabilityTimeStorage;
import lumaceon.mods.aeonicraft.network.message.MessagePlayerTCUpdate;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class HandlerPlayerTCUpdate implements IMessageHandler<MessagePlayerTCUpdate, IMessage>
{
    @CapabilityInject(CapabilityTimeStorage.ITimeStorage.class)
    private static final Capability<CapabilityTimeStorage.ITimeStorage> TIME_STORAGE = null;

    @Override
    public IMessage onMessage(MessagePlayerTCUpdate message, MessageContext ctx) {
        if(ctx.side != Side.CLIENT)
        {
            System.err.println(this.getClass().toString() + " received on wrong side:" + ctx.side);
            return null;
        }

        Aeonicraft.proxy.getThreadListener(ctx).addScheduledTask(() ->
                {
                    EntityPlayer player = Aeonicraft.proxy.getClientPlayer();
                    CapabilityTimeStorage.ITimeStorage cap = player.getCapability(TIME_STORAGE, null);
                    if(cap != null)
                    {
                        if(cap instanceof CapabilityTimeStorage.TimeStorage)
                        {
                            CapabilityTimeStorage.TimeStorage ts = (CapabilityTimeStorage.TimeStorage) cap;
                            ts.updateClient(message.updateSpeed, message.passiveMillisGenPerTick, message.newTime);
                        }
                        cap.setTime(message.newTime);
                    }
                }
        );

        return null;
    }
}
