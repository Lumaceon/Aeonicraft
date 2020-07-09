package lumaceon.mods.aeonicraft.util;

import net.minecraft.nbt.NBTTagCompound;

/**
 * To be used in update methods that're called every tick, but only want to update on a specific interval (every
 * 20 ticks/1 second for example). Many temporal machines work by performing the maximum amount of work possible every
 * internal tick, as defined by the tick interval.
 */
public class TickInterval
{
    protected int tickInterval = 1;
    protected int skippedUpdates = 0;

    protected int minIntervalConfig = 1;
    protected int maxIntervalConfig = 20*60; // 1 minute in in-game ticks.

    public TickInterval() {
        //NOOP
    }

    public TickInterval(int tickInterval) {
        this();
        this.tickInterval = tickInterval;
    }

    public TickInterval(int tickInterval, int minIntervalConfig, int maxIntervalConfig) {
        this(tickInterval);
        this.minIntervalConfig = minIntervalConfig;
        this.maxIntervalConfig = maxIntervalConfig;
    }

    public TickInterval(NBTTagCompound nbt) {
        this (
                nbt.getInteger("tick_interval"),
                nbt.getInteger("config_min"),
                nbt.getInteger("config_max")
        );
    }

    /**
     * @return True if this game tick should cause an internal tick.
     */
    public boolean receiveTick()
    {
        skippedUpdates++;
        if(skippedUpdates >= tickInterval)
        {
            skippedUpdates = 0;
            return true;
        }
        return false;
    }

    public void setTickInterval(int tickInterval) {
        this.tickInterval = tickInterval;
    }

    public int getTickInterval() {
        return this.tickInterval;
    }

    public int getMinIntervalConfig() {
        return minIntervalConfig;
    }

    public int getMaxIntervalConfig() {
        return maxIntervalConfig;
    }

    public void setIntervalConfigRange(int minIntervalConfig, int maxIntervalConfig) {
        this.minIntervalConfig = minIntervalConfig;
        this.maxIntervalConfig = maxIntervalConfig;
    }

    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("tick_interval", this.tickInterval);
        nbt.setInteger("skipped_updated", this.skippedUpdates);
        nbt.setInteger("config_min", this.minIntervalConfig);
        nbt.setInteger("config_max", this.maxIntervalConfig);
        return nbt;
    }

    public void deserializeNBT(NBTTagCompound nbt)
    {
        this.tickInterval = nbt.getInteger("tick_interval");
        this.skippedUpdates = nbt.getInteger("skipped_updated");
        this.minIntervalConfig = nbt.getInteger("config_min");
        this.maxIntervalConfig = nbt.getInteger("config_max");
    }
}
