package net.minecraft.mangrove.mod.thrive.robofarmer.entity.action;

import net.minecraft.block.Block;
import net.minecraft.mangrove.core.cs.CS;
import net.minecraft.mangrove.core.cs.CSPosition3i;
import net.minecraft.mangrove.mod.thrive.robofarmer.entity.Activity;
import net.minecraft.mangrove.mod.thrive.robofarmer.entity.IServerExecuteAction;
import net.minecraft.world.World;

public class MoveAction extends AbstractServerAction{
	

	@Override
	public void execute() {
//		toWorld();
//		final Block block = this.worldObj.getBlock(worldPos.x, worldPos.y, worldPos.z);
//		System.out.println("Block Base at ("+
//				this.position.x+","+ 
//				this.position.y+","+
//				this.position.z);	
//		System.out.println("     Block at ("+
//				(worldPos.x)+","+ 
//				(worldPos.y)+","+
//				(worldPos.z)+
//				" Block="+block);	
//		if( it >=0 && it<3 && !block.isAir(this.worldObj,worldPos.x, worldPos.y, worldPos.z)){
//			fail();			
//			it=5;
//		} else if(it==3 && block.isAir(this.worldObj,worldPos.x, worldPos.y, worldPos.z) ){
//			fail();			
//			it=5;
//		}else{		
//			success();
//		}
//		this.local.y--;
		it++;
	}
	
	@Override
	public void update() {
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

	@Override
	public boolean isDone() {
		return it>3;
	}
	
}
