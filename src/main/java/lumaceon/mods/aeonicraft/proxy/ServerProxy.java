package lumaceon.mods.aeonicraft.proxy;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ServerProxy extends CommonProxy
{
    @Override
    public void preInit() {}
    @Override
    public World getClientWorld() { return null; }
    @Override
    public EntityPlayer getClientPlayer() { return null; }
    @Override
    public void registerBlockModel(Block block, String unlocalizedName) {}
    @Override
    public void registerItemModel(Item item, String unlocalizedName) {}
    @Override
    public IThreadListener getThreadListener(MessageContext context) { return null; }
}
