package lumaceon.mods.aeonicraft.capability;

import lumaceon.mods.aeonicraft.api.hourglass.HourglassFunction;
import lumaceon.mods.aeonicraft.network.PacketHandler;
import lumaceon.mods.aeonicraft.network.message.MessageHourglassRequestTCUpdate;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nullable;
import java.util.ArrayList;

/**
 * A capability specifically for the Temporal Hourglass, storing any relevant data.
 */
public class CapabilityHourglass
{
    @CapabilityInject(IHourglassHandler.class)
    public static final Capability<IHourglassHandler> HOURGLASS = null;

    public static void register()
    {
        CapabilityManager.INSTANCE.register(CapabilityHourglass.IHourglassHandler.class, new Capability.IStorage<CapabilityHourglass.IHourglassHandler>()
                {
                    @Override
                    public NBTBase writeNBT(Capability<CapabilityHourglass.IHourglassHandler> capability, CapabilityHourglass.IHourglassHandler instance, EnumFacing side) {
                        return instance.saveToNBT();
                    }

                    @Override
                    public void readNBT(Capability<CapabilityHourglass.IHourglassHandler> capability, CapabilityHourglass.IHourglassHandler instance, EnumFacing side, NBTBase base) {
                        instance.loadFromNBT((NBTTagCompound) base);
                    }
                },
                () -> new CapabilityHourglass.HourglassHandler()
        );
    }

    public interface IHourglassHandler
    {
        /**
         * @return The active function of the hourglass.
         */
        @Nullable
        HourglassFunction getActiveFunction();

        void cycleActiveFunction(EntityPlayer player, boolean forward);

        /**
         * Typically used to send an update request to the server to get initial values.
         * @param slotIndex The index of this stack in the containing inventory.
         */
        void checkForUpdatesClientSide(int slotIndex);

        void loadFromNBT(NBTTagCompound nbt);
        NBTTagCompound saveToNBT();
    }

    public static class HourglassHandler implements IHourglassHandler
    {
        HourglassFunction active = null;

        // Hourglass needs to send data on initial load (and a few other cases).
        private boolean shouldClientSendRequestForUpdate = true;
        public boolean reequipAnimation = false;

        public void checkForUpdatesClientSide(int slotIndex)
        {
            if(shouldClientSendRequestForUpdate)
            {
                shouldClientSendRequestForUpdate = false;
                PacketHandler.INSTANCE.sendToServer(new MessageHourglassRequestTCUpdate(slotIndex));
            }
        }

        @Override
        public HourglassFunction getActiveFunction() {
            return active;
        }

        @Override
        public void cycleActiveFunction(EntityPlayer player, boolean forward)
        {
            CapabilityAeonicraftProgression.IAeonicraftProgressionHandler cap = player.getCapability(CapabilityAeonicraftProgression.AEONICRAFT_PROGRESSION_CAPABILITY, null);

            if(cap == null)
                return;

            HourglassFunction[] functions = GameRegistry.findRegistry(HourglassFunction.class).getValuesCollection().toArray(new HourglassFunction[0]);
            ArrayList<HourglassFunction> temp = new ArrayList<>();
            for(HourglassFunction f : functions)
            {
                if(cap.isUnlocked(f.requiredUnlockable()))
                {
                    temp.add(f);
                }
            }

            if(temp.isEmpty())
            {
                active = null;
                return;
            }

            functions = temp.toArray(new HourglassFunction[0]);

            reequipAnimation = true;
            if(forward)
            {
                // null active function - set to first function.
                if(active == null && functions.length > 0)
                {
                    active = functions[0];
                    return;
                }

                // last active function - loop to null function.
                if(active != null && active.equals(functions[functions.length - 1]))
                {
                    active = null;
                    return;
                }

                // set to next function.
                for(int i = 0; i < functions.length; i++)
                {
                    HourglassFunction f = functions[i];
                    if(f.equals(active))
                    {
                        active = functions[i+1];
                        return;
                    }
                }
            }
            else //backward
            {
                // null active function - set to last function.
                if(active == null && functions.length > 0)
                {
                    active = functions[functions.length - 1];
                    return;
                }

                // first active function - set to null function.
                if(active != null && active.equals(functions[0]))
                {
                    active = null;
                    return;
                }

                // set to previous function.
                for(int i = functions.length - 1; i > 0; i--)
                {
                    HourglassFunction f = functions[i];
                    if(f.equals(active))
                    {
                        active = functions[i-1];
                        return;
                    }
                }
            }
        }

        @Override
        public void loadFromNBT(NBTTagCompound nbt)
        {
            if(nbt.hasKey("active_function"))
            {
                String activeFunc = nbt.getString("active_function");
                for(HourglassFunction f : GameRegistry.findRegistry(HourglassFunction.class).getValuesCollection())
                {
                    if(activeFunc.equals(f.getRegistryName().toString()))
                    {
                        this.active = f;
                        break;
                    }
                }
            }
        }

        @Override
        public NBTTagCompound saveToNBT()
        {
            NBTTagCompound nbt = new NBTTagCompound();
            HourglassFunction active = this.active;
            if(this.active != null)
                nbt.setString("active_function", active.getRegistryName().toString());
            return nbt;
        }
    }
}
