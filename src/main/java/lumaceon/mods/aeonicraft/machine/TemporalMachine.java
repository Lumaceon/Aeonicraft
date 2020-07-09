package lumaceon.mods.aeonicraft.machine;

import lumaceon.mods.aeonicraft.util.MachineActionBurstConfig;
import lumaceon.mods.aeonicraft.util.TickInterval;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.energy.EnergyStorage;

import java.util.function.Function;

public class TemporalMachine extends Machine
{
    protected MachineActionBurstConfig actionBurstConfig;

    public TemporalMachine(EnergyStorage energyStorage, TickInterval tickInterval, Function<Machine, Integer> getMaxActions, Function<Integer, Integer> takeActions) {
        super(energyStorage, tickInterval, getMaxActions, takeActions);
        actionBurstConfig = new MachineActionBurstConfig(64, 0, Integer.MAX_VALUE);
    }

    public TemporalMachine(EnergyStorage energyStorage,
                           TickInterval tickInterval,
                           Function<Machine, Integer> getMaxActions,
                           Function<Integer, Integer> takeActions,
                           MachineActionBurstConfig burstConfig)
    {
        super(energyStorage, tickInterval, getMaxActions, takeActions);
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
