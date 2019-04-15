package lumaceon.mods.aeonicraft.util;

import com.google.common.base.MoreObjects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Similar to BlockPos but overrided to also include dimension ID.
 */
public class BlockLoc extends BlockPos
{
    private int dimID;

    public BlockLoc(int x, int y, int z, int dimID) {
        super(x, y, z);
        this.dimID = dimID;
    }

    public BlockLoc(BlockPos pos, int dimID) {
        this(pos.getX(), pos.getY(), pos.getZ(), dimID);
    }

    public BlockLoc(BlockPos pos, World world) {
        this(pos.getX(), pos.getY(), pos.getZ(), world.provider.getDimension());
    }

    public void setDimensionID(int dimID)
    {
        this.dimID = dimID;
    }

    public int getDimensionID()
    {
        return dimID;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        else if (!(o instanceof BlockLoc))
        {
            return false;
        }
        else
        {
            BlockLoc loc = (BlockLoc) o;

            if (this.getX() != loc.getX())
            {
                return false;
            }
            else if (this.getY() != loc.getY())
            {
                return false;
            }
            else if (this.getZ() != loc.getZ())
            {
                return false;
            }
            else
            {
                return this.dimID == loc.getDimensionID();
            }
        }
    }

    public String toString()
    {
        return MoreObjects.toStringHelper(this).add("x", this.getX()).add("y", this.getY()).add("z", this.getZ()).add("dim_id", this.getDimensionID()).toString();
    }
}
