package lumaceon.mods.aeonicraft.proxy;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

public interface IProxy
{
    void registerBlockModel(Block block, String unlocalizedName);
    void registerItemModel(Item item, String unlocalizedName);
}
