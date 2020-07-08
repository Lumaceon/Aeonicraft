package lumaceon.mods.aeonicraft.item;

import lumaceon.mods.aeonicraft.Aeonicraft;
import lumaceon.mods.aeonicraft.api.hourglass.HourglassFunction;
import lumaceon.mods.aeonicraft.api.temporal.temporalnetwork.ITemporalNetworkBlock;
import lumaceon.mods.aeonicraft.api.temporal.temporalnetwork.TemporalNetworkGenerationStats;
import lumaceon.mods.aeonicraft.api.temporal.temporalnetwork.TemporalNetwork;
import lumaceon.mods.aeonicraft.api.util.BlockLoc;
import lumaceon.mods.aeonicraft.capability.CapabilityHourglass;
import lumaceon.mods.aeonicraft.capability.CapabilityTemporalNetworkLinker;
import lumaceon.mods.aeonicraft.util.*;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@SuppressWarnings("NullableProblems")
public class ItemTemporalHourglass extends ItemAeonicraft
{
    @CapabilityInject(CapabilityHourglass.IHourglassHandler.class)
    public static final Capability<CapabilityHourglass.IHourglassHandler> HOURGLASS = null;

    @CapabilityInject(CapabilityTemporalNetworkLinker.ITemporalNetworkLinker.class)
    public static final Capability<CapabilityTemporalNetworkLinker.ITemporalNetworkLinker> NETWORK_LINKER = null;

    public ItemTemporalHourglass(int maxStack, int maxDamage, String name) {
        super(maxStack, maxDamage, name);
    }

    /**
     * Changes the current function of the hourglass.
     * @param shift Relative number to shift forward (or backward if negative).
     */
    public void shiftFunction(EntityPlayer player, ItemStack stack, int shift)
    {
        CapabilityHourglass.IHourglassHandler cap = stack.getCapability(HOURGLASS, null);
        if(cap != null)
        {
            while(shift > 0)
            {
                cap.cycleActiveFunction(player, true);
                shift--;
            }

            while(shift < 0)
            {
                cap.cycleActiveFunction(player, false);
                shift++;
            }
        }
    }

    @Nullable
    public HourglassFunction getActiveHourglassFunction(ItemStack stack)
    {
        CapabilityHourglass.IHourglassHandler cap = stack.getCapability(HOURGLASS, null);
        if(cap != null)
            return cap.getActiveFunction();
        return null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
        //tooltip.add(Colors.AQUA + "TC: " + TCToRealTime.parseTimeValue(availableTime(stack, null, Side.CLIENT), 2));
    }

    @SuppressWarnings("deprecation")
    @Override
    public String getItemStackDisplayName(ItemStack stack)
    {
        CapabilityHourglass.IHourglassHandler hg = stack.getCapability(HOURGLASS, null);
        if(hg != null)
        {
            HourglassFunction hourglassFunction = hg.getActiveFunction();
            if(hourglassFunction != null)
            {
                return I18n.translateToLocal(this.getUnlocalizedNameInefficiently(stack) + ".name").trim()
                        + " " + I18n.translateToLocal(hourglassFunction.getRegistryName().toString() + ".name").trim();
            }
        }
        return I18n.translateToLocal(this.getUnlocalizedNameInefficiently(stack) + ".name").trim();
    }

    public boolean showDurabilityBar(ItemStack stack) {
        return false;
    }

