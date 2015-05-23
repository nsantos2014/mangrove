package net.minecraft.mangrove.mod.freight;

import net.minecraft.mangrove.core.GUIHandler;
import net.minecraft.mangrove.mod.freight.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;


@Mod(modid = MGFreightForge.ID, name = MGFreightForge.NAME, version = MGFreightForge.VERSION, useMetadata = false)
public class MGFreightForge {
	public static final String ID = "mgfreight";
	public static final String NAME = "Mangrove Freight";
	public static final String VERSION = "0.0.1";
	public static final String CLIENT_SIDE_PROXY = "net.minecraft.mangrove.mod.freight.proxy.ClientProxy";
	public static final String SERVER_SIDE_PROXY = "net.minecraft.mangrove.mod.freight.proxy.CommonProxy";

	@Instance(MGFreightForge.ID)
	public static MGFreightForge instance;
	@SidedProxy(clientSide = MGFreightForge.CLIENT_SIDE_PROXY, serverSide = MGFreightForge.SERVER_SIDE_PROXY)
	public static CommonProxy proxy;
	public static GUIHandler handler = new GUIHandler();

	@EventHandler
	public void preInit(FMLPreInitializationEvent evt) {
		MGFreightItems.preInit();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.registerRenderers();
		MGFreightItems.init(event);
		MGFreightEntities.init(event);
	}	
	
}
