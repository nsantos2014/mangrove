package net.minecraft.mangrove.mod.hud;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.client.Minecraft;
import net.minecraft.mangrove.mod.hud.minimap.Mw;
import net.minecraft.mangrove.mod.hud.minimap.forge.CommonProxy;
import net.minecraft.world.gen.MapGenBase;
import net.minecraft.world.gen.structure.MapGenStronghold;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.InitMapGenEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

@Mod(modid=MGHudForge.ID, name=MGHudForge.NAME,version=MGHudForge.VERSION,useMetadata=false)
public class MGHudForge {
	public static final String ID = "mghud";
    public static final String NAME = "Mangrove HUD";
    public static final String VERSION = "8.0.1";
    
	@Instance(MGHudForge.ID)
	public static MGHudForge instance;
	
	@SidedProxy(clientSide="net.minecraft.mangrove.mod.hud.minimap.forge.ClientProxy", serverSide="net.minecraft.mangrove.mod.hud.minimap.forge.CommonProxy")
	public static CommonProxy proxy;
	
	public static Logger logger = LogManager.getLogger("MGHud");
	
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
		MinecraftForge.EVENT_BUS.register(new MGHudController(Minecraft.getMinecraft()));
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
        MapGenBase newGen = event.newGen;
        	Class<? extends MapGenBase> class1 = newGen.getClass();
			System.out.println("Event : "+event.type+":"+newGen+":"+Arrays.asList(class1.getFields()));
		try {
			for(Field method:class1.getDeclaredFields()){
				System.out.println("Field  : "+method);
			}
			for(Method method:class1.getDeclaredMethods()){
				System.out.println("Method : "+method);
			}
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if( newGen instanceof MapGenStronghold){
			try {
				Method method = class1.getDeclaredMethod("getCoordList");
				method.setAccessible(true);
				System.out.println("Field  : "+method.invoke(newGen));
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    }
}
