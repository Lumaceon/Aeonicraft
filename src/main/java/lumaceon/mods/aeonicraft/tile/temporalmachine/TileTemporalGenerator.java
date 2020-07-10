package lumaceon.mods.aeonicraft.tile.temporalmachine;

import lumaceon.mods.aeonicraft.machine.Machine;
import lumaceon.mods.aeonicraft.machine.TemporalMachine;
import lumaceon.mods.aeonicraft.util.MachineActionBurstConfig;
import lumaceon.mods.aeonicraft.util.TickInterval;
import net.minecraftforge.energy.EnergyStorage;

public class TileTemporalGenerator extends TileTemporalMachine
{
    private int internalFuel = 0;

    public TileTemporalGenerator() {
        super();
        this.temporalMachine = new TemporalMachine(
                new EnergyStorage(1000000),
                new TickInterval(20),
                this::getMaxActions,
                this::takeActions,
                new MachineActionBurstConfig()
        );
    }

    @Override
    public int getMaxActions(Machine machine) {
        return 0;
    }

    @Override
    public int takeActions(int max) {
        return 0;
    }
}
