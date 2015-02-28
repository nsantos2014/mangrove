package net.minecraft.mangrove.mod.thrive;

import net.minecraft.block.properties.IProperty;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.mangrove.mod.thrive.autobench.BlockAutobench;
import net.minecraft.mangrove.mod.thrive.autobench.TileEntityAutobench;
import net.minecraft.mangrove.mod.thrive.autobench.gui.AutobenchContainer;
import net.minecraft.mangrove.mod.thrive.autobench.gui.AutobenchGui;
import net.minecraft.mangrove.mod.thrive.autocon.connector.BlockHeadConnector;
import net.minecraft.mangrove.mod.thrive.autocon.connector.TileHeadConnector;
import net.minecraft.mangrove.mod.thrive.autocon.conveyor.BlockItemConveyor;
import net.minecraft.mangrove.mod.thrive.autocon.conveyor.TileItemConveyor;
import net.minecraft.mangrove.mod.thrive.autocon.harvester.farmer.BlockHarvesterFarmer;
import net.minecraft.mangrove.mod.thrive.autocon.harvester.farmer.TileHarvesterFarmer;
import net.minecraft.mangrove.mod.thrive.autocon.harvester.miner.BlockHarvesterMiner;
import net.minecraft.mangrove.mod.thrive.autocon.harvester.miner.TileHarvesterMiner;
import net.minecraft.mangrove.mod.thrive.autocon.itembroker.BlockItemBroker;
import net.minecraft.mangrove.mod.thrive.autocon.itembroker.TileItemBroker;
import net.minecraft.mangrove.mod.thrive.autocon.junction.BlockStorageJunction;
import net.minecraft.mangrove.mod.thrive.autocon.junction.TileStorageJunction;
import net.minecraft.mangrove.mod.thrive.duct.AbstractBlockDuct;
import net.minecraft.mangrove.mod.thrive.duct.simpleduct.BlockSimpleDuct;
import net.minecraft.mangrove.mod.thrive.duct.simpleduct.TileSimpleDuct;
import net.minecraft.mangrove.mod.thrive.link.TileEntityLink;
import net.minecraft.mangrove.mod.thrive.robot.block.BlockKernel;
import net.minecraft.mangrove.mod.thrive.robot.block.BlockLink;
import net.minecraft.mangrove.mod.thrive.robot.entity.TileRobotKernel;
import net.minecraft.mangrove.mod.thrive.robot.farmer.BlockRobotFarmerNode;
import net.minecraft.mangrove.mod.thrive.robot.farmer.TileRobotFarmerNode;
import net.minecraft.mangrove.mod.thrive.robot.miner.BlockRobotMinerNode;
import net.minecraft.mangrove.mod.thrive.robot.miner.TileRobotMinerNode;
import net.minecraft.tileentity.TileEntity;

public class MGThriveBlocks {

	public static BlockSimpleDuct simpleduct=null;
//	public static final BlockHarvester harvester=new BlockHarvester();
	public static  BlockKernel robot_kernel=null;
	public static BlockLink robot_link=null;
	public static BlockRobotFarmerNode farmer_node=null;
	public static BlockRobotMinerNode miner_node=null;
//	public static final BlockFeeder feeder=new BlockFeeder();
	
	public static BlockAutobench autobench=null;

	
	public static BlockItemBroker item_broker=null;
	
//	public static net.minecraft.mangrove.mod.thrive.link.BlockLink link=null;

	public static BlockItemConveyor duct_conveyor=null;
	
	public static BlockHeadConnector duct_connector=null;
	
	public static BlockStorageJunction storage_junction=null;
	public static BlockHarvesterFarmer harvester_farmer=null;
	public static BlockHarvesterMiner harvester_miner=null;
	
	public static void preInit(){
//		robot_kernel=new BlockKernel();
//		
//		robot_link=new BlockLink();
//		
//		farmer_node=new BlockRobotFarmerNode();
//		miner_node=new BlockRobotMinerNode();
//		
		duct_conveyor=new BlockItemConveyor();
		duct_connector=new BlockHeadConnector();
		
		autobench=new BlockAutobench();
		
//		simpleduct=new BlockSimpleDuct();
		
//		link=new net.minecraft.mangrove.mod.thrive.link.BlockLink();
		item_broker = new BlockItemBroker();
		storage_junction=new BlockStorageJunction();
		
		harvester_farmer=new BlockHarvesterFarmer();
		harvester_miner=new BlockHarvesterMiner();
	}
	
	public static void init(){
//		BlockModelShapes bms = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes();
//        bms.registerBlockWithStateMapper(simpleduct, (new StateMap.Builder()).addPropertiesToIgnore(new IProperty[] { AbstractBlockDuct.ENABLED/*, FACING, CONNECTORS */}).build());	
		
		
//		TileEntity.addMapping(TileRobotFarmerNode.class, "robot_farmer_head");
//		TileEntity.addMapping(TileRobotMinerNode.class, "robot_miner_head");
//		TileEntity.addMapping(TileRobotKernel.class, "robot_kernel");
		
		TileEntity.addMapping(TileItemConveyor.class, duct_conveyor.getName());
		TileEntity.addMapping(TileHeadConnector.class, duct_connector.getName());
		
		TileEntity.addMapping(TileEntityAutobench.class, "autobench");
//		TileEntity.addMapping(TileSimpleDuct.class, "simpleduct");
		
//		TileEntity.addMapping(TileEntityLink.class, "link");
		
		
		TileEntity.addMapping(TileItemBroker.class, item_broker.getName());
		TileEntity.addMapping(TileStorageJunction.class, storage_junction.getName());

		TileEntity.addMapping(TileHarvesterFarmer.class, harvester_farmer.getName());
		TileEntity.addMapping(TileHarvesterMiner.class, harvester_miner.getName());
		
		MGThriveForge.handler.registerClass(TileEntityAutobench.class, AutobenchContainer.class, AutobenchGui.class);
	}
	
}
