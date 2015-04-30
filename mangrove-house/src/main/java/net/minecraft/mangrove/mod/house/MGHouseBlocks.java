package net.minecraft.mangrove.mod.house;

import net.minecraft.mangrove.mod.house.block.BlockGlassLamp;
import net.minecraft.mangrove.mod.house.block.BlockGlowLadder;
import net.minecraft.mangrove.mod.house.doors.drawbridge.BlockDrawbridge;

public class MGHouseBlocks {
	public static BlockGlassLamp glass_lamp=null;
	public static BlockGlowLadder glow_ladder=null;
	public static BlockDrawbridge drawbridge=null;
		
	 public static void preInit() {
	    glass_lamp=new BlockGlassLamp();
	    glow_ladder=new BlockGlowLadder();
	    drawbridge=new BlockDrawbridge();
	}
	 
	 public static void init(){
	     
	 }
	 
	
}
