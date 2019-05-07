package lumaceon.mods.aeonicraft.item;

import lumaceon.mods.aeonicraft.Aeonicraft;
import lumaceon.mods.aeonicraft.api.IHourglassFunction;
import lumaceon.mods.aeonicraft.capability.hourglass.CapabilityHourglass;
import lumaceon.mods.aeonicraft.capability.timelink.CapabilityTimeLink;
import lumaceon.mods.aeonicraft.entity.EntityTravelGhost;
import lumaceon.mods.aeonicraft.network.PacketHandler;
import lumaceon.mods.aeonicraft.network.message.MessageHourglassTCUpdate;
import lumaceon.mods.aeonicraft.temporalcompression.ITemporalCompressorLinkableBlock;
import lumaceon.mods.aeonicraft.temporalcompression.TemporalCompressor;
import lumaceon.mods.aeonicraft.util.BlockLoc;
import lumaceon.mods.aeonicraft.util.Colors;
import lumaceon.mods.aeonicraft.util.InventoryHelper;
import lumaceon.mods.aeonicraft.util.TimeParser;
import lumaceon.mods.aeonicraft.worlddata.ExtendedWorldData;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("NullableProblems")
public class ItemTemporalHourglass extends ItemAeonicraft
{
    @CapabilityInject(CapabilityTimeLink.ITimeLinkHandler.class)
    private static final Capability<CapabilityTimeLink.ITimeLinkHandler> TIME_LINK = null;
    @CapabilityInject(CapabilityHourglass.IHourglassHandler.class)
    public static final Capability<CapabilityHourglass.IHourglassHandler> HOURGLASS = null;

    public ItemTemporalHourglass(int maxStack, int maxDamage, String name) {
        super(maxStack, maxDamage, name);
    }

    @Nullable
    public IHourglassFunction getActiveHourglassFunction(ItemStack stack)
    {
        CapabilityHourglass.IHourglassHandler cap = stack.getCapability(HOURGLASS, null);
        if(cap != null)
            return cap.getActiveFunction();
        return null;
    }

    public long availableTime(ItemStack stack, @Nullable World world, Side side)
    {
        if(side.isServer())
        {
            CapabilityTimeLink.ITimeLinkHandler cap = stack.getCapability(TIME_LINK, null);
            if(cap != null) {
                return cap.getTimeServer(world);
            }
        }
        else
        {
            CapabilityTimeLink.ITimeLinkHandler cap = stack.getCapability(TIME_LINK, null);
            if(cap != null) {
                return cap.getTimeClient();
            }
        }
        return 0;
    }

    /**
     * To be called server-side, which will automatically update the client. Can be called client-side, in which case
     * this will be a no-op method which returns 0.
     * @param indexOfStack Index of the hourglass within the containing inventory.
     * @return The amount of time actually consumed.
     */
    public long consumeTime(EntityPlayer player, ItemStack stack, World world, int indexOfStack, long maxConsume)
    {
        if(!world.isRemote)
        {
            CapabilityTimeLink.ITimeLinkHandler cap = stack.getCapability(TIME_LINK, null);
            if(cap != null)
            {
                ExtendedWorldData worldData = ExtendedWorldData.getInstance(world);
                BlockLoc[] locs = cap.getCompressorLocations();
                ArrayList<TemporalCompressor> tcs = new ArrayList<>(locs.length);
                ArrayList<TemporalCompressor> temporalCompressorsCopy = new ArrayList<>(tcs);
                for(BlockLoc loc : locs)
                {
                    TemporalCompressor tc = worldData.temporalCompressorProcesses.get(loc);
                    if(tc != null)
                        tcs.add(tc);
                }

                tcs.sort((o1, o2) -> {
                    long diff = o1.getRemainingSpaceInMilliseconds() - o2.getRemainingSpaceInMilliseconds();
                    return diff == 0 ? 0 : diff < 0 ? -1 : 1;
                });

                long amountConsumed = 0;
                int i = 0;
                while(amountConsumed < maxConsume && i < tcs.size())
                {
                    TemporalCompressor tc = tcs.get(i);
                    amountConsumed += tc.consumeTime(maxConsume - amountConsumed);
                    i++;
                }

                if(amountConsumed != 0)
                {
                    worldData.markDirty();
                    PacketHandler.INSTANCE.sendTo(new MessageHourglassTCUpdate(temporalCompressorsCopy.toArray(new TemporalCompressor[0]), indexOfStack), (EntityPlayerMP) player);
                }
                return amountConsumed;
            }
        }

        return 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
        tooltip.add(Colors.AQUA + "TC: " + TimeParser.parseTimeValue(availableTime(stack, null, Side.CLIENT), 2));
    }

    @SuppressWarnings("deprecation")
    @Override
    public String getItemStackDisplayName(ItemStack stack)
    {
        CapabilityHourglass.IHourglassHandler hg = stack.getCapability(HOURGLASS, null);
        if(hg != null)
        {
            IHourglassFunction hourglassFunction = hg.getActiveFunction();
            if(hourglassFunction != null)
            {
                return I18n.translateToLocal(this.getUnlocalizedNameInefficiently(stack) + ".name").trim()
                        + " " + I18n.translateToLocal(hourglassFunction.getRegistryIDString() + ".name").trim();
            }
        }
        return I18n.translateToLocal(this.getUnlocalizedNameInefficiently(stack) + ".name").trim();
    }

