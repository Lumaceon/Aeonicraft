package lumaceon.mods.aeonicraft.client.handler;

import lumaceon.mods.aeonicraft.Aeonicraft;
import lumaceon.mods.aeonicraft.client.particle.ModParticles;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

@Mod.EventBusSubscriber
public class RenderHandler
{
    private static Minecraft MC = Minecraft.getMinecraft();
    private static ResourceLocation MOD_PARTICLES = new ResourceLocation(Aeonicraft.MOD_ID, "textures/particles.png");

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onRenderWorld(RenderWorldLastEvent event)
    {
        Entity renderViewEntity = MC.getRenderViewEntity();

        // Render custom mod particles...
        if(renderViewEntity != null)
        {
            ArrayList<Particle> particleList = ModParticles.getParticleList();

            if(!particleList.isEmpty())
            {
                GL11.glPushMatrix();
                GL11.glPushAttrib(0);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glDisable(GL11.GL_LIGHTING);
                Minecraft.getMinecraft().renderEngine.bindTexture(MOD_PARTICLES);

                GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
                GlStateManager.color(1.0F, 1.0F, 1.0F, 0.8F);

                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder bufferbuilder = tessellator.getBuffer();
                bufferbuilder.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
                for(int n = 0; n < particleList.size(); n++)
                {
                    Particle fx = particleList.get(n);
                    if(fx == null || !fx.isAlive())
                    {
                        particleList.remove(n);
                        --n;
                    }
                    else
                    {
                        fx.renderParticle(bufferbuilder, renderViewEntity, event.getPartialTicks(), ActiveRenderInfo.getRotationX(), ActiveRenderInfo.getRotationXZ(), ActiveRenderInfo.getRotationZ(), ActiveRenderInfo.getRotationYZ(), ActiveRenderInfo.getRotationXY());
                    }
                }
                tessellator.draw();
                GL11.glPopAttrib();
                GL11.glPopMatrix();
            }
        }
    }
}
