package lumaceon.mods.aeonicraft.block;

import lumaceon.mods.aeonicraft.Aeonicraft;
import lumaceon.mods.aeonicraft.api.temporalnetwork.BlockTemporalNetwork;
import lumaceon.mods.aeonicraft.api.temporalnetwork.TemporalNetwork;
import lumaceon.mods.aeonicraft.api.util.BlockLoc;
import lumaceon.mods.aeonicraft.api.util.TCToRealTime;
import lumaceon.mods.aeonicraft.temporalcompressor.TemporalCompressorGeneration;
import lumaceon.mods.aeonicraft.tile.TileTemporalCompressor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Objects;

@SuppressWarnings("deprecation")
public class BlockTemporalCompressor extends BlockTemporalNetwork
{
    public BlockTemporalCompressor(Material blockMaterial, String name) {
        super(blockMaterial);
        this.setCreativeTab(Aeonicraft.instance.CREATIVE_TAB);
        this.setHardness(3.0F);
        this.setRegistryName(Aeonicraft.MOD_ID, name);
        this.setUnlocalizedName(Objects.requireNonNull(this.getRegistryName()).toString());
        this.setLightOpacity(0);
        this.setLightLevel(0.75F);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        TemporalNetwork tn = TemporalNetwork.getTemporalNetwork(new BlockLoc(pos, worldIn));
        if(tn != null)
        {
            Aeonicraft.logger.info(tn);
        }
        return false;
    }

    @Override
    public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        return false;
    }

    @Override
    public boolean isBlockNormalCube(IBlockState blockState) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState blockState) {
        return false;
    }

    @Override
    public long getTCGenerationPerSecond() {
        return TCToRealTime.SECOND;
    }

    @Override
    public long getTCCapacity() {
        return 0;
    }

    @Override
    public boolean canConnectOnSide(EnumFacing mySide) {
        return true;
    }
}
