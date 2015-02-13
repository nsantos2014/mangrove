package net.minecraft.mangrove.mod.thrive.robot;

import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;

public interface IRobotComponent{
//    UUID  getSid(World world,int x,int y,int z);
    void updateNetwork(IBlockAccess world,BlockPos blockPos);
}
