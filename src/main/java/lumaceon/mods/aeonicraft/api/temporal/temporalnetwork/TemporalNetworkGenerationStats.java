package lumaceon.mods.aeonicraft.api.temporal.temporalnetwork;

import lumaceon.mods.aeonicraft.api.Internal;
import lumaceon.mods.aeonicraft.api.temporal.TC;
import lumaceon.mods.aeonicraft.api.util.BlockLoc;
import lumaceon.mods.aeonicraft.api.util.ChunkLoc;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.*;

public class TemporalNetworkGenerationStats
{
    protected HashMap<BlockLoc, TemporalNetworkLocationStats> locationsInTheNetwork = new HashMap<>(100);
    protected HashMap<ChunkLoc, TemporalNetworkChunkStats> tcGenPerChunk = new HashMap<>(20);

    protected TemporalNetwork temporalNetwork;
    protected boolean isDirty = true;

    protected TC temporalCompressionCapacity = TC.NONE;


    public TemporalNetworkGenerationStats(TemporalNetwork temporalNetwork) {
        this.temporalNetwork = temporalNetwork;
    }

    public TC getTCCapacity() {
        return this.temporalCompressionCapacity;
    }

    public void markDirty() {
        this.isDirty = true;
    }

    /**
     * Add a block to this network.
     */
    public void addLocation(BlockLoc loc, ITemporalNetworkBlock block)
    {
        TemporalNetworkLocationStats stats;

        boolean[] connectableSides = new boolean[6];
        for (EnumFacing facing : EnumFacing.values())
        {
            connectableSides[facing.getIndex()] = block.canConnectOnSide(facing);
        }

        stats = new TemporalNetworkLocationStats(block.getTCGenerationPerSecond(), block.getTCCapacity(), connectableSides, loc);
        locationsInTheNetwork.put(loc, stats);
        this.temporalCompressionCapacity = this.temporalCompressionCapacity.add(stats.getTCCap());
        this.temporalNetwork.checkCapacityChangeLoss(this.getTCCapacity());
        updateChunkStatsWhenAddingLocation(stats);

        markDirty();
    }

