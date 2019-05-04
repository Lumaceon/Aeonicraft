package lumaceon.mods.aeonicraft.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MessageHourglassRequestTCUpdate implements IMessage
{
    public int stackIndex;

    public MessageHourglassRequestTCUpdate() { this.stackIndex = -1; }

    public MessageHourglassRequestTCUpdate(int stackIndex)
    {
        this.stackIndex = stackIndex;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(stackIndex);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.stackIndex = buf.readInt();
    }
}
