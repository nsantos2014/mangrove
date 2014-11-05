package net.minecraft.mangrove.mod.thrive.robofarmer.entity.action;

import net.minecraft.mangrove.core.cs.CSPosition3i;
import net.minecraft.mangrove.mod.thrive.robofarmer.entity.Activity;
import net.minecraft.mangrove.mod.thrive.robofarmer.entity.IServerExecuteAction;
import net.minecraft.mangrove.mod.thrive.robofarmer.entity.IServerUpdateAction;
import net.minecraft.world.World;

public class VoidServerUpdateAction implements IServerUpdateAction{

	private Activity current;
	private int step;

	@Override
	public void init(World worldObj, CSPosition3i position, Activity current,int step) {
		this.current = current;
		this.step = step;
	}

	@Override
	public void prepare() {
	}

	@Override
	public void update() {
	}

	@Override
	public boolean isDone() {
		return true;
	}

	@Override
	public boolean isSuccessfull() {
		return true;
	}

	@Override
	public Activity nextActivity() {
		return current;
	}

	@Override
	public int nextStep() {
		return step<10?step+1:0;
	}

	

}
