package net.minecraft.mangrove.mod.thrive.robofarmer.entity.behaviour;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.mangrove.core.entity.EntityBlock;
import net.minecraft.mangrove.core.utils.WorldUtils;
import net.minecraft.mangrove.mod.thrive.robofarmer.entity.IBehaviour;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class IrrigateStartBehavior extends AbstractBehaviour {

	public IrrigateStartBehavior(EntityBlock head) {
	}

	

	@Override
	public void start() {
		this.tick=0;
		WorldUtils.spawnParticle(worldObj, "splash", position.clone().moveRight(0.25).moveUp(1.1).moveForwards(0.25), 0.0D, 0.0D, 0.0D);
		WorldUtils.spawnParticle(worldObj, "splash", position.clone().moveRight(0.25).moveUp(1.1).moveForwards(0.75), 0.0D, 0.0D, 0.0D);
		WorldUtils.spawnParticle(worldObj, "splash", position.clone().moveRight(0.50).moveUp(1.1).moveForwards(0.50), 0.0D, 0.0D, 0.0D);
		WorldUtils.spawnParticle(worldObj, "splash", position.clone().moveRight(0.75).moveUp(1.1).moveForwards(0.25), 0.0D, 0.0D, 0.0D);
		WorldUtils.spawnParticle(worldObj, "splash", position.clone().moveRight(0.75).moveUp(1.1).moveForwards(0.75), 0.0D, 0.0D, 0.0D);
	}

	@Override
	public void execute() {
		this.tick++;

	}

	@Override
	public boolean hasStopped() {
		return tick<80;
	}

}
