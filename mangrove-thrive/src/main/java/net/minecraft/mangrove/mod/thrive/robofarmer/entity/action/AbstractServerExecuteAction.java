package net.minecraft.mangrove.mod.thrive.robofarmer.entity.action;

import net.minecraft.mangrove.core.cs.CS;
import net.minecraft.mangrove.core.cs.CSPosition3i;
import net.minecraft.mangrove.mod.thrive.robofarmer.entity.Activity;
import net.minecraft.mangrove.mod.thrive.robofarmer.entity.IServerExecuteAction;
import net.minecraft.world.World;

public abstract class AbstractServerExecuteAction implements IServerExecuteAction{
	protected World worldObj;
	protected CSPosition3i position;
	protected CSPosition3i local;
	protected CSPosition3i worldPos;
	protected int it=0;
	protected CS localCS;
	private boolean successfull=false;
	private int step;
	
	public AbstractServerExecuteAction() {	
	}
	
	@Override
	public void init(World worldObj, CSPosition3i position,int step) {
		this.worldObj = worldObj;
		this.position = position;
		this.step = step;
		
		this.local=new CSPosition3i();		
		this.local.direction=position.direction;
		this.localCS = CS.subSystem(this.position);
	}
	
	protected CSPosition3i toWorld(){
		this.worldPos = this.localCS.toWorld(this.local);
		return this.worldPos;
	}

	public boolean isSuccessfull() {
		return successfull;
	}
	
	protected void success(){
		this.successfull=true;
	}
	protected void fail(){
		this.successfull=false;
	}
}
