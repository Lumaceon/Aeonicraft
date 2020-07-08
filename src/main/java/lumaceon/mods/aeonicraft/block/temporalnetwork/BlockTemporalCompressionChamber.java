package lumaceon.mods.aeonicraft.block.temporalnetwork;

import lumaceon.mods.aeonicraft.Aeonicraft;
import lumaceon.mods.aeonicraft.api.temporal.TC;
import lumaceon.mods.aeonicraft.api.temporal.temporalnetwork.BlockTemporalNetwork;
import net.minecraft.block.material.Material;
import net.minecraft.util.EnumFacing;

import java.util.Objects;

public class BlockTemporalCompressionChamber extends BlockTemporalNetwork
{
    public BlockTemporalCompressionChamber(Material blockMaterialIn, String name) {
        super(blockMaterialIn);
        this.setCreativeTab(Aeonicraft.instance.CREATIVE_TAB);
        this.setHardness(5.0F);
        this.setRegistryName(Aeonicraft.MOD_ID, name);
        this.setUnlocalizedName(Objects.requireNonNull(this.getRegistryName()).toString());
        this.setLightOpacity(0);
        this.setLightLevel(0.5F);
    }

    @Override
    public TC getTCGenerationPerSecond() {
        return TC.MINUTE;
    }

    @Override
    public TC getTCCapacity() {
        return TC.HOUR;
    }

    @Override
    public boolean canConnectOnSide(EnumFacing mySide) {
        return true;
    }
}
