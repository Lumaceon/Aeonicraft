package lumaceon.mods.aeonicraft.handler;

import lumaceon.mods.aeonicraft.api.IHourglassFunction;
import lumaceon.mods.aeonicraft.capability.hourglass.CapabilityHourglass;
import lumaceon.mods.aeonicraft.entity.EntityTemporalFishHook;
import lumaceon.mods.aeonicraft.init.ModItems;
import lumaceon.mods.aeonicraft.item.ItemTemporalHourglass;
import lumaceon.mods.aeonicraft.util.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Random;

@Mod.EventBusSubscriber
public class PlayerEventHandler
{
    @SubscribeEvent
    public static void onBlockAboutToBeBroken(PlayerEvent.BreakSpeed event)
    {
        EntityPlayer player = event.getEntityPlayer();
        if(player != null)
        {
            ItemStack firstHourglass = InventoryHelper.getFirstStackOfTypeInInventory(player.inventory, ModItems.temporalHourglass);
            if(firstHourglass != null)
            {
                ItemTemporalHourglass hourglass = (ItemTemporalHourglass) firstHourglass.getItem();
                IHourglassFunction hourglassFunction = hourglass.getActiveHourglassFunction(firstHourglass);
                if(hourglassFunction != null && hourglassFunction.equals(ModItems.hgFuncExcavation))
                {
                    long timeToBreakBlock = TimeCosts.getTimeToBreakBlock(player.world, event.getPos(), event.getState(), player, player.inventory.getCurrentItem());
                    if(hourglass.availableTime(firstHourglass, player.world, player.world.isRemote ? Side.CLIENT : Side.SERVER) >= timeToBreakBlock)
                    {
                        int hourglassSlotIndex = -1;
                        for(int i = 0; i < player.inventory.getSizeInventory(); i++)
                        {
                            if(player.inventory.getStackInSlot(i) == firstHourglass)
                            {
                                hourglassSlotIndex = i;
                                break;
                            }
                        }
                        hourglass.consumeTime(player, firstHourglass, player.world, hourglassSlotIndex, timeToBreakBlock);
                        event.setNewSpeed(Float.MAX_VALUE);

                        SoundHelper.playShortTimeDing(player, player.world, player.posX, player.posY, player.posZ);
                        ParticleHelper.spawnTemporalBurstParticles(new Vec3d(event.getPos()).addVector(0.5, 0.5, 0.5), new Vec3d(1, 1, 1), 10);
                    }
                    else
                    {
                        event.setNewSpeed(0.0F);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerUpdate(TickEvent.PlayerTickEvent event)
    {
        EntityPlayer player = event.player;
        if(!player.world.isRemote && player.fishEntity != null)
        {
            ItemStack stack = InventoryHelper.getFirstStackOfTypeInInventory(player.inventory, ModItems.temporalHourglass);
            CapabilityHourglass.IHourglassHandler cap = stack.getCapability(CapabilityHourglass.HOURGLASS, null);
            if(cap != null)
            {
                IHourglassFunction func = cap.getActiveFunction();
                if(func != null && func.equals(ModItems.hgfuncFish) && !(player.fishEntity instanceof EntityTemporalFishHook))
                {
                    player.fishEntity.setDead();
                    EntityTemporalFishHook fishHook = new EntityTemporalFishHook(player.world, player);
                    fishHook.setPosition(player.fishEntity.posX, player.fishEntity.posY, player.fishEntity.posZ);
                    player.world.spawnEntity(fishHook);
                    player.fishEntity = fishHook;
                }
            }
        }
    }


    @SubscribeEvent
    public static void onPlayerInteractWithEntity(PlayerInteractEvent.EntityInteract event)
    {
        EntityPlayer player = event.getEntityPlayer();
        Entity target = event.getTarget();
        if(player != null && !player.world.isRemote && target != null)
        {
            if(target instanceof EntitySheep)
            {
                EntitySheep sheep = (EntitySheep) target;
                ItemStack stackInHand = player.inventory.getCurrentItem();
                ItemStack hourglass = InventoryHelper.getFirstStackOfTypeInInventory(player.inventory, ModItems.temporalHourglass);
                IHourglassFunction func = InventoryHelper.getHourglassFunctionFromHourglass(hourglass);
                BlockPos centralPosition = event.getPos();
                int foundGrass = 0;
                BlockPos firstGrass = null;
                for(int x = -2; x <= 2 && foundGrass < 2; x++)
                {
                    for(int z = -2; z <= 2 && foundGrass < 2; z++)
                    {
                        for(int y = 0; y > -3 && foundGrass < 2; y--)
                        {
                            IBlockState state = sheep.world.getBlockState(centralPosition.add(x, y, z));
                            if(state.getBlock().equals(Blocks.GRASS))
                            {
                                if(firstGrass == null)
                                    firstGrass = centralPosition.add(x, y, z);
                                foundGrass += 1;
                            }
                        }
                    }
                }
                if (
                        func != null
                        && func.equals(ModItems.hgFuncAnimal)
                        && hourglass.getItem() instanceof ItemTemporalHourglass
                        && sheep.isShearable(stackInHand, player.world, event.getPos())
                        && stackInHand.getItem() instanceof ItemShears
                        && foundGrass > 0
                )
                {

                    long availableTime = ((ItemTemporalHourglass) hourglass.getItem()).availableTime(hourglass, player.world, player.world.isRemote ? Side.CLIENT : Side.SERVER);

                    // do we have time to shear a sheep MY WAY...?
                    if(availableTime >= 20000)
                    {
                        long timeToConsume = 0;

                        // we time-shear a floofy sheep on grass...
                        shearDatSheep(sheep, stackInHand, event.getPos(), player);

                        // also cancel the event to avoid double-shearing...
                        event.setCanceled(true);
                        event.setCancellationResult(EnumActionResult.SUCCESS);

                        // auto-floof sheep...
                        timeToConsume += 20000;
                        sheep.setSheared(false);

                        if(availableTime < 20000 + TimeParser.SECOND * 10)
                        {
                            // within range of sheep regrowth, but not grass growth, so RIP grass...
                            sheep.world.setBlockState(firstGrass, Blocks.DIRT.getDefaultState());
                        }
                        else
                        {
                            // time for both sheep AND grass...
                            timeToConsume += TimeParser.SECOND * 10;

                            if(foundGrass == 1)
                            {
                                // no nearby grass, so set it to dirt...
                                sheep.world.setBlockState(firstGrass, Blocks.DIRT.getDefaultState());
                            }

                            // either way, consume the time...
                            ((ItemTemporalHourglass) hourglass.getItem()).consumeTime(player, hourglass, player.world, InventoryHelper.getIndexOfStackInInventory(hourglass, player.inventory), timeToConsume);

                            Random r = player.world.rand;
                            BlockPos pos = event.getPos();

                            SoundHelper.playShortTimeDing(null, player.world, pos.getX(), pos.getY(), pos.getZ());
                            ParticleHelper.spawnTemporalBurstParticles(target.getPositionVector(), new Vec3d(1, 1, 1), 8);
                        }
                    }
                }
            }
        }
    }

    private static void shearDatSheep(EntitySheep sheep, ItemStack stackInHand, BlockPos pos, EntityPlayer player)
    {
        java.util.List<ItemStack> drops = sheep.onSheared(stackInHand, sheep.world, pos,
                net.minecraft.enchantment.EnchantmentHelper.getEnchantmentLevel(net.minecraft.init.Enchantments.FORTUNE, stackInHand));

        java.util.Random rand = new java.util.Random();
        for(ItemStack stack : drops)
        {
            net.minecraft.entity.item.EntityItem ent = sheep.entityDropItem(stack, 1.0F);
            assert ent != null;
            ent.motionY += rand.nextFloat() * 0.05F;
            ent.motionX += (rand.nextFloat() - rand.nextFloat()) * 0.1F;
            ent.motionZ += (rand.nextFloat() - rand.nextFloat()) * 0.1F;
        }
        stackInHand.damageItem(1, player);
    }
}
