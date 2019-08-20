package lumaceon.mods.aeonicraft.client.init;

import lumaceon.mods.aeonicraft.api.client.HourglassGuiTabFactory;
import lumaceon.mods.aeonicraft.api.client.HourglassGuiTabs;
import lumaceon.mods.aeonicraft.api.client.IHourglassGuiTab;
import lumaceon.mods.aeonicraft.client.gui.hourglass.GuiHourglassTabAdvancementsProxy;
import lumaceon.mods.aeonicraft.client.gui.hourglass.GuiHourglassTabTCSummary;
import lumaceon.mods.aeonicraft.client.gui.hourglass.GuiHourglassTabUnlocks;
import lumaceon.mods.aeonicraft.registry.ModBlocks;
import lumaceon.mods.aeonicraft.registry.ModItems;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;

public class ModHourglassGuiTabs
{
    public static void init()
    {
        HourglassGuiTabs.HOURGLASS_GUI_TABS.add(new HourglassGuiTabFactory() {
            @Override
            public IHourglassGuiTab createNewHourglassGuiTab(GuiScreen hourglassGui) {
                return new GuiHourglassTabTCSummary(new ItemStack(ModItems.temporal_hourglass), "aeonicraft:hggui.summary");
            }
        });
        HourglassGuiTabs.HOURGLASS_GUI_TABS.add(new HourglassGuiTabFactory() {
            @Override
            public IHourglassGuiTab createNewHourglassGuiTab(GuiScreen hourglassGui) {
                return new GuiHourglassTabUnlocks(new ItemStack(ModItems.temporal_hourglass), "aeonicraft:hggui.unlocks");
            }
        });
        HourglassGuiTabs.HOURGLASS_GUI_TABS.add(new HourglassGuiTabFactory() {
            @Override
            public IHourglassGuiTab createNewHourglassGuiTab(GuiScreen hourglassGui) {
                return new GuiHourglassTabAdvancementsProxy(new ItemStack(ModBlocks.temporal_connection_amplifier), "Test 3");
            }
        });
        HourglassGuiTabs.HOURGLASS_GUI_TABS.add(new HourglassGuiTabFactory() {
            @Override
            public IHourglassGuiTab createNewHourglassGuiTab(GuiScreen hourglassGui) {
                return new GuiHourglassTabAdvancementsProxy(new ItemStack(ModItems.ingot_brass), "Test 4");
            }
        });
    }
}
