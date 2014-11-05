package net.minecraft.mangrove.mod.thrive.robofarmer.entity.action;

import net.minecraft.mangrove.core.cs.CS;
import net.minecraft.mangrove.core.cs.CSPosition3i;
import net.minecraft.mangrove.mod.thrive.robofarmer.entity.Activity;
import net.minecraft.mangrove.mod.thrive.robofarmer.entity.IServerExecuteAction;
import net.minecraft.mangrove.mod.thrive.robofarmer.entity.IServerUpdateAction;
import net.minecraft.world.World;

public abstract class AbstractServerAction implements IServerUpdateAction,IServerExecuteAction{
	protected Activity nextActivity=Activity.Idle;
	protected World worldObj;
	protected CSPosition3i position;
	protected CSPosition3i local;
	protected CSPosition3i worldPos;
	protected int it=0;
	protected CS localCS;
	protected Activity current;
	protected int step;
	private boolean successfull=false;
	
	@Override
	public void init(World worldObj, CSPosition3i position,Activity current,int step) {
		this.worldObj = worldObj;
		this.position = position;
		this.current = current;
		this.step = step;
		
		this.local=new CSPosition3i();		
		this.local.direction=position.direction;
		this.localCS = CS.subSystem(this.position);
	}
	
	@Override
	public void init(World worldObj, CSPosition3i position,int step) {
		this.worldObj = worldObj;
		this.position = position;
		this.current = Activity.Idle;
		this.step = step;
		
		this.local=new CSPosition3i();		
		this.local.direction=position.direction;
		this.localCS = CS.subSystem(this.position);
	}
	
	protected CSPosition3i toWorld(){
		this.worldPos = this.localCS.toWorld(this.local);
		return this.worldPos;
	}

	@Override
	public void start() {
		this.local=new CSPosition3i();
		this.local.direction=position.direction;
		it=0;
		fail();
	}
	@Override
	public void prepare() {
		this.local=new CSPosition3i();
		this.local.direction=position.direction;
		it=0;
		fail();
	}
	
	@Override
	public Activity nextActivity() {
		return nextActivity;
	}
	@Override
	public boolean isSuccessfull() {
		return successfull;
	}
	
	@Override
	public int nextStep() {
		return this.step;
	}
	
	protected void success(){
		this.successfull=true;
	}
	protected void fail(){
		this.successfull=false;
	}
}
