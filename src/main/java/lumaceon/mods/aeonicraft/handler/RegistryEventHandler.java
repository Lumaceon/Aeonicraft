package lumaceon.mods.aeonicraft.handler;

import lumaceon.mods.aeonicraft.Aeonicraft;
import lumaceon.mods.aeonicraft.api.AeonicraftAPIRegistry;
import lumaceon.mods.aeonicraft.api.HourglassUnlocks;
import lumaceon.mods.aeonicraft.api.hourglass.HourglassUnlockable;
import lumaceon.mods.aeonicraft.api.hourglass.HourglassUnlockableCategory;
import lumaceon.mods.aeonicraft.api.hourglass.IHourglassFunction;
import lumaceon.mods.aeonicraft.api.util.Icon;
import lumaceon.mods.aeonicraft.block.BlockHourglassProgrammer;
import lumaceon.mods.aeonicraft.block.BlockTemporalCompressor;
import lumaceon.mods.aeonicraft.block.BlockTemporalConnectionAmplifier;
import lumaceon.mods.aeonicraft.client.model.AeonicraftModelLoader;
import lumaceon.mods.aeonicraft.entity.EntityTravelGhost;
import lumaceon.mods.aeonicraft.lib.Textures;
import lumaceon.mods.aeonicraft.registry.ModItems;
import lumaceon.mods.aeonicraft.registry.ModSounds;
import lumaceon.mods.aeonicraft.item.ItemAeonicraft;
import lumaceon.mods.aeonicraft.item.ItemHourglassFunction;
import lumaceon.mods.aeonicraft.item.ItemTemporalHourglass;
import lumaceon.mods.aeonicraft.tile.TileHourglassProgrammer;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.Objects;

@Mod.EventBusSubscriber(modid = Aeonicraft.MOD_ID)
public class RegistryEventHandler
{
    private static final ArrayList<Block> BLOCKS = new ArrayList<>();
    private static Block block(Block block, RegistryEvent.Register<Block> event) {
        event.getRegistry().register(block);
        BLOCKS.add(block);
        return block;
    }

    private static final ArrayList<Item> ITEMS = new ArrayList<>();
    private static Item item(Item item, RegistryEvent.Register<Item> event) {
        event.getRegistry().register(item);
        ITEMS.add(item);
        return item;
    }

    private static HourglassUnlockableCategory hgULCat(HourglassUnlockableCategory cat, RegistryEvent.Register<HourglassUnlockableCategory> event) {
        event.getRegistry().register(cat);
        return cat;
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event)
    {
        block(new BlockTemporalCompressor(Material.IRON, "temporal_compressor"), event);
        block(new BlockTemporalConnectionAmplifier(Material.IRON, "temporal_connection_amplifier"), event);
        // etc...

        Block temp = block(new BlockHourglassProgrammer(Material.IRON, "hourglass_programmer"), event);
        GameRegistry.registerTileEntity(TileHourglassProgrammer.class, Objects.requireNonNull(temp.getRegistryName()));
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event)
    {
        Item temp;

        item(new ItemTemporalHourglass(1, 10000, "temporal_hourglass"), event);

        // Ore Dictionary Items.
        temp = item(new ItemAeonicraft(64, 100, "ingot_temporal"), event);
        OreDictionary.registerOre("ingot_temporal", temp);
        temp = item(new ItemAeonicraft(64, 100, "ingot_brass"), event);
        OreDictionary.registerOre("ingot_brass", temp);

        // Hourglass Functions.
        temp = item(new ItemHourglassFunction(1, 10000, "hourglass_function_excavator","item/hourglass_function_excavator"), event);
        AeonicraftAPIRegistry.registerHourglassFunction((IHourglassFunction) temp);
        temp = item(new ItemHourglassFunction(1, 10000, "hourglass_function_fish","item/hourglass_function_fish"), event);
        AeonicraftAPIRegistry.registerHourglassFunction((IHourglassFunction) temp);
        temp = item(new ItemHourglassFunction(1, 10000, "hourglass_function_animal","item/hourglass_function_animal"), event);
        AeonicraftAPIRegistry.registerHourglassFunction((IHourglassFunction) temp);
        temp = item(new ItemHourglassFunction(1, 10000, "hourglass_function_general","item/hourglass_function_general"), event);
        AeonicraftAPIRegistry.registerHourglassFunction((IHourglassFunction) temp);
        temp = item(new ItemHourglassFunction(1, 10000, "hourglass_function_travel","item/hourglass_function_travel")
                    {
                        @Override
                        public ActionResult<ItemStack> onHourglassRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
                            if(!worldIn.isRemote) {
                                worldIn.spawnEntity(new EntityTravelGhost(worldIn, playerIn));
                            }
                            return super.onHourglassRightClick(worldIn, playerIn, handIn);
                        }
                    }, event
        );
        AeonicraftAPIRegistry.registerHourglassFunction((IHourglassFunction) temp);


