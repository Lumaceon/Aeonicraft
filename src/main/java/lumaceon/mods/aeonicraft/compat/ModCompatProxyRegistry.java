package lumaceon.mods.aeonicraft.compat;

import lumaceon.mods.aeonicraft.Aeonicraft;
import lumaceon.mods.aeonicraft.compat.betteradvancements.BetterAdvancementsCompatActive;
import lumaceon.mods.aeonicraft.compat.betteradvancements.BetterAdvancementsCompatVanilla;
import lumaceon.mods.aeonicraft.compat.betteradvancements.IBetterAdvancementsCompat;
import net.minecraftforge.fml.common.Loader;

/**
 * Credits to diesieben07 for the idea.
 */
public class ModCompatProxyRegistry
{
    public static IBetterAdvancementsCompat betterAdvancementsCompat;

    public static void init()
    {
        if(Loader.isModLoaded("betteradvancements"))
        {
            try {
                betterAdvancementsCompat = BetterAdvancementsCompatActive.class.newInstance();
                Aeonicraft.logger.info("+ BetterAdvancements Compat is active.");
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassCastException e) {
                e.printStackTrace();
            }
        }
        else
        {
            betterAdvancementsCompat = new BetterAdvancementsCompatVanilla();
            Aeonicraft.logger.info("- BetterAdvancements Compat is inactive.");
        }
    }
}
