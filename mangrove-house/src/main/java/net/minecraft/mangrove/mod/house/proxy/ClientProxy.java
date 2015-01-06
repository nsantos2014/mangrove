package net.minecraft.mangrove.mod.house.proxy;

import net.minecraft.mangrove.mod.house.block.BlockGlassLampRenderer;
import net.minecraft.mangrove.mod.house.block.door.BoatDoorRenderer;
import net.minecraft.mangrove.mod.house.duct.render.HopperDuctRenderer;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy{
	private BoatDoorRenderer boatDoorRenderer=new BoatDoorRenderer();
	private BlockGlassLampRenderer blockGlassLampRenderer=new BlockGlassLampRenderer(); 
	private HopperDuctRenderer ductRenderer=new HopperDuctRenderer();
	
	public void registerRenderers() {
	    boatDoorRendererID=RenderingRegistry.getNextAvailableRenderId();
	    blockGlassLamp=RenderingRegistry.getNextAvailableRenderId();
	    
	    ductRendererID=RenderingRegistry.getNextAvailableRenderId();
	    
		
		RenderingRegistry.registerBlockHandler(boatDoorRendererID,this.boatDoorRenderer);
		RenderingRegistry.registerBlockHandler(blockGlassLamp,this.blockGlassLampRenderer);
		RenderingRegistry.registerBlockHandler(ductRendererID,ductRenderer);
	}
}
