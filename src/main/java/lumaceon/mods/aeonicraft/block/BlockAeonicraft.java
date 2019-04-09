package lumaceon.mods.aeonicraft.block;

import lumaceon.mods.aeonicraft.Aeonicraft;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

import java.util.Objects;

public class BlockAeonicraft extends Block
{
    public BlockAeonicraft(Material blockMaterial, String name) {
        super(blockMaterial);
        this.setCreativeTab(Aeonicraft.instance.CREATIVE_TAB);
        this.setHardness(3.0F);
        this.setRegistryName(Aeonicraft.MOD_ID, name);
        this.setUnlocalizedName(Objects.requireNonNull(this.getRegistryName()).toString());
    }
}
