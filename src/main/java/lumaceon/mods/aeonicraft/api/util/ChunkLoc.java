package lumaceon.mods.aeonicraft.api.util;

import com.google.common.base.MoreObjects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

import java.util.Objects;

public class ChunkLoc
{
    private int x;
    private int z;
    private int dimID;

    public ChunkLoc(int x, int y, int dimID) {
        this.x = x;
        this.z = y;
        this.dimID = dimID;
    }

    public ChunkLoc(int x, int y, World world) {
        this(x, y, world.provider.getDimension());
    }

    public ChunkLoc(BlockPos pos, int dimID) {
        x = pos.getX() >> 4;
        z = pos.getZ() >> 4;
        this.dimID = dimID;
    }

    public ChunkLoc(BlockPos pos, World world) {
        x = pos.getX() >> 4;
        z = pos.getZ() >> 4;
        dimID = world.provider.getDimension();
    }

    public ChunkLoc(BlockLoc loc) {
        x = loc.getX() >> 4;
        z = loc.getZ() >> 4;
        dimID = loc.getDimensionID();
    }

    public void setX(int x) {
        this.x = x;
    }
    public int getX() {
        return x;
    }
    public void setZ(int z) {
        this.z = z;
    }
    public int getZ() {
        return z;
    }

    public void setDimensionID(int dimID)
    {
        this.dimID = dimID;
    }

    public int getDimensionID()
    {
        return dimID;
    }

    public ChunkPos toChunkPos() {
        return new ChunkPos(x, z);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChunkLoc chunkLoc = (ChunkLoc) o;
        return getX() == chunkLoc.getX() &&
                getZ() == chunkLoc.getZ() &&
                getDimensionID() == chunkLoc.getDimensionID();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getX(), getZ(), getDimensionID());
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this).add("x", this.getX()).add("z", this.getZ()).add("dim_id", this.getDimensionID()).toString();
    }
}
