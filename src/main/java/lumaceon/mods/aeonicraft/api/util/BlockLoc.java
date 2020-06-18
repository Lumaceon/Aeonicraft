package lumaceon.mods.aeonicraft.api.util;

import com.google.common.base.MoreObjects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import java.util.Objects;

/**
 * BlockPos extended to includes a dimension.
 */
public class BlockLoc
{
    private final int x, y, z;
    private int dimID;

    public BlockLoc(int x, int y, int z, int dimID) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.dimID = dimID;
    }

    public BlockLoc(BlockPos pos, int dimID) {
        this(pos.getX(), pos.getY(), pos.getZ(), dimID);
    }

    public BlockLoc(BlockPos pos, World world) {
        this(pos.getX(), pos.getY(), pos.getZ(), world.provider.getDimension());
    }

    public BlockLoc(NBTTagCompound tag)
    {
        this(
                tag.getInteger("x"),
                tag.getInteger("y"),
                tag.getInteger("z"),
                tag.getInteger("dim")
        );
    }

    public BlockPos getPos() {
        return new BlockPos(getX(), getY(), getZ());
    }

    public int getX() { return this.x; }
    public int getY() { return this.y; }
    public int getZ() { return this.z; }
    public int getDimensionID()
    {
        return this.dimID;
    }

    public BlockLoc add(int x, int y, int z) { return x == 0 && y == 0 && z == 0 ? this : new BlockLoc(this.getX() + x, this.getY() + y, this.getZ() + z, this.getDimensionID()); }
    public BlockLoc add(Vec3i vec)
    {
        return this.add(vec.getX(), vec.getY(), vec.getZ());
    }
    public BlockLoc subtract(Vec3i vec)
    {
        return this.add(-vec.getX(), -vec.getY(), -vec.getZ());
    }
    public BlockLoc up()
    {
        return this.up(1);
    }
    public BlockLoc up(int n)
    {
        return this.offset(EnumFacing.UP, n);
    }
    public BlockLoc down()
    {
        return this.down(1);
    }
    public BlockLoc down(int n)
    {
        return this.offset(EnumFacing.DOWN, n);
    }
    public BlockLoc north()
    {
        return this.north(1);
    }
    public BlockLoc north(int n)
    {
        return this.offset(EnumFacing.NORTH, n);
    }
    public BlockLoc south()
    {
        return this.south(1);
    }
    public BlockLoc south(int n)
    {
        return this.offset(EnumFacing.SOUTH, n);
    }
    public BlockLoc west()
    {
        return this.west(1);
    }
    public BlockLoc west(int n)
    {
        return this.offset(EnumFacing.WEST, n);
    }
    public BlockLoc east()
    {
        return this.east(1);
    }
    public BlockLoc east(int n)
    {
        return this.offset(EnumFacing.EAST, n);
    }
    public BlockLoc offset(EnumFacing facing)
    {
        return this.offset(facing, 1);
    }
    public BlockLoc offset(EnumFacing facing, int n)
    {
        return n == 0 ? this : new BlockLoc(this.getX() + facing.getFrontOffsetX() * n,
                this.getY() + facing.getFrontOffsetY() * n,
                this.getZ() + facing.getFrontOffsetZ() * n,
                this.getDimensionID());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlockLoc blockLoc = (BlockLoc) o;
        return getX() == blockLoc.getX() &&
                getY() == blockLoc.getY() &&
                getZ() == blockLoc.getZ() &&
                dimID == blockLoc.dimID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z, dimID);
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this).add("x", this.getX()).add("y", this.getY()).add("z", this.getZ()).add("dim_id", this.getDimensionID()).toString();
    }

    public NBTTagCompound serializeToNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();

        tag.setInteger("x", this.getX());
        tag.setInteger("y", this.getY());
        tag.setInteger("z", this.getZ());
        tag.setInteger("dim", this.getDimensionID());

        return tag;
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
