package lumaceon.mods.aeonicraft.item.clockwork;

import lumaceon.mods.aeonicraft.api.clockwork.ClockworkComponentTypes;
import lumaceon.mods.aeonicraft.api.clockwork.IClockworkComponentItem;
import lumaceon.mods.aeonicraft.api.clockwork.baseStats.tooltip.IClockworkTooltips;
import lumaceon.mods.aeonicraft.api.clockwork.baseStats.*;
import lumaceon.mods.aeonicraft.item.ItemAeonicraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemClockworkComponent extends ItemAeonicraft implements IClockworkComponentItem, IClockworkTooltips
{
    public ItemClockworkComponent(int maxStack, int maxDamage, String name, float progress, float windupCost, float efficiency) {
        super(maxStack, maxDamage, name);

        //Initialize values of the clockwork matrix variables, if one isn't used simply use 0. Decided for this possibility for now
        //ToDo: Better way?
        this.progress = BaseStatBuilder.getNewProgressStatInstance(progress);
        this.windupCost = BaseStatBuilder.getNewWindupStatInstance(windupCost);
        this.efficiency = BaseStatBuilder.getNewEfficiencyStatInstance(efficiency);
    }

    //Different values that are important for the clockworkMatrix
    //ToDo: Better way?
    //ToDO: Add the enum type
    private ClockworkBaseStat  progress;
    private ClockworkBaseStat windupCost;
    private ClockworkBaseStat  windUpMaxMod;
    private ClockworkBaseStat  efficiency;

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);

        tooltip.addAll(getTooltip(getClockworkStatCollection()));

    }

    @Override
    public ClockworkBaseStat getProgress() {
        return progress;
    }

    @Override
    public ClockworkBaseStat getWindupCost() { return windupCost; }
    @Override
    public ClockworkBaseStat  getEfficiency() {
        return efficiency;
    }

    @Override
    public ClockworkComponentTypes getType() {
        return ClockworkComponentTypes.GEAR;
    }
}
