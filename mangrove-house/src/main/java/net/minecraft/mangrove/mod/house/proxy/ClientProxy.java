package net.minecraft.mangrove.mod.house.proxy;

import net.minecraft.mangrove.mod.house.block.BlockGlassLampRenderer;
import net.minecraft.mangrove.mod.house.block.door.BoatDoorRenderer;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy{
	private BoatDoorRenderer boatDoorRenderer=new BoatDoorRenderer();
	private BlockGlassLampRenderer blockGlassLampRenderer=new BlockGlassLampRenderer(); 
	
	public void registerRenderers() {
		boatDoorRendererID=RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(boatDoorRendererID,this.boatDoorRenderer);
		blockGlassLamp=RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(blockGlassLamp,this.blockGlassLampRenderer);
	}
}
