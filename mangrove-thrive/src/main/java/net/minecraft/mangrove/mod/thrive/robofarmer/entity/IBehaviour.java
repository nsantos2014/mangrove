package net.minecraft.mangrove.mod.thrive.robofarmer.entity;

import net.minecraft.mangrove.core.INBTTagable;
import net.minecraft.mangrove.core.Position;
import net.minecraft.mangrove.core.cs.CSPosition3i;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public interface IBehaviour extends INBTTagable{
	void start();
	void execute();
	boolean hasStopped();
	//void init(World worldObj, Position position);
	void init(World worldObj, CSPosition3i position);
}
