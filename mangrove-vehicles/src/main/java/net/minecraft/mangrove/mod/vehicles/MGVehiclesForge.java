package net.minecraft.mangrove.mod.vehicles;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.mangrove.GUIHandler;
import net.minecraft.mangrove.mod.vehicles.mav.EntityMAV;
import net.minecraft.mangrove.mod.vehicles.mav.gui.ContainerMAV;
import net.minecraft.mangrove.mod.vehicles.mav.gui.GuiMAV;
import net.minecraft.mangrove.mod.vehicles.proxy.CommonProxy;
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
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid = MGVehiclesForge.ID, name = MGVehiclesForge.NAME, version = MGVehiclesForge.VERSION, useMetadata = false)
public class MGVehiclesForge {
	public static final String ID = "Mangrove|Vehicles";
	public static final String NAME = "Mangrove Vehicles";
	public static final String VERSION = "0.0.1";
	public static final String CLIENT_SIDE_PROXY = "net.minecraft.mangrove.mod.vehicles.proxy.ClientProxy";
	public static final String SERVER_SIDE_PROXY = "net.minecraft.mangrove.mod.vehicles.proxy.CommonProxy";

	@Instance(MGVehiclesForge.ID)
	public static MGVehiclesForge instance;
	@SidedProxy(clientSide = MGVehiclesForge.CLIENT_SIDE_PROXY, serverSide = MGVehiclesForge.SERVER_SIDE_PROXY)
	public static CommonProxy proxy;
	public static GUIHandler handler = new GUIHandler();
	

	@EventHandler
	public void preInit(FMLPreInitializationEvent evt) {
	    registerEntityMAV();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.registerRenderers();
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, handler);
		FMLCommonHandler.instance().bus().register(this);
		MinecraftForge.EVENT_BUS.register(this);		
	}	
	
	public void registerEntityMAV(){
	    GameRegistry.registerItem(MGVehiclesItems.mav, "mav");
        EntityRegistry.registerModEntity(EntityMAV.class, "mav", 12090, instance, 80, 3, true);
        handler.registerID(CommonProxy.idGuiMAV, ContainerMAV.class, GuiMAV.class,EntityMAV.class);
	}
}
