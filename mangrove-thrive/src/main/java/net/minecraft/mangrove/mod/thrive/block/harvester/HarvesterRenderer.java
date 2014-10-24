package net.minecraft.mangrove.mod.thrive.block.harvester;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHopper;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class HarvesterRenderer  implements ISimpleBlockRenderingHandler {
	public void renderInventoryBlock(Block block, int metadata, int modelID,
			RenderBlocks renderer) {
	}

	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z,
			Block block, int modelId, RenderBlocks renderer) {
		renderBlockGratedHopper(renderer, (BlockHarvester) block, x, y, z);

//		renderer.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
//		Tessellator.instance.setColorOpaque_F(1.0F, 1.0F, 1.0F);
//		renderer.renderFaceYPos(Blocks.iron_bars, x, y, z,
//				Blocks.iron_bars.getIcon(0, 0));

		return true;
	}

	public int getRenderId() {
		return 0;
	}

	public boolean renderBlockGratedHopper(RenderBlocks renderer,BlockHarvester block, int x, int y, int z) {
		Tessellator tessellator = Tessellator.instance;
		tessellator.setBrightness(block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z));
		float f = 1.0F;
		int l = block.colorMultiplier(renderer.blockAccess, x,y, z);
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
		return renderBlockGratedHopperMetadata(renderer, block, x,y, z,	renderer.blockAccess.getBlockMetadata(x, y, z), false);
	}

	public boolean renderBlockGratedHopperMetadata(RenderBlocks renderer,BlockHarvester block, int x, int y, int z,int meta, boolean inventory) {
		Tessellator tessellator = Tessellator.instance;
		int i1 = BlockHopper.getDirectionFromMetadata(meta);
		IIcon icon = BlockHopper.getHopperIcon("hopper_outside");
		IIcon icon1 = BlockHopper.getHopperIcon("hopper_inside");
		
		
		
		double d0 = 0.625D;
		
		double d1 = 0.25D;
		double d2 = 0.75D;
		
		renderer.setRenderBounds(0.0D, d0, 0.0D, 1.0D, 1.0D, 1.0D);
		
		if (inventory) {
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, -1.0F, 0.0F);
			renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D,renderer.getBlockIconFromSideAndMetadata(block, 0, meta));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 1.0F, 0.0F);
			renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D,renderer.getBlockIconFromSideAndMetadata(block, 1, meta));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 0.0F, -1.0F);
			renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D,renderer.getBlockIconFromSideAndMetadata(block, 2, meta));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 0.0F, 1.0F);
			renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D,renderer.getBlockIconFromSideAndMetadata(block, 3, meta));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(-1.0F, 0.0F, 0.0F);
			renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D,renderer.getBlockIconFromSideAndMetadata(block, 4, meta));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(1.0F, 0.0F, 0.0F);
			renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D,renderer.getBlockIconFromSideAndMetadata(block, 5, meta));
			tessellator.draw();
		} else {
			renderer.setRenderBounds(0,0.0 , 0, 1D, 0.25, 1D );
			
			renderer.renderStandardBlock(block, x, y, z);
			
			renderer.setOverrideBlockTexture(icon);
			
			renderer.setRenderBounds(0.125,0.25 , 0.125, .875, 0.5, .875 );
			
			renderer.renderStandardBlock(block, x, y, z);
		}

		if (!(inventory)) {
			tessellator.setBrightness(block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z));
			float f1 = 1.0F;
			int j1 = block.colorMultiplier(renderer.blockAccess, x, y, z);
			float f = (j1 >> 16 & 0xFF) / 255.0F;
			float f2 = (j1 >> 8 & 0xFF) / 255.0F;
			float f3 = (j1 & 0xFF) / 255.0F;

			if (EntityRenderer.anaglyphEnable) {
				float f4 = (f * 30.0F + f2 * 59.0F + f3 * 11.0F) / 100.0F;
				float f5 = (f * 30.0F + f2 * 70.0F) / 100.0F;
				float f6 = (f * 30.0F + f3 * 70.0F) / 100.0F;
				f = f4;
				f2 = f5;
				f3 = f6;
			}

			tessellator.setColorOpaque_F(f1 * f, f1 * f2, f1 * f3);
		}

		
		float f = 0.125F;

		if (inventory) {
			tessellator.startDrawingQuads();
			tessellator.setNormal(1.0F, 0.0F, 0.0F);
			renderer.renderFaceXPos(block, -1.0F + f, 0.0D, 0.0D, icon);
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(-1.0F, 0.0F, 0.0F);
			renderer.renderFaceXNeg(block, 1.0F - f, 0.0D, 0.0D, icon);
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 0.0F, 1.0F);
			renderer.renderFaceZPos(block, 0.0D, 0.0D, -1.0F + f, icon);
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 0.0F, -1.0F);
			renderer.renderFaceZNeg(block, 0.0D, 0.0D, 1.0F - f, icon);
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 1.0F, 0.0F);
			renderer.renderFaceYPos(block, 0.0D, -1.0D + d0, 0.0D, icon1);
			tessellator.draw();
		} else {
//			renderer.setRenderBounds(0.0D, d0, 0.0D, 1.0D, 1.0D, 1.0D);
			renderer.setRenderBounds(0,0.0 , 0, 1D, 0.25, 1D );
			
			renderer.renderFaceXPos(block, x - 1.0F + f, y,	z, icon);
			renderer.renderFaceXNeg(block, x + 1.0F - f, y,z, icon);
			renderer.renderFaceZPos(block, x, y,z- 1.0F + f, icon);
			renderer.renderFaceZNeg(block, x, y,z+ 1.0F - f, icon);
			renderer.renderFaceYNeg(block, x, y- 0.5 + d0,z, icon1);
			
//			renderer.renderFaceXPos(block, x - 1.0F + f, y, z, icon);
//			renderer.renderFaceXNeg(block, x + 1.0F - f, y, z, icon);
//			renderer.renderFaceZPos(block, x, y, z - 1.0F + f, icon);
//			renderer.renderFaceZNeg(block, x, y, z + 1.0F - f, icon);
//			renderer.renderFaceYPos(block, x, y - 1.0F + d0, z, icon1);
		}

