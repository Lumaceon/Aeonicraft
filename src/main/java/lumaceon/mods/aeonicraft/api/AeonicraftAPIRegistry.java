package lumaceon.mods.aeonicraft.api;

import lumaceon.mods.aeonicraft.api.hourglass.IHourglassFunction;

import java.util.HashMap;

public class AeonicraftAPIRegistry
{
    private static final HashMap<String, IHourglassFunction> HOURGLASS_FUNCTION_REGISTRY = new HashMap<>();


    /**
     * Adds the hourglass function to the registry map. Hourglass functions are serialized as NBTTagCompounds mapped to
     * strings.
     * @param hourglassFunction The new hourglass function to register.
     */
    public static void registerHourglassFunction(IHourglassFunction hourglassFunction) {
        HOURGLASS_FUNCTION_REGISTRY.put(hourglassFunction.getRegistryIDString(), hourglassFunction);
    }

    public static IHourglassFunction getHourglassFunction(String registryName) {
        return HOURGLASS_FUNCTION_REGISTRY.get(registryName);
    }

    public static IHourglassFunction[] getAllFunctionsRegistered() {
        return HOURGLASS_FUNCTION_REGISTRY.values().toArray(new IHourglassFunction[0]);
    }
}
