package lumaceon.mods.aeonicraft.capability;

import lumaceon.mods.aeonicraft.lib.ConfigValues;
import lumaceon.mods.aeonicraft.util.TimeParser;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nullable;

public class CapabilityTimeStorage
{
    @CapabilityInject(ITimeStorage.class)
    public static final Capability<ITimeStorage> TIME_STORAGE_CAPABILITY = null;

    public static void register()
    {
        CapabilityManager.INSTANCE.register(ITimeStorage.class, new Capability.IStorage<ITimeStorage>()
        {
            @Override
            public NBTBase writeNBT(Capability<ITimeStorage> capability, ITimeStorage instance, EnumFacing side)
            {
                NBTTagCompound nbt = new NBTTagCompound();
                nbt.setLong("ticks_stored", instance.getTimeInTicks());
                nbt.setLong("max_capacity", instance.getMaxCapacity());
                return nbt;
            }

            @Override
            public void readNBT(Capability<ITimeStorage> capability, ITimeStorage instance, EnumFacing side, NBTBase base)
            {
                instance.setMaxCapacity(((NBTTagCompound) base).getLong("max_capacity"));
                instance.insertTime(((NBTTagCompound) base).getLong("ticks_stored"));
            }
        }, TimeStorage::new);
    }

    public interface ITimeStorage
    {
        /**
         * Inserts the given amount of time into this storage.
         * @param ticksToInsert The time, in ticks, to insert into this storage.
         * @return The amount of ticks accepted into this storage.
         */
        long insertTime(long ticksToInsert);

        /**
         * Extracts the given amount of time out of this storage.
         * @param ticksToExtract The time, in ticks, to extract out of this storage.
         * @return The amount of ticks removed from this storage.
         */
        long extractTime(long ticksToExtract);

        /**
         * Returns the actual time stored.
         * @return How much time is currently stored.
         */
        long getTimeInTicks();

        /**
         * How much time can currently be stored in here?
         * @return The max capacity of this storage.
         */
        long getMaxCapacity();

        /**
         * Sets a new maximum capacity for this storage. If downsizing occurs, this returns the amount of time removed.
         * @param maxCapacity New max capacity to set this storage to.
         * @return The amount of time that was removed due to downsizing, or 0 if no time was lost.
         */
        long setMaxCapacity(long maxCapacity);

        void setTime(long newTime);

        /**
         * Used to update values for the player's internal capability. Can otherwise ignore this.
         */
        void renderUpdateTick(float secondsPassed);
    }

    public static class TimeStorage implements ITimeStorage
    {
        protected long capacity;
        protected long timeStored;

        // Client-only fields for smooth time display...
        protected long previousTimeStored = 0L;
        protected long passiveMilisTimePerTick = 0L;
        protected double updatePercentage = 0.0;

        protected long previousFastTime = 0;
        protected float fastUpdatePercentage = 0.0F;

        public UpdateSpeed speed = UpdateSpeed.SLOW;

        public TimeStorage() {
            capacity = TimeParser.ETERNITY;
        }

        public TimeStorage(long capacity) {
            this.capacity = capacity;
        }

        @Override
        public long insertTime(long ticksToInsert)
        {
            long timeAccepted = Math.min(capacity - timeStored, ticksToInsert);
            timeStored += timeAccepted;
            return timeAccepted;
        }

        @Override
        public long extractTime(long ticksToExtract)
        {
            long timeRemoved = Math.min(timeStored, ticksToExtract);
            timeStored -= timeRemoved;
            return timeRemoved;
        }

        @Override
        public long getTimeInTicks() {
            return timeStored;
        }

        /**
         * Returns an inaccurate value of time for the client to display.
         *
         * This is usually only useful in situations where update packets are infrequent; by default, player-based
         * time storage is only updated on the client every few seconds, outside of special cases. This then returns
         * the interpolated value between the previous update value and the current (real) value.
         * @return How much time the client should tell the player is stored.
         */
        public long getTimeInTicksForDisplay()
        {
            long ret;
            if(speed == UpdateSpeed.SLOW)
            {
                ret = (long) (previousTimeStored + (timeStored - previousTimeStored) * updatePercentage);
                if(ret <= 0) return 0;
                return ret;
            }
            else if(speed == UpdateSpeed.FAST)
            {
                ret = (long) (previousFastTime + ((previousTimeStored + (timeStored - previousTimeStored) - previousFastTime) * fastUpdatePercentage));
                if(ret <= 0) return 0;
                return ret;
            }
            return 0;
        }

