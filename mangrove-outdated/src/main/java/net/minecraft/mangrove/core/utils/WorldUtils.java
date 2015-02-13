package net.minecraft.mangrove.core.utils;

import net.minecraft.mangrove.core.Position;
import net.minecraft.world.World;

public class WorldUtils {

	public static void spawnParticle(World worldObj,String particle,Position position, double velX, double velY, double velZ){
		worldObj.spawnParticle(particle,position.x, position.y, position.z,  velX, velY, velZ);	
	}
}
