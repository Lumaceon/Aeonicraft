package com.lumaceon.aeonicraft;

import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = Aeonicraft.MODID, name = Aeonicraft.NAME, version = Aeonicraft.VERSION)
public class Aeonicraft
{
    public static final String MODID = "aeonicraft"; //Also in resources/mcmod.info
    public static final String NAME = "Aeonicraft"; //Also in resources/mcmod.info
    public static final String VERSION = "1.0a"; //Also in build.gradle

    private static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        // some example code
        logger.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }
}
