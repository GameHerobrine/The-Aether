package com.gildedgames.aether.entity.mobs;

import net.minecraft.util.maths.Box;
import net.minecraft.util.maths.Vec3f;
import net.minecraft.entity.Living;
import net.minecraft.util.maths.MathHelper;
import net.minecraft.level.Level;

import com.gildedgames.aether.entity.projectile.EntityZephyrSnowball;
import com.gildedgames.aether.registry.AetherBlocks;

import net.minecraft.entity.EntityBase;
import net.minecraft.entity.monster.MonsterEntityType;
import net.minecraft.entity.living.FlyingBase;
/*That mob is terrible*/
public class EntityZephyr extends FlyingBase implements MonsterEntityType {
    private long checkTime;
    private final long checkTimeInterval = 3000L;
    private double checkX;
    private double checkY;
    private double checkZ;
    private final double minTraversalDist = 3.0;
    private boolean isStuckWarning;
    public int courseChangeCooldown;
    public double waypointX;
    public double waypointY;
    public double waypointZ;
    private EntityBase targetedEntity;
    private int aggroCooldown;
    public int prevAttackCounter;
    public int attackCounter;
    
    public EntityZephyr(final Level level) {
        super(level);
        this.checkTime = 0L;
        this.checkX = 0.0;
        this.checkY = 0.0;
        this.checkZ = 0.0;
        this.isStuckWarning = false;
        this.courseChangeCooldown = 0;
        this.targetedEntity = null;
        this.aggroCooldown = 0;
        this.prevAttackCounter = 0;
        this.attackCounter = 0;
        this.texture = "/assets/aether/stationapi/textures/entity/Zephyr.png";
        this.setSize(4.0f, 4.0f);
    }
    
    @Override
    protected void tickHandSwing() {
        if (this.y < -2.0 || this.y > 130.0) {
            this.method_920();
        }
        this.prevAttackCounter = this.attackCounter;
        final double d = this.waypointX - this.x;
        final double d2 = this.waypointY - this.y;
        final double d3 = this.waypointZ - this.z;
        final double d4 = MathHelper.sqrt(d * d + d2 * d2 + d3 * d3);
        if (d4 < 1.0 || d4 > 60.0) {
            this.waypointX = this.x + (this.rand.nextFloat() * 2.0f - 1.0f) * 16.0f;
            this.waypointY = this.y + (this.rand.nextFloat() * 2.0f - 1.0f) * 16.0f;
            this.waypointZ = this.z + (this.rand.nextFloat() * 2.0f - 1.0f) * 16.0f;
        }
        if (this.courseChangeCooldown-- <= 0) {
            this.courseChangeCooldown += this.rand.nextInt(5) + 2;
            if (this.isCourseTraversable(this.waypointX, this.waypointY, this.waypointZ, d4)) {
                this.velocityX += d / d4 * 0.1;
                this.velocityY += d2 / d4 * 0.1;
                this.velocityZ += d3 / d4 * 0.1;
            }
            else {
                this.waypointX = this.x;
                this.waypointY = this.y;
                this.waypointZ = this.z;
            }
        }
        if (this.targetedEntity != null && this.targetedEntity.removed) {
            this.targetedEntity = null;
        }
        if (this.targetedEntity == null || this.aggroCooldown-- <= 0) {
            this.targetedEntity = this.level.getClosestPlayerTo(this, 100.0);
            if (this.targetedEntity != null) {
                this.aggroCooldown = 20;
            }
        }
        final double d5 = 64.0;
        if (this.targetedEntity != null && this.targetedEntity.method_1352(this) < d5 * d5) {
            final double d6 = this.targetedEntity.x - this.x;
            final double d7 = this.targetedEntity.boundingBox.minY + this.targetedEntity.height / 2.0f - (this.y + this.height / 2.0f);
            final double d8 = this.targetedEntity.z - this.z;
            final float n = -(float)Math.atan2(d6, d8) * 180.0f / 3.141593f;
            this.yaw = n;
            this.field_1012 = n;
            if (this.method_928(this.targetedEntity)) {
                if (this.attackCounter == 10) {
                    this.level.playSound((EntityBase)this, "aether:aether.sound.mobs.zephyr.zephyrcall", this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f + 1.0f);
                }
                ++this.attackCounter;
                if (this.attackCounter == 20) {
                    this.level.playSound((EntityBase)this, "aether:aether.sound.mobs.zephyr.zephyrshoot", this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f + 1.0f);
                    final EntityZephyrSnowball entitysnowball = new EntityZephyrSnowball(this.level, this, d6, d7, d8);
                    final double d9 = 4.0;
                    final Vec3f vec3d = this.method_926(1.0f);
                    entitysnowball.x = this.x + vec3d.x * d9;
                    entitysnowball.y = this.y + this.height / 2.0f + 0.5;
                    entitysnowball.z = this.z + vec3d.z * d9;
                    this.level.spawnEntity(entitysnowball);
                    this.attackCounter = -40;
                }
            }
            else if (this.attackCounter > 0) {
                --this.attackCounter;
            }
        }
        else {
            final float n2 = -(float)Math.atan2(this.velocityX, this.velocityZ) * 180.0f / 3.141593f;
            this.yaw = n2;
            this.field_1012 = n2;
            if (this.attackCounter > 0) {
                --this.attackCounter;
            }
        }
        this.texture = ((this.attackCounter <= 10) ? "/assets/aether/stationapi/textures/entity/Zephyr.png" : "/assets/aether/stationapi/textures/entity/Zephyr.png");
        //if (!this.level.isServerSide && this.level.difficulty == 0) {
        //this.remove();
        //}
        this.checkForBeingStuck();
    }
    
