package net.minecraft.mangrove.mod.house;

import net.minecraft.block.Block;
import net.minecraft.mangrove.mod.house.block.BlockGlassLamp;
import net.minecraft.mangrove.mod.house.block.BlockGlowLadder;
import net.minecraft.mangrove.mod.house.block.crate.BlockCrate;
import net.minecraft.mangrove.mod.house.block.door.BlockBoatDoor;

public interface MGHouseBlocks {
	public static final BlockGlassLamp glass_lamp=new BlockGlassLamp();
	public static final BlockGlowLadder glow_lader=new BlockGlowLadder();
	public static final BlockBoatDoor boat_door=new BlockBoatDoor();
	public static final BlockCrate crate = new BlockCrate();
	
}