//		renderer.setOverrideBlockTexture(icon);
		
//		renderer.setRenderBounds(d1, d2, d1, 1.0D - d1, d0 - 0.002D, 1.0D - d1);		
//		renderer.setRenderBounds(d1, d0 - 0.002D, d1, 1.0D - d1, d2, 1.0D - d1);
		renderer.setRenderBounds(0.25, 0.5, 0.25, 0.75, 0.75, 0.75);
		
		if (inventory) {
			tessellator.startDrawingQuads();
			tessellator.setNormal(1.0F, 0.0F, 0.0F);
			renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, icon);
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(-1.0F, 0.0F, 0.0F);
			renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, icon);
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 0.0F, 1.0F);
			renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, icon);
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 0.0F, -1.0F);
			renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, icon);
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 1.0F, 0.0F);
			renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, icon);
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, -1.0F, 0.0F);
			renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, icon);
			tessellator.draw();
		} else {			
			renderer.renderStandardBlock(block, x, y, z);
		}

		if (!(inventory)) {
			double d3 = 0.375D;			
			double d4 = 0.25D;
			

//			if (i1 == 0) {
//				renderer.setRenderBounds(d3, 0.0D, d3, 1.0D - d3, 1,0.25D - d3);
////				renderer.setRenderBounds(d3, 0.75D, d3, 1.0D - d3, 1,1.0D - d3);
//				renderer.renderStandardBlock(block, x, y, z);
//			}
			
			if (i1 == 1) {
//				renderer.setRenderBounds(d3, 0.0D, d3, 1.0D - d3, 0.25D,1.0D - d3);
				renderer.setRenderBounds(d3, 0.75D, d3, 1.0D - d3, 1.0D,1.0D - d3);
				renderer.renderStandardBlock(block, x, y, z);
			}

			if (i1 == 2) {
				renderer.setRenderBounds(d3, d2, 0.0D, 1.0D - d3, d2 + d4, d1);
				renderer.renderStandardBlock(block, x, y, z);
			}

			if (i1 == 3) {
				renderer.setRenderBounds(d3, d2, 1.0D - d1, 1.0D - d3, d2 + d4,
						1.0D);
				renderer.renderStandardBlock(block, x, y, z);
			}

			if (i1 == 4) {
				renderer.setRenderBounds(0.0D, d2, d3, d1, d2 + d4, 1.0D - d3);
				renderer.renderStandardBlock(block, x, y, z);
			}

			if (i1 == 5) {
				renderer.setRenderBounds(1.0D - d1, d2, d3, 1.0D, d2 + d4,
						1.0D - d3);
				renderer.renderStandardBlock(block, x, y, z);
			}
		}

		renderer.clearOverrideBlockTexture();
		return true;
	}

	public boolean shouldRender3DInInventory(int modelId) {
		return false;
	}
}
