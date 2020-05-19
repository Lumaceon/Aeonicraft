package lumaceon.mods.aeonicraft.api.util;

import com.google.common.base.MoreObjects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ChunkLoc
{
    private int x;
    private int y;
    private int dimID;

    public ChunkLoc(int x, int y, int dimID) {
        this.x = x;
        this.y = y;
        this.dimID = dimID;
    }

    public ChunkLoc(int x, int y, World world) {
        this(x, y, world.provider.getDimension());
    }

    public ChunkLoc(BlockPos pos, int dimID) {
        x = pos.getX() >> 4;
        y = pos.getZ() >> 4;
        this.dimID = dimID;
    }

    public ChunkLoc(BlockPos pos, World world) {
        x = pos.getX() >> 4;
        y = pos.getZ() >> 4;
        dimID = world.provider.getDimension();
    }

    public ChunkLoc(BlockLoc loc) {
        x = loc.getX() >> 4;
        y = loc.getZ() >> 4;
        dimID = loc.getDimensionID();
    }

    public void setX(int x) {
        this.x = x;
    }
    public int getX() {
        return x;
    }
    public void setY(int y) {
        this.y = y;
    }
    public int getY() {
        return y;
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
            return true;
        else if (!(o instanceof ChunkLoc))
            return false;
        else
        {
            ChunkLoc loc = (ChunkLoc) o;

            if (this.getX() != loc.getX())
            {
                return false;
            }
            else if (this.getY() != loc.getY())
            {
                return false;
            }
            else
            {
                return this.dimID == loc.getDimensionID();
            }
        }
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this).add("x", this.getX()).add("y", this.getY()).add("dim_id", this.getDimensionID()).toString();
    }
}
