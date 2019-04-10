package lumaceon.mods.aeonicraft.item;

import lumaceon.mods.aeonicraft.Aeonicraft;
import lumaceon.mods.aeonicraft.capability.timelink.CapabilityTimeLink;
import lumaceon.mods.aeonicraft.temporalcompression.ITemporalCompressorLinkableBlock;
import lumaceon.mods.aeonicraft.util.BlockLoc;
import lumaceon.mods.aeonicraft.worlddata.ExtendedWorldData;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SuppressWarnings("NullableProblems")
public class ItemTemporalHourglass extends ItemAeonicraft
{
    @CapabilityInject(CapabilityTimeLink.ITimeLinkHandler.class)
    private static final Capability<CapabilityTimeLink.ITimeLinkHandler> TIME_LINK = null;

    public ItemTemporalHourglass(int maxStack, int maxDamage, String name) {
        super(maxStack, maxDamage, name);
    }


    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        Block block = world.getBlockState(pos).getBlock();
        if(block instanceof ITemporalCompressorLinkableBlock)
        {
            ((ITemporalCompressorLinkableBlock) block).onLinkAttempt(player, world, pos, hand, facing, hitX, hitY, hitZ);
            return EnumActionResult.SUCCESS;
        }

        ExtendedWorldData worldData = ExtendedWorldData.getInstance(world);

        ItemStack hourglassInHand = player.getHeldItem(hand);
        CapabilityTimeLink.ITimeLinkHandler cap = hourglassInHand.getCapability(TIME_LINK, null);
        if(cap != null)
        {
            BlockLoc[] locs = cap.getCompressorLocations();
            for(BlockLoc loc : locs)
            {
                Aeonicraft.logger.info(worldData.temporalCompressorProcesses.get(loc).getLocation().toString());
            }
        }
        return EnumActionResult.PASS;
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected)
    {
        ExtendedWorldData worldData = ExtendedWorldData.getInstance(world);
        CapabilityTimeLink.ITimeLinkHandler cap = stack.getCapability(TIME_LINK, null);
        if(cap != null)
        {
            BlockLoc[] locs = cap.getCompressorLocations();
            for(BlockLoc loc : locs)
            {
                //TODO - check for side-specific NullPointerExceptions
                worldData.temporalCompressorProcesses.get(loc).updateTick(world);
            }
        }
    }

    @Override
    public net.minecraftforge.common.capabilities.ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
        return new Provider();
    }


    private static class Provider implements ICapabilitySerializable<NBTTagCompound>
    {
        CapabilityTimeLink.ITimeLinkHandler timeLinkHandler;

        private Provider()
        {
            timeLinkHandler = new CapabilityTimeLink.TimeLinkHandler();
            //Init stuff if necessary...
        }

        @Override
        public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
            return capability == TIME_LINK;
        }

        @Override
        public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing)
        {
            if(capability == TIME_LINK)
            {
                return TIME_LINK.cast(timeLinkHandler);
            }
            return null;
        }

        @Override
        public NBTTagCompound serializeNBT()
        {
            return timeLinkHandler.saveToNBT();
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt)
        {
            timeLinkHandler.loadFromNBT(nbt);
        }
    }
}
