package lumaceon.mods.aeonicraft.util;

import lumaceon.mods.aeonicraft.Aeonicraft;
import lumaceon.mods.aeonicraft.lib.Particles;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

public class ParticleHelper
{
    private static Random r = new Random();

    public static void spawnTemporalBurstParticles(Vec3d center, Vec3d spread, int numberOfWisps)
    {
        for(int i = 0; i < numberOfWisps; i++)
        {
            Aeonicraft.proxy.spawnParticle(
                    Particles.TEMPORAL_WISP,
                    center.x - spread.x / 2.0 + r.nextFloat() * spread.x,
                    center.y - spread.y / 2.0 + r.nextFloat() * spread.y,
                    center.z - spread.z / 2.0 + r.nextFloat() * spread.z
            );
        }
        Aeonicraft.proxy.spawnParticle(Particles.TEMPORAL_EXPLOSION, center.x, center.y, center.z);
    }
}
