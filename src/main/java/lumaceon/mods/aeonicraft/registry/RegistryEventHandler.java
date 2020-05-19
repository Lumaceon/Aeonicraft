package lumaceon.mods.aeonicraft.registry;

import lumaceon.mods.aeonicraft.Aeonicraft;
import lumaceon.mods.aeonicraft.api.HourglassUnlocks;
import lumaceon.mods.aeonicraft.api.hourglass.HourglassUnlockable;
import lumaceon.mods.aeonicraft.api.hourglass.HourglassUnlockableCategory;
import lumaceon.mods.aeonicraft.api.hourglass.HourglassFunction;
import lumaceon.mods.aeonicraft.api.util.Icon;
import lumaceon.mods.aeonicraft.block.BlockAeonPearl;
import lumaceon.mods.aeonicraft.block.BlockTemporalCompressor;
import lumaceon.mods.aeonicraft.client.model.AeonicraftModelLoader;
import lumaceon.mods.aeonicraft.entity.EntityTravelGhost;
import lumaceon.mods.aeonicraft.hourglassunlockable.HourglassUnlockableHGFunction;
import lumaceon.mods.aeonicraft.item.ItemAeonPearlSeeds;
import lumaceon.mods.aeonicraft.item.ItemTemporalCompressorComponent;
import lumaceon.mods.aeonicraft.lib.Textures;
import lumaceon.mods.aeonicraft.lib.TimeCosts;
import lumaceon.mods.aeonicraft.item.ItemAeonicraft;
import lumaceon.mods.aeonicraft.item.ItemTemporalHourglass;
import lumaceon.mods.aeonicraft.api.temporalcompression.TemporalCompressorComponent;
import lumaceon.mods.aeonicraft.temporalcompressor.TemporalCompressorComponentModifierLibrary;
import lumaceon.mods.aeonicraft.tile.TileAeonPearl;
import lumaceon.mods.aeonicraft.util.*;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
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
import java.util.List;
import java.util.Objects;

