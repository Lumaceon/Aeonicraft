package lumaceon.mods.aeonicraft.network.message;

import io.netty.buffer.ByteBuf;
import lumaceon.mods.aeonicraft.api.AeonicraftAPIRegistry;
import lumaceon.mods.aeonicraft.api.hourglass.IHourglassFunction;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MessageHourglassFunctionChange implements IMessage
{
    public IHourglassFunction targetFunction;

    public MessageHourglassFunctionChange() {
        targetFunction = null;
    }

    public MessageHourglassFunctionChange(IHourglassFunction targetFunction)
    {
        this.targetFunction = targetFunction;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        if(targetFunction != null)
            ByteBufUtils.writeUTF8String(buf, targetFunction.getRegistryIDString());
        else
            ByteBufUtils.writeUTF8String(buf, "");
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        String s = ByteBufUtils.readUTF8String(buf);
        if(s.isEmpty())
            targetFunction = null;
        else
            targetFunction = AeonicraftAPIRegistry.getHourglassFunction(s);
    }
}
