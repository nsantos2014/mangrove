package net.minecraft.mangrove.mod.thrive;

import net.minecraft.client.Minecraft;
import net.minecraft.mangrove.mod.thrive.probe.MGProbeOverlay;
import net.minecraft.mangrove.mod.thrive.proxy.CommonProxy;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

@Mod(modid = MGThriveForge.ID, name = MGThriveForge.NAME, version = MGThriveForge.VERSION, useMetadata = false)
public class MGThriveForge {
	public static final String	     ID	               = "mgthrive";
	public static final String	     NAME	           = "Mangrove Thrive";
	public static final String	     VERSION	       = "0.0.1";
	public static final String	     CLIENT_SIDE_PROXY	= "net.minecraft.mangrove.mod.thrive.proxy.ClientProxy";
	public static final String	     SERVER_SIDE_PROXY	= "net.minecraft.mangrove.mod.thrive.proxy.CommonProxy";

	@Instance(MGThriveForge.ID)
	public static MGThriveForge	     instance;
	@SidedProxy(clientSide = MGThriveForge.CLIENT_SIDE_PROXY, serverSide = MGThriveForge.SERVER_SIDE_PROXY)
	public static CommonProxy	     proxy;
	public static MGThriveGuiHandler	handler	       = new MGThriveGuiHandler();

	public int	                     cooldownTime	   = 20;

	@EventHandler
	public void preInit(FMLPreInitializationEvent evt) {
		FMLCommonHandler.instance().bus().register(this);
		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.TERRAIN_GEN_BUS.register(this);
		MGThriveBlocks.preInit();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.registerRenderers();
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, handler);
		MGThriveBlocks.init();
		MGThriveItems.init(event);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		// MinecraftForge.EVENT_BUS.register(new
		// MGProbeOverlay(Minecraft.getMinecraft()));
	}

	@SubscribeEvent
	public void entity(EntityJoinWorldEvent event) {
		if (event.entity == Minecraft.getMinecraft().thePlayer) {
			System.out.println("Player enter world :" + event.entity+":"+ event.world.getWorldTime());
		}
	}
}
