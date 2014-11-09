package net.minecraft.mangrove.mod.thrive.proxy;

import net.minecraft.mangrove.core.entity.EntityBlock;
import net.minecraft.mangrove.mod.thrive.block.harvester.HarvesterRenderer;
import net.minecraft.mangrove.mod.thrive.robofarmer.entity.RenderEntityBlock;
import net.minecraft.mangrove.mod.thrive.robofarmer.render.BlockFarmerLinkRenderer;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy{
	private HarvesterRenderer harvesterRenderer=new HarvesterRenderer();
	private BlockFarmerLinkRenderer blockFarmerLinkRender=new BlockFarmerLinkRenderer();
	
	public void registerRenderers() {
		harvesterRendererID=RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(harvesterRendererID,this.harvesterRenderer);
		//-----------------------------------------------------------------------------------
		blockFarmerLinkRenderId=RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(blockFarmerLinkRenderId,this.blockFarmerLinkRender);
		//----------------------------------------------		
		RenderingRegistry.registerEntityRenderingHandler(EntityBlock.class, RenderEntityBlock.INSTANCE);
	}
}
