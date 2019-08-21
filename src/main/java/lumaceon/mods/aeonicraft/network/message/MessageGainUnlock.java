package lumaceon.mods.aeonicraft.network.message;

import io.netty.buffer.ByteBuf;
import lumaceon.mods.aeonicraft.api.hourglass.HourglassUnlockable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class MessageGainUnlock implements IMessage
{
    public HourglassUnlockable unlockable;

    public MessageGainUnlock() {}

    public MessageGainUnlock(HourglassUnlockable unlockableToUnlock) {
        this.unlockable = unlockableToUnlock;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, unlockable.getRegistryName().getResourceDomain());
        ByteBufUtils.writeUTF8String(buf, unlockable.getRegistryName().getResourcePath());
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.unlockable = GameRegistry.findRegistry(HourglassUnlockable.class).getValue(
                new ResourceLocation(ByteBufUtils.readUTF8String(buf), ByteBufUtils.readUTF8String(buf))
        );
    }
}
