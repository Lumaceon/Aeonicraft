package lumaceon.mods.aeonicraft.block.temporalnetwork;

import lumaceon.mods.aeonicraft.Aeonicraft;
import lumaceon.mods.aeonicraft.api.temporal.TC;
import lumaceon.mods.aeonicraft.api.temporal.temporalnetwork.BlockTemporalNetwork;
import net.minecraft.block.material.Material;
import net.minecraft.util.EnumFacing;

import java.util.Objects;

public class BlockTemporalConduit extends BlockTemporalNetwork
{
    public BlockTemporalConduit(Material blockMaterialIn, String name) {
        super(blockMaterialIn);
        this.setCreativeTab(Aeonicraft.instance.CREATIVE_TAB);
        this.setHardness(2.0F);
        this.setRegistryName(Aeonicraft.MOD_ID, name);
        this.setUnlocalizedName(Objects.requireNonNull(this.getRegistryName()).toString());
        this.setLightOpacity(0);
        this.setLightLevel(0.34F);
    }

    @Override
    public TC getTCGenerationPerSecond() {
        return TC.NONE;
    }

    @Override
    public TC getTCCapacity() {
        return TC.NONE;
    }

    @Override
    public boolean canConnectOnSide(EnumFacing mySide) {
        return true;
    }
}
