package lumaceon.mods.aeonicraft.util;

import net.minecraft.entity.EntityLiving;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SpawnHelper
{
    /**
     * @return True if successfully spawned, false if no valid position could be found in 100 attempts.
     */
    public static boolean spawnEntityNearby(EntityLiving newEntity, World world, BlockPos centerPos)
    {
        BlockPos randomPos = new BlockPos(centerPos.getX() + world.rand.nextFloat() * 12F - 6F, centerPos.getY(), centerPos.getZ() + world.rand.nextFloat() * 12F - 6F);
        randomPos = findNearbyGround(randomPos, world);
        newEntity.setPosition(randomPos.getX(), randomPos.getY(), randomPos.getZ());

        int attempts = 0;
        while(attempts < 99 && !newEntity.isNotColliding())
        {
            randomPos = new BlockPos(centerPos.getX() + (world.rand.nextFloat() + 0.2F) * 10F, centerPos.getY(), centerPos.getZ() + (world.rand.nextFloat() + 0.2F) * 10F);
            randomPos = findNearbyGround(randomPos, world);
            newEntity.setPosition(randomPos.getX(), randomPos.getY(), randomPos.getZ());
            attempts++;
        }

        if(newEntity.isNotColliding())
        {
            world.spawnEntity(newEntity);
            return true;
        }

        return false;
    }

    private static BlockPos findNearbyGround(BlockPos position, World world)
    {
        if(world.isAirBlock(position))
        {
            while(world.isAirBlock(position) && position.getY() > 0)
            {
                position = position.down();
            }
            position = position.up(); // Once we found a solid block, spawn it one up from it.
        }
        else
        {
            while(!world.isAirBlock(position) && position.getY() < world.getHeight())
            {
                position = position.up();
            }
        }
        return position;
    }
}