    private boolean isCourseTraversable(final double d, final double d1, final double d2, final double d3) {
        final double d4 = (this.waypointX - this.x) / d3;
        final double d5 = (this.waypointY - this.y) / d3;
        final double d6 = (this.waypointZ - this.z) / d3;
        final Box axisalignedbb = this.boundingBox.method_92();
        for (int i = 1; i < d3; ++i) {
            axisalignedbb.method_102(d4, d5, d6);
            if (this.level.method_190(this, axisalignedbb).size() > 0) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    protected String getAmbientSound() {
        return "aether:aether.sound.mobs.zephyr.zephyrcall";
    }
    
    @Override
    protected String getHurtSound() {
        return "aether:aether.sound.mobs.zephyr.zephyrcall";
    }
    
    @Override
    protected String getDeathSound() {
        return "aether:aether.sound.mobs.zephyr.zephyrcall";
    }
    
    @Override
    protected int getMobDrops() {
        return AetherBlocks.AERCLOUD.id;
    }
    
    public boolean method_940() {
        return true;
    }
    
    @Override
    protected float getSoundVolume() {
        return 3.0f;
    }
    
    private void checkForBeingStuck() {
        final long curtime = System.currentTimeMillis();
        if (curtime > this.checkTime + 3000L) {
            final double diffx = this.x - this.checkX;
            final double diffy = this.y - this.checkY;
            final double diffz = this.z - this.checkZ;
            final double distanceTravelled = Math.sqrt(diffx * diffx + diffy * diffy + diffz * diffz);
            if (distanceTravelled < 3.0) {
                if (!this.isStuckWarning) {
                    this.isStuckWarning = true;
                }
                else {
                    this.remove();
                }
            }
            this.checkX = this.x;
            this.checkY = this.y;
            this.checkZ = this.z;
            this.checkTime = curtime;
        }
    }
    
    @Override
    public boolean canSpawn() {
        final int i = MathHelper.floor(this.x);
        final int j = MathHelper.floor(this.boundingBox.minY);
        final int k = MathHelper.floor(this.z);
        boolean flag = this.rand.nextInt(85) == 0 && this.level.canSpawnEntity(this.boundingBox) && this.level.method_190(this, this.boundingBox).size() == 0 
        		&& !this.level.method_218(this.boundingBox) && this.level.getTileId(i, j - 1, k) != /*AetherBlocks.DUNGEON_STONE.id*/ 0 &&
        		this.level.getTileId(i, j - 1, k) != /*AetherBlocks.LIGHT_DUNGEON_STONE.id*/ 0 && this.level.getTileId(i, j - 1, k) != /*AetherBlocks.LOCKED_DUNGEON_STONE.id*/ 0 &&
        		this.level.getTileId(i, j - 1, k) != /*AetherBlocks.LOCKED_LIGHT_DUNGEON_STONE.id*/ 0 && this.level.getTileId(i, j - 1, k) != AetherBlocks.HOLYSTONE.id &&
        		this.level.difficulty > 0;
        if(flag)System.out.println("XYZ: "+i+" "+j+" "+k+", "+flag);
        return flag;
    }
    
    @Override
    public int getLimitPerChunk() {
        return 1;
    }
}
