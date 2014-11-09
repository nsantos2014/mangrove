package net.minecraft.mangrove.mod.thrive.robofarmer;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.mangrove.core.cs.CSPoint3i;
import net.minecraft.mangrove.core.inventory.ITransactor;
import net.minecraft.world.World;

public interface IRobotControl extends IRobotComponent{

    boolean isPowered(World world,CSPoint3i point);
    boolean isPowered(World world,int x, int y, int z);
    
    ITransactor getTransactor(World world, CSPoint3i point);
    ITransactor getTransactor(World world,int x, int y, int z);
    
}
