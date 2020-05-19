package lumaceon.mods.aeonicraft.api.temporalnetwork;

import lumaceon.mods.aeonicraft.api.util.BlockLoc;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;

/**
 * Subclasses are marked as part of a temporal network. The block need not have a tile entity, though it can if needed.
 */
public abstract class BlockTemporalNetwork extends Block
{
    public BlockTemporalNetwork(Material blockMaterialIn) {
        super(blockMaterialIn);
    }

    public BlockTemporalNetwork(Material blockMaterialIn, MapColor mapColor) {
        super(blockMaterialIn, mapColor);
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        ArrayList<TemporalNetwork> connectedNetworks = new ArrayList<>(6);
        for(EnumFacing face : EnumFacing.values())
        {
            TemporalNetwork tn = TemporalNetwork.getTemporalNetwork(new BlockLoc(pos.add(face.getDirectionVec()), worldIn));
            if(tn != null && !connectedNetworks.contains(tn))
            {
                connectedNetworks.add(tn);
            }
        }

        if(connectedNetworks.size() == 0)
        {
            TemporalNetwork tn = new TemporalNetwork();
            connectedNetworks.add(tn);
        }

        while (connectedNetworks.size() > 1)
        {
            TemporalNetwork firstNetwork = connectedNetworks.get(0);
            firstNetwork.mergeNetworks(connectedNetworks.get(1));
            connectedNetworks.remove(1);
        }

        assert connectedNetworks.size() == 1;

        connectedNetworks.get(0).addLocation(new BlockLoc(pos, worldIn), this);

        super.onBlockAdded(worldIn, pos, state);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        BlockLoc loc = new BlockLoc(pos, worldIn);
        TemporalNetwork currentNetwork = TemporalNetwork.getTemporalNetwork(loc);
        if(currentNetwork == null)
            return;

        TemporalNetwork.TemporalNetworkLocationStats stats = currentNetwork.getLocationFromSide(loc, null);
        ArrayList<TemporalNetwork.TemporalNetworkLocationStats> adjacencies = currentNetwork.getEffectivelyAdjacent(stats, true);

        currentNetwork.removeLocation(loc);
        currentNetwork.destroyAllForcedAdjacencyAtLocation(loc);
        currentNetwork.destroyNetwork();

        while(!adjacencies.isEmpty())
        {
            TemporalNetwork newTN = currentNetwork.buildSubsetNetworkFrom(adjacencies.get(0));
            adjacencies.removeAll(newTN.getLocations()); // TODO Fix.
        }

        super.breakBlock(worldIn, pos, state);
    }

    /**
     * Gets the TC (milliseconds) that this block will contribute to the temporal network while unloaded every second.
     * This value is then saved and referenced. If you need a dynamic value, you'll have to get the TemporalNetwork and
     * update it there.
     *
     * A 0 value can be passed in to create a simple network connector. Most machines and other blocks that make use of
     * TC should conventionally pass in a negative value; -(TCToRealTime.SECOND) is a good baseline for simple machines.
     * @return The TC this will generate each second when unloaded.
     */
    public abstract long initTCGenValue();

    /**
     * Queried when first added to the network. If you need more dynamic sides, you'll have to get the TemporalNetwork
     * and update it there.
     * @return True of the given side of this block can connect to a network, false otherwise.
     */
    public abstract boolean canConnectOnSide(EnumFacing mySide);
}
