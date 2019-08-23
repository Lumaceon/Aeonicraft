package lumaceon.mods.aeonicraft.temporalcompressor;

import lumaceon.mods.aeonicraft.api.temporalcompression.TemporalCompressorComponent;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

import javax.vecmath.Point2i;

public class TemporalCompressorMatrix
{
    public TemporalCompressorComponent[][] matrix; // Array of column arrays

    public TemporalCompressorMatrix(int size) {
        matrix = new TemporalCompressorComponent[size][size];
    }

    public Point2i getCenter() {
        return new Point2i(matrix.length/2, matrix[matrix.length/2].length/2);
    }

    public void setComponentAtCoordinates(TemporalCompressorComponent component, int x, int y) {
        matrix[x][y] = component;
    }

    public TemporalCompressorComponent getComponentForCoordinates(int x, int y) {
        return matrix[x][y];
    }

    public boolean isPartOfMatrix(int x, int y) {
        return x < matrix.length && y < matrix[x].length && !(x == matrix.length / 2 && y == matrix.length / 2);
    }

    public NBTTagCompound saveToNBT()
    {
        NBTTagCompound nbt = new NBTTagCompound();
        NBTTagList list = new NBTTagList();

        for(int x = 0; x < matrix.length; x++)
        {
            for(int y = 0; y < matrix[0].length; y++)
            {
                TemporalCompressorComponent component = matrix[x][y];
                if(component != null)
                {
                    list.appendTag(new NBTTagString(component.getRegistryName().toString()));
                }
                else
                {
                    list.appendTag(new NBTTagString(""));
                }
            }
        }

        nbt.setTag("matrix", list);

        return nbt;
    }

    public void loadFromNBT(NBTTagCompound nbt)
    {
        if(nbt.hasKey("matrix"))
        {
            IForgeRegistry<TemporalCompressorComponent> componentRegistry = GameRegistry.findRegistry(TemporalCompressorComponent.class);

            NBTTagList list = nbt.getTagList("matrix", Constants.NBT.TAG_STRING);

            String s;
            TemporalCompressorComponent c;
            for(int x = 0; x < matrix.length; x++)
            {
                for(int y = 0; y < matrix[0].length; y++)
                {
                    s = list.getStringTagAt(x * matrix[0].length + y);
                    if(s.equals(""))
                        continue;

                    c = componentRegistry.getValue(new ResourceLocation(s));
                    if(c == null)
                        continue; // Most likely to happen if an add-on mod is uninstalled or we remove stuff later...

                    matrix[x][y] = c;
                }
            }
        }
    }
}
