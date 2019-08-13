package lumaceon.mods.aeonicraft;

import lumaceon.mods.aeonicraft.compat.ModCompatProxyRegistry;
import lumaceon.mods.aeonicraft.init.ModBlocks;
import lumaceon.mods.aeonicraft.init.ModCapabilities;
import lumaceon.mods.aeonicraft.init.ModEntities;
import lumaceon.mods.aeonicraft.init.ModItems;
import lumaceon.mods.aeonicraft.network.PacketHandler;
import lumaceon.mods.aeonicraft.proxy.IProxy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

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

    private static ItemStack CREATIVE_TAB_DISPLAY_ITEMSTACK;
    public CreativeTabs CREATIVE_TAB = new CreativeTabs(NAME) {
        @SuppressWarnings("NullableProblems")
        @Override
        public ItemStack getTabIconItem() {
            if(CREATIVE_TAB_DISPLAY_ITEMSTACK == null)
                CREATIVE_TAB_DISPLAY_ITEMSTACK = new ItemStack(ModItems.ingotTemporal);
            return CREATIVE_TAB_DISPLAY_ITEMSTACK;
        }
    };


    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();

        proxy.preInit();


        ModBlocks.initTE();
        ModCapabilities.init();

        //GameRegistry.registerWorldGenerator(oreGenerator, 0);
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        //ModBlocks.initModels();
        ModEntities.init();
        PacketHandler.init();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        ModCompatProxyRegistry.init();
    }
}
