package net.minecraft.mangrove.mod.thrive.robot;

import net.minecraft.mangrove.core.inventory.transactor.ITransactor;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public interface IRobotControl extends IRobotComponent{

    boolean isPowered(World world,BlockPos point);
//    boolean isPowered(World world,int x, int y, int z);
    
    ITransactor getTransactor(World world, BlockPos point);
//    ITransactor getTransactor(World world,int x, int y, int z);
    
}
