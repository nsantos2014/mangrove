package net.minecraft.mangrove.mod.thrive.robofarmer.entity;

import net.minecraft.mangrove.core.cs.CSPosition3i;
import net.minecraft.world.World;

public interface IServerExecuteAction {
	void init(World worldObj, CSPosition3i position,int step);
	void start();
	void execute();
	boolean isStopped();
	boolean isSuccessfull();	
}
