package net.minecraft.mangrove.mod.thrive.proxy;

import net.minecraft.mangrove.mod.thrive.robot.render.BlockRobotKernelRenderer;
import net.minecraft.mangrove.mod.thrive.robot.render.BlockRobotLinkRenderer;
import net.minecraft.mangrove.mod.thrive.robot.render.BlockRobotNodeRenderer;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy{
//	private HarvesterRenderer harvesterRenderer=new HarvesterRenderer();
    private BlockRobotKernelRenderer blockFarmerKernelRender=new BlockRobotKernelRenderer();
	private BlockRobotLinkRenderer blockFarmerLinkRender=new BlockRobotLinkRenderer();
	private BlockRobotNodeRenderer blockFarmerNodeRender=new BlockRobotNodeRenderer();
	
	
	
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
		
		
	}
}
