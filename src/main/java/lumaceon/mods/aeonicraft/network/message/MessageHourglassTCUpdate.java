package lumaceon.mods.aeonicraft.network.message;

import io.netty.buffer.ByteBuf;
import lumaceon.mods.aeonicraft.temporalcompression.TemporalCompressor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.ArrayList;

public class MessageHourglassTCUpdate implements IMessage
{
    public TemporalCompressor[] tcs;
    public int targetInventorySlotOfHourglass = 0;

    public MessageHourglassTCUpdate()
    {
        tcs = new TemporalCompressor[0];
    }

    public MessageHourglassTCUpdate(TemporalCompressor[] tcs, int targetInventorySlotOfHourglass)
    {
        this.tcs = tcs;
        this.targetInventorySlotOfHourglass = targetInventorySlotOfHourglass;
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        NBTTagCompound nbt = new NBTTagCompound();
        NBTTagList list = new NBTTagList();
        for(TemporalCompressor t : tcs)
        {
            NBTTagCompound tag = new NBTTagCompound();
            t.writeToNBT(tag);
            list.appendTag(tag);
        }
        nbt.setTag("data", list);
        ByteBufUtils.writeTag(buf, nbt);
        buf.writeInt(targetInventorySlotOfHourglass);
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        NBTTagCompound nbt = ByteBufUtils.readTag(buf);
        if(nbt != null)
        {
            ArrayList<TemporalCompressor> tcs = new ArrayList<>();
            NBTTagList list = nbt.getTagList("data", Constants.NBT.TAG_COMPOUND);
            for(int i = 0; i < list.tagCount(); i++)
            {
                tcs.add(new TemporalCompressor(list.getCompoundTagAt(i)));
            }
            this.tcs = tcs.toArray(new TemporalCompressor[0]);
        }
        this.targetInventorySlotOfHourglass = buf.readInt();
    }
}
