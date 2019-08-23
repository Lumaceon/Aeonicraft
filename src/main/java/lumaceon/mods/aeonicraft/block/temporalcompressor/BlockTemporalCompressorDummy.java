package lumaceon.mods.aeonicraft.block.temporalcompressor;

import lumaceon.mods.aeonicraft.api.temporalcompression.ITemporalCompressorComponentItem;
import lumaceon.mods.aeonicraft.api.temporalcompression.TemporalCompressorComponent;
import lumaceon.mods.aeonicraft.block.BlockAeonicraft;
import lumaceon.mods.aeonicraft.tile.TileTemporalCompressor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

// TODO temporary solutions until these are automatically placed and can be handled more appropriately...
public class BlockTemporalCompressorDummy extends BlockAeonicraft
{
    public BlockTemporalCompressorDummy(Material blockMaterial, String name) {
        super(blockMaterial, name);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        ItemStack stackInHand = playerIn.getHeldItem(hand);
        if(stackInHand.getItem() instanceof ITemporalCompressorComponentItem)
        {
            TemporalCompressorComponent component = ((ITemporalCompressorComponentItem) stackInHand.getItem()).getTemporalCompressorComponent(stackInHand);
            if(component != null)
            {
                // Check for an actual compressor nearby...
                TileEntity te;
                int initialX = pos.getX();
                int initialZ = pos.getZ();
                for(int x = initialX - 5; x <= initialX + 5; x++)
                {
                    for(int z = initialZ - 5; z <= initialZ + 5; z++)
                    {
                        if(x == 0 && z == 0)
                            continue;

                        te = worldIn.getTileEntity(pos.add(x, 0, z));
                        //noinspection ConstantConditions
                        if(te instanceof TileTemporalCompressor)
                        {
                            ItemStack stack = ((TileTemporalCompressor) te).requestTemporalComponentChange(component, -(x - initialX), -(z - initialZ));
                            playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, stack);
                        }
                    }
                }
            }
        }
        return false;
    }
}
