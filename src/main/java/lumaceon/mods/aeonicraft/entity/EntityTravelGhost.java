package lumaceon.mods.aeonicraft.entity;

import lumaceon.mods.aeonicraft.Aeonicraft;
import lumaceon.mods.aeonicraft.capability.travelghost.CapabilityTravelGhost;
import lumaceon.mods.aeonicraft.lib.Particles;
import lumaceon.mods.aeonicraft.util.ParticleHelper;
import lumaceon.mods.aeonicraft.util.SoundHelper;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class EntityTravelGhost extends EntityCreature
{
    @CapabilityInject(CapabilityTravelGhost.ITravelGhostHandler.class)
    private static final Capability<CapabilityTravelGhost.ITravelGhostHandler> TRAVEL_GHOST = null;

    private static final double SPEED = 1.2;

    private EntityPlayer player;

    public EntityTravelGhost(World worldIn) {
        super(worldIn);
        this.setSize(0.6F, 1.95F);
        this.stepHeight = 1.0F;
        this.player = Aeonicraft.proxy.getClientPlayer();
        if(player != null)
        {
            CapabilityTravelGhost.ITravelGhostHandler cap = player.getCapability(TRAVEL_GHOST, null);
            if(cap != null)
            {
                cap.setTravelGhost(this);
            }
        }
    }

    public EntityTravelGhost(World world, EntityPlayer player)
    {
        this(world);
        this.player = player;
        this.setPositionAndRotation(player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);
        CapabilityTravelGhost.ITravelGhostHandler cap = player.getCapability(TRAVEL_GHOST, null);
        if(cap != null)
        {
            cap.setTravelGhost(this);
        }
    }

    @Override
    public boolean canDespawn() {
        return false;
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();
        if(this.ticksExisted > 40)
        {
            // the shortest of lifespans... not even fruit flies may compete.
            this.setDead();

            CapabilityTravelGhost.ITravelGhostHandler cap = player.getCapability(TRAVEL_GHOST, null);
            if(cap != null && cap.getTravelGhost() == this)
            {
                cap.setTravelGhost(null);
            }

            if(player != null && player.world.equals(this.world))
            {
                player.setPositionAndUpdate(this.posX, this.posY, this.posZ);

                BlockPos p = player.getPosition().offset(player.getHorizontalFacing());
                SoundHelper.playLongTimeDing(player, world, p.getX(), p.getY(), p.getZ());

                Vec3d eyePos = player.getPositionEyes(player.getEyeHeight());
                ParticleHelper.spawnTemporalBurstParticles(eyePos, new Vec3d(8, 2, 8), 125);
            }
        }

        if(player != null)
            this.setRotation(player.rotationYaw, player.rotationPitch);
    }

    @Override
    protected void initEntityAI()
    {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIMoveInDirection(this));
    }

    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10000.0D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(50.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(SPEED);
    }

    public class EntityAIMoveInDirection extends EntityAIBase
    {
        EntityTravelGhost travelGhost;
        double movementSpeed = SPEED;
        int range = 50;
        BlockPos destinationBlock = BlockPos.ORIGIN;

        public EntityAIMoveInDirection(EntityTravelGhost travelGhost)
        {
            this.travelGhost = travelGhost;
            this.setMutexBits(5);
        }

        @Override
        public boolean shouldExecute() {
            return true;
        }

        public void updateTask()
        {
            findDestination();
            this.travelGhost.getNavigator().tryMoveToXYZ((double)((float)this.destinationBlock.getX()) + 0.5D, (double)(this.destinationBlock.getY() + 1), (double)((float)this.destinationBlock.getZ()) + 0.5D, this.movementSpeed);
        }

        private void findDestination()
        {
            Vec3d look = travelGhost.getLookVec();
            for(int i = range; i > 0; i--)
            {
                Vec3d lookScaled = look.scale(i);
                Vec3d targetPos = travelGhost.getPositionVector().add(lookScaled);
                destinationBlock = new BlockPos(targetPos);
            }
        }
    }
}
