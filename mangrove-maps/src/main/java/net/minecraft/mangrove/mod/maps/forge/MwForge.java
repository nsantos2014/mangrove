package net.minecraft.mangrove.mod.maps.forge;

import java.io.File;
import java.net.InetSocketAddress;

import net.minecraft.client.Minecraft;
import net.minecraft.mangrove.mod.maps.Mw;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.InitMapGenEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid=MwForge.ID, name=MwForge.NAME,version=MwForge.VERSION,useMetadata=false)
public class MwForge {
	public static final String ID = "Mangrove|Maps";
	public static final String DOMAIN = "mangrove";
    public static final String NAME = "Mangrove Maps";
    public static final String VERSION = "0.0.1";
    
	@Instance(MwForge.ID)
	public static MwForge instance;
	
	@SidedProxy(clientSide="net.minecraft.mangrove.mod.maps.forge.ClientProxy", serverSide="net.minecraft.mangrove.mod.maps.forge.CommonProxy")
	public static CommonProxy proxy;
	
	public static Logger logger = LogManager.getLogger("MGMaps");
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
        FMLCommonHandler.instance().bus().register(this);
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.TERRAIN_GEN_BUS.register(this);
        proxy.preInit(new File(event.getModConfigurationDirectory(), "mangrove/maps.conf"));
	}
	
	@EventHandler
	public void load(FMLInitializationEvent event) {
		proxy.load();
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit();
	}
	
    @SubscribeEvent
    public void renderMap(RenderGameOverlayEvent.Post event){
        if(event.type == RenderGameOverlayEvent.ElementType.ALL){
            Mw.instance.onTick();
        }
    }

    @SubscribeEvent
    public void onConnected(FMLNetworkEvent.ClientConnectedToServerEvent event){
    	if (!event.isLocal) {
    		InetSocketAddress address = (InetSocketAddress) event.manager.getRemoteAddress();
    		Mw.instance.setServerDetails(address.getHostName(), address.getPort());
    	}
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event){
        if (event.phase == TickEvent.Phase.START){
        	// run the cleanup code when Mw is loaded and the player becomes null.
        	// a bit hacky, but simpler than checking if the connection has closed.
            if ((Mw.instance.ready) && (Minecraft.getMinecraft().thePlayer == null)) {
                Mw.instance.close();
            }
        }
    }
    @SubscribeEvent
    public void onInitWorld(InitMapGenEvent event){
        System.out.println("Event : "+event.type+":"+event.newGen);
    }
}
