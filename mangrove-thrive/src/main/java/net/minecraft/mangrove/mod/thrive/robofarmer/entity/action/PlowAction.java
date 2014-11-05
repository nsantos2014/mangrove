package net.minecraft.mangrove.mod.thrive.robofarmer.entity.action;

import net.minecraft.block.Block;
import net.minecraft.mangrove.core.CoordinatesTransform;
import net.minecraft.mangrove.core.cs.CS;
import net.minecraft.mangrove.core.cs.CSPoint3i;
import net.minecraft.mangrove.core.cs.CSPosition3i;
import net.minecraft.mangrove.mod.thrive.robofarmer.entity.Activity;
import net.minecraft.mangrove.mod.thrive.robofarmer.entity.IServerExecuteAction;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class PlowAction extends AbstractServerExecuteAction{

		@Override
	public void start() {
		this.local=new CSPosition3i();
		this.local.direction=position.direction;
		it=0;	
	}

	@Override
	public void execute() {
		toWorld();
		final Block block = this.worldObj.getBlock(worldPos.x, worldPos.y, worldPos.z);
		System.out.println("Block Base at ("+
				this.position.x+","+ 
				this.position.y+","+
				this.position.z);	
		System.out.println("     Block at ("+
				(worldPos.x)+","+ 
				(worldPos.y)+","+
				(worldPos.z)+
				" Block="+block);	
		if( it >=0 && it<3 && !block.isAir(this.worldObj,worldPos.x, worldPos.y, worldPos.z)){
			fail();
			it=5;
		} else if(it==3 && block.isAir(this.worldObj,worldPos.x, worldPos.y, worldPos.z) ){
			fail();
			it=5;
		}else{
			success();			
		}
		this.local.y--;
		it++;
	}

	@Override
	public boolean isStopped() {
		return it>3;
	}	
}