    /**
     * More explicit version of the above method. Also assumes the connection is legit.
     */
    public void addLocation(TemporalNetworkLocationStats loc)
    {
        if(locationsInTheNetwork.containsKey(loc.location))
            return;

        locationsInTheNetwork.put(loc.location, loc);
        this.temporalCompressionCapacity = this.temporalCompressionCapacity.add(loc.getTCCap());
        this.temporalNetwork.checkCapacityChangeLoss(this.getTCCapacity());
        updateChunkStatsWhenAddingLocation(loc);

        markDirty();
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
            chunkStats.tcGenPerSecond = chunkStats.tcGenPerSecond.add(loc.tcGenPerSecond);
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

        this.temporalCompressionCapacity = this.temporalCompressionCapacity.subtract(stats.getTCCap());
        this.temporalNetwork.checkCapacityChangeLoss(this.getTCCapacity());

        ChunkLoc cLoc = new ChunkLoc(loc);
        if(tcGenPerChunk.containsKey(cLoc))
        {
            TemporalNetworkChunkStats chunkStats = tcGenPerChunk.get(new ChunkLoc(loc));
            chunkStats.tcGenPerSecond = chunkStats.tcGenPerSecond.subtract(stats.tcGenPerSecond);
            chunkStats.locsInChunk--;
            if(chunkStats.locsInChunk <= 0)
            {
                tcGenPerChunk.remove(cLoc);
            }
        }

        markDirty();
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

    public Collection<TemporalNetworkLocationStats> getLocations()
    {
        return locationsInTheNetwork.values();
    }

    /**
     * Updates the TC generation rate associated with the block at the given location.
     */
    public void updateTCGen(BlockLoc loc, TC newTCGenPerSecond)
    {
        if(locationsInTheNetwork.containsKey(loc))
        {
            TemporalNetworkLocationStats stats = locationsInTheNetwork.get(loc);
            long howMuchNewGenAdded = newTCGenPerSecond.getVal() - stats.tcGenPerSecond.getVal();
            stats.setTCGenPerSecond(newTCGenPerSecond);

            ChunkLoc cLoc = new ChunkLoc(loc);
            if(tcGenPerChunk.containsKey(cLoc))
            {
                TemporalNetworkChunkStats chunkStats = tcGenPerChunk.get(new ChunkLoc(loc));
                chunkStats.tcGenPerSecond = chunkStats.tcGenPerSecond.add(howMuchNewGenAdded);
            }
        }

        markDirty();
    }

    public void updateTCCapacity(BlockLoc loc, TC newTCCapacity)
    {
        if(locationsInTheNetwork.containsKey(loc))
        {
            TemporalNetworkLocationStats stats = locationsInTheNetwork.get(loc);
            long howMuchNewCapAdded = newTCCapacity.getVal() - stats.tcGenPerSecond.getVal();
            stats.setTCCap(newTCCapacity);

            this.temporalCompressionCapacity = this.temporalCompressionCapacity.add(howMuchNewCapAdded);
        }

        markDirty();
    }

    /**
     * Creates a forced adjacency: defined as two locations which will be considered adjacent to each other.
     */
    public void createForcedAdjacency(BlockLoc loc1, BlockLoc loc2)
    {
        TemporalNetworkLocationStats stats = locationsInTheNetwork.get(loc1);
        if(stats != null && !stats.forcedAdjacencyConnections.contains(loc2))
            stats.forcedAdjacencyConnections.add(loc2);

        stats = locationsInTheNetwork.get(loc2);
        if(stats != null && !stats.forcedAdjacencyConnections.contains(loc1))
            stats.forcedAdjacencyConnections.add(loc1);

        Internal.mergeTemporalNetworks.apply(new BlockLoc.Pair(loc1, loc2, false));

        markDirty();
    }

    /**
     * Deletes a forced a adjacency between two points, if one exists.
     */
    public void destroyForcedAdjacency(BlockLoc loc1, BlockLoc loc2)
    {
        TemporalNetworkLocationStats s1 = getLocationFromSide(loc1, null);
        TemporalNetworkLocationStats s2 = getLocationFromSide(loc2, null);

        if(s1 != null)
            s1.forcedAdjacencyConnections.remove(loc2);
        if(s2 != null)
            s2.forcedAdjacencyConnections.remove(loc1);

        Internal.separateNetworksIfNecessary.apply(new BlockLoc.Pair(loc1, loc2, false));

        markDirty();
    }

    /**
     * Destroys all forced adjacencies where the passed in location is one of the two ends.
     */
    public void destroyAllForcedAdjacencyAtLocation(BlockLoc loc)
    {
        TemporalNetworkLocationStats stats = getLocationFromSide(loc, null);
        if(stats != null)
        {
            ArrayList<BlockLoc> locs = stats.getForcedAdjacencies();
            stats.forcedAdjacencyConnections.clear();
            for(BlockLoc l : locs)
            {
                Internal.separateNetworksIfNecessary.apply(new BlockLoc.Pair(loc, l, false));
            }
        }

        markDirty();
    }

    /**
     * Gets all the blocks that are forced to be adjacent to the location passed in. Note that this CAN return blocks
     * that are naturally adjacent, so you should check for duplication if necessary.
     * @return The blocks which are forced to be adjacent to the given location.
     */
    public ArrayList<TemporalNetworkLocationStats> getForcedNeighbors(TemporalNetworkLocationStats loc)
    {
        if(loc == null || loc.location == null)
            return new ArrayList<>();

        ArrayList<TemporalNetworkLocationStats> adjacents = new ArrayList<>();
        TemporalNetworkLocationStats s;
        for(BlockLoc target : loc.forcedAdjacencyConnections)
        {
            s = getLocationFromSide(target, null);
            if(s != null)
            {
                adjacents.add(s);
            }
        }
        return adjacents;
    }

    /**
     * Gets all the network blocks that're adjacent to the one passed in, either forced or naturally.
     * @param checkLocConnectivity If true, loc will be checked to make sure it can connect in a given direction.
     */
    public ArrayList<TemporalNetworkLocationStats> getEffectiveNeighbors(TemporalNetworkLocationStats loc, boolean checkLocConnectivity)
    {
        ArrayList<TemporalNetworkLocationStats> adjacents = getForcedNeighbors(loc);

        for(EnumFacing facing : EnumFacing.values())
        {
            if(checkLocConnectivity && getLocationFromSide(loc.location, facing) == null)
                continue;

            BlockLoc l = loc.location.add(facing.getDirectionVec());
            TemporalNetworkLocationStats stats = getLocationFromSide(l, facing.getOpposite());
            if(stats != null && !adjacents.contains(stats))
            {
                adjacents.add(stats);
            }
        }

        return adjacents;
    }

    private NBTTagList locationNBT = null;
    public NBTTagList getNBT()
    {
        // Only update this if cached location NBT isn't available or accurate.
        if(isDirty || locationNBT == null)
        {
            NBTTagList locationList = new NBTTagList();

            // Save each location...
            for (TemporalNetworkLocationStats loc : getLocations())
                locationList.appendTag(loc.serializeToNBT());

            locationNBT = locationList;
            isDirty = false;
        }

        return locationNBT;
    }

    public void fromNBT(NBTTagList locationNBT)
    {
        for (int i = 0; i < locationNBT.tagCount(); i++)
            this.addLocation(new TemporalNetworkLocationStats(locationNBT.getCompoundTagAt(i)));
    }

    public class TemporalNetworkLocationStats
    {
        private TC tcGenPerSecond;
        private TC tcCapacity;
        private boolean[] connections = {
                true, //DOWN
                true, //UP
                true, //NORTH
                true, //SOUTH
                true, //WEST
                true, //EAST
        };
        private BlockLoc location;
        private ArrayList<BlockLoc> forcedAdjacencyConnections = new ArrayList<>(2);

        public TemporalNetworkLocationStats(TC tcGenPerSecond, TC tcCapacity, boolean[] connectableSides, BlockLoc location) {
            this.tcGenPerSecond = tcGenPerSecond;
            this.tcCapacity = tcCapacity;
            this.connections = connectableSides;
            this.location = location;
        }

        public TemporalNetworkLocationStats(NBTTagCompound tag)
        {
            this.tcGenPerSecond = new TC(tag.getLong("tc_gen"));
            this.tcCapacity = new TC(tag.getLong("tc_cap"));

            this.connections[0] = tag.getBoolean("c_down");
            this.connections[1] = tag.getBoolean("c_up");
            this.connections[2] = tag.getBoolean("c_north");
            this.connections[3] = tag.getBoolean("c_south");
            this.connections[4] = tag.getBoolean("c_west");
            this.connections[5] = tag.getBoolean("c_east");

            this.location = new BlockLoc(tag.getCompoundTag("loc"));

            NBTTagList forcedAdjacentTargets = tag.getTagList("adj_targets", Constants.NBT.TAG_COMPOUND);
            for(int i = 0; i < forcedAdjacentTargets.tagCount(); i++)
                this.forcedAdjacencyConnections.add(new BlockLoc(forcedAdjacentTargets.getCompoundTagAt(i)));
        }

        public void setTCGenPerSecond(TC tcGenPerSecond) {
            this.tcGenPerSecond = tcGenPerSecond;
        }

        public TC getTCGenPerSecond() {
            return this.tcGenPerSecond;
        }

        public void setTCCap(TC tcCap) {
            this.tcCapacity = tcCap;
        }

        public TC getTCCap() {
            return this.tcCapacity;
        }

        public BlockLoc getLocation() {
            return this.location;
        }

        public ArrayList<BlockLoc> getForcedAdjacencies() {
            return forcedAdjacencyConnections;
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

            TemporalNetworkLocationStats ret = new TemporalNetworkLocationStats(this.tcGenPerSecond, this.tcCapacity, connections, this.location);

            for(BlockLoc l : this.forcedAdjacencyConnections)
            {
                ret.forcedAdjacencyConnections.add(new BlockLoc(l.getPos(), l.getDimensionID()));
            }

            return ret;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TemporalNetworkLocationStats that = (TemporalNetworkLocationStats) o;
            return tcGenPerSecond == that.tcGenPerSecond &&
                    this.getTCCap() == that.getTCCap() &&
                    Arrays.equals(connections, that.connections) &&
                    getLocation().equals(that.getLocation()) &&
                    forcedAdjacencyConnections.equals(that.forcedAdjacencyConnections);
        }

        @Override
        public int hashCode() {
            int result = Objects.hash(tcGenPerSecond, tcCapacity, getLocation(), forcedAdjacencyConnections);
            result = 31 * result + Arrays.hashCode(connections);
            return result;
        }

        public NBTTagCompound serializeToNBT()
        {
            NBTTagCompound nbt = new NBTTagCompound();

            // Save TC Generation value.
            nbt.setLong("tc_gen", getTCGenPerSecond().getVal());
            nbt.setLong("tc_cap", getTCCap().getVal());

            // Save side connections.
            nbt.setBoolean("c_down", this.canConnect(EnumFacing.DOWN));
            nbt.setBoolean("c_up", this.canConnect(EnumFacing.UP));
            nbt.setBoolean("c_north", this.canConnect(EnumFacing.NORTH));
            nbt.setBoolean("c_south", this.canConnect(EnumFacing.SOUTH));
            nbt.setBoolean("c_west", this.canConnect(EnumFacing.WEST));
            nbt.setBoolean("c_east", this.canConnect(EnumFacing.EAST));

            // Save location.
            nbt.setTag("loc", this.location.serializeToNBT());

            // Save forced adjacencies.
            NBTTagList forcedAdjacentTargets = new NBTTagList();
            for(BlockLoc l : this.forcedAdjacencyConnections)
                forcedAdjacentTargets.appendTag(l.serializeToNBT());
            nbt.setTag("adj_targets", forcedAdjacentTargets);

            return nbt;
        }
    }

    public class TemporalNetworkChunkStats
    {
        public TC tcGenPerSecond;
        public int locsInChunk = 1;

        public TemporalNetworkChunkStats(TC tcGenPerSecond)
        {
            this.tcGenPerSecond = tcGenPerSecond;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TemporalNetworkChunkStats that = (TemporalNetworkChunkStats) o;
            return tcGenPerSecond == that.tcGenPerSecond &&
                    locsInChunk == that.locsInChunk;
        }

        @Override
        public int hashCode() {
            return Objects.hash(tcGenPerSecond, locsInChunk);
        }
    }
}
