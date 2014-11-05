package net.minecraft.mangrove.mod.thrive.robofarmer.entity.action;

import net.minecraft.mangrove.core.cs.CSPosition3i;
import net.minecraft.mangrove.mod.thrive.robofarmer.entity.Activity;
import net.minecraft.mangrove.mod.thrive.robofarmer.entity.IServerExecuteAction;
import net.minecraft.world.World;

public class VoidServerExecuteAction implements IServerExecuteAction{

	private int step;

	@Override
	public void init(World worldObj, CSPosition3i position, int step) {
		this.step = step;
	}

	@Override
	public void start() {
	}

	@Override
	public void execute() {
	}

	@Override
	public boolean isStopped() {
		return true;
	}

	@Override
	public boolean isSuccessfull() {
		return true;
	}	

}
