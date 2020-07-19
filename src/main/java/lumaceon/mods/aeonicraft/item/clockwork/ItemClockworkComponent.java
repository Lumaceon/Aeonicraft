package lumaceon.mods.aeonicraft.item.clockwork;

import lumaceon.mods.aeonicraft.api.clockwork.ClockworkComponentTypes;
import lumaceon.mods.aeonicraft.api.clockwork.IClockworkComponentItem;
import lumaceon.mods.aeonicraft.api.clockwork.IClockworkTooltip;
import lumaceon.mods.aeonicraft.api.clockwork.baseStats.*;
import lumaceon.mods.aeonicraft.item.ItemAeonicraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemClockworkComponent extends ItemAeonicraft implements IClockworkComponentItem, IClockworkTooltip
{
    public ItemClockworkComponent(int maxStack, int maxDamage, String name, float progress, float windupCost, float efficiency, float windUpMaxMod) {
        super(maxStack, maxDamage, name);

        //Initialize values of the clockwork matrix variables, if one isn't used simply use 0. Decided for this possibility for now
        //ToDo: Better way?
        this.progress = new ClockworkProgressStat(progress);
        this.windUpCost = new ClockworkWindUpStat(windupCost);
        this.windUpMaxMod = new ClockworkMaxWindUpStat(windUpMaxMod);
        this.efficiency = new ClockworkEfficiencyStat(efficiency);
    }

    //Different values that are important for the clockworkMatrix
    //ToDo: Better way?
    //ToDO: Add the enum type
    private ClockworkProgressStat progress;
    private ClockworkWindUpStat windUpCost;
    private ClockworkMaxWindUpStat windUpMaxMod;
    private ClockworkEfficiencyStat efficiency;

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);

        tooltip = getTooltip(getClockworkStatCollection(), tooltip);

    }

    @Override
    public ClockworkProgressStat getProgress() {
        return progress;
    }

    @Override
    public ClockworkWindUpStat getWindUpCost() {
        return windUpCost;
    }

    @Override
    public ClockworkMaxWindUpStat getWindUpMaxMod() {
        return windUpMaxMod;
    }

    @Override
    public ClockworkEfficiencyStat getEfficiency() {
        return efficiency;
    }

    @Override
    public ClockworkComponentTypes getType() {
        return ClockworkComponentTypes.GEAR;
    }
}
