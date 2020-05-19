package lumaceon.mods.aeonicraft.api.temporalnetwork;

import lumaceon.mods.aeonicraft.api.Internal;
import lumaceon.mods.aeonicraft.api.util.BlockLoc;
import lumaceon.mods.aeonicraft.api.util.ChunkLoc;
import net.minecraft.util.EnumFacing;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class TemporalNetwork
{
    protected HashMap<BlockLoc, TemporalNetworkLocationStats> locationsInTheNetwork = new HashMap<>(100);
    protected HashMap<ChunkLoc, TemporalNetworkChunkStats> tcGenPerChunk = new HashMap<>(20);

    /**
     * Gets the temporal network associated with the block location (BlockPos with dimension field).
     * @return The temporal network at the given position, or null if none exist.
     */
    @Nullable
    public static TemporalNetwork getTemporalNetwork(BlockLoc blockLocation) {
        return Internal.temporalNetworkRetriever.apply(blockLocation);
    }

    /**
     * Add a block to this network.
     */
    public void addLocation(BlockLoc loc, BlockTemporalNetwork block)
    {
        TemporalNetworkLocationStats stats;

        boolean[] connectableSides = new boolean[6];
        for (EnumFacing facing : EnumFacing.values())
        {
            connectableSides[facing.getIndex()] = block.canConnectOnSide(facing);
        }

        stats = new TemporalNetworkLocationStats(block.initTCGenValue(), connectableSides, loc);
        locationsInTheNetwork.put(loc, stats);
        updateChunkStatsWhenAddingLocation(stats);
    }

    /**
     * More explicit version of the above method. Also assumes the connection is legit.
     */
    public void addLocation(TemporalNetworkLocationStats loc)
    {
        if(locationsInTheNetwork.containsKey(loc.location))
            return;

        locationsInTheNetwork.put(loc.location, loc);
        updateChunkStatsWhenAddingLocation(loc);
    }

    private void updateChunkStatsWhenAddingLocation(TemporalNetworkLocationStats loc)
    {
        ChunkLoc cLoc = new ChunkLoc(loc.location);
        if(!tcGenPerChunk.containsKey(cLoc))
        {
            tcGenPerChunk.put(cLoc, new TemporalNetworkChunkStats(loc.tcGenPerSecond));
        }
        else
        {
            TemporalNetworkChunkStats chunkStats = tcGenPerChunk.get(cLoc);
            chunkStats.tcGenPerSecond += loc.tcGenPerSecond;
            chunkStats.locsInChunk++;
        }
    }

    /**
     * Remove a block from this network.
     */
    public void removeLocation(BlockLoc loc)
    {
        TemporalNetworkLocationStats stats = locationsInTheNetwork.remove(loc);
        if(stats == null)
            return;

        ChunkLoc cLoc = new ChunkLoc(loc);
        if(tcGenPerChunk.containsKey(cLoc))
        {
            TemporalNetworkChunkStats chunkStats = tcGenPerChunk.get(new ChunkLoc(loc));
            chunkStats.tcGenPerSecond -= stats.tcGenPerSecond;
            chunkStats.locsInChunk--;
            if(chunkStats.locsInChunk <= 0)
            {
                tcGenPerChunk.remove(cLoc);
            }
        }

        if(locationsInTheNetwork.size() <= 0)
            destroyNetwork();
    }

    /**
     * Attempt to get stats from a given side (that is, the side of the target block you're trying to access).
     * @param targetSide The side you're trying to access at, or null to ignore sided connections.
     * @return Stats representing the block at this location, or null if one doesn't exist (or can't be accessed).
     */
    @Nullable
    public TemporalNetworkLocationStats getLocationFromSide(BlockLoc loc, @Nullable EnumFacing targetSide)
    {
        TemporalNetworkLocationStats stats = locationsInTheNetwork.get(loc);
        if(stats == null)
            return null;

        if(targetSide != null && !stats.connections[targetSide.getIndex()])
            return null;

        return stats;
    }

    public Set<BlockLoc> getLocations()
    {
        return locationsInTheNetwork.keySet();
    }

    /**
     * Updates the TC generation rate associated with the block at the given location.
     */
    public void updateTCGen(BlockLoc loc, long newTCGenPerSecond)
    {
        if(locationsInTheNetwork.containsKey(loc))
        {
            TemporalNetworkLocationStats stats = locationsInTheNetwork.get(loc);
            long difference = newTCGenPerSecond - stats.tcGenPerSecond;
            stats.tcGenPerSecond = newTCGenPerSecond;

            ChunkLoc cLoc = new ChunkLoc(loc);
            if(tcGenPerChunk.containsKey(cLoc))
            {
                TemporalNetworkChunkStats chunkStats = tcGenPerChunk.get(new ChunkLoc(loc));
                chunkStats.tcGenPerSecond += difference;
            }
        }
    }

    /**
     * Updates the side connectivity for the block at the given location. May cause significant network adjustments
     * and should be used sparingly.
     */
    public void updateSideConnectivity(BlockLoc loc, EnumFacing blockSide, boolean canConnect)
    {
        //TODO - noop for now, due to the relative complexity of rebuilding the network.
    }

    /**
     * Creates a forced adjacency: defined as two locations which will be considered adjacent to each other.
     */
    public void createForcedAdjacency(BlockLoc loc1, BlockLoc loc2)
    {
        BlockLoc.Pair pair = new BlockLoc.Pair(loc1, loc2, false);

        TemporalNetworkLocationStats stats = locationsInTheNetwork.get(loc1);
        if(stats != null && !stats.forcedAdjacencyConnections.contains(pair))
            stats.forcedAdjacencyConnections.add(pair);

        stats = locationsInTheNetwork.get(loc2);
        if(stats != null && !stats.forcedAdjacencyConnections.contains(pair))
            stats.forcedAdjacencyConnections.add(pair);
    }

    /**
     * Deletes a forced a adjacency between two points, if one exists.
     */
    public void destroyForcedAdjacency(BlockLoc loc1, BlockLoc loc2)
    {
        BlockLoc.Pair p = new BlockLoc.Pair(loc1, loc2, false);
        TemporalNetworkLocationStats s1 = getLocationFromSide(loc1, null);
        TemporalNetworkLocationStats s2 = getLocationFromSide(loc2, null);

        if(s1 != null)
            s1.forcedAdjacencyConnections.remove(p);
        if(s2 != null)
            s2.forcedAdjacencyConnections.remove(p);
    }

    /**
     * Destroys all forced adjacencies where the passed in location is one of the two ends.
     */
    public void destroyAllForcedAdjacencyAtLocation(BlockLoc loc)
    {
        TemporalNetworkLocationStats stats = getLocationFromSide(loc, null);
        stats.forcedAdjacencyConnections.clear();
    }

    /**
     * Gets all the blocks that are forced to be adjacent to the location passed in. Note that this CAN return blocks
     * that are naturally adjacent, so you should check for duplication if necessary.
     * @return The blocks which are forced to be adjacent to the given location.
     */
    public ArrayList<TemporalNetworkLocationStats> getForcedAdjacentBlocks(TemporalNetworkLocationStats loc)
    {
        if(loc == null || loc.location == null)
            return new ArrayList<>();

        ArrayList<TemporalNetworkLocationStats> adjacents = new ArrayList<>();
        TemporalNetworkLocationStats s;
        for(BlockLoc.Pair pair : loc.forcedAdjacencyConnections)
        {
            if(!pair.loc1.equals(loc.location))
            {
                s = getLocationFromSide(pair.loc1, null);
                if(s != null)
                    adjacents.add(s);
            }
            if(!pair.loc2.equals(loc.location))
            {
                s = getLocationFromSide(pair.loc2, null);
                if(s != null)
                    adjacents.add(s);
            }
        }
        return adjacents;
    }

    /**
     * Gets all the network blocks that're adjacent to the one passed in, either forced or naturally.
     * @param checkLocConnectivity If true, loc will be checked to make sure it can connect in a given direction.
     */
    public ArrayList<TemporalNetworkLocationStats> getEffectivelyAdjacent(TemporalNetworkLocationStats loc, boolean checkLocConnectivity)
    {
        ArrayList<TemporalNetworkLocationStats> adjacents = getForcedAdjacentBlocks(loc);

        for(EnumFacing facing : EnumFacing.values())
        {
            if(checkLocConnectivity && getLocationFromSide(loc.location, facing) == null)
                continue;

            BlockLoc l = new BlockLoc(loc.location.add(facing.getDirectionVec()), loc.location.getDimensionID());
            TemporalNetworkLocationStats stats = getLocationFromSide(l, facing.getOpposite());
            if(stats != null)
            {
                adjacents.add(stats);
            }
        }

        return adjacents;
    }

    /**
     * Builds a new network out of this one using adjacency rules to include only connected locations. Forced adjacency
     * is also taken into account, and any unused forced adjacency is also left out.
     * @param origin The origin point of the rebuilding process.
     * @return A new network built as a subset of this one.
     */
    public TemporalNetwork buildSubsetNetworkFrom(TemporalNetworkLocationStats origin)
    {
        TemporalNetwork newNet = new TemporalNetwork();

        ArrayList<TemporalNetworkLocationStats> checked = new ArrayList<>(locationsInTheNetwork.size());
        ArrayList<TemporalNetworkLocationStats> toCheck = new ArrayList<>(locationsInTheNetwork.size());

        toCheck.add(origin);

        while(!toCheck.isEmpty())
        {
            TemporalNetworkLocationStats stats = toCheck.get(0);
            ArrayList<TemporalNetworkLocationStats> adjacents = getEffectivelyAdjacent(stats, true);
            adjacents.removeAll(checked);
            checked.add(stats);
            toCheck.remove(0);
            toCheck.addAll(adjacents);
        }

        for(TemporalNetworkLocationStats s : checked)
        {
            newNet.addLocation(s.deepCopy());
        }

        return newNet;
    }

    /**
     * Merges another temporal network into this one and destroys the other one.
     * @param other The other network to merge with this network.
     */
    public void mergeNetworks(TemporalNetwork other)
    {
        //TODO Impl.
        destroyNetwork();
    }

    /**
     * Destroys this network from Aeonicraft's map storage.
     */
    public void destroyNetwork()
    {
        Internal.destroyTemporalNetwork.apply(this);
    }

    public class TemporalNetworkLocationStats
    {
        public long tcGenPerSecond = 0;
        private boolean[] connections = {
                true, //DOWN
                true, //UP
                true, //NORTH
                true, //SOUTH
                true, //WEST
                true, //EAST
        };
        public BlockLoc location;
        public ArrayList<BlockLoc.Pair> forcedAdjacencyConnections = new ArrayList<>(0);


        public TemporalNetworkLocationStats(long tcGenPerSecond, boolean[] connectableSides, BlockLoc location) {
            this.tcGenPerSecond = tcGenPerSecond;
            this.connections = connectableSides;
            this.location = location;
        }

        public void setTCGenPerSecond(long tcGenPerSecond) {
            this.tcGenPerSecond = tcGenPerSecond;
        }

        public long getTCGenPerSecond() {
            return this.tcGenPerSecond;
        }

        public void setCanConnect(EnumFacing side, boolean canConnect) {
            connections[side.getIndex()] = canConnect;
        }

        public boolean canConnect(EnumFacing side) {
            return connections[side.getIndex()];
        }

        public TemporalNetworkLocationStats deepCopy()
        {
            boolean[] connections = {
                    this.connections[0],
                    this.connections[1],
                    this.connections[2],
                    this.connections[3],
                    this.connections[4],
                    this.connections[5],
            };

            TemporalNetworkLocationStats ret = new TemporalNetworkLocationStats(this.tcGenPerSecond, connections, this.location);

            for(BlockLoc.Pair p : this.forcedAdjacencyConnections)
            {
                ret.forcedAdjacencyConnections.add(new BlockLoc.Pair(p.loc1, p.loc2, false));
            }

            return ret;
        }
    }

    public class TemporalNetworkChunkStats
    {
        public long tcGenPerSecond;
        public int locsInChunk = 1;

        public TemporalNetworkChunkStats(long tcGenPerSecond)
        {
            this.tcGenPerSecond = tcGenPerSecond;
        }
    }
}