    /**
     * Queries the percentage of the 'Durability' bar that should be drawn.
     *
     * @param stack The current ItemStack
     * @return 0.0 for 100% (no damage / full bar), 1.0 for 0% (fully damaged / empty bar)
     */
    public double getDurabilityForDisplay(ItemStack stack)
    {
        /*CapabilityTimeLink.ITimeLinkHandler cap = stack.getCapability(TIME_LINK, null);
        if(cap != null) {
            return 1.0f - (cap.getTimeClient() / 60000.0F);
        }*/
        return 1.0F;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack stack = playerIn.getHeldItem(handIn);
        CapabilityHourglass.IHourglassHandler hg = stack.getCapability(HOURGLASS, null);
        if(hg != null)
        {
            HourglassFunction func = hg.getActiveFunction();
            if(func != null)
            {
                return func.onHourglassRightClick(worldIn, playerIn, handIn);
            }
            else
            {
                //playerIn.openGui(Aeonicraft.instance, GUIs.TEMPORAL_HOURGLASS.ordinal(), worldIn, (int) playerIn.posX, (int) playerIn.posY, (int) playerIn.posZ);
            }
        }
        return new ActionResult<>(EnumActionResult.PASS, stack);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        ItemStack stack = InventoryHelper.getFirstStackOfTypeInInventory(player.inventory, this);
        if(stack == null)
            return EnumActionResult.FAIL;

        Block block = world.getBlockState(pos).getBlock();
        if(block instanceof ITemporalNetworkBlock && ((ITemporalNetworkBlock) block).isRelay())
        {
            CapabilityTemporalNetworkLinker.ITemporalNetworkLinker linker = stack.getCapability(NETWORK_LINKER, facing);
            if(linker != null)
            {
                BlockLoc otherRelay = linker.getRelayLocation();
                BlockLoc thisRelay = new BlockLoc(pos, world);
                if(otherRelay == null)
                {
                    linker.setRelayLocation(new BlockLoc(pos, world));
                    player.sendMessage(new TextComponentString("Hourglass linking to relay."));
                }
                else
                {
                    TemporalNetwork network = TemporalNetwork.getTemporalNetwork(otherRelay);
                    if(network == null)
                    {
                        linker.setRelayLocation(null);
                        player.sendMessage(new TextComponentString("The first relay was not found: stopped linking."));
                    }
                    else
                    {
                        TemporalNetworkGenerationStats.TemporalNetworkLocationStats stats = network.generationStats.getLocationFromSide(otherRelay, null);
                        if(stats == null)
                        {
                            player.sendMessage(new TextComponentString("Something went wrong: please contact the mod author."));
                            Aeonicraft.logger.error("Missing Temporal Network block error:");
                            Aeonicraft.logger.error(otherRelay.toString());
                            for (TemporalNetworkGenerationStats.TemporalNetworkLocationStats stat : network.generationStats.getLocations()) {
                                Aeonicraft.logger.error(stat.getLocation().toString());
                            }
                        }
                        else if(stats.getForcedAdjacencies().contains(thisRelay))
                        {
                            linker.setRelayLocation(null);
                            player.sendMessage(new TextComponentString("The two relays are already connected. Stopped linking."));
                        }
                        else
                        {
                            network.generationStats.createForcedAdjacency(thisRelay, otherRelay);
                            linker.setRelayLocation(null);
                            player.sendMessage(new TextComponentString("Relays Connected~! Stopped linking."));
                        }
                    }
                }
                return EnumActionResult.SUCCESS;
            }
        }

        CapabilityHourglass.IHourglassHandler hg = stack.getCapability(HOURGLASS, null);

        if(!world.isRemote)
        {
            if(hg != null)
            {
                Aeonicraft.logger.info(hg.getActiveFunction());
            }
        }

        if(hg != null)
        {
            HourglassFunction func = hg.getActiveFunction();
            if(func != null)
            {
                return func.onHourglassBlockRightClick(player, world, pos, hand, facing, hitX, hitY, hitZ);
            }
        }

        return EnumActionResult.PASS;
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected)
    {
        CapabilityHourglass.IHourglassHandler hgcap = stack.getCapability(HOURGLASS, null);
        if(hgcap != null)
        {
            if(world.isRemote)
                hgcap.checkForUpdatesClientSide(itemSlot);

            HourglassFunction function = hgcap.getActiveFunction();
            if(function != null)
                function.onUpdate(stack, world, entity, itemSlot, isSelected);
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
        CapabilityHourglass.IHourglassHandler hourglassHandler;
        CapabilityTemporalNetworkLinker.ITemporalNetworkLinker networkLinker;

        private Provider()
        {
            hourglassHandler = new CapabilityHourglass.HourglassHandler();
            networkLinker = new CapabilityTemporalNetworkLinker.TemporalNetworkLinker();
        }

        @Override
        public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
            return capability == HOURGLASS || capability == NETWORK_LINKER;
        }

        @Override
        public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing)
        {
            if(capability == HOURGLASS) {
                return HOURGLASS.cast(hourglassHandler);
            } else if(capability == NETWORK_LINKER) {
                return NETWORK_LINKER.cast(networkLinker);
            }
            return null;
        }

        @Override
        public NBTTagCompound serializeNBT()
        {
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setTag("hourglass_data", hourglassHandler.saveToNBT());

            BlockLoc loc = networkLinker.getRelayLocation();
            if(loc != null)
                nbt.setTag("network_linker_tag", networkLinker.getRelayLocation().serializeToNBT());

            return nbt;
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt)
        {
            if(nbt.hasKey("network_linker_tag"))
                this.networkLinker.setRelayLocation(new BlockLoc(nbt.getCompoundTag("network_linker_tag")));

            hourglassHandler.loadFromNBT(nbt.getCompoundTag("hourglass_data"));
        }
    }
}
