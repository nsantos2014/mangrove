package net.minecraft.mangrove.mod.thrive;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.mangrove.GUIHandler;
import net.minecraft.mangrove.mod.thrive.block.harvester.TileEntityHarvester;
import net.minecraft.mangrove.mod.thrive.block.harvester.gui.ContainerHarvester;
import net.minecraft.mangrove.mod.thrive.block.harvester.gui.GuiHarvester;
import net.minecraft.mangrove.mod.thrive.feeder.ContainerFeeder;
import net.minecraft.mangrove.mod.thrive.feeder.GuiFeeder;
import net.minecraft.mangrove.mod.thrive.feeder.TileEntityFeeder;
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
		registerHarvester();
		registerRobotFarmer();
		registerFeeder();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.registerRenderers();
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, handler);
		FMLCommonHandler.instance().bus().register(this);
		MinecraftForge.EVENT_BUS.register(this);		
	}

	public void registerRobotFarmer() {
		GameRegistry.registerBlock(MGThriveBlocks.farmer_link, "farmer_link");		
		GameRegistry.registerBlock(MGThriveBlocks.farmer_node, "farmer_node");
		GameRegistry.registerBlock(MGThriveBlocks.farmer_kernel, "farmer_kernel");
		
		TileEntity.addMapping(TileFarmerNode.class, "farmer_node");
		TileEntity.addMapping(TileFarmerKernel.class, "farmer_kernel");
		
		handler.registerClass(TileFarmerKernel.class, KernelContainer.class, KernelGui.class);
	}
	public void registerHarvester() {
		GameRegistry.registerBlock(MGThriveBlocks.harvester, "harvester");
		handler.registerClass(TileEntityHarvester.class, ContainerHarvester.class, GuiHarvester.class);
		// harvester.setLightLevel(0.9f);
		TileEntity.addMapping(TileEntityHarvester.class, "harvester");

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(
				MGThriveBlocks.harvester), new Object[] {
				Boolean.valueOf(true), " w ", "wiw", "   ",
				Character.valueOf('i'), "iron_ore", Character.valueOf('w'),
				"plankWood" }));
	}
	
	public void registerFeeder() {
        GameRegistry.registerBlock(MGThriveBlocks.feeder, "feeder");
        handler.registerClass(TileEntityFeeder.class, ContainerFeeder.class, GuiFeeder.class);
        // harvester.setLightLevel(0.9f);
        TileEntity.addMapping(TileEntityFeeder.class, "feeder");

//        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(
//                MGThriveBlocks.harvester), new Object[] {
//                Boolean.valueOf(true), " w ", "wiw", "   ",
//                Character.valueOf('i'), "iron_ore", Character.valueOf('w'),
//                "plankWood" }));
    }
}
