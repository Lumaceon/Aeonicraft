package lumaceon.mods.aeonicraft.init;

import lumaceon.mods.aeonicraft.block.BlockHourglassProgrammer;
import lumaceon.mods.aeonicraft.block.BlockTemporalCompressor;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

import java.util.ArrayList;

public class ModBlocks
{
    public static final ArrayList<Block> BLOCKS = new ArrayList<>();

    public static final Block temporalCompressor = init(new BlockTemporalCompressor(Material.IRON, "temporal_compressor"));
    public static final Block hourglassProgrammer = init(new BlockHourglassProgrammer(Material.IRON, "hourglass_programmer"));
    //public static final Block blockBrass = init(new BlockAeonicraft(64, 100, "ingot_brass", "ingotBrass"));

    private static Block init(Block block) {
        BLOCKS.add(block);
        return block;
    }

    public static void initTE()
    {
        //Example line:
        //GameRegistry.registerTileEntity(TileMultiblockAssembler.class, multiblockAssembler.getUnlocalizedName());
    }


    //Removed until it becomes relevant to replace it with something better...
    /**
     * By default, register also includes an ItemBlock (blocks are held in the inventory as ItemBlocks, which extend
     * Item). This will skip the ItemBlock and only register the Block, making it unable to be held in the inventory.
     *
     * Generally you would want to use this when you want to register your own custom ItemBlock for the block.

    private static void registerWithoutItemBlock(Block block)
    {
        ForgeRegistries.BLOCKS.register(block);
        blocksForModel.add(block);
    }*/
}
