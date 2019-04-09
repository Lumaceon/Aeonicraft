package lumaceon.mods.aeonicraft.proxy;

import lumaceon.mods.aeonicraft.client.ModelRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class ClientProxy extends CommonProxy
{
    @Override
    public void registerBlockModel(Block block, String unlocalizedName) {
        ModelRegistry.registerItemBlockModel(block, unlocalizedName);
    }

    @Override
    public void registerItemModel(Item item, String unlocalizedName) {
        ModelRegistry.registerItemModel(item, unlocalizedName);
    }
}