@Mod.EventBusSubscriber(modid = Aeonicraft.MOD_ID)
public class RegistryEventHandler
{
    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event)
    {
        Block temp;

        // Simple blocks
        //block(new BlockAeonicraft(Material.IRON, "block_temporal"), event);


        // Slightly-less simple blocks
        block(new BlockTemporalCompressor(Material.IRON, "temporal_compressor"), event);


        // Tile Entity Blocks
        temp = block(new BlockAeonPearl(Material.PLANTS, "aeon_pearl"), event);
        GameRegistry.registerTileEntity(TileAeonPearl.class, Objects.requireNonNull(temp.getRegistryName()));
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event)
    {
        Item temp;

        // Simple Items
        item(new ItemAeonicraft(64, 10000, "matter_temporal"), event);

        // Slightly-less Simple Items
        item(new ItemAeonPearlSeeds(64, 10000, "aeon_pearl_seeds"), event);
        item(new ItemTemporalHourglass(1, 10000, "temporal_hourglass"), event);

        //
        item(new ItemTemporalCompressorComponent(1, 10000, "gear_wood"), event);
        item(new ItemTemporalCompressorComponent(1, 10000, "gear_stone"), event);
        item(new ItemTemporalCompressorComponent(1, 10000, "gear_iron"), event);
        item(new ItemTemporalCompressorComponent(1, 10000, "gear_gold"), event);
        item(new ItemTemporalCompressorComponent(1, 10000, "gear_diamond"), event);

        // Ore Dictionary Items.
        temp = item(new ItemAeonicraft(64, 100, "ingot_temporal"), event);
        OreDictionary.registerOre("ingot_temporal", temp);
        temp = item(new ItemAeonicraft(64, 100, "ingot_brass"), event);
        OreDictionary.registerOre("ingot_brass", temp);


        // Register simple default ItemBlock for every registered block.
        for(Block block : BLOCKS)
        {
            event.getRegistry().register(new ItemBlock(block).setRegistryName(Objects.requireNonNull(block.getRegistryName())));
        }
    }

    public static void registerSmeltingRecipes()  // Called during mod init phase.
    {
        GameRegistry.addSmelting(ModItems.matter_temporal, new ItemStack(ModItems.ingot_temporal), 1.0F);
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
                        new Icon(new ItemStack(Items.CLOCK)),
                        Aeonicraft.MOD_ID + ":" + "category_des.progression",
                        new ResourceLocation(Aeonicraft.MOD_ID, "textures/gui/hgcat/progression.png"),
                        32, 48
                ), event);
        HourglassUnlocks.categoryHourglassFunction = hgULCat(new HourglassUnlockableCategory(
                        new ResourceLocation(Aeonicraft.MOD_ID, "hourglass_functions"),
                        new Icon(Textures.STACK_HOURGLASS),
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
        // Hourglass Functions
        hgUL(new HourglassUnlockableHGFunction(new ResourceLocation(Aeonicraft.MOD_ID, "hgf_excavation_overclocker"),10, 10), event);
        hgUL(new HourglassUnlockableHGFunction(new ResourceLocation(Aeonicraft.MOD_ID, "hgf_aquatic_lure_overclocker"), 5, 5), event);
        hgUL(new HourglassUnlockableHGFunction(new ResourceLocation(Aeonicraft.MOD_ID, "hgf_livestock_overclocker"), 5, 5), event);
        hgUL(new HourglassUnlockableHGFunction(new ResourceLocation(Aeonicraft.MOD_ID, "hgf_proxy_traveller"), 15, 15), event);
        hgUL(new HourglassUnlockableHGFunction(new ResourceLocation(Aeonicraft.MOD_ID, "hgf_endattractor"), 50, 20), event);

        // Misc

    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void registerHourglassFunctions(RegistryEvent.Register<HourglassFunction> event)
    {
        hgFunc(new HourglassFunction(new ResourceLocation(Aeonicraft.MOD_ID, "hgf_excavation_overclocker")), event);
        hgFunc(new HourglassFunction(new ResourceLocation(Aeonicraft.MOD_ID, "hgf_aquatic_lure_overclocker")), event);
        hgFunc(new HourglassFunction(new ResourceLocation(Aeonicraft.MOD_ID, "hgf_livestock_overclocker")), event);
        hgFunc(new HourglassFunction(new ResourceLocation(Aeonicraft.MOD_ID, "hgf_proxy_traveller"))
        {
            @Override
            public ActionResult<ItemStack> onHourglassRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
                if(!worldIn.isRemote) {
                    if(TimeHelper.getTime(playerIn) >= TimeCosts.TRAVEL_GHOST) {
                        TimeHelper.consumeTime(playerIn, TimeCosts.TRAVEL_GHOST);
                        worldIn.spawnEntity(new EntityTravelGhost(worldIn, playerIn));
                    }
                }
                return super.onHourglassRightClick(worldIn, playerIn, handIn);
            }
        }, event);
        hgFunc(new HourglassFunction(new ResourceLocation(Aeonicraft.MOD_ID, "hgf_endattractor"))
        {
            @Override
            public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected)
            {
                if(!world.isRemote)
                {
                    if(world.getTotalWorldTime() % 10 == 0)
                    {
                        if(TimeHelper.getTime(entity) >= TimeCosts.ENDATTRACTOR_SPAWN)
                        {
                            List<EntityEnderman> enders = world.getEntities(EntityEnderman.class, input -> input != null && input.getDistance(entity) < 32F);
                            if(enders.size() < 3)
                            {
                                EntityEnderman newEntity = new EntityEnderman(world);
                                if(SpawnHelper.spawnEntityNearby(newEntity, world, entity.getPosition()))
                                {
                                    TimeHelper.consumeTime(entity, TimeCosts.ENDATTRACTOR_SPAWN);
                                    if(entity instanceof EntityPlayer)
                                    {
                                        ParticleHelper.spawnTemporalBurstParticles(new Vec3d(newEntity.posX, newEntity.posY + newEntity.height*0.5, newEntity.posZ), new Vec3d(1.5, newEntity.height, 1.5), 75);
                                        SoundHelper.playMediumTimeDing(null, world, newEntity.posX, newEntity.posY, newEntity.posZ);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }, event);
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void registerTemporalCompressorComponent(RegistryEvent.Register<TemporalCompressorComponent> event)
    {
        tCompComponent(new TemporalCompressorComponent(new ResourceLocation(Aeonicraft.MOD_ID, "gear_wood"),1).makeModifable(), event);
        tCompComponent(new TemporalCompressorComponent(new ResourceLocation(Aeonicraft.MOD_ID, "gear_stone"),10), event);
        tCompComponent(new TemporalCompressorComponent(new ResourceLocation(Aeonicraft.MOD_ID, "gear_iron"),100), event);
        tCompComponent(new TemporalCompressorComponent(new ResourceLocation(Aeonicraft.MOD_ID, "gear_gold"),1000), event);
        tCompComponent(new TemporalCompressorComponent(new ResourceLocation(Aeonicraft.MOD_ID, "gear_diamond"),0).addTCModifier(TemporalCompressorComponentModifierLibrary.plusFiveModified), event);
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


    // --------- CONVENIENCE METHODS --------- \\
    // (and also convenience lists I guess...) \\

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

    private static HourglassUnlockable hgUL(HourglassUnlockable unlockable, RegistryEvent.Register<HourglassUnlockable> event) {
        event.getRegistry().register(unlockable);
        return unlockable;
    }

    private static HourglassFunction hgFunc(HourglassFunction func, RegistryEvent.Register<HourglassFunction> event) {
        event.getRegistry().register(func);
        return func;
    }

    private static TemporalCompressorComponent tCompComponent(TemporalCompressorComponent component, RegistryEvent.Register<TemporalCompressorComponent> event) {
        event.getRegistry().register(component);
        return component;
    }
}