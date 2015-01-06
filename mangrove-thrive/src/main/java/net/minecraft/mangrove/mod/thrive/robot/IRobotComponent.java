package net.minecraft.mangrove.mod.thrive.robot;

import java.util.UUID;

import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public interface IRobotComponent{
//    UUID  getSid(World world,int x,int y,int z);
    void updateNetwork(IBlockAccess world,int x,int y,int z);
}
