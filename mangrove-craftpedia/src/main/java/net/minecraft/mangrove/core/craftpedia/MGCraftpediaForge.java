package net.minecraft.mangrove.core.craftpedia;

import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = MGCraftpediaForge.ID, name = MGCraftpediaForge.NAME, useMetadata = false, version = MGCraftpediaForge.VERSION, dependencies = "required-after:FML")
public class MGCraftpediaForge {
	public static final String ID = "mgcraftpedia";
	public static final String NAME = "Mangrove Craftpedia";
	public static final String VERSION = "8.0.1";

	@Instance(MGCraftpediaForge.ID)
	public static MGCraftpediaForge instance;
	
	public MGCraftpediaForge() {
    }

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		Craftpedia.instance.registerKeys();
	}
	@EventHandler
	public void postInit(FMLPostInitializationEvent event){	    
	}

	public void addNewRecipes() {
	}
	@SubscribeEvent
    public void renderWorldLastEvent(RenderWorldLastEvent evt) {
	}
}
