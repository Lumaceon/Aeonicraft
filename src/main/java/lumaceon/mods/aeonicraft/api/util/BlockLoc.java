package lumaceon.mods.aeonicraft.api.util;

import com.google.common.base.MoreObjects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * BlockPos extended to includes a dimension.
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

    public int getDimensionID()
    {
        return dimID;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;
        else if (!(o instanceof BlockLoc))
            return false;
        else
        {
            BlockLoc loc = (BlockLoc) o;

            if (this.getX() != loc.getX())
                return false;
            else if (this.getY() != loc.getY())
                return false;
            else if (this.getZ() != loc.getZ())
                return false;
            else
                return this.dimID == loc.getDimensionID();
        }
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this).add("x", this.getX()).add("y", this.getY()).add("z", this.getZ()).add("dim_id", this.getDimensionID()).toString();
    }

    public static class Pair
    {
        public BlockLoc loc1;
        public BlockLoc loc2;
        private final boolean isOrdered;

        public Pair(BlockLoc loc1, BlockLoc loc2, boolean isOrdered)
        {
            this.loc1 = loc1;
            this.loc2 = loc2;
            this.isOrdered = isOrdered;
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o)
                return true;
            else if (!(o instanceof Pair))
                return false;
            else
            {
                Pair pair = (Pair) o;

                if (this.loc1.equals(pair.loc1) && this.loc2.equals(pair.loc2))
                    return true;
                else if (isOrdered)
                    return false;
                else
                    return this.loc1.equals(pair.loc2) && this.loc2.equals(pair.loc1);
            }
        }
    }
}