        // Register simple default ItemBlock for every registered block.
        for(Block block : BLOCKS)
        {
            event.getRegistry().register(new ItemBlock(block).setRegistryName(Objects.requireNonNull(block.getRegistryName())));
        }
    }

    @SubscribeEvent
    public static void registerSounds(RegistryEvent.Register<SoundEvent> event)
    {
        event.getRegistry().register(ModSounds.create(new ResourceLocation(Aeonicraft.MOD_ID, "time_ding_short")));
        event.getRegistry().register(ModSounds.create(new ResourceLocation(Aeonicraft.MOD_ID, "time_ding_medium")));
        event.getRegistry().register(ModSounds.create(new ResourceLocation(Aeonicraft.MOD_ID, "time_ding_long")));
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void registerHourglassUnlockableCategories(RegistryEvent.Register<HourglassUnlockableCategory> event)
    {
        HourglassUnlocks.categoryProgression = hgULCat(new HourglassUnlockableCategory(
                        new ResourceLocation(Aeonicraft.MOD_ID, "progression"),
                        new Icon(Textures.STACK_HOURGLASS),
                        Aeonicraft.MOD_ID + ":" + "category_des.progression",
                        new ResourceLocation(Aeonicraft.MOD_ID, "textures/gui/hgcat/progression.png"),
                        32, 48
                ), event);
        HourglassUnlocks.categoryHourglassFunction = hgULCat(new HourglassUnlockableCategory(
                        new ResourceLocation(Aeonicraft.MOD_ID, "hourglass_functions"),
                        new Icon(Textures.STACK_HOURGLASS_FUNC_EXCAVATE),
                        Aeonicraft.MOD_ID + ":" + "category_des.hourglass_functions",
                        new ResourceLocation(Aeonicraft.MOD_ID, "textures/gui/hgcat/hourglass_functions.png"),
                        32, 32
                ), event);
        HourglassUnlocks.categoryTemporalMachination = hgULCat(new HourglassUnlockableCategory(
                        new ResourceLocation(Aeonicraft.MOD_ID, "temporal_machination"),
                        new Icon(Textures.STACK_COMPRESSOR),
                        Aeonicraft.MOD_ID + ":" + "category_des.temporal_machination",
                        new ResourceLocation(Aeonicraft.MOD_ID, "textures/gui/hgcat/temporal_machination.png"),
                        32, 32
                ), event);
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void registerHourglassUnlockables(RegistryEvent.Register<HourglassUnlockable> event)
    {
        Aeonicraft.logger.info(event.getName());
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event)
    {
        // register custom model loaders which will load custom IModels
        ModelLoaderRegistry.registerLoader(new AeonicraftModelLoader());


        for(Block block : BLOCKS)
        {
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(Objects.requireNonNull(block.getRegistryName()), "inventory"));
        }

        for(Item item : ITEMS)
        {
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(Objects.requireNonNull(item.getRegistryName()), "inventory"));
        }
    }
}