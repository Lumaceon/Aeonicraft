package lumaceon.mods.aeonicraft.worlddata;

import lumaceon.mods.aeonicraft.api.temporalnetwork.BlockTemporalNetwork;
import lumaceon.mods.aeonicraft.api.temporalnetwork.TemporalNetworkGenerationStats;
import lumaceon.mods.aeonicraft.api.temporalnetwork.ITemporalNetworkBlock;
import lumaceon.mods.aeonicraft.api.temporalnetwork.TemporalNetwork;
import lumaceon.mods.aeonicraft.api.util.BlockLoc;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class TemporalNetworkData
{
    private ExtendedSaveData data;
    private HashMap<BlockLoc, TemporalNetwork> temporalNetworkBlockMap = new HashMap<>();

    public TemporalNetworkData(ExtendedSaveData data) {
        this.data = data;
    }

    public TemporalNetwork get(BlockLoc blockLoc) {
        return temporalNetworkBlockMap.get(blockLoc);
    }

    public void update()
    {
        for(TemporalNetwork network : temporalNetworkBlockMap.values())
        {
            network.onUpdate();
        }
    }

    /**
     * Adds a location to the Temporal Network block map, either creating a new one, or adding/merging others.
     * @return The network at the current position.
     */
    public TemporalNetwork setupLocationForNetworkBlockMap(BlockLoc blockLocation)
    {
        World world = DimensionManager.getWorld(blockLocation.getDimensionID());
        TemporalNetwork networkToAddThisTo;

        // Get the unique networks surrounding this position.
        ArrayList<TemporalNetwork> surroundingNetworks = new ArrayList<>(6);
        for(EnumFacing face : EnumFacing.values())
        {
            TemporalNetwork tn = TemporalNetwork.getTemporalNetwork(blockLocation.add(face.getDirectionVec()));
            if(tn != null && !surroundingNetworks.contains(tn))
            {
                surroundingNetworks.add(tn);
            }
        }

        // If this block is alone (0 surrounding networks), it creates a new, empty network.
        if(surroundingNetworks.size() == 0)
        {
            networkToAddThisTo = new TemporalNetwork();
        }
        else if (surroundingNetworks.size() == 1) // If only 1 neighbor, we'll add to it.
        {
            networkToAddThisTo = surroundingNetworks.get(0);
        }
        else // If there's multiple neighbors, merge them and use the composite.
        {
            networkToAddThisTo = createCompositeNetwork(surroundingNetworks);
        }

        // Finally, add the new location to the network, getting the stats from the block at that position.
        Block block = world.getBlockState(blockLocation.getPos()).getBlock();
        if(block instanceof ITemporalNetworkBlock)
        {
            networkToAddThisTo.generationStats.addLocation(blockLocation, (BlockTemporalNetwork) block);
            temporalNetworkBlockMap.put(blockLocation, networkToAddThisTo);
        }

        data.markDirty();

        return networkToAddThisTo;
    }

    /**
     * Removes the temporal network mapping from the given position, separating networks if necessary.
     * @return True if something was removed, false otherwise.
     */
    public int removeNetworkFromLocation(BlockLoc location)
    {
        int numberOfNetworkAfterSeparation = 0;
        TemporalNetwork compositeNetwork = temporalNetworkBlockMap.remove(location);

        // Get the internal stats for this location, then find which blocks are adjacent to it.
        TemporalNetworkGenerationStats.TemporalNetworkLocationStats stats = compositeNetwork.generationStats.getLocationFromSide(location, null);
        ArrayList<TemporalNetworkGenerationStats.TemporalNetworkLocationStats> adjacentBlocks = compositeNetwork.generationStats.getEffectiveNeighbors(stats, true);

        // Remove the location and destroy adjacency so we don't use the destroyed block during subnet creation.
        compositeNetwork.generationStats.removeLocation(location);

        while (!adjacentBlocks.isEmpty())
        {
            TemporalNetwork newTN = buildSubsetNetworkFrom(compositeNetwork, adjacentBlocks.get(0));
            adjacentBlocks.removeAll(newTN.generationStats.getLocations()); // Adjacent blocks still connected don't need new networks.
            numberOfNetworkAfterSeparation++;
        }

        data.markDirty();

        return numberOfNetworkAfterSeparation;
    }

    public boolean checkSeparation(BlockLoc.Pair pair)
    {
        TemporalNetwork composite = TemporalNetwork.getTemporalNetwork(pair.loc1);
        if(composite == null)
            return false;

        Collection<TemporalNetworkGenerationStats.TemporalNetworkLocationStats> locs = composite.generationStats.getLocations();

        TemporalNetwork newNetwork = buildSubsetNetworkFrom(composite, composite.generationStats.getLocationFromSide(pair.loc1, null));
        Collection<TemporalNetworkGenerationStats.TemporalNetworkLocationStats> locsNew = newNetwork.generationStats.getLocations();

        if(locs.containsAll(locsNew))
            return false; // If the networks were already connected, no need to create a second subset net.

        buildSubsetNetworkFrom(composite, composite.generationStats.getLocationFromSide(pair.loc2, null));
        return false;
    }

    public TemporalNetwork mergeNetworks(BlockLoc.Pair pair)
    {
        ArrayList<TemporalNetwork> list = new ArrayList<>();
        list.add(TemporalNetwork.getTemporalNetwork(pair.loc1));
        list.add(TemporalNetwork.getTemporalNetwork(pair.loc2));
        return createCompositeNetwork(list);
    }

    /**
     * Takes a collection of networks and merges them into one composite network, adjusting the network blockmap
     * accordingly. The largest subnet object will be reused as the new network and will absorb the others.
     * @param networks The networks to merge.
     * @return The composite network.
     */
    private TemporalNetwork createCompositeNetwork(ArrayList<TemporalNetwork> networks)
    {
        // Set the largest network as a starting point for the composite network.
        TemporalNetwork compositeNetwork = networks.get(0);
        for (TemporalNetwork n : networks) {
            if(n.generationStats.getLocations().size() > compositeNetwork.generationStats.getLocations().size())
                compositeNetwork = n;
        }

        // Remove the composite from the networks to merge.
        networks.remove(compositeNetwork);

        Collection<TemporalNetworkGenerationStats.TemporalNetworkLocationStats> compositeNetworkLocs = compositeNetwork.generationStats.getLocations();

        // Add network data to the composite network one by one.
        while (!networks.isEmpty())
        {
            TemporalNetwork networkToAbsorb = networks.get(0);
            networks.remove(0);

            // Put each location associated with the absorbed network into the composite.
            Collection<TemporalNetworkGenerationStats.TemporalNetworkLocationStats> locs = networkToAbsorb.generationStats.getLocations();
            for(TemporalNetworkGenerationStats.TemporalNetworkLocationStats locStats : locs)
            {
                if(compositeNetworkLocs.contains(locStats))
                    continue; // Just in case we try to merge identical networks.

                compositeNetwork.generationStats.addLocation(locStats); // This automatically adjusts the chunk values as well.
                temporalNetworkBlockMap.put(locStats.getLocation(), compositeNetwork);
            }
        }

        return compositeNetwork;
    }

    /**
     * Builds a new network out of a composite network using adjacency rules to include only connected locations.
     * Forced adjacency is also taken into account. Location to network mapping also occurs here as well.
     * @param compositeNetwork The composite network, containing all the locations and forced adjacencies to query.
     * @param origin The origin point of the rebuilding process.
     * @return A new network built as a subset of this one.
     */
    public TemporalNetwork buildSubsetNetworkFrom(TemporalNetwork compositeNetwork, TemporalNetworkGenerationStats.TemporalNetworkLocationStats origin)
    {
        TemporalNetwork newSubnet = new TemporalNetwork();

        ArrayList<TemporalNetworkGenerationStats.TemporalNetworkLocationStats> locationsFound = new ArrayList<>(compositeNetwork.generationStats.getLocations().size());
        ArrayList<TemporalNetworkGenerationStats.TemporalNetworkLocationStats> toCheck = new ArrayList<>(compositeNetwork.generationStats.getLocations().size());

        toCheck.add(origin);

        // Recursively add all accessible locations to the arraylist.
        while(!toCheck.isEmpty())
        {
            TemporalNetworkGenerationStats.TemporalNetworkLocationStats workingLocation = toCheck.remove(0);
            locationsFound.add(workingLocation);

            ArrayList<TemporalNetworkGenerationStats.TemporalNetworkLocationStats> adjacentLocations = compositeNetwork.generationStats.getEffectiveNeighbors(workingLocation, true);
            adjacentLocations.removeAll(locationsFound); // Avoid duplicates.

            toCheck.addAll(adjacentLocations);
        }

        for(TemporalNetworkGenerationStats.TemporalNetworkLocationStats s : locationsFound)
        {
            newSubnet.generationStats.addLocation(s.deepCopy());
            temporalNetworkBlockMap.put(s.getLocation(), newSubnet);
        }

        return newSubnet;
    }

    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound networksCompound = new NBTTagCompound();
        NBTTagList networkList = new NBTTagList();

        List<TemporalNetwork> networksSaved = new ArrayList<>(networkList.tagCount());

        for(TemporalNetwork network : temporalNetworkBlockMap.values())
        {
            if(!networksSaved.contains(network)) // Make sure duplicate entries aren't saved.
            {
                networkList.appendTag(network.serializeNBT());
                networksSaved.add(network);
            }
        }

        networksCompound.setTag("network_list", networkList);

        return networksCompound;
    }

    public void deserializeNBT(NBTTagCompound networksCompound)
    {
        temporalNetworkBlockMap.clear();

        NBTTagList networkList = networksCompound.getTagList("network_list", Constants.NBT.TAG_COMPOUND);
        for(int i = 0; i < networkList.tagCount(); i++)
        {
            TemporalNetwork tn = new TemporalNetwork();
            tn.deserializeNBT(networkList.getCompoundTagAt(i));

            // Update the block map with the block locations from this network.
            Collection<TemporalNetworkGenerationStats.TemporalNetworkLocationStats> locs = tn.generationStats.getLocations();
            for(TemporalNetworkGenerationStats.TemporalNetworkLocationStats loc : locs)
            {
                temporalNetworkBlockMap.put(loc.getLocation(), tn);
            }
        }
    }
}
