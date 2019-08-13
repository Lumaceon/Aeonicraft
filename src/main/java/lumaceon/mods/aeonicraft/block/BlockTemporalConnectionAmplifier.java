package lumaceon.mods.aeonicraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Random;

@SuppressWarnings("deprecation")
public class BlockTemporalConnectionAmplifier extends BlockAeonicraft
{
    public static final PropertyEnum<ConnectionEnum> CONNECTION = PropertyEnum.create("connections", ConnectionEnum.class);
    public BlockTemporalConnectionAmplifier(Material blockMaterial, String name) {
        super(blockMaterial, name);
        this.setDefaultState(this.blockState.getBaseState().withProperty(CONNECTION, ConnectionEnum.SINGLE));
        this.setLightLevel(0.7F);
        this.setLightOpacity(0);
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        /*Vec3d target = null;
        for(int i = 1; i < 255; i++)
        {
            if(!worldIn.getBlockState(pos.up(i)).getBlock().equals(this)) {
                target = new Vec3d(pos.up(i)).addVector(0.5, 0.5, 0.5);
                break;
            }
        }

        if(target != null)
        {
            ParticleHelper.spawnTemporalBurstParticles(target, new Vec3d(0.01, 0.01, 0.01), 3);
        }*/
    }

    @Override
    public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {
        return false;
    }

    @Nonnull
    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        IBlockState up = worldIn.getBlockState(pos.up());
        IBlockState down = worldIn.getBlockState(pos.down());
        boolean isAbove = up.getBlock().equals(this);
        boolean isBelow = down.getBlock().equals(this);

        if(isAbove && isBelow) {
            return state.withProperty(CONNECTION, ConnectionEnum.MIDDLE);
        }
        if(isAbove) {
            return state.withProperty(CONNECTION, ConnectionEnum.BOTTOM);
        }
        if(isBelow) {
            return state.withProperty(CONNECTION, ConnectionEnum.TOP);
        }
        return state.withProperty(CONNECTION, ConnectionEnum.SINGLE);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, CONNECTION);
    }

    private enum ConnectionEnum implements IStringSerializable {
        SINGLE("single"), MIDDLE("middle"), TOP("top"), BOTTOM("bottom");

        private final String name;

        ConnectionEnum(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }
}
