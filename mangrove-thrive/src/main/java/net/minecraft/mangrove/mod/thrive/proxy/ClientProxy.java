package net.minecraft.mangrove.mod.thrive.proxy;

import net.minecraft.mangrove.mod.thrive.autocon.TESRDuct;
import net.minecraft.mangrove.mod.thrive.autocon.connector.TileHeadConnector;
import net.minecraft.mangrove.mod.thrive.autocon.conveyor.TileItemConveyor;
import net.minecraft.mangrove.mod.thrive.duct.simpleduct.TESRSimpleDuct;
import net.minecraft.mangrove.mod.thrive.duct.simpleduct.TileSimpleDuct;
import net.minecraft.mangrove.mod.thrive.link.TESRLink;
import net.minecraft.mangrove.mod.thrive.link.TileEntityLink;
import net.minecraftforge.fml.client.registry.ClientRegistry;


public class ClientProxy extends CommonProxy{
//	private HarvesterRenderer harvesterRenderer=new HarvesterRenderer();
//    private BlockRobotKernelRenderer blockFarmerKernelRender=new BlockRobotKernelRenderer();
//	private BlockRobotLinkRenderer blockFarmerLinkRender=new BlockRobotLinkRenderer();
//	private BlockRobotNodeRenderer blockFarmerNodeRender=new BlockRobotNodeRenderer();
	
	private TESRSimpleDuct tesrSimpleDuct=new TESRSimpleDuct();
	
	private TESRLink tesrLink=new TESRLink();
	private TESRDuct tesrDuct=new TESRDuct();
	
	public void registerRenderers() {
		ClientRegistry.bindTileEntitySpecialRenderer(TileSimpleDuct.class, tesrSimpleDuct);
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLink.class, tesrLink);
		
		ClientRegistry.bindTileEntitySpecialRenderer(TileItemConveyor.class, tesrDuct);
		ClientRegistry.bindTileEntitySpecialRenderer(TileHeadConnector.class, tesrDuct);
//		harvesterRendererID=RenderingRegistry.getNextAvailableRenderId();
//		RenderingRegistry.registerBlockHandler(harvesterRendererID,this.harvesterRenderer);
//		//-----------------------------------------------------------------------------------
//	    blockFarmerKernelRenderId=RenderingRegistry.getNextAvailableRenderId();
//		blockFarmerLinkRenderId=RenderingRegistry.getNextAvailableRenderId();
//		blockFarmerNodeRenderId=RenderingRegistry.getNextAvailableRenderId();
//		
//		
//		
//		RenderingRegistry.registerBlockHandler(blockFarmerKernelRenderId,this.blockFarmerKernelRender);
//		RenderingRegistry.registerBlockHandler(blockFarmerLinkRenderId,this.blockFarmerLinkRender);
//		RenderingRegistry.registerBlockHandler(blockFarmerNodeRenderId,this.blockFarmerNodeRender);
		//----------------------------------------------		
		
		
	}
}
