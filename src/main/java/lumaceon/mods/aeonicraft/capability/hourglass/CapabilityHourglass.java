package lumaceon.mods.aeonicraft.capability.hourglass;

import lumaceon.mods.aeonicraft.api.AeonicraftAPIRegistry;
import lumaceon.mods.aeonicraft.api.IHourglassFunction;
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
import java.util.HashMap;

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
         * @return True if the function exists and is active.
         */
        boolean functionActive(IHourglassFunction function);

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

        void loadFromNBT(NBTTagCompound nbt);
        NBTTagCompound saveToNBT();
    }

    public static class HourglassHandler implements IHourglassHandler
    {
        ArrayList<IHourglassFunction> functions = new ArrayList<>();
        HashMap<String, Boolean> activeMap = new HashMap<>();

        @Override
        public boolean functionActive(IHourglassFunction function)
        {
            for(IHourglassFunction f : functions)
            {
                if(f.equals(function) && activeMap.get(f.getRegistryIDString()))
                {
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
            functions.add((IHourglassFunction) module.getItem());
            activeMap.put(((IHourglassFunction) module.getItem()).getRegistryIDString(), false);
            return true;
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
            activeMap.put(hourglassFunction.getRegistryIDString(), false);
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
                    activeMap.put(s, tag.getBoolean("active"));
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
                tag.setBoolean("active", activeMap.get(function.getRegistryIDString()));
                list.appendTag(tag);
            }
            nbt.setTag("function_list", list);
            return nbt;
        }
    }
}
