package lumaceon.mods.aeonicraft.api.temporal.temporalnetwork;

import lumaceon.mods.aeonicraft.api.Internal;
import lumaceon.mods.aeonicraft.api.util.BlockLoc;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * An essential part of a balanced temporal network~! The block need not have a tile entity, though it can if needed.
 */
public abstract class BlockTemporalNetwork extends Block implements ITemporalNetworkBlock
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
        if(!worldIn.isRemote)
        {
            Internal.addTemporalNetworkLocation.apply(new BlockLoc(pos, worldIn));
        }

        super.onBlockAdded(worldIn, pos, state);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        if(!worldIn.isRemote)
        {
            Internal.removeTemporalNetworkLocation.apply(new BlockLoc(pos, worldIn));
        }

        super.breakBlock(worldIn, pos, state);
    }
}
