package net.minecraft.mangrove.mod.warfare.rifle;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityBlastingBolt extends EntityThrowable{

    private float explosionRadius=1.0f;
    public EntityBlastingBolt(World par1World)
    {
        super(par1World);
    }
    public EntityBlastingBolt(World par1World, EntityLivingBase par2EntityLivingBase)
    {
        super(par1World, par2EntityLivingBase);
    }
    public EntityBlastingBolt(World par1World, double par2, double par4, double par6)
    {
        super(par1World, par2, par4, par6);
    }
    @Override
    protected void onImpact(MovingObjectPosition par1MovingObjectPosition){
        if( par1MovingObjectPosition.entityHit instanceof EntityLivingBase){
            System.out.println("Health:"+((EntityLivingBase)par1MovingObjectPosition.entityHit).getHealth());         
        }
        this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, (float)this.explosionRadius, true);
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
