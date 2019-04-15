package lumaceon.mods.aeonicraft.network;

import lumaceon.mods.aeonicraft.Aeonicraft;
import lumaceon.mods.aeonicraft.network.message.MessageHourglassTCUpdate;
import lumaceon.mods.aeonicraft.network.message.handler.HandlerHourglassTCUpdate;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler
{
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Aeonicraft.MOD_ID);
    private static int messageID = 1;

    public static void init()
    {
        //Note: the side passed in is the RECEIVING side.
        registerMessage(HandlerHourglassTCUpdate.class, MessageHourglassTCUpdate.class, Side.CLIENT);
    }

    private static <REQ extends IMessage, REPLY extends IMessage> void registerMessage(Class<? extends IMessageHandler<REQ, REPLY>> messageHandler, Class<REQ> requestMessageType, Side side) {
        INSTANCE.registerMessage(messageHandler, requestMessageType, messageID++, side);
    }
}
