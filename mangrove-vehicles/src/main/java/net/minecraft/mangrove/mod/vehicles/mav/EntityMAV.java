package net.minecraft.mangrove.mod.vehicles.mav;

import net.minecraft.mangrove.mod.vehicles.mav.i2.EntityVehicleBase;
import net.minecraft.world.World;

public class EntityMAV extends EntityVehicleBase{
	
	public EntityMAV(World world){
		super(world);
	}
	public EntityMAV(World world, double posX, double posY, double posZ){
        this(world);
//        this.setPosition(posX, posY + (double)this.yOffset, posZ);
        this.setPosition(posX, posY , posZ);        
        
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
        this.prevPosX = posX;
        this.prevPosY = posY;
        this.prevPosZ = posZ;
    }
}
