package net.minecraft.mangrove.mod.thrive.proxy;

import net.minecraft.mangrove.core.entity.EntityBlock;
import net.minecraft.mangrove.mod.thrive.block.harvester.HarvesterRenderer;
import net.minecraft.mangrove.mod.thrive.robofarmer.entity.RenderEntityBlock;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy{
	private HarvesterRenderer harvesterRenderer=new HarvesterRenderer();
	
	public void registerRenderers() {
		harvesterRendererID=RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(harvesterRendererID,this.harvesterRenderer);
		
		RenderingRegistry.registerEntityRenderingHandler(EntityBlock.class, RenderEntityBlock.INSTANCE);
	}
}
