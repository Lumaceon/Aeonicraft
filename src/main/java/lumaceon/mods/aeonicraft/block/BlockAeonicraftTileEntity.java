package lumaceon.mods.aeonicraft.block;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;

public abstract class BlockAeonicraftTileEntity extends BlockAeonicraft implements ITileEntityProvider
{
    public BlockAeonicraftTileEntity(Material blockMaterial, String name) {
        super(blockMaterial, name);
    }

    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }
}
