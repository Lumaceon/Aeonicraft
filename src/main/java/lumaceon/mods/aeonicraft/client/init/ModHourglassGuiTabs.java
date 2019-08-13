package lumaceon.mods.aeonicraft.client.init;

import lumaceon.mods.aeonicraft.api.client.HourglassGuiTabFactory;
import lumaceon.mods.aeonicraft.api.client.HourglassGuiTabs;
import lumaceon.mods.aeonicraft.api.client.IHourglassGuiTab;
import lumaceon.mods.aeonicraft.client.gui.hourglass.GuiHourglassTabAdvancementsProxy;
import lumaceon.mods.aeonicraft.client.gui.hourglass.GuiHourglassTabTCSummary;
import lumaceon.mods.aeonicraft.init.ModItems;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class ModHourglassGuiTabs
{
    public static void init()
    {
        HourglassGuiTabs.HOURGLASS_GUI_TABS.add(new HourglassGuiTabFactory() {
            @Override
            public IHourglassGuiTab createNewHourglassGuiTab(GuiScreen hourglassGui) {
                return new GuiHourglassTabTCSummary(new ItemStack(Items.CLOCK), "aeonicraft:hggui.summary");
            }
        });
        HourglassGuiTabs.HOURGLASS_GUI_TABS.add(new HourglassGuiTabFactory() {
            @Override
            public IHourglassGuiTab createNewHourglassGuiTab(GuiScreen hourglassGui) {
                return new GuiHourglassTabAdvancementsProxy(new ItemStack(ModItems.temporalHourglass), "Test 2");
            }
        });
        HourglassGuiTabs.HOURGLASS_GUI_TABS.add(new HourglassGuiTabFactory() {
            @Override
            public IHourglassGuiTab createNewHourglassGuiTab(GuiScreen hourglassGui) {
                return new GuiHourglassTabAdvancementsProxy(new ItemStack(ModItems.hgFuncFish), "Test 3");
            }
        });
        HourglassGuiTabs.HOURGLASS_GUI_TABS.add(new HourglassGuiTabFactory() {
            @Override
            public IHourglassGuiTab createNewHourglassGuiTab(GuiScreen hourglassGui) {
                return new GuiHourglassTabAdvancementsProxy(new ItemStack(ModItems.ingotBrass), "Test 4");
            }
        });
    }
}
