package lumaceon.mods.aeonicraft.machine;

import lumaceon.mods.aeonicraft.util.TickInterval;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.energy.EnergyStorage;

import java.util.function.Function;

public class Machine
{
    protected EnergyStorage energyStorage;

    /**
     * Tick interval is how many ingame ticks are required for a machine tick, which may possibly be configurable.
     * In some cases, certain checks may be performed based on game ticks instead of machine ticks.
     */
    protected TickInterval tickInterval;

    protected float currentProgress = 0.0F;
    protected float progressPerGameTick = 1.0F;
    protected float progressPerAction = 100.0F;
    protected float energyPerProgress = 1.0F;

    /**
     * Output the maximum number of actions that could be taken when called.
     */
    protected Function<Machine, Integer> getMaxActions;

    /**
     * Maximum number of allowed actions as input; amount actually taken as output. Take as many as possible up to max.
     */
    protected Function<Integer, Integer> takeActions;


    public Machine(EnergyStorage energyStorage,
                   TickInterval tickInterval,
                   Function<Machine, Integer> getMaxActions,
                   Function<Integer, Integer> takeActions) {
        this.energyStorage = energyStorage;
        this.tickInterval = tickInterval;
        this.getMaxActions = getMaxActions;
        this.takeActions = takeActions;
    }

    /**
     * To be called every available update tick.
     */
    public void gameTick()
    {
        if(tickInterval.receiveTick())
            machineTick();
    }

    /**
     * Called internally every internal tick interval.
     */
    protected void machineTick()
    {
        progressTick();

        int actions = this.takeActions.apply((int) (this.currentProgress / this.progressPerAction));
        this.currentProgress -= (actions * this.progressPerAction);
    }

    /**
     * Called every internal tick interval; specifically handles progress.
     */
    protected void progressTick()
    {
        float progressToAdd = tickInterval.getTickInterval() * this.progressPerGameTick;
        progressToAdd = Math.min(progressToAdd, getMaxProgressFromEnergy());
        progressToAdd = Math.min(progressToAdd, getMaxActions.apply(this) * progressPerAction - currentProgress);

        if(this.energyStorage != null) {
            this.energyStorage.extractEnergy((int) (currentProgress * energyPerProgress), false);
        }

        currentProgress += progressToAdd;
    }

    /**
     * @return The maximum progress that can be gained from the currently available energy.
     */
    protected float getMaxProgressFromEnergy() {
        if(this.energyPerProgress <= 0) return Float.MAX_VALUE;
        if(energyStorage == null) return 0.0F;
        return energyStorage.extractEnergy(Integer.MAX_VALUE, true) / energyPerProgress;
    }


    // *** OPTIONAL NBT FUNCTIONS *** //

    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setFloat("current_progress", this.currentProgress);
        nbt.setFloat("progress_per_game_tick", this.progressPerGameTick);
        nbt.setFloat("progress_per_action", this.progressPerAction);
        nbt.setFloat("energy_per_progress", this.energyPerProgress);
        return nbt;
    }

    public void deserializeNBT(NBTTagCompound nbt)
    {
        this.currentProgress = nbt.getFloat("current_progress");
        this.progressPerGameTick = nbt.getFloat("progress_per_game_tick");
        this.progressPerAction = nbt.getFloat("progress_per_action");
        this.energyPerProgress = nbt.getFloat("energy_per_progress");
    }


    // *** GENERATED MUTATORS AND ACCESSORS *** //

    public TickInterval getTickInterval() {
        return tickInterval;
    }
    public float getCurrentProgress() {
        return currentProgress;
    }
    public void setCurrentProgress(float currentProgress) {
        this.currentProgress = currentProgress;
    }
    public float getProgressPerGameTick() {
        return progressPerGameTick;
    }
    public void setProgressPerGameTick(float progressPerGameTick) {
        this.progressPerGameTick = progressPerGameTick;
    }
    public float getProgressPerAction() {
        return progressPerAction;
    }
    public float getEnergyPerProgress() {
        return energyPerProgress;
    }
    public void setEnergyPerProgress(float energyPerProgress) {
        this.energyPerProgress = energyPerProgress;
    }
}
