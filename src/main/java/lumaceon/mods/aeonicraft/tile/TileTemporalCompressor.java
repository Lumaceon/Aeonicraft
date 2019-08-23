package lumaceon.mods.aeonicraft.tile;

import lumaceon.mods.aeonicraft.api.temporalcompression.ITemporalCompressorComponentItem;
import lumaceon.mods.aeonicraft.api.temporalcompression.TemporalCompressorComponent;
import lumaceon.mods.aeonicraft.temporalcompressor.TemporalCompressorMatrix;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.vecmath.Point2i;
import java.util.Collection;

public class TileTemporalCompressor extends TileAeonicraft
{
    public TemporalCompressorMatrix componentMatrix;

    public TileTemporalCompressor() {
        super();
        componentMatrix = new TemporalCompressorMatrix(9);
    }

    public ItemStack requestTemporalComponentChange(TemporalCompressorComponent newComponent, int xOffset, int zOffset)
    {
        Point2i center = componentMatrix.getCenter();
        TemporalCompressorComponent oldComponent = componentMatrix.getComponentForCoordinates(center.x + xOffset, center.y + zOffset);

        // Handle creating new itemstack from the old component if one is there
        ItemStack ret = ItemStack.EMPTY;
        if(oldComponent != null)
        {
            // TODO - this is hideously inefficient and should be replaced...
            Collection<Item> allItems = GameRegistry.findRegistry(Item.class).getValuesCollection();
            for(Item i : allItems)
            {
                if(i instanceof ITemporalCompressorComponentItem)
                {
                    TemporalCompressorComponent t = ((ITemporalCompressorComponentItem) i).getTemporalCompressorComponent(ItemStack.EMPTY);
                    if(t.equals(oldComponent))
                    {
                        ret = new ItemStack(i);
                        break;
                    }
                }
            }
        }

        componentMatrix.setComponentAtCoordinates(newComponent, center.x + xOffset, center.y + zOffset);
        return ret;
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        compound = super.writeToNBT(compound);
        NBTTagCompound nbt = componentMatrix.saveToNBT();
        compound.setTag("component_matrix", nbt);
        return compound;
    }

    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        if(compound.hasKey("component_matrix"))
            componentMatrix.loadFromNBT(compound.getCompoundTag("component_matrix"));
    }
}
