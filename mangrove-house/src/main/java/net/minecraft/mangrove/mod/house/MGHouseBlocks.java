package net.minecraft.mangrove.mod.house;

import net.minecraft.mangrove.mod.house.block.BlockGlassLamp;
import net.minecraft.mangrove.mod.house.block.BlockGlowLadder;
import net.minecraft.mangrove.mod.house.block.crate.BlockCrate;
import net.minecraft.mangrove.mod.house.block.crate.TileEntityCrate;
import net.minecraft.mangrove.mod.house.block.crate.gui.ContainerCrate;
import net.minecraft.mangrove.mod.house.block.crate.gui.GuiCrate;
import net.minecraft.tileentity.TileEntity;

public class MGHouseBlocks {
	public static BlockGlassLamp glass_lamp=null;
	public static BlockGlowLadder glow_ladder=null;
//	public static final BlockBoatDoor boat_door=new BlockBoatDoor();
//	public static BlockCrate crate=null;
	
//	public static final BlockDuct duct=new BlockDuct();    
//    public static final BlockGratedHopper duct_filter=new BlockGratedHopper();
	
	 public static void preInit() {
	    glass_lamp=new BlockGlassLamp();
	    glow_ladder=new BlockGlowLadder();
//	    crate = new BlockCrate();
//	    TileEntity.addMapping(TileEntityCrate.class, "crate");
	}
	 
	 public static void init(){
//	     MGHouseForge.handler.registerClass(TileEntityCrate.class, ContainerCrate.class, GuiCrate.class);
	     
	 }
	 
	
}
