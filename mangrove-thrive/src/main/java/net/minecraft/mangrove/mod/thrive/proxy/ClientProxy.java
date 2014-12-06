package net.minecraft.mangrove.mod.thrive.proxy;

import net.minecraft.mangrove.core.entity.EntityBlock;
import net.minecraft.mangrove.mod.thrive.robofarmer.entity.RenderEntityBlock;
import net.minecraft.mangrove.mod.thrive.robofarmer.render.BlockFarmerKernelRenderer;
import net.minecraft.mangrove.mod.thrive.robofarmer.render.BlockFarmerLinkRenderer;
import net.minecraft.mangrove.mod.thrive.robofarmer.render.BlockFarmerNodeRenderer;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy{
//	private HarvesterRenderer harvesterRenderer=new HarvesterRenderer();
    private BlockFarmerKernelRenderer blockFarmerKernelRender=new BlockFarmerKernelRenderer();
	private BlockFarmerLinkRenderer blockFarmerLinkRender=new BlockFarmerLinkRenderer();
	private BlockFarmerNodeRenderer blockFarmerNodeRender=new BlockFarmerNodeRenderer();
	
	public void registerRenderers() {
//		harvesterRendererID=RenderingRegistry.getNextAvailableRenderId();
//		RenderingRegistry.registerBlockHandler(harvesterRendererID,this.harvesterRenderer);
		//-----------------------------------------------------------------------------------
	    blockFarmerKernelRenderId=RenderingRegistry.getNextAvailableRenderId();
		blockFarmerLinkRenderId=RenderingRegistry.getNextAvailableRenderId();
		blockFarmerNodeRenderId=RenderingRegistry.getNextAvailableRenderId();
		
		RenderingRegistry.registerBlockHandler(blockFarmerKernelRenderId,this.blockFarmerKernelRender);
		RenderingRegistry.registerBlockHandler(blockFarmerLinkRenderId,this.blockFarmerLinkRender);
		RenderingRegistry.registerBlockHandler(blockFarmerNodeRenderId,this.blockFarmerNodeRender);
		//----------------------------------------------		
		RenderingRegistry.registerEntityRenderingHandler(EntityBlock.class, RenderEntityBlock.INSTANCE);
	}
}
