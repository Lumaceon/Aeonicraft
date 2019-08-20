package lumaceon.mods.aeonicraft.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MessageHourglassFunctionChange implements IMessage
{
    public int relativeShift = 0;

    public MessageHourglassFunctionChange() {}

    public MessageHourglassFunctionChange(int relativeShift)
    {
        this.relativeShift = relativeShift;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(relativeShift);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.relativeShift = buf.readInt();
    }
}
