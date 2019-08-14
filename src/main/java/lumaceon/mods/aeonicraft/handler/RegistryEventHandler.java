package lumaceon.mods.aeonicraft.handler;

import lumaceon.mods.aeonicraft.Aeonicraft;
import lumaceon.mods.aeonicraft.api.AeonicraftAPIRegistry;
import lumaceon.mods.aeonicraft.api.hourglass.IHourglassFunction;
import lumaceon.mods.aeonicraft.client.model.AeonicraftModelLoader;
import lumaceon.mods.aeonicraft.init.ModBlocks;
import lumaceon.mods.aeonicraft.init.ModItems;
import lumaceon.mods.aeonicraft.init.ModSounds;
import lumaceon.mods.aeonicraft.tile.TileHourglassProgrammer;
import lumaceon.mods.aeonicraft.util.IOreDict;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = Aeonicraft.MOD_ID)
public class RegistryEventHandler
{
    @SubscribeEvent
    public static void registerSounds(RegistryEvent.Register<SoundEvent> event)
    {
        event.getRegistry().register(ModSounds.create(new ResourceLocation(Aeonicraft.MOD_ID, "time_ding_short")));
        event.getRegistry().register(ModSounds.create(new ResourceLocation(Aeonicraft.MOD_ID, "time_ding_medium")));
        event.getRegistry().register(ModSounds.create(new ResourceLocation(Aeonicraft.MOD_ID, "time_ding_long")));
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event)
    {
        event.getRegistry().registerAll(ModBlocks.getBlocks().toArray(new Block[0]));
        GameRegistry.registerTileEntity(TileHourglassProgrammer.class, Objects.requireNonNull(ModBlocks.hourglassProgrammer.getRegistryName()));
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().registerAll( ModItems.getItems().toArray(new Item[0]));

        for(Item item : ModItems.getItems())
        {
            if(item instanceof IOreDict)
            {
                OreDictionary.registerOre(((IOreDict) item).getOreDictionaryString(), item);
            }

            if(item instanceof IHourglassFunction)
            {
                AeonicraftAPIRegistry.registerHourglassFunction((IHourglassFunction) item);
            }
        }

        for(Block block : ModBlocks.getBlocks())
        {
            event.getRegistry().register(new ItemBlock(block).setRegistryName(Objects.requireNonNull(block.getRegistryName())));
        }
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event)
    {
        // register custom model loaders which will load custom IModels
        ModelLoaderRegistry.registerLoader(new AeonicraftModelLoader());


        for(Block block : ModBlocks.getBlocks())
        {
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(Objects.requireNonNull(block.getRegistryName()), "inventory"));
        }

        for(Item item : ModItems.getItems())
        {
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(Objects.requireNonNull(item.getRegistryName()), "inventory"));
        }
    }
}