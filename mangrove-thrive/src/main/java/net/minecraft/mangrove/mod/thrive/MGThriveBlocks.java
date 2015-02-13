package net.minecraft.mangrove.mod.thrive;

import net.minecraft.mangrove.mod.thrive.autobench.BlockAutobench;
import net.minecraft.mangrove.mod.thrive.autobench.TileEntityAutobench;
import net.minecraft.mangrove.mod.thrive.autobench.gui.AutobenchContainer;
import net.minecraft.mangrove.mod.thrive.autobench.gui.AutobenchGui;
import net.minecraft.mangrove.mod.thrive.robot.block.BlockKernel;
import net.minecraft.mangrove.mod.thrive.robot.block.BlockLink;
import net.minecraft.mangrove.mod.thrive.robot.entity.TileRobotKernel;
import net.minecraft.mangrove.mod.thrive.robot.farmer.BlockRobotFarmerNode;
import net.minecraft.mangrove.mod.thrive.robot.farmer.TileRobotFarmerNode;
import net.minecraft.mangrove.mod.thrive.robot.miner.BlockRobotMinerNode;
import net.minecraft.mangrove.mod.thrive.robot.miner.TileRobotMinerNode;
import net.minecraft.tileentity.TileEntity;

public class MGThriveBlocks {

//	public static final BlockHarvester harvester=new BlockHarvester();
	
	public static  BlockKernel robot_kernel=null;
	
	public static BlockLink robot_link=null;
	
	public static BlockRobotFarmerNode farmer_node=null;
	public static BlockRobotMinerNode miner_node=null;
	
	//public static final BlockFeeder feeder=new BlockFeeder();
	
	public static BlockAutobench autobench=null;
	
	public static void preInit(){
		robot_kernel=new BlockKernel();
		
		robot_link=new BlockLink();
		
		farmer_node=new BlockRobotFarmerNode();
		miner_node=new BlockRobotMinerNode();
		
		autobench=new BlockAutobench();
	}
	public static void init(){
		TileEntity.addMapping(TileRobotFarmerNode.class, "robot_farmer_head");
		TileEntity.addMapping(TileRobotMinerNode.class, "robot_miner_head");
		TileEntity.addMapping(TileRobotKernel.class, "robot_kernel");
		TileEntity.addMapping(TileEntityAutobench.class, "autobench");
		MGThriveForge.handler.registerClass(TileEntityAutobench.class, AutobenchContainer.class, AutobenchGui.class);
	}
	
}
