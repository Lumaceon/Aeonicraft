package lumaceon.mods.aeonicraft.proxy;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public interface IProxy
{
    void preInit();

    World getClientWorld();
    EntityPlayer getClientPlayer();
    void registerBlockModel(Block block, String unlocalizedName);
    void registerItemModel(Item item, String unlocalizedName);
    IThreadListener getThreadListener(MessageContext context);
}
