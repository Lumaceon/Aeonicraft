package lumaceon.mods.aeonicraft.network.message;

import io.netty.buffer.ByteBuf;
import lumaceon.mods.aeonicraft.capability.timestorage.CapabilityTimeStorage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MessagePlayerTCUpdate implements IMessage
{
    public long newTime = 0;
    public long passiveMillisGenPerTick = 0;
    public CapabilityTimeStorage.TimeStorage.UpdateSpeed updateSpeed = CapabilityTimeStorage.TimeStorage.UpdateSpeed.SLOW;

    public MessagePlayerTCUpdate() {}

    public MessagePlayerTCUpdate(long newTime, long passiveMillisGenPerTick, CapabilityTimeStorage.TimeStorage.UpdateSpeed updateSpeed)
    {
        this.newTime = newTime;
        this.passiveMillisGenPerTick = passiveMillisGenPerTick;
        this.updateSpeed = updateSpeed;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(this.newTime);
        buf.writeLong(this.passiveMillisGenPerTick);
        buf.writeShort(this.updateSpeed.ordinal());
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.newTime = buf.readLong();
        this.passiveMillisGenPerTick = buf.readLong();
        this.updateSpeed = CapabilityTimeStorage.TimeStorage.UpdateSpeed.values()[buf.readShort()];
    }
}
