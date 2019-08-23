package lumaceon.mods.aeonicraft.proxy;

import lumaceon.mods.aeonicraft.Aeonicraft;
import lumaceon.mods.aeonicraft.client.ModelRegistry;
import lumaceon.mods.aeonicraft.client.gui.GuiHandler;
import lumaceon.mods.aeonicraft.client.init.ModHourglassGuiTabs;
import lumaceon.mods.aeonicraft.client.particle.ModParticles;
import lumaceon.mods.aeonicraft.client.particle.ParticleHourglassExplosion;
import lumaceon.mods.aeonicraft.client.particle.ParticleTemporalWisp;
import lumaceon.mods.aeonicraft.client.tesr.TESRTemporalCompressor;
import lumaceon.mods.aeonicraft.lib.Particles;
import lumaceon.mods.aeonicraft.registry.ModBlocks;
import lumaceon.mods.aeonicraft.tile.TileTemporalCompressor;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.World;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ClientProxy extends CommonProxy
{
    @Override
    public void preInit() {
        OBJLoader.INSTANCE.addDomain(Aeonicraft.MOD_ID);
        new GuiHandler();
        ModHourglassGuiTabs.init();
    }

    @Override
    public World getClientWorld() {
        return Minecraft.getMinecraft().world;
    }

    @Override
    public EntityPlayer getClientPlayer() {
        return Minecraft.getMinecraft().player;
    }

    @Override
    public void registerBlockModel(Block block, String unlocalizedName) {
        ModelRegistry.registerItemBlockModel(block, unlocalizedName);

        if(block.equals(ModBlocks.temporal_compressor))
        {
            ClientRegistry.bindTileEntitySpecialRenderer(TileTemporalCompressor.class, new TESRTemporalCompressor());
        }
    }

    @Override
    public void registerItemModel(Item item, String unlocalizedName) {
        ModelRegistry.registerItemModel(item, unlocalizedName);
    }

    @Override
    public IThreadListener getThreadListener(MessageContext context) {
        if(context.side.isClient())
        {
            return Minecraft.getMinecraft();
        }
        else if(context.side.isServer())
        {
            return context.getServerHandler().player.mcServer;
        }
        return null;
    }

    @Override
    public void spawnParticle(Particles particleToSpawn, double x, double y, double z)
    {
        switch(particleToSpawn)
        {
            case TEMPORAL_WISP:
            {
                if(ModParticles.canSpawnParticle(x, y, z, 24))
                {
                    ModParticles.addParticle(new ParticleTemporalWisp(Minecraft.getMinecraft().world, x, y, z));
                }
                break;
            }
            case TEMPORAL_EXPLOSION:
            {
                if(ModParticles.canSpawnParticle(x, y, z, 32))
                {
                    ModParticles.addParticle(new ParticleHourglassExplosion(Minecraft.getMinecraft().world, x, y, z));
                }
                break;
            }
        }
    }
}
