package net.minecraft.mangrove.mod.thrive;

import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockStone;
import net.minecraft.block.properties.IProperty;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.mangrove.core.MGCoreItems;
import net.minecraft.mangrove.core.recipes.RecipeBuilder;
import net.minecraft.mangrove.mod.thrive.autocon.autobench.ContainerAutobench;
import net.minecraft.mangrove.mod.thrive.autocon.autobench.GuiAutobench;
import net.minecraft.mangrove.mod.thrive.autocon.autobench.BlockAutobench;
import net.minecraft.mangrove.mod.thrive.autocon.autobench.TileEntityAutobench;
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
import net.minecraft.mangrove.mod.thrive.autocon.junction.ContainerStorageJunction;
import net.minecraft.mangrove.mod.thrive.autocon.junction.GuiStorageJunction;
import net.minecraft.mangrove.mod.thrive.autocon.junction.TileStorageJunction;
import net.minecraft.mangrove.mod.thrive.autocon.pump.BlockPump;
import net.minecraft.mangrove.mod.thrive.autocon.pump.TilePump;
import net.minecraft.mangrove.mod.thrive.cistern.BlockCistern;
import net.minecraft.mangrove.mod.thrive.cistern.TileCistern;
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
import net.minecraft.mangrove.mod.thrive.strongbox.BlockStrongbox;
import net.minecraft.mangrove.mod.thrive.strongbox.ContainerStrongbox;
import net.minecraft.mangrove.mod.thrive.strongbox.GuiStrongbox;
import net.minecraft.mangrove.mod.thrive.strongbox.TileEntityStrongbox;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class MGThriveBlocks {

	public static BlockStrongbox strongbox = null;
	public static BlockCistern cistern = null;
	
	public static BlockPump pump = null;
	
	public static BlockSimpleDuct simpleduct = null;
	public static BlockKernel robot_kernel = null;
	public static BlockLink robot_link = null;
	public static BlockRobotFarmerNode farmer_node = null;
	public static BlockRobotMinerNode miner_node = null;

	public static BlockItemConveyor duct_conveyor = null;

	public static BlockHeadConnector duct_connector = null;
	
	public static BlockAutobench autobench = null;
	public static BlockItemBroker item_broker = null;	
	public static BlockStorageJunction storage_junction = null;
	
	public static BlockHarvesterFarmer harvester_farmer = null;
	public static BlockHarvesterMiner harvester_miner = null;

	public static void preInit() {

		pump=new BlockPump();
		
		cistern=new BlockCistern();
		strongbox=new BlockStrongbox();
		
		autobench = new BlockAutobench();
		
		duct_conveyor = new BlockItemConveyor();
		duct_connector = new BlockHeadConnector();

		item_broker = new BlockItemBroker();
		storage_junction = new BlockStorageJunction();

		harvester_farmer = new BlockHarvesterFarmer();
		harvester_miner = new BlockHarvesterMiner();
	}

	public static void init() {

		TileEntity.addMapping(TileCistern.class, cistern.getName());
		TileEntity.addMapping(TilePump.class, pump.getName());
		
		
		TileEntity.addMapping(TileEntityStrongbox.class, strongbox.getName());
		
		TileEntity.addMapping(TileEntityAutobench.class, autobench.getName());
		
		TileEntity.addMapping(TileItemConveyor.class, duct_conveyor.getName());
		TileEntity.addMapping(TileHeadConnector.class, duct_connector.getName());

		TileEntity.addMapping(TileItemBroker.class, item_broker.getName());
		TileEntity.addMapping(TileStorageJunction.class, storage_junction.getName());

		TileEntity.addMapping(TileHarvesterFarmer.class, harvester_farmer.getName());
		TileEntity.addMapping(TileHarvesterMiner.class, harvester_miner.getName());

		

		MGThriveForge.handler.registerClass(TileStorageJunction.class, ContainerStorageJunction.class, GuiStorageJunction.class);
//		MGThriveForge.handler.registerClass(TileEntityAutobench.class, ContainerAutobench.class, GuiAutobench.class);
		MGThriveForge.handler.registerClass(TileEntityStrongbox.class, ContainerStrongbox.class, GuiStrongbox.class);

//		ItemStack woolCyan = new ItemStack(Blocks.wool,1,EnumDyeColor.CYAN.getMetadata());
//		ItemStack woolGreen = new ItemStack(Blocks.wool,1,EnumDyeColor.GREEN.getMetadata());
		ItemStack stoneGranite = new ItemStack(Blocks.stone,1,BlockStone.EnumType.GRANITE.getMetadata());
		ItemStack stoneDiorite = new ItemStack(Blocks.stone,1,BlockStone.EnumType.DIORITE.getMetadata());
		ItemStack stoneAndesite = new ItemStack(Blocks.stone,1,BlockStone.EnumType.ANDESITE.getMetadata());
		ItemStack logSpruce = new ItemStack(Blocks.log,1 , BlockPlanks.EnumType.BIRCH.getMetadata());
		ItemStack logOak = new ItemStack(Blocks.log,1 , BlockPlanks.EnumType.OAK.getMetadata());
		ItemStack blueDye=new ItemStack(Items.dye, EnumDyeColor.BLUE.getMetadata(), EnumDyeColor.BLUE.getDyeDamage());
		ItemStack greenDye=new ItemStack(Items.dye, EnumDyeColor.GREEN.getMetadata(), EnumDyeColor.GREEN.getDyeDamage());
		
		GameRegistry.addRecipe(RecipeBuilder.newRecipe().of(MGThriveBlocks.pump)
				.line(stoneAndesite,stoneAndesite, stoneAndesite)
				.line(Blocks.glass, stoneAndesite, Blocks.glass)
				.line(Blocks.glass, Items.redstone, Blocks.glass).build()
		);
		
		GameRegistry.addRecipe(RecipeBuilder.newRecipe().of(MGThriveBlocks.cistern)
				.line(MGCoreItems.hardconium_rod, null, MGCoreItems.hardconium_rod)
				.line(MGCoreItems.hardconium_rod, null, MGCoreItems.hardconium_rod)
				.line(MGCoreItems.hardconium_rod, MGCoreItems.hardconium_rod, MGCoreItems.hardconium_rod).build()
		);
		
 		GameRegistry.addRecipe(RecipeBuilder.newRecipe().of(MGThriveBlocks.strongbox)
 				.line(Blocks.planks, Blocks.planks, Blocks.planks)
 				.line(Blocks.planks, Blocks.hopper, Blocks.planks)
 				.line(Blocks.planks, Blocks.planks, Blocks.planks).build()
 				);
		
		GameRegistry.addRecipe(RecipeBuilder.newRecipe().of(MGThriveBlocks.autobench)
				.line(Blocks.planks, Blocks.planks, Blocks.planks)
				.line(Blocks.planks, Blocks.crafting_table, Blocks.planks)
				.line(Blocks.planks, Blocks.planks, Blocks.planks).build()
		);
		
		
		
		
		
		GameRegistry.addRecipe(RecipeBuilder.newRecipe().of(MGThriveBlocks.duct_connector,8)
				.line(null, blueDye, null)
				.line(blueDye, Blocks.hopper, blueDye)
				.line(null, blueDye, null).build()
		);
		
		GameRegistry.addRecipe(RecipeBuilder.newRecipe().of(MGThriveBlocks.duct_conveyor,8)
				.line(null, greenDye, null)
				.line(greenDye, Blocks.hopper, greenDye)
				.line(null, greenDye, null).build()
		);

		
		GameRegistry.addRecipe(RecipeBuilder.newRecipe().of(MGThriveBlocks.item_broker)
				.line(stoneGranite,stoneGranite, stoneGranite)
				.line(Blocks.glass, stoneGranite, Blocks.glass)
				.line(Blocks.glass, Items.redstone, Blocks.glass).build()
		);
		
		GameRegistry.addRecipe(RecipeBuilder.newRecipe().of(MGThriveBlocks.storage_junction)
				.line(stoneDiorite,stoneDiorite, stoneDiorite)
				.line(Blocks.glass, stoneDiorite, Blocks.glass)
				.line(Blocks.glass, Items.redstone, Blocks.glass).build()
		);
		
		GameRegistry.addRecipe(RecipeBuilder.newRecipe().of(MGThriveBlocks.harvester_farmer)
				.line(stoneDiorite,Blocks.glass,Blocks.glass )
				.line(stoneDiorite, greenDye, logSpruce)
				.line(stoneDiorite, Blocks.glass, Blocks.glass).build()
		);
		GameRegistry.addRecipe(RecipeBuilder.newRecipe().of(MGThriveBlocks.harvester_miner)
				.line(stoneGranite,Blocks.glass,Blocks.glass )
				.line(stoneGranite, blueDye, logOak)
				.line(stoneGranite, Blocks.glass, Blocks.glass).build()
		);
	}

}
