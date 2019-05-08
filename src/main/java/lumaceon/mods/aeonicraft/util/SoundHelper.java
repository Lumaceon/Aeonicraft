package lumaceon.mods.aeonicraft.util;

import lumaceon.mods.aeonicraft.init.ModSounds;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

import java.util.Random;

public class SoundHelper
{
    private static float lastValueClient = -50.0F;
    private static float lastValueServer = -50.0F;
    private static final Scale IONIAN_MODE_SCALE = new Scale( new float[] { // standard major scale
            0.0F, 2.0F/12F, 4.0F/12F, 5.0F/12F, 7.0F/12F, 9.0F/12F, 11.0F/12F
    });
    private static final Scale DORIAN_MODE_SCALE = new Scale( new float[] {
            0.0F, 2.0F/12F, 3.0F/12F, 5.0F/12F, 7.0F/12F, 9.0F/12F, 10.0F/12F
    });
    private static final Scale LYDIAN_MODE_SCALE = new Scale( new float[] {
            0.0F, 2.0F/12F, 4.0F/12F, 6.0F/12F, 7.0F/12F, 9.0F/12F, 11.0F/12F
    });
    private static final Scale AEOLIAN_MODE_SCALE = new Scale( new float[] {
            0.0F, 2.0F/12F, 3.0F/12F, 5.0F/12F, 7.0F/12F, 8.0F/12F, 10.0F/12F
    });


    public static void playShortTimeDing(EntityPlayer player, World world, double x, double y, double z)
    {
        world.playSound(player, x, y, z, ModSounds.time_ding_short, SoundCategory.PLAYERS, 0.5F, randomNoteScaleShift(world.isRemote, 5000, LYDIAN_MODE_SCALE, AEOLIAN_MODE_SCALE, DORIAN_MODE_SCALE));
    }

    public static void playMediumTimeDing(EntityPlayer player, World world, double x, double y, double z)
    {
        world.playSound(player, x, y, z, ModSounds.time_ding_medium, SoundCategory.PLAYERS, 0.75F, world.rand.nextFloat() * 0.5F + 0.75F);
    }

    public static void playLongTimeDing(EntityPlayer player, World world, double x, double y, double z)
    {
        world.playSound(player, x, y, z, ModSounds.time_ding_long, SoundCategory.PLAYERS, 0.5F, world.rand.nextFloat() * 0.5F + 0.75F);
    }

    private static float randomNote(boolean isClient, Scale scale)
    {
        float ret = scale.randomNote();
        if(isClient)
        {
            while (ret == lastValueClient) {
                ret = scale.randomNote();
            }
            lastValueClient = ret;
        }
        else
        {
            while (ret == lastValueServer) {
                ret = scale.randomNote();
            }
            lastValueServer = ret;
        }
        return ret;
    }

    /**
     * Shifts between the given scales based on the current time.
     * @param scales Scales to shift between.
     * @return A random note from one of the scales.
     */
    private static float randomNoteScaleShift(boolean isClient, int scaleDuration, Scale... scales)
    {
        long time = System.currentTimeMillis();
        long cycleNumber = time / scaleDuration;
        return randomNote(isClient, scales[(int)(cycleNumber % scales.length)]);
    }

    private static class Scale
    {
        private static final Random r = new Random();
        float[] scale;

        private Scale(float[] scale) {
            this.scale = scale;
        }

        private float randomNote() {
            return scale[r.nextInt(scale.length)];
        }
    }
}
