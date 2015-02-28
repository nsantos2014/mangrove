package net.minecraft.mangrove.mod.thrive;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.mangrove.core.GUIHandler;
import net.minecraft.mangrove.mod.thrive.autobench.TileEntityAutobench;
import net.minecraft.mangrove.mod.thrive.autobench.gui.AutobenchContainer;
import net.minecraft.mangrove.mod.thrive.autobench.gui.AutobenchGui;
import net.minecraft.mangrove.mod.thrive.autocon.itembroker.ContainerItemBroker;
import net.minecraft.mangrove.mod.thrive.autocon.itembroker.GuiItemBroker;
import net.minecraft.mangrove.mod.thrive.autocon.itembroker.TileItemBroker;
import net.minecraft.mangrove.mod.thrive.proxy.CommonProxy;
import net.minecraft.mangrove.mod.thrive.robot.entity.TileRobotKernel;
import net.minecraft.mangrove.mod.thrive.robot.farmer.TileRobotFarmerNode;
import net.minecraft.mangrove.mod.thrive.robot.gui.KernelContainer;
import net.minecraft.mangrove.mod.thrive.robot.gui.KernelGui;
import net.minecraft.mangrove.mod.thrive.robot.miner.TileRobotMinerNode;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

@Mod(modid = MGThriveForge.ID, name = MGThriveForge.NAME, version = MGThriveForge.VERSION, useMetadata = false)
public class MGThriveForge {
	public static final String ID = "mgthrive";
	public static final String NAME = "Mangrove Thrive";
	public static final String VERSION = "0.0.1";
	public static final String CLIENT_SIDE_PROXY = "net.minecraft.mangrove.mod.thrive.proxy.ClientProxy";
	public static final String SERVER_SIDE_PROXY = "net.minecraft.mangrove.mod.thrive.proxy.CommonProxy";

	@Instance(MGThriveForge.ID)
	public static MGThriveForge instance;
	@SidedProxy(clientSide = MGThriveForge.CLIENT_SIDE_PROXY, serverSide = MGThriveForge.SERVER_SIDE_PROXY)
	public static CommonProxy proxy;
//	public static GUIHandler handler = new GUIHandler();
	public static MGThriveGuiHandler handler=new MGThriveGuiHandler();
	
	public int cooldownTime=20;
	
	//public static CreativeTabs tabThrive = new CreativeTabs("MyMod");

	@EventHandler
	public void preInit(FMLPreInitializationEvent evt) {
		FMLCommonHandler.instance().bus().register(this);
	    MinecraftForge.EVENT_BUS.register(this);
	    MinecraftForge.TERRAIN_GEN_BUS.register(this);
		MGThriveBlocks.preInit();
//	    registerRobot();
//		registerRobotFarmer();
//		registerRobotMiner();
//		registerAutobench();	
		
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.registerRenderers();
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, handler);
		MGThriveBlocks.init();
		MGThriveItems.init(event);
//		FMLCommonHandler.instance().bus().register(this);
//		MinecraftForge.EVENT_BUS.register(this);		
		
//		handler.registerClass(TileItemBroker.class, ContainerItemBroker.class, GuiItemBroker.class);
	}
	public void registerAutobench() {
	    GameRegistry.registerBlock(MGThriveBlocks.autobench, "autobench");
	    TileEntity.addMapping(TileEntityAutobench.class, "autobench");
	    handler.registerClass(TileEntityAutobench.class, AutobenchContainer.class, AutobenchGui.class);
	    
	    GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MGThriveBlocks.autobench,1,0), new Object[] {
            Boolean.valueOf(true), 
            "www", 
            "waw", 
            "www",
            Character.valueOf('a'), Blocks.crafting_table, 
            Character.valueOf('w'), Blocks.planks 
        }));
	}
//	public void registerRobot() {
//		GameRegistry.registerBlock(MGThriveBlocks.robot_link, "robot_link");		
//		
//		GameRegistry.registerBlock(MGThriveBlocks.robot_kernel, "robot_kernel");
//		
//		
//		TileEntity.addMapping(TileRobotKernel.class, "robot_kernel");
//		
//		handler.registerClass(TileRobotKernel.class, KernelContainer.class, KernelGui.class);
//		
//		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MGThriveBlocks.robot_link,16,0), new Object[] {
//            Boolean.valueOf(true), 
//            "iwi", 
//            "iri", 
//            "iwi",
//            Character.valueOf('i'), Items.iron_ingot,
//            Character.valueOf('r'), Items.redstone, 
//            Character.valueOf('w'), Blocks.planks 
//        }));
//		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MGThriveBlocks.robot_kernel,1,0), new Object[] {
//            Boolean.valueOf(true), 
//            "iwi", 
//            "iai", 
//            "iri",
//            Character.valueOf('i'), Items.iron_ingot,
//            Character.valueOf('r'), Items.redstone, 
//            Character.valueOf('w'), Blocks.planks,
//            Character.valueOf('a'), Blocks.crafting_table,
//        }));
//		
//	}
//	public void registerRobotFarmer() {
//	    GameRegistry.registerBlock(MGThriveBlocks.farmer_node, "farmer_node");
//	    TileEntity.addMapping(TileRobotFarmerNode.class, "farmer_node");
//	    GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MGThriveBlocks.farmer_node,4,0), new Object[] {
//            Boolean.valueOf(true), 
//            "iwi", 
//            "iti", 
//            "iri",
//            Character.valueOf('i'), Items.iron_ingot,
//            Character.valueOf('r'), Items.redstone, 
//            Character.valueOf('w'), Blocks.planks,
//            Character.valueOf('a'), Blocks.crafting_table,
//            Character.valueOf('t'), Items.stone_hoe,
//        }));
//	}
//	public void registerRobotMiner() {
//        GameRegistry.registerBlock(MGThriveBlocks.miner_node, "miner_node");
//        TileEntity.addMapping(TileRobotMinerNode.class, "miner_node");
//        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MGThriveBlocks.miner_node,4,0), new Object[] {
//            Boolean.valueOf(true), 
//            "iwi", 
//            "iti", 
//            "iri",
//            Character.valueOf('i'), Items.iron_ingot,
//            Character.valueOf('r'), Items.redstone, 
//            Character.valueOf('w'), Blocks.planks,
//            Character.valueOf('a'), Blocks.crafting_table,
//            Character.valueOf('t'), Items.stone_pickaxe,
//        }));
//    }
	
}
