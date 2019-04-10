package lumaceon.mods.aeonicraft.block;

import lumaceon.mods.aeonicraft.Aeonicraft;
import lumaceon.mods.aeonicraft.capability.timelink.CapabilityTimeLink;
import lumaceon.mods.aeonicraft.temporalcompression.ITemporalCompressorLinkableBlock;
import lumaceon.mods.aeonicraft.temporalcompression.TemporalCompressor;
import lumaceon.mods.aeonicraft.util.BlockLoc;
import lumaceon.mods.aeonicraft.worlddata.ExtendedWorldData;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

@SuppressWarnings("NullableProblems")
public class BlockTemporalCompressor extends BlockAeonicraft implements ITemporalCompressorLinkableBlock
{
    @CapabilityInject(CapabilityTimeLink.ITimeLinkHandler.class)
    private static final Capability<CapabilityTimeLink.ITimeLinkHandler> TIME_LINK = null;

    public BlockTemporalCompressor(Material blockMaterial, String name) {
        super(blockMaterial, name);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        ExtendedWorldData worldData = ExtendedWorldData.getInstance(worldIn);
        worldData.registerTemporalCompressor(new TemporalCompressor(60000, 5, new BlockLoc(pos, worldIn)));
        Aeonicraft.logger.info("Registered.");
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        super.breakBlock(worldIn, pos, state);
        ExtendedWorldData worldData = ExtendedWorldData.getInstance(worldIn);
        worldData.removeTemporalCompressorAt(new BlockLoc(pos, worldIn));
        Aeonicraft.logger.info("breakBlock called for BlockTemporalCompressor");
    }

    @Override
    public void onLinkAttempt(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack hourglassInHand = player.getHeldItem(hand);
        CapabilityTimeLink.ITimeLinkHandler cap = hourglassInHand.getCapability(TIME_LINK, null);
        if(cap != null)
        {
            cap.addCompressorLocationIfUnique(new BlockLoc(pos, world));
        }
    }
}
