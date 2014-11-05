package net.minecraft.mangrove.mod.thrive.robofarmer.entity.behaviour;

import net.minecraft.mangrove.core.Position;
import net.minecraft.mangrove.core.cs.CS;
import net.minecraft.mangrove.core.cs.CSPosition3d;
import net.minecraft.mangrove.core.cs.CSPosition3i;
import net.minecraft.mangrove.mod.thrive.robofarmer.entity.IBehaviour;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public abstract class AbstractBehaviour implements IBehaviour{

	protected World worldObj;
	protected int tick;
	protected Position position=new Position();
	protected CS localCS;
	private CSPosition3i pivot;
	
//	@Override
//	public void init(World worldObj, Position position) {
//		this.worldObj = worldObj;
//		this.position = position;
//	}
	
	@Override
	public void init(World worldObj, CSPosition3i position) {
		this.worldObj = worldObj;
		this.pivot = position;
		localCS=CS.subSystem(position);
		this.position.x=position.x;
		this.position.y=position.y;
		this.position.z=position.z;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {		
	}
	
}
