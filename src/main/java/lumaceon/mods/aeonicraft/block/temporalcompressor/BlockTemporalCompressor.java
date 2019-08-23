package lumaceon.mods.aeonicraft.block.temporalcompressor;

import lumaceon.mods.aeonicraft.block.BlockAeonicraft;
import lumaceon.mods.aeonicraft.temporalcompressor.TemporalCompressorGeneration;
import lumaceon.mods.aeonicraft.tile.TileTemporalCompressor;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockTemporalCompressor extends BlockAeonicraft implements ITileEntityProvider
{
    public BlockTemporalCompressor(Material blockMaterial, String name) {
        super(blockMaterial, name);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        TileEntity te = worldIn.getTileEntity(pos);
        if(te instanceof TileTemporalCompressor)
        {
            TemporalCompressorGeneration.calculateStatsPerTickFromCompressor(((TileTemporalCompressor) te).componentMatrix).printStatsToConsole();
        }
        return false;
    }

    @Override
    public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {
        return face == EnumFacing.DOWN;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileTemporalCompressor();
    }
}
