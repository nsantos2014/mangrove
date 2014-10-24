package net.minecraft.mangrove.mod.thrive.proxy;

import net.minecraft.mangrove.mod.thrive.block.harvester.HarvesterRenderer;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy{
	private HarvesterRenderer harvesterRenderer=new HarvesterRenderer();
	
	public void registerRenderers() {
		harvesterRendererID=RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(harvesterRendererID,this.harvesterRenderer);
	}
}
