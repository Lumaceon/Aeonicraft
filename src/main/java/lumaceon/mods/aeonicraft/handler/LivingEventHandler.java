package lumaceon.mods.aeonicraft.handler;

import lumaceon.mods.aeonicraft.Aeonicraft;
import lumaceon.mods.aeonicraft.api.hourglass.HourglassFunction;
import lumaceon.mods.aeonicraft.lib.TimeCosts;
import lumaceon.mods.aeonicraft.registry.ModHourglassFunctions;
import lumaceon.mods.aeonicraft.registry.ModItems;
import lumaceon.mods.aeonicraft.util.InventoryHelper;
import lumaceon.mods.aeonicraft.util.ParticleHelper;
import lumaceon.mods.aeonicraft.util.SoundHelper;
import lumaceon.mods.aeonicraft.util.TimeHelper;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.living.BabyEntitySpawnEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Random;

@Mod.EventBusSubscriber(modid = Aeonicraft.MOD_ID)
public class LivingEventHandler
{
    @SubscribeEvent
    public static void onBabyAnimalBreedEvent(BabyEntitySpawnEvent event)
    {
        EntityPlayer player = event.getCausedByPlayer();
        if(player != null && !player.world.isRemote)
        {
            ItemStack hourglass = InventoryHelper.getFirstStackOfTypeInInventory(player.inventory, ModItems.temporal_hourglass);
            assert hourglass != null;
            if(!hourglass.isEmpty())
            {
                HourglassFunction function = InventoryHelper.getHourglassFunctionFromHourglass(hourglass);
                if(function != null && function.equals(ModHourglassFunctions.hgf_livestock_overclocker))
                {
                    EntityAgeable target = event.getChild();
                    if(target != null && event.getParentA() instanceof EntityAnimal && event.getParentB() instanceof EntityAnimal)
                    {
                        if(TimeHelper.getTime(player).getVal() >= TimeCosts.AUTO_ADULT_BREED.getVal())
                        {
                            TimeHelper.consumeTime(player, TimeCosts.AUTO_ADULT_BREED);

                            // because the post-event logic resets the baby entity's age, we have to spawn manually...
                            event.setCanceled(true);

                            EntityAnimal parentA = (EntityAnimal) event.getParentA();
                            EntityAnimal parentB = (EntityAnimal) event.getParentB();

                            if(player instanceof EntityPlayerMP)
                            {
                                player.addStat(StatList.ANIMALS_BRED);
                                CriteriaTriggers.BRED_ANIMALS.trigger((EntityPlayerMP) player, parentA, parentB, target);
                            }

                            // Sadly, this is always overridden, even if the event is canceled.
                            // If it weren't, this would allow the animals to immediately begin breeding again.
                            /*parentA.resetInLove();
                            parentB.resetInLove();
                            parentA.setGrowingAge(0);
                            parentB.setGrowingAge(0);*/

                            // the relevant line to create an adult baby; or rather, a baby with a 1-tick childhood...
                            target.setGrowingAge(-1);

                            target.setLocationAndAngles(parentA.posX, parentA.posY, parentA.posZ, 0.0F, 0.0F);
                            parentA.world.spawnEntity(target);
                            Random r = parentA.getRNG();

                            for (int i = 0; i < 7; ++i)
                            {
                                double d0 = r.nextGaussian() * 0.02D;
                                double d1 = r.nextGaussian() * 0.02D;
                                double d2 = r.nextGaussian() * 0.02D;
                                double d3 = r.nextDouble() * parentA.width * 2.0D - (double)parentA.width;
                                double d4 = 0.5D + r.nextDouble() * (double)parentA.height;
                                double d5 = r.nextDouble() * parentA.width * 2.0D - (double)parentA.width;
                                parentA.world.spawnParticle(EnumParticleTypes.HEART, parentA.posX + d3, parentA.posY + d4, parentA.posZ + d5, d0, d1, d2);
                            }

                            if (parentA.world.getGameRules().getBoolean("doMobLoot"))
                            {
                                parentA.world.spawnEntity(new EntityXPOrb(parentA.world, parentA.posX, parentA.posY, parentA.posZ, r.nextInt(7) + 1));

                                SoundHelper.playLongTimeDing(player, target.world, target.posX, target.posY, target.posZ);
                                ParticleHelper.spawnTemporalBurstParticles(target.getPositionVector(), new Vec3d(1.5, 1.5, 1.5), 50);
                            }
                        }
                    }
                }
            }
        }
    }
}
