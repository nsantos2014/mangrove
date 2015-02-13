package net.minecraft.mangrove.mod.house.duct.render;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHopper;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.mangrove.mod.house.duct.AbstractBlockDuct;
import net.minecraft.mangrove.mod.house.proxy.CommonProxy;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class HopperDuctRenderer implements ISimpleBlockRenderingHandler {
	
	public boolean renderBlockHopperMetadata(AbstractBlockDuct block, int x, int y, int z, int meta, boolean inventory, RenderBlocks renderer, IBlockAccess world)
	  {
	    Tessellator tessellator = Tessellator.instance;
	    int i1 = BlockHopper.getDirectionFromMetadata(meta);
	    
	    double d0 = 0.625D;
	    renderer.setRenderBounds(0.0D, d0, 0.0D, 1.0D, 1.0D, 1.0D);
	    /* Renders Main Block */
	    if (!(inventory)) {
	      tessellator.setBrightness(block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z));
	      float f1 = 1.0F;
	      int j1 = block.colorMultiplier(renderer.blockAccess, x, y, z);
	      float f = (j1 >> 16 & 0xFF) / 255.0F;
	      float f2 = (j1 >> 8 & 0xFF) / 255.0F;
	      float f3 = (j1 & 0xFF) / 255.0F;

	      if (EntityRenderer.anaglyphEnable)
	      {
	        float f4 = (f * 30.0F + f2 * 59.0F + f3 * 11.0F) / 100.0F;
	        float f5 = (f * 30.0F + f2 * 70.0F) / 100.0F;
	        float f6 = (f * 30.0F + f3 * 70.0F) / 100.0F;
	        f = f4;
	        f2 = f5;
	        f3 = f6;
	      }

	      tessellator.setColorOpaque_F(f1 * f, f1 * f2, f1 * f3);
	    }

	    
	    //IIcon icon1 = BlockHopper.getHopperIcon("hopper_inside");
	    float f = 0.125F;

	    double d1 = 0.25D;
	    double d2 = 0.25D;

	    renderer.setRenderBounds(d1, d2, d1, 1.0D - d1, 1.0D - d1, 1.0D - d1);

	    if (inventory){
	      IIcon icon = AbstractBlockDuct.getHopperIcon("hopper_outside");
	      tessellator.startDrawingQuads();
	      tessellator.setNormal(1.0F, 0.0F, 0.0F);
	      renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, icon);

	      tessellator.setNormal(-1.0F, 0.0F, 0.0F);
	      renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, icon);

	      tessellator.setNormal(0.0F, 0.0F, 1.0F);
	      renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, icon);

	      tessellator.setNormal(0.0F, 0.0F, -1.0F);
	      renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, icon);

	      tessellator.setNormal(0.0F, 1.0F, 0.0F);
	      renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, icon);

	      tessellator.setNormal(0.0F, -1.0F, 0.0F);
	      renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, icon);
	      tessellator.draw();
	    } else {
//	      IIcon icon = block.getIcon("duct_back");
//		  renderer.setOverrideBlockTexture(icon);
	      renderer.renderStandardBlock(block, x, y, z);
	    }

	    if (!(inventory)){
	      double d3 = 0.375D;
	      double d4 = 0.25D;

	      IIcon overrideicon = renderer.overrideBlockTexture;
	      if (overrideicon == null){
	        renderer.setOverrideBlockTexture(Blocks.redstone_ore.getIcon(1, 0));
	      }	      	      
	      
	      if (i1 == 0){ 
	        renderer.setRenderBounds(d3, 0.0D, d3, 1.0D - d3, 0.25D, 1.0D - d3);
	        renderer.renderStandardBlock(block, x, y, z);	        
	      }

	      if (i1 == 1){
	        renderer.setRenderBounds(d3, 0.5D, d3, 1.0D - d3, 1.0D, 1.0D - d3);
	        renderer.renderStandardBlock(block, x, y, z);
	      }

	      if (i1 == 2){
	        renderer.setRenderBounds(d3, d2 + d2 / 2.0D, 0.0D, 1.0D - d3, d2 + d4 + d2 / 2.0D, d1);
	        renderer.renderStandardBlock(block, x, y, z);
	      }

	      if (i1 == 3){
	        renderer.setRenderBounds(d3, d2 + d2 / 2.0D, 1.0D - d1, 1.0D - d3, d2 + d4 + d2 / 2.0D, 1.0D);
	        renderer.renderStandardBlock(block, x, y, z);
	      }

	      if (i1 == 4){
	        renderer.setRenderBounds(0.0D, d2 + d2 / 2.0D, d3, d1, d2 + d4 + d2 / 2.0D, 1.0D - d3);
	        renderer.renderStandardBlock(block, x, y, z);
	      }

	      if (i1 == 5){
	        renderer.setRenderBounds(1.0D - d1, d2 + d2 / 2.0D, d3, 1.0D, d2 + d4 + d2 / 2.0D, 1.0D - d3);
	        renderer.renderStandardBlock(block, x, y, z);
	      }

	      
	      /* Renders back connector*/
//	      d1 += 0.04D;
//	      d2 += 0.04D;
	      IIcon icon = block.getIcon("duct_back");
	      renderer.setOverrideBlockTexture(icon);
	      
	      for (int cdir = 0; cdir < 6; ++cdir)
	      {
	        if (cdir != i1) {
	          //ForgeDirection fdir = ForgeDirection.getOrientation(cdir);
//	        	boolean isconnected = AbstractBlockDuct.isDuctConnectedTo(world, par2 + fdir.offsetX, par3 + fdir.offsetY, par4 + fdir.offsetZ, fdir.getOpposite().ordinal());
//		          if (!(isconnected))
//		            continue;	          
//		          switch (fdir.ordinal())
//		          {
	          boolean isconnected = AbstractBlockDuct.isDuctReceivingFrom(world, 
	        		  	x + Facing.offsetsXForSide[cdir], 
	        		  	y + Facing.offsetsYForSide[cdir], 
	        		  	z + Facing.offsetsZForSide[cdir], 
	        		  	Facing.oppositeSide[cdir]);
	          if (!(isconnected))
	            continue;
	          // "DOWN", "UP", "NORTH", "SOUTH", "WEST", "EAST"
//	          System.out.println(" > DIR : "+cdir+"="+Facing.facings[cdir]);
	          switch (cdir){
	          case 0:
	        	  renderer.setRenderBounds(d1, 0.0D, d1, 1.0D - d1, d2, 1.0D - d1);break;//switch with 5	        	  
	          case 1:
	        	  renderer.setRenderBounds(d1, 1.0D - d2, d1, 1.0D - d1, 1.0D, 1.0D - d1); break;
	          case 2:
	            renderer.setRenderBounds(d1, d2, 0.0D, 1.0D - d1, 1.0D - d1, d1); break;
	          case 3:
	            renderer.setRenderBounds(d1, d2, 1.0D - d1, 1.0D - d1, 1.0D - d1, 1.0D); break;
	          case 4:	            
	            renderer.setRenderBounds(0.0D, d2, d1, d1, 1.0D - d1, 1.0D - d1); break;
	          case 5:
	        	renderer.setRenderBounds(1.0D - d1, d2, d1, 1.0D, 1.0D - d1, 1.0D - d1); break;// was 1	            
	          }

	          renderer.renderStandardBlock(block, x, y, z);
	        }

	      }
	      renderer.setOverrideBlockTexture(overrideicon);
	    }

	    renderer.clearOverrideBlockTexture();
	    return true;
	  }

		public void renderInventoryBlock(Block block, int metadata, int modelID,
				RenderBlocks renderer) {
			renderBlockHopperMetadata((AbstractBlockDuct) block, 0, 0, 0, 0, true,
					renderer, null);
		}

		public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z,
				Block block, int modelId, RenderBlocks renderer) {
			Tessellator tessellator = Tessellator.instance;
			tessellator.setBrightness(block.getMixedBrightnessForBlock(renderer.blockAccess,
					x, y, z));
			float f = 1.0F;
			int l = block.colorMultiplier(renderer.blockAccess, x, y, z);
			float f1 = (l >> 16 & 0xFF) / 255.0F;
			float f2 = (l >> 8 & 0xFF) / 255.0F;
			float f3 = (l & 0xFF) / 255.0F;

			if (EntityRenderer.anaglyphEnable) {
				float f4 = (f1 * 30.0F + f2 * 59.0F + f3 * 11.0F) / 100.0F;
				float f5 = (f1 * 30.0F + f2 * 70.0F) / 100.0F;
				float f6 = (f1 * 30.0F + f3 * 70.0F) / 100.0F;
				f1 = f4;
				f2 = f5;
				f3 = f6;
			}

			tessellator.setColorOpaque_F(f * f1, f * f2, f * f3);
			return renderBlockHopperMetadata((AbstractBlockDuct) block, x, y, z,
					renderer.blockAccess.getBlockMetadata(x, y, z), false, renderer,
					world);
		}

		public int getRenderId() {
			return CommonProxy.ductRendererID;
		}

		public boolean shouldRender3DInInventory(int modelId) {
			return false;
		}
}
