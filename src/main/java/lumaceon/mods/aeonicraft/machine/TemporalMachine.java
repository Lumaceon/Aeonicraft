package lumaceon.mods.aeonicraft.machine;

import lumaceon.mods.aeonicraft.api.clockwork.EnergyStorageModular;
import lumaceon.mods.aeonicraft.util.MachineActionBurstConfig;
import lumaceon.mods.aeonicraft.util.TickInterval;
import net.minecraft.nbt.NBTTagCompound;

import java.util.function.Function;

public class TemporalMachine extends Machine
{
    protected MachineActionBurstConfig actionBurstConfig;

    public TemporalMachine(EnergyStorageModular energyStorage,
                           TickInterval tickInterval,
                           float progressCostPerAction,
                           float progressGainPerGameTick,
                           float energyCostPerProgress,
                           Function<Machine, Integer> getMaxActions, Function<Integer, Integer> takeActions) {
        super(energyStorage, tickInterval, progressCostPerAction, progressGainPerGameTick, energyCostPerProgress, getMaxActions, takeActions);
        actionBurstConfig = new MachineActionBurstConfig(64, 0, Integer.MAX_VALUE);
    }

    public TemporalMachine(EnergyStorageModular energyStorage,
                           TickInterval tickInterval,
                           float progressCostPerAction,
                           float progressGainPerGameTick,
                           float energyCostPerProgress,
                           Function<Machine, Integer> getMaxActions,
                           Function<Integer, Integer> takeActions,
                           MachineActionBurstConfig burstConfig)
    {
        super(energyStorage, tickInterval, progressCostPerAction, progressGainPerGameTick, energyCostPerProgress, getMaxActions, takeActions);
        actionBurstConfig = burstConfig;
    }

    public MachineActionBurstConfig getBurstConfig() {
        return this.actionBurstConfig;
    }

    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound nbt = super.serializeNBT();
        nbt.setTag("burst_config", this.actionBurstConfig.serializeNBT());
        return nbt;
    }

    public void deserializeNBT(NBTTagCompound nbt)
    {
        super.deserializeNBT(nbt);
        actionBurstConfig.deserializeNBT(nbt.getCompoundTag("burst_config"));
    }
}
