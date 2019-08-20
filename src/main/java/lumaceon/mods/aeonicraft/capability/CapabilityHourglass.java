package lumaceon.mods.aeonicraft.capability;

import lumaceon.mods.aeonicraft.api.AeonicraftAPIRegistry;
import lumaceon.mods.aeonicraft.api.hourglass.IHourglassFunction;
import lumaceon.mods.aeonicraft.network.PacketHandler;
import lumaceon.mods.aeonicraft.network.message.MessageHourglassRequestTCUpdate;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.ArrayList;

/**
 * A capability specifically for the Temporal Hourglass, and any container with similar functionality. Capability stores
 * a series of strings which refer to instances of IHourglassFunction. Most of the relevant calculations occur during
 * events and only care whether the hourglass has the particular string.
 *
 * A map also contains a boolean for each string/hourglassfunction, allowing them to be toggled on/off, which should
 * also be checked for in the relevant logic.
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
        @Nullable IHourglassFunction getActiveFunction();

        void cycleActiveFunction(boolean forward);

        /**
         * @return True if function was found, false if not.
         */
        boolean setActiveFunction(IHourglassFunction function);

        /**
         * Get and return the first hourglass function for the given string.
         */
        IHourglassFunction getHourglassFunctionFromString(String registryName);

        /**
         * @return True if successful, false if denied.
         */
        boolean addHourglassStack(ItemStack module);
        boolean addHourglassFunction(IHourglassFunction hourglassFunction);

        @Nullable ItemStack convertHourglassFunctionToItemstackAndRemove(IHourglassFunction function);

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
        ArrayList<IHourglassFunction> functions = new ArrayList<>();
        IHourglassFunction active = null;

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
        public IHourglassFunction getActiveFunction() {
            return active;
        }

        @Override
        public void cycleActiveFunction(boolean forward)
        {
            reequipAnimation = true;
            if(forward)
            {
                // null active function - set to first function.
                if(active == null && functions.size() > 0)
                {
                    active = functions.get(0);
                    return;
                }

                // last active function - loop to null function.
                if(active != null && active.equals(functions.get(functions.size() - 1)))
                {
                    active = null;
                    return;
                }

                // set to next function.
                for(int i = 0; i < functions.size(); i++)
                {
                    IHourglassFunction f = functions.get(i);
                    if(f.equals(active))
                    {
                        active = functions.get(i+1);
                        return;
                    }
                }
            }
            else //backward
            {
                // null active function - set to last function.
                if(active == null && functions.size() > 0)
                {
                    active = functions.get(functions.size() - 1);
                    return;
                }

                // first active function - set to null function.
                if(active != null && active.equals(functions.get(0)))
                {
                    active = null;
                    return;
                }

                // set to previous function.
                for(int i = functions.size() - 1; i > 0; i--)
                {
                    IHourglassFunction f = functions.get(i);
                    if(f.equals(active))
                    {
                        active = functions.get(i-1);
                        return;
                    }
                }
            }
        }

        @Override
        public boolean setActiveFunction(IHourglassFunction function)
        {
            reequipAnimation = true;
            if(function == null)
            {
                active = null;
                return true;
            }

            for(IHourglassFunction f : functions)
            {
                if(f.equals(function))
                {
                    active = f;
                    return true;
                }
            }
            return false;
        }

        @Override
        public IHourglassFunction getHourglassFunctionFromString(String registryName)
        {
            IHourglassFunction hourglassFunction = AeonicraftAPIRegistry.getHourglassFunction(registryName);
            for(IHourglassFunction is : functions)
            {
                if(is.equals(hourglassFunction))
                {
                    return is;
                }
            }
            return null;
        }

        @Override
        public boolean addHourglassStack(ItemStack module)
        {
            IHourglassFunction[] funcs = functions.toArray(new IHourglassFunction[0]);
            if(!(module.getItem() instanceof IHourglassFunction)
                    || !((IHourglassFunction)module.getItem()).canCoexistWith(funcs)
            )
            {
                return false;
            }
            return addHourglassFunction((IHourglassFunction) module.getItem());
        }

        @Override
        public boolean addHourglassFunction(IHourglassFunction hourglassFunction)
        {
            IHourglassFunction[] funcs = functions.toArray(new IHourglassFunction[0]);
            if(!hourglassFunction.canCoexistWith(funcs) )
            {
                return false;
            }
            functions.add(hourglassFunction);
            if(active == null)
                active = hourglassFunction;
            return true;
        }

        @Override
        @Nullable
        public ItemStack convertHourglassFunctionToItemstackAndRemove(IHourglassFunction function) {
            ItemStack is = function.createItemstackContainer();
            if(is != null)
            {
                functions.remove(function);
                return is;
            }
            return null;
        }

        @Override
        public void loadFromNBT(NBTTagCompound nbt)
        {
            if(nbt.hasKey("function_list"))
            {
                NBTTagList list = nbt.getTagList("function_list", Constants.NBT.TAG_COMPOUND);
                for(int i = 0; i < list.tagCount(); i++)
                {
                    NBTTagCompound tag = list.getCompoundTagAt(i);
                    String s = tag.getString("id");
                    IHourglassFunction hourglassFunction = AeonicraftAPIRegistry.getHourglassFunction(s);
                    functions.add(hourglassFunction);
                }
            }

            if(nbt.hasKey("active"))
            {
                String activeFunc = nbt.getString("active");
                for(IHourglassFunction f : functions)
                {
                    if(activeFunc.equals(f.getRegistryIDString()))
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
            NBTTagList list = new NBTTagList();
            for(IHourglassFunction function : functions)
            {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setString("id", function.getRegistryIDString());
                list.appendTag(tag);
            }
            nbt.setTag("function_list", list);
            IHourglassFunction active = this.active;
            if(this.active != null)
                nbt.setString("active", active.getRegistryIDString());
            return nbt;
        }
    }
}
