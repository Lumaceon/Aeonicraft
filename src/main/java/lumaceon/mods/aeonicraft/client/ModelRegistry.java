package lumaceon.mods.aeonicraft.client;

import lumaceon.mods.aeonicraft.Aeonicraft;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

public class ModelRegistry
{
    public static void registerItemBlockModel(Block block, String blockUnlocalizedName) {
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(block), 0, new ModelResourceLocation(Aeonicraft.MOD_ID + ":" + blockUnlocalizedName, "inventory"));
    }

    public static void registerItemBlockCustomModel(Block block, String blockUnlocalizedName) {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(Aeonicraft.MOD_ID.toLowerCase() + ":" + blockUnlocalizedName, "inventory"));
    }

    public static void registerItemModel(Item item, String unlocalizedName) {
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, new ModelResourceLocation(Aeonicraft.MOD_ID + ":" + unlocalizedName, "inventory"));
    }
}
