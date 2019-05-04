package lumaceon.mods.aeonicraft.block;

import lumaceon.mods.aeonicraft.api.IHourglassFunction;
import lumaceon.mods.aeonicraft.capability.hourglass.CapabilityHourglass;
import lumaceon.mods.aeonicraft.init.ModItems;
import lumaceon.mods.aeonicraft.tile.TileHourglassProgrammer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockHourglassProgrammer extends BlockAeonicraft implements ITileEntityProvider
{
    public BlockHourglassProgrammer(Material blockMaterial, String name) {
        super(blockMaterial, name);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        TileHourglassProgrammer hourglassProgrammer = (TileHourglassProgrammer) worldIn.getTileEntity(pos);
        if(hourglassProgrammer != null)
        {
            ItemStack stackInHand = playerIn.inventory.getCurrentItem();
            if(!hourglassProgrammer.hourglass.isEmpty())
            {
                if(stackInHand.isEmpty())
                {
                    playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, hourglassProgrammer.hourglass);
                    hourglassProgrammer.hourglass = ItemStack.EMPTY;
                    return true;
                }
                else if(stackInHand.getItem() instanceof IHourglassFunction)
                {
                    CapabilityHourglass.IHourglassHandler cap = hourglassProgrammer.hourglass.getCapability(CapabilityHourglass.HOURGLASS, null);
                    if(cap != null)
                    {
                        if(cap.addHourglassStack(stackInHand))
                        {
                            playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, ItemStack.EMPTY);
                            return true;
                        }
                    }
                }
            }
            else
            {
                if(stackInHand.getItem().equals(ModItems.temporalHourglass))
                {
                    hourglassProgrammer.hourglass = stackInHand;
                    playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, ItemStack.EMPTY);
                    return true;
                }
            }
        }
        return false;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileHourglassProgrammer();
    }
}
