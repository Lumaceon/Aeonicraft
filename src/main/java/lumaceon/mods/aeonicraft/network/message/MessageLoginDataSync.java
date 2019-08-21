package lumaceon.mods.aeonicraft.network.message;

import io.netty.buffer.ByteBuf;
import lumaceon.mods.aeonicraft.capability.CapabilityAeonicraftProgression;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MessageLoginDataSync implements IMessage
{
    public NBTTagCompound progressionHandlerNBT;

    public MessageLoginDataSync() {}

    public MessageLoginDataSync(CapabilityAeonicraftProgression.IAeonicraftProgressionHandler progressionHandler) {
        this.progressionHandlerNBT = progressionHandler.saveToNBT();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeTag(buf, this.progressionHandlerNBT);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        progressionHandlerNBT = ByteBufUtils.readTag(buf);
    }
}
