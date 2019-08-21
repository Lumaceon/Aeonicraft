package lumaceon.mods.aeonicraft.hourglassunlockable;

import lumaceon.mods.aeonicraft.Aeonicraft;
import lumaceon.mods.aeonicraft.api.HourglassUnlocks;
import lumaceon.mods.aeonicraft.api.hourglass.HourglassUnlockable;
import lumaceon.mods.aeonicraft.api.util.Icon;
import net.minecraft.util.ResourceLocation;

public class HourglassUnlockableHGFunction extends HourglassUnlockable
{
    private Icon icon;

    public HourglassUnlockableHGFunction(ResourceLocation registryName, int timeCostWeight, int advancementCostWeight) {
        super(registryName, HourglassUnlocks.categoryHourglassFunction, timeCostWeight, advancementCostWeight);
        icon = new Icon(new ResourceLocation(Aeonicraft.MOD_ID, "hourglassfunction/" + registryName.getResourcePath()));
    }

    @Override
    public Icon getIcon() {
        return icon;
    }
}
