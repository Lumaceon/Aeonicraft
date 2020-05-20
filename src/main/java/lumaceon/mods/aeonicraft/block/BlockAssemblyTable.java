package lumaceon.mods.aeonicraft.block;

import lumaceon.mods.aeonicraft.Aeonicraft;
import lumaceon.mods.aeonicraft.lib.GUIs;
import lumaceon.mods.aeonicraft.registry.ModItems;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

@SuppressWarnings("ALL")
public class BlockAssemblyTable extends BlockAeonicraft
{
    public static final PropertyEnum<EnumPartType> PART = PropertyEnum.create("part", EnumPartType.class);

    public BlockAssemblyTable(Material blockMaterial, String registryName)
    {
        super(blockMaterial, registryName);
        this.setDefaultState(this.blockState.getBaseState().withProperty(CustomProperties.FACING_HORIZONTAL, EnumFacing.NORTH).withProperty(PART, EnumPartType.LEFT));
        this.setCreativeTab(null);
        this.setHardness(3.0F);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        super.breakBlock(worldIn, pos, state);
        EnumFacing enumfacing = state.getValue(CustomProperties.FACING_HORIZONTAL);
        if(state.getValue(PART) == EnumPartType.RIGHT)
        {
            if(worldIn.getBlockState(pos.offset(enumfacing)).getBlock() == this)
            {
                worldIn.setBlockToAir(pos.offset(enumfacing));
            }
        }
        else if(worldIn.getBlockState(pos.offset(enumfacing.getOpposite())).getBlock() == this)
        {
            worldIn.setBlockToAir(pos.offset(enumfacing.getOpposite()));
        }
    }

    private static final AxisAlignedBB TABLE_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D - 3.0/16.0, 1.0D);

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return TABLE_AABB;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing enumfacing = EnumFacing.getHorizontal(meta);
        return (meta & 8) > 0 ? this.getDefaultState().withProperty(PART, EnumPartType.RIGHT).withProperty(CustomProperties.FACING_HORIZONTAL, enumfacing) : this.getDefaultState().withProperty(PART, EnumPartType.LEFT).withProperty(CustomProperties.FACING_HORIZONTAL, enumfacing);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int i = 0;
        i = i | state.getValue(CustomProperties.FACING_HORIZONTAL).getHorizontalIndex();

        if(state.getValue(PART) == EnumPartType.RIGHT)
            i |= 8;
        return i;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, CustomProperties.FACING_HORIZONTAL, PART);
    }

    @Override
    public boolean canPlaceBlockAt(World world, BlockPos pos) {
        return world.getBlockState(pos) == null || world.getBlockState(pos).getBlock() == null || world.getBlockState(pos).getBlock().isReplaceable(world, pos);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return ModItems.assembly_table_item;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if(!player.isSneaking())
        {
            if(!world.isRemote)
                player.openGui(Aeonicraft.instance, GUIs.ASSEMBLY_TABLE.ordinal(), world, pos.getX(), pos.getY(), pos.getZ());
            return true;
        }
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public EnumPushReaction getMobilityFlag(IBlockState state) {
        return EnumPushReaction.BLOCK;
    }

    public enum EnumPartType implements IStringSerializable
    {
        LEFT("left"),
        RIGHT("right");

        private final String name;

        private EnumPartType(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }

        public String getName() {
            return this.name;
        }
    }
}