    public boolean showDurabilityBar(ItemStack stack) {
        return true;
    }

    /**
     * Queries the percentage of the 'Durability' bar that should be drawn.
     *
     * @param stack The current ItemStack
     * @return 0.0 for 100% (no damage / full bar), 1.0 for 0% (fully damaged / empty bar)
     */
    public double getDurabilityForDisplay(ItemStack stack)
    {
        CapabilityTimeLink.ITimeLinkHandler cap = stack.getCapability(TIME_LINK, null);
        if(cap != null) {
            return 1.0f - (cap.getTimeClient() / 60000.0F);
        }
        return 1.0F;
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        Block block = world.getBlockState(pos).getBlock();
        if(block instanceof ITemporalCompressorLinkableBlock)
        {
            if(!world.isRemote) {
                ((ITemporalCompressorLinkableBlock) block).onLinkAttempt(player, world, pos, hand, facing, hitX, hitY, hitZ);
            }
            return EnumActionResult.SUCCESS;
        }

        if(!world.isRemote)
        {
            ItemStack stack = InventoryHelper.getFirstStackOfTypeInInventory(player.inventory, this);
            assert stack != null;
            CapabilityHourglass.IHourglassHandler hg = stack.getCapability(HOURGLASS, null);
            if(hg != null)
            {
                Aeonicraft.logger.info(hg.getActiveFunction());
            }


            CapabilityTimeLink.ITimeLinkHandler cap = stack.getCapability(TIME_LINK, null);
            if(cap != null) {
                Aeonicraft.logger.info("Time (server): " + cap.getTimeServer(world));
                Aeonicraft.logger.info("Should be 0: " + cap.getTimeClient());
                for(BlockLoc l : cap.getCompressorLocations())
                {
                    Aeonicraft.logger.info("Compressor" + l.toString());
                }
            }

            world.spawnEntity(new EntityTravelGhost(world, player));
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
            if(!world.isRemote)
            {
                boolean isDirty = false;

                BlockLoc[] locs = cap.getCompressorLocations();
                ArrayList<TemporalCompressor> tcs = new ArrayList<>();
                for(BlockLoc loc : locs)
                {
                    TemporalCompressor tc = worldData.temporalCompressorProcesses.get(loc);
                    if(tc != null)
                    {
                        tcs.add(tc);
                        if(worldData.updateCompressorAt(loc, world))
                        {
                            isDirty = true;
                        }
                    }
                    else
                    {
                        Aeonicraft.logger.info("Hourglass searched for null compressor at: ("+loc.toString()+")");
                    }
                }

                if(isDirty && entity instanceof EntityPlayerMP)
                {
                    PacketHandler.INSTANCE.sendTo(new MessageHourglassTCUpdate(tcs.toArray(new TemporalCompressor[0]), itemSlot), (EntityPlayerMP) entity);
                }
            }
        }
        CapabilityHourglass.IHourglassHandler hgcap = stack.getCapability(HOURGLASS, null);
        if(hgcap != null && world.isRemote)
        {
            hgcap.checkForUpdatesClientSide(itemSlot);
        }
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged)
    {
        CapabilityHourglass.IHourglassHandler cap = newStack.getCapability(HOURGLASS, null);
        if(!(cap instanceof CapabilityHourglass.HourglassHandler))
        {
            return !oldStack.equals(newStack);
        }

        if(((CapabilityHourglass.HourglassHandler) cap).reequipAnimation)
        {
            ((CapabilityHourglass.HourglassHandler) cap).reequipAnimation = false;
            return true;
        }
        return !oldStack.equals(newStack);
    }

    @Override
    public net.minecraftforge.common.capabilities.ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
        return new Provider();
    }


    private static class Provider implements ICapabilitySerializable<NBTTagCompound>
    {
        CapabilityTimeLink.ITimeLinkHandler timeLinkHandler;
        CapabilityHourglass.IHourglassHandler hourglassHandler;

        private Provider()
        {
            timeLinkHandler = new CapabilityTimeLink.TimeLinkHandler();
            hourglassHandler = new CapabilityHourglass.HourglassHandler();
            //Init stuff if necessary...
        }

        @Override
        public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
            return capability == TIME_LINK || capability == HOURGLASS;
        }

        @Override
        public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing)
        {
            if(capability == TIME_LINK) {
                return TIME_LINK.cast(timeLinkHandler);
            } else if(capability == HOURGLASS) {
                return HOURGLASS.cast(hourglassHandler);
            }
            return null;
        }

        @Override
        public NBTTagCompound serializeNBT()
        {
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setTag("time_link", timeLinkHandler.saveToNBT());
            nbt.setTag("hourglass_data", hourglassHandler.saveToNBT());
            return nbt;
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt)
        {
            timeLinkHandler.loadFromNBT(nbt.getCompoundTag("time_link"));
            hourglassHandler.loadFromNBT(nbt.getCompoundTag("hourglass_data"));
        }
    }
}