        @Override
        public long getMaxCapacity() {
            return capacity;
        }

        @Override
        public long setMaxCapacity(long maxCapacity)
        {
            if(maxCapacity >= this.capacity)
                this.capacity = maxCapacity;
            else if(maxCapacity < this.timeStored)
            {
                long timeLost = this.timeStored - maxCapacity;
                this.capacity = maxCapacity;
                this.timeStored = maxCapacity;
                return timeLost;
            }
            return 0;
        }

        @Override
        public void setTime(long newTime) {
            this.timeStored = newTime;
        }

        /**
         * Call before updating values on the client side.
         */
        public void updateClient(UpdateSpeed updateSpeed, long passiveMilisGenPerTick, long newTime)
        {
            this.passiveMilisTimePerTick = passiveMilisGenPerTick;
            if(updateSpeed.equals(UpdateSpeed.INSTANT))
            {
                this.updatePercentage = Math.min(this.fastUpdatePercentage, ConfigValues.SECONDS_BETWEEN_PLAYER_TC_UPDATE_PACKET) / ConfigValues.SECONDS_BETWEEN_PLAYER_TC_UPDATE_PACKET;
                previousTimeStored = newTime - passiveMilisGenPerTick * 20 * ConfigValues.SECONDS_BETWEEN_PLAYER_TC_UPDATE_PACKET;
            }
            else if(updateSpeed.equals(UpdateSpeed.FAST))
            {
                this.speed = UpdateSpeed.FAST;
                this.fastUpdatePercentage = 0.0F;
                previousFastTime = this.timeStored;
                previousTimeStored = newTime - passiveMilisGenPerTick * 20 * ConfigValues.SECONDS_BETWEEN_PLAYER_TC_UPDATE_PACKET;
            }
            else
            {
                this.previousTimeStored = this.timeStored;
                this.updatePercentage = 0.0;
            }
        }

        @Override
        public void renderUpdateTick(float secondsPassed) {
            if(updatePercentage < 1.0)
                this.updatePercentage = Math.min(this.updatePercentage + secondsPassed / ConfigValues.SECONDS_BETWEEN_PLAYER_TC_UPDATE_PACKET, 1.0);
            if(speed == UpdateSpeed.FAST && fastUpdatePercentage < 1.0F)
                fastUpdatePercentage = Math.min(fastUpdatePercentage + secondsPassed * 0.25F, 1.0F);
        }

        @Override
        public boolean equals(@Nullable final Object obj)
        {
            if(this == obj) return true;
            if(obj == null || getClass() != obj.getClass()) return false;

            final TimeStorage that = (TimeStorage) obj;

            return capacity == that.capacity && timeStored == that.timeStored;
        }

        public enum UpdateSpeed
        {
            SLOW, FAST, INSTANT
        }
    }

    // Default provider
    public static class Provider implements ICapabilitySerializable<NBTTagCompound>
    {
        TimeStorage implementation;

        public Provider() {
            implementation = new TimeStorage();
        }

        @Override
        public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
            return TIME_STORAGE_CAPABILITY == capability;
        }

        @Override
        public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
            if(hasCapability(capability, facing))
                return TIME_STORAGE_CAPABILITY.cast(implementation);
            return null;
        }

        @Override
        public NBTTagCompound serializeNBT() {
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setLong("capacity", implementation.getMaxCapacity());
            nbt.setLong("time", implementation.getTimeInTicks());
            return nbt;
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt) {
            if(nbt.hasKey("capacity"))
                implementation.setMaxCapacity(nbt.getLong("capacity"));
            if(nbt.hasKey("time"))
                implementation.setTime(nbt.getLong("time"));
        }
    }
}

