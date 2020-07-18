package lumaceon.mods.aeonicraft.item.clockwork;

import lumaceon.mods.aeonicraft.api.clockwork.IClockworkComponentItem;
import lumaceon.mods.aeonicraft.api.clockwork.baseStats.ClockworkBaseStat;
import lumaceon.mods.aeonicraft.api.clockwork.baseStats.ClockworkEfficiencyStat;
import lumaceon.mods.aeonicraft.api.clockwork.baseStats.ClockworkMaxWindUpStat;
import lumaceon.mods.aeonicraft.api.clockwork.baseStats.ClockworkWindUpStat;
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
        this.progress = new ClockworkEfficiencyStat(progress);
        this.windUpCost = new ClockworkWindUpStat(windupCost);
        this.windUpMaxMod = new ClockworkMaxWindUpStat(windUpMaxMod);
        this.efficiency = new ClockworkEfficiencyStat(efficiency);
    }

    private ClockworkEfficiencyStat progress = new ClockworkEfficiencyStat(5);
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
    public float getProgress() {
        return 0;
    }

    @Override
    public float getWindUpCost() {
        return 0;
    }

    @Override
    public float getWindUpMaxMod() {
        return 0;
    }

    @Override
    public float getEfficiency() {
        return 0;
    }
}
