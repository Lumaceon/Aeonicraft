package lumaceon.mods.aeonicraft.machine;

import net.minecraftforge.energy.EnergyStorage;

public class Machine
{
    public EnergyStorage energyStorage;

    // Tick interval is how often the machine goes through a work tick, which may possibly be configurable.
    public int tickInterval = 20;
    public int minTickInterval = 20;
    public int maxTickInterval = 1200;

    public long currentProgress = 0;
    public long currentPerTick = 0;
    public long progressPerAction = 1;

    public float energyPerProgress = 1.0F;

    protected int gameTicksSinceLastInternalTick = 0;


}
