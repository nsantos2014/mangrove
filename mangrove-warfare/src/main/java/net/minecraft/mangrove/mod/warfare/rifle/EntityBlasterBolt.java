package net.minecraft.mangrove.mod.warfare.rifle;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityBlasterBolt extends EntityThrowable{

    private float explosionRadius=5.0f;
    public EntityBlasterBolt(World par1World)
    {
        super(par1World);
    }
    public EntityBlasterBolt(World par1World, EntityLivingBase par2EntityLivingBase)
    {
        super(par1World, par2EntityLivingBase);
    }
    public EntityBlasterBolt(World par1World, double par2, double par4, double par6)
    {
        super(par1World, par2, par4, par6);
    }
    @Override
    protected void onImpact(MovingObjectPosition par1MovingObjectPosition){
        this.worldObj.spawnParticle("snowballpoof", this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
        if( par1MovingObjectPosition.entityHit!=null){
            if( par1MovingObjectPosition.entityHit instanceof EntityLivingBase){
                System.out.println("Health:"+((EntityLivingBase)par1MovingObjectPosition.entityHit).getHealth());
                par1MovingObjectPosition.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), (float)3.0);
            }
        }
//        this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, (float)this.explosionRadius, true);
        this.setDead();
    }

    @Override
    public float getBrightness(float p_70013_1_) {
        return 15.0f;
    }
    @Override
    protected float getGravityVelocity() {
        return /*super.getGravityVelocity()*/0;
    }
    
}
