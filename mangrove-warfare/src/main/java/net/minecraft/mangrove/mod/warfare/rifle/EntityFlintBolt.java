package net.minecraft.mangrove.mod.warfare.rifle;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityFlintBolt extends EntityThrowable{

    private float explosionRadius=5.0f;
    public EntityFlintBolt(World par1World)
    {
        super(par1World);
    }
    public EntityFlintBolt(World par1World, EntityLivingBase par2EntityLivingBase)
    {
        super(par1World, par2EntityLivingBase);
    }
    public EntityFlintBolt(World par1World, double par2, double par4, double par6)
    {
        super(par1World, par2, par4, par6);
    }
    @Override
    protected void onImpact(MovingObjectPosition mop){
        if( mop.entityHit instanceof EntityLivingBase){
            System.out.println("Health:"+((EntityLivingBase)mop.entityHit).getHealth());        
        }        
        this.worldObj.spawnParticle("snowballpoof", this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
        if( mop.entityHit!=null){
            mop.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), (float)3.0);            
        }
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
