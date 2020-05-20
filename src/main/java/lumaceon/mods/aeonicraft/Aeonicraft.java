package lumaceon.mods.aeonicraft;

import lumaceon.mods.aeonicraft.api.hourglass.HourglassFunction;
import lumaceon.mods.aeonicraft.api.hourglass.HourglassUnlockable;
import lumaceon.mods.aeonicraft.api.temporalcompression.TemporalCompressorComponent;
import lumaceon.mods.aeonicraft.block.CustomProperties;
import lumaceon.mods.aeonicraft.compat.ModCompatProxyRegistry;
import lumaceon.mods.aeonicraft.init.APIFunctionInitialization;
import lumaceon.mods.aeonicraft.init.ModCapabilities;
import lumaceon.mods.aeonicraft.init.ModEntities;
import lumaceon.mods.aeonicraft.item.ItemTemporalCompressorComponent;
import lumaceon.mods.aeonicraft.registry.ModItems;
import lumaceon.mods.aeonicraft.network.PacketHandler;
import lumaceon.mods.aeonicraft.proxy.IProxy;
import lumaceon.mods.aeonicraft.registry.RegistryEventHandler;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.logging.log4j.Logger;

import java.util.Random;

@Mod(modid = Aeonicraft.MOD_ID, name = Aeonicraft.NAME, version = Aeonicraft.VERSION)//, dependencies = "required:betteradvancements;")
public class Aeonicraft
{
    public static final String MOD_ID = "aeonicraft"; //Also in resources/mcmod.info
    public static final String NAME = "Aeonicraft"; //Also in resources/mcmod.info
    public static final String VERSION = "1.0a"; //Also in build.gradle
    public static final String CLIENT_PROXY = "lumaceon.mods.aeonicraft.proxy.ClientProxy";
    public static final String SERVER_PROXY = "lumaceon.mods.aeonicraft.proxy.ServerProxy";

    @Mod.Instance(MOD_ID)
    public static Aeonicraft instance;

    @SidedProxy(clientSide = CLIENT_PROXY, serverSide = SERVER_PROXY)
    public static IProxy proxy;

    public static Logger logger;

    public static Random rng = new Random();

    private static ItemStack CREATIVE_TAB_DISPLAY_ITEMSTACK;
    public CreativeTabs CREATIVE_TAB = new CreativeTabs(NAME) {
        @SuppressWarnings("NullableProblems")
        @Override
        public ItemStack getTabIconItem() {
            if(CREATIVE_TAB_DISPLAY_ITEMSTACK == null)
                CREATIVE_TAB_DISPLAY_ITEMSTACK = new ItemStack(ModItems.ingot_temporal);
            return CREATIVE_TAB_DISPLAY_ITEMSTACK;
        }
    };


    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();

        APIFunctionInitialization.initFunctions();

        proxy.preInit();

        ModCapabilities.init();
        CustomProperties.init();

        //GameRegistry.registerWorldGenerator(oreGenerator, 0);
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        //ModBlocks.initModels();
        ModEntities.init();
        PacketHandler.init();
        RegistryEventHandler.registerSmeltingRecipes();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        ModCompatProxyRegistry.init();
        this.linkHourglassFunctionsToUnlockables();
    }

    private void linkHourglassFunctionsToUnlockables()
    {
        // Link hourglass functions to unlockables of the same registry name
        IForgeRegistry<HourglassUnlockable> registry = GameRegistry.findRegistry(HourglassUnlockable.class);
        HourglassUnlockable unlockable;
        for(HourglassFunction function : GameRegistry.findRegistry(HourglassFunction.class).getValuesCollection())
        {
            if((unlockable = registry.getValue(function.getRegistryName())) != null)
            {
                function.requiredUnlockable = unlockable;
            }
        }

        // Link temporal compressor components to Items of the same name
        IForgeRegistry<Item> itemRegistry = GameRegistry.findRegistry(Item.class);
        for(TemporalCompressorComponent component : GameRegistry.findRegistry(TemporalCompressorComponent.class))
        {
            Item item = itemRegistry.getValue(component.getRegistryName());
            if(item instanceof ItemTemporalCompressorComponent)
            {
                ((ItemTemporalCompressorComponent) item).setCompressorComponent(component);
                component.itemToRender = new ItemStack(item);
            }
        }
    }
}
