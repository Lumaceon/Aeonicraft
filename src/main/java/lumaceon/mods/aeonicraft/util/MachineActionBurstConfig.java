package lumaceon.mods.aeonicraft.util;

import net.minecraft.nbt.NBTTagCompound;

/**
 * A configuration for a temporal machine's ability to burst-complete multiple 'actions.' Actions can be anything:
 * "smelt one item," "generate one tick's worth of energy," etc...
 */
public class MachineActionBurstConfig
{
    private int burst = 64;
    private int minBurst = 0;
    private int maxBurst = 1024;
    private boolean unboundedMax = false;

    /**
     * Instant Input Actions refers to a machine's ability to try and instantly process something as it goes into the
     * machine's input/inventory. In some cases, setting this to true will process and output the item so fast the item
     * won't even enter the machine at all.
     */
    private boolean instantInputProcessing = false;


    public MachineActionBurstConfig() {}

    public MachineActionBurstConfig(int burst) {
        this();
        this.burst = burst;
    }

    public MachineActionBurstConfig(int burst, int minBurst, int maxBurst) {
        this(burst);
        this.minBurst = minBurst;
        this.maxBurst = maxBurst;
    }

    public MachineActionBurstConfig(int burst, int minBurst, int maxBurst, boolean unboundedMax) {
        this(burst, minBurst, maxBurst);
        this.unboundedMax = unboundedMax;
    }

    public MachineActionBurstConfig(int burst, int minBurst, int maxBurst, boolean unboundedMax, boolean instantInputProcessing) {
        this(burst, minBurst, maxBurst, unboundedMax);
        this.instantInputProcessing = instantInputProcessing;
    }

    public MachineActionBurstConfig(NBTTagCompound nbt) {
        this.burst = nbt.getInteger("burst");
        this.minBurst = nbt.getInteger("min_burst");
        this.maxBurst = nbt.getInteger("max_burst");
        this.unboundedMax = nbt.getBoolean("unboundedMax");
        this.instantInputProcessing = nbt.getBoolean("instantInputProcessing");
    }

    public int getBurst() {
        return burst;
    }
    public void setBurst(int burst) {
        this.burst = burst;
    }
    public int getMinBurst() {
        return minBurst;
    }
    public int getMaxBurst() {
        return maxBurst;
    }
    public void setBurstCapRange(int minBurstCap, int maxBurstCap) {
        this.minBurst = minBurstCap;
        this.maxBurst = maxBurstCap;
    }
    public boolean isUnboundedMax() {
        return unboundedMax;
    }
    public void setUnboundedMax(boolean unboundedMax) {
        this.unboundedMax = unboundedMax;
    }
    public boolean isInstantInputProcessing() {
        return instantInputProcessing;
    }
    public void setInstantInputProcessing(boolean instantInputProcessing) {
        this.instantInputProcessing = instantInputProcessing;
    }

    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("burst", this.burst);
        nbt.setInteger("min_burst", this.minBurst);
        nbt.setInteger("max_burst", this.maxBurst);
        nbt.setBoolean("unboundedMax", this.unboundedMax);
        nbt.setBoolean("instantInputProcessing", this.instantInputProcessing);
        return nbt;
    }

    public void deserializeNBT(NBTTagCompound nbt)
    {
        this.burst = nbt.getInteger("burst");
        this.minBurst = nbt.getInteger("min_burst");
        this.maxBurst = nbt.getInteger("max_burst");
        this.unboundedMax = nbt.getBoolean("unboundedMax");
        this.instantInputProcessing = nbt.getBoolean("instantInputProcessing");
    }
}
