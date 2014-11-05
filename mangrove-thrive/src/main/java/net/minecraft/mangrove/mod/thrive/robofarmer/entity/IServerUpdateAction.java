package net.minecraft.mangrove.mod.thrive.robofarmer.entity;

import net.minecraft.mangrove.core.cs.CSPosition3i;
import net.minecraft.world.World;

public interface IServerUpdateAction {
	void init(World worldObj, CSPosition3i position,Activity current,int step);
	void prepare();
	void update();
	boolean isDone();
	boolean isSuccessfull();
	
	Activity nextActivity();
	int nextStep();
}
