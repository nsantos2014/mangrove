package net.minecraft.mangrove.mod.thrive;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.mangrove.GUIHandler;
import net.minecraft.mangrove.mod.thrive.autobench.TileEntityAutobench;
import net.minecraft.mangrove.mod.thrive.autobench.gui.AutobenchContainer;
import net.minecraft.mangrove.mod.thrive.autobench.gui.AutobenchGui;
import net.minecraft.mangrove.mod.thrive.proxy.CommonProxy;
import net.minecraft.mangrove.mod.thrive.robofarmer.entity.TileFarmerKernel;
import net.minecraft.mangrove.mod.thrive.robofarmer.entity.TileFarmerNode;
import net.minecraft.mangrove.mod.thrive.robofarmer.gui.KernelContainer;
import net.minecraft.mangrove.mod.thrive.robofarmer.gui.KernelGui;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.ShapedOreRecipe;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = MGThriveForge.ID, name = MGThriveForge.NAME, version = MGThriveForge.VERSION, useMetadata = false)
public class MGThriveForge {
	public static final String ID = "Mangrove|Thrive";
	public static final String NAME = "Mangrove Thrive";
	public static final String VERSION = "0.0.1";
	public static final String CLIENT_SIDE_PROXY = "net.minecraft.mangrove.mod.thrive.proxy.ClientProxy";
	public static final String SERVER_SIDE_PROXY = "net.minecraft.mangrove.mod.thrive.proxy.CommonProxy";

	@Instance(MGThriveForge.ID)
	public static MGThriveForge instance;
	@SidedProxy(clientSide = MGThriveForge.CLIENT_SIDE_PROXY, serverSide = MGThriveForge.SERVER_SIDE_PROXY)
	public static CommonProxy proxy;
	public static GUIHandler handler = new GUIHandler();
	
	//public static CreativeTabs tabThrive = new CreativeTabs("MyMod");

	@EventHandler
	public void preInit(FMLPreInitializationEvent evt) {
		registerRobotFarmer();
		registerAutobench();		
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.registerRenderers();
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, handler);
		FMLCommonHandler.instance().bus().register(this);
		MinecraftForge.EVENT_BUS.register(this);		
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
	public void registerRobotFarmer() {
		GameRegistry.registerBlock(MGThriveBlocks.farmer_link, "farmer_link");		
		GameRegistry.registerBlock(MGThriveBlocks.farmer_node, "farmer_node");
		GameRegistry.registerBlock(MGThriveBlocks.farmer_kernel, "farmer_kernel");
		
		TileEntity.addMapping(TileFarmerNode.class, "farmer_node");
		TileEntity.addMapping(TileFarmerKernel.class, "farmer_kernel");
		
		handler.registerClass(TileFarmerKernel.class, KernelContainer.class, KernelGui.class);
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MGThriveBlocks.farmer_link,16,0), new Object[] {
            Boolean.valueOf(true), 
            "iwi", 
            "iri", 
            "iwi",
            Character.valueOf('i'), Items.iron_ingot,
            Character.valueOf('r'), Items.redstone, 
            Character.valueOf('w'), Blocks.planks 
        }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MGThriveBlocks.farmer_kernel,1,0), new Object[] {
            Boolean.valueOf(true), 
            "iwi", 
            "iai", 
            "iri",
            Character.valueOf('i'), Items.iron_ingot,
            Character.valueOf('r'), Items.redstone, 
            Character.valueOf('w'), Blocks.planks,
            Character.valueOf('a'), Blocks.crafting_table,
        }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MGThriveBlocks.farmer_node,4,0), new Object[] {
            Boolean.valueOf(true), 
            "iwi", 
            "i i", 
            "iri",
            Character.valueOf('i'), Items.iron_ingot,
            Character.valueOf('r'), Items.redstone, 
            Character.valueOf('w'), Blocks.planks,
            Character.valueOf('a'), Blocks.crafting_table,
        }));
	}
//	public void registerHarvester() {
//		GameRegistry.registerBlock(MGThriveBlocks.harvester, "harvester");
//		handler.registerClass(TileEntityHarvester.class, ContainerHarvester.class, GuiHarvester.class);
//		// harvester.setLightLevel(0.9f);
//		TileEntity.addMapping(TileEntityHarvester.class, "harvester");
//
//		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(
//				MGThriveBlocks.harvester), new Object[] {
//				Boolean.valueOf(true), " w ", "wiw", "   ",
//				Character.valueOf('i'), "iron_ore", Character.valueOf('w'),
//				"plankWood" }));
//	}
	
//	public void registerFeeder() {
//        GameRegistry.registerBlock(MGThriveBlocks.feeder, "feeder");
//        handler.registerClass(TileEntityFeeder.class, ContainerFeeder.class, GuiFeeder.class);
//        // harvester.setLightLevel(0.9f);
//        TileEntity.addMapping(TileEntityFeeder.class, "feeder");
//
////        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(
////                MGThriveBlocks.harvester), new Object[] {
////                Boolean.valueOf(true), " w ", "wiw", "   ",
////                Character.valueOf('i'), "iron_ore", Character.valueOf('w'),
////                "plankWood" }));
//    }
}
