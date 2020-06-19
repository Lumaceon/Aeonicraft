package lumaceon.mods.aeonicraft.api;

import lumaceon.mods.aeonicraft.api.util.ChunkLoc;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import java.util.HashMap;
import java.util.function.Predicate;

/**
 * Provides several methods for returning whether a chunk is considered 'frozen in time' or not. By default, this is
 * true if the chunk is simply unloaded.
 *
 * Additional predicates can be registered to consider chunks time-frozen, or time-active, regardless of whether the
 * chunk is loaded or not.
 */
public class TemporalChunks
{
    private static HashMap<String, Predicate<WorldChunkPositionPair>> FROZEN_CONDITIONS = new HashMap<>();
    private static HashMap<String, Predicate<WorldChunkPositionPair>> UNFROZEN_CONDITIONS = new HashMap<>();

    /**
     * Primary method for getting whether a chunk is considered frozen in time or not.
     * @param world The world to query.
     * @param pos The position of the chunk in the given world.
     * @return True if the chunk is 'time-frozen,' false otherwise.
     */
    public static boolean isChunkFrozen(World world, ChunkPos pos)
    {
        WorldChunkPositionPair worldAndPosition = new WorldChunkPositionPair(world, pos);

        // Force frozen (unloaded) chunks.
        for (Predicate<WorldChunkPositionPair> predicate : FROZEN_CONDITIONS.values())
        {
            if(predicate.test(worldAndPosition))
                return true;
        }

        // Force activate (loaded) chunks.
        for (Predicate<WorldChunkPositionPair> predicate : UNFROZEN_CONDITIONS.values())
        {
            if(predicate.test(worldAndPosition))
                return false;
        }

        return !world.isAreaLoaded(new BlockPos(pos.getXStart() + 7, 127, pos.getZStart() + 7), 1);
    }

    /**
     * Primary method for getting whether a chunk is considered frozen in time or not.
     * @param loc The location of the chunk.
     * @return True if the chunk is 'time-frozen,' false otherwise.
     */
    public static boolean isChunkFrozen(ChunkLoc loc) {
        return isChunkFrozen(DimensionManager.getWorld(loc.getDimensionID()), new ChunkPos(loc.getX(), loc.getZ()));
    }

    /**
     * Register a predicate for your mod.
     * If this predicate returns true, the chunk will be considered unloaded or in 'frozen time.'
     */
    public static void registerFrozenTimeCondition(String modID, Predicate<WorldChunkPositionPair> worldAndPosition) {
        FROZEN_CONDITIONS.put(modID, worldAndPosition);
    }

    /**
     * Register a predicate for your mod.
     * If this predicate returns true, the chunk will be considered loaded or in 'active time.'
     *
     * Note: no actual chunk-loading is performed here; this is only for mod systems like temporal networks.
     */
    public static void registerActiveTimeCondition(String modID, Predicate<WorldChunkPositionPair> worldAndPosition) {
        UNFROZEN_CONDITIONS.put(modID, worldAndPosition);
    }

    public static class WorldChunkPositionPair
    {
        public World world;
        public ChunkPos pos;

        public WorldChunkPositionPair(World world, ChunkPos pos) {
            this.world = world;
            this.pos = pos;
        }
    }
}
