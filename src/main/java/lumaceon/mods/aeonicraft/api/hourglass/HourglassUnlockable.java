package lumaceon.mods.aeonicraft.api.hourglass;

import lumaceon.mods.aeonicraft.api.util.Icon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class HourglassUnlockable extends IForgeRegistryEntry.Impl<HourglassUnlockable>
{
    private int timeCostWeight;
    private int advancementCostWeight;

    public HourglassUnlockable(ResourceLocation registryName, int timeCostWeight, int advancementCostWeight)
    {
        this.setRegistryName(registryName);
        this.timeCostWeight = timeCostWeight;
        this.advancementCostWeight = advancementCostWeight;
    }

    public Icon getIcon() {
        return null;
    }

    public String getShortDescription() {
        return "Change me pls.";
    }

    // Cost for unlockables is usually relative to what's available.
    // Weights are summed up and the total values a player is expected to obtain is split between the weights.
    // Time cost should have a more exponential cost growth compared to advancements.
    // A good way to measure is to make sure the first unlock costs roughly 1 second and 1 advancement point.
    public int getTimeCostWeight() {
        return this.timeCostWeight;
    }
    public int getAdvancementCostWeight() {
        return this.advancementCostWeight;
    }

    public HourglassUnlockable[] getPrerequisites() {
        return new HourglassUnlockable[0];
    }

    public HourglassUnlockableCategory getCategory() {
        return null;
    }
}
