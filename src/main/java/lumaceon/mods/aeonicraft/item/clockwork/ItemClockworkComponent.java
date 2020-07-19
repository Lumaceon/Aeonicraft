package lumaceon.mods.aeonicraft.item.clockwork;

import lumaceon.mods.aeonicraft.api.clockwork.ClockworkComponentTypes;
import lumaceon.mods.aeonicraft.api.clockwork.IClockworkComponentItem;
import lumaceon.mods.aeonicraft.api.clockwork.baseStats.*;
import lumaceon.mods.aeonicraft.item.ItemAeonicraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemClockworkComponent extends ItemAeonicraft implements IClockworkComponentItem
{
    public ItemClockworkComponent(int maxStack, int maxDamage, String name, float progress, float windupCost, float efficiency, float windUpMaxMod) {
        super(maxStack, maxDamage, name);
        this.progress = new ClockworkProgressStat(progress);
        this.windUpCost = new ClockworkWindUpStat(windupCost);
        this.windUpMaxMod = new ClockworkMaxWindUpStat(windUpMaxMod);
        this.efficiency = new ClockworkEfficiencyStat(efficiency);
    }

    private ClockworkProgressStat progress;
    private ClockworkWindUpStat windUpCost;
    private ClockworkMaxWindUpStat windUpMaxMod;
    private ClockworkEfficiencyStat efficiency;

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);

        ClockworkBaseStat[] stats = {progress, windUpCost,windUpMaxMod,efficiency};

        for(ClockworkBaseStat stat : stats){
            if(stat.StatValue != 0){
                tooltip.add(getTooltip(stat));
            }
        }
    }

    private String getTooltip(ClockworkBaseStat stat){

        String returnValue = "§l"+stat.getColorCode()+stat.getStatName()+":§r " + stat.StatValue;
        return returnValue;
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
