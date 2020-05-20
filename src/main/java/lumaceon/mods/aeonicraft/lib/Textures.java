package lumaceon.mods.aeonicraft.lib;

import lumaceon.mods.aeonicraft.Aeonicraft;
import lumaceon.mods.aeonicraft.registry.ModBlocks;
import lumaceon.mods.aeonicraft.registry.ModItems;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class Textures
{
    public static class GUI
    {
        public static ResourceLocation ASSEMBLY_TABLE = new ResourceLocation(Aeonicraft.MOD_ID, "textures/gui/assembly_table.png");
    }
    public static final ResourceLocation TEMPORAL_ELECTRIC_BASE = new ResourceLocation(Aeonicraft.MOD_ID, "textures/gui/fx/temporal_electricity.png");
    public static final ResourceLocation TEMPORAL_ELECTRIC_BASE2 = new ResourceLocation(Aeonicraft.MOD_ID, "textures/gui/fx/temporal_electricity2.png");
    public static final ResourceLocation TEMPORAL_BASE = new ResourceLocation(Aeonicraft.MOD_ID, "textures/gui/fx/temporal_base.png");
    public static final ResourceLocation HOURGLASS_UL_CAT_BG = new ResourceLocation(Aeonicraft.MOD_ID, "textures/gui/fx/hourglass_unlock_category_background.png");
    public static final ResourceLocation TEMPORAL_DOTS = new ResourceLocation(Aeonicraft.MOD_ID, "textures/item/temporal_dots.png");
    public static final ResourceLocation CLOUD_OVERLAY = new ResourceLocation(Aeonicraft.MOD_ID, "textures/gui/fx/cloud_overlay.png");
    public static final ResourceLocation TEMPORAL_CLOUD_RING = new ResourceLocation(Aeonicraft.MOD_ID, "textures/gui/fx/temporal_effect_ring.png");
    public static final ResourceLocation HOURGLASS_BASE_GUI = new ResourceLocation(Aeonicraft.MOD_ID, "textures/gui/gui_hourglass_base.png");

    // Itemstacks for visual display...
    public static final ItemStack STACK_HOURGLASS = new ItemStack(ModItems.temporal_hourglass);
    public static final ItemStack STACK_COMPRESSOR = new ItemStack(ModBlocks.temporal_compressor);
}
