package net.minecraft.mangrove.mod.thrive.autocon;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

public class TESRDuct extends TileEntitySpecialRenderer {

	
	

	public void renderKnot(AbstractTileDuct tile, double x, double y, double z, float u, int v) {
		GlStateManager.alphaFunc(516, 0.1F);
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();

		
//		GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
//		GL11.glEnable(GL11.GL_LIGHTING);
//		GL11.glDisable(GL11.GL_BLEND);
//		GL11.glEnable(GL11.GL_CULL_FACE);
		
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, 10497.0F);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, 10497.0F);
		GlStateManager.disableLighting();
		GlStateManager.disableCull();
		GlStateManager.disableBlend();
		GlStateManager.depthMask(true);
		GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0);

		GL11.glPushMatrix();
		GL11.glTranslatef((float) x, (float) y, (float) z);

		float scale = 1.001f;
		// GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		// GL11.glScalef(scale, scale, scale);
		// GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		
//		renderBlock(tile.getBlockType().getBlockState()., x, y, z, tile.getPos(), true, true);
		{ // draw sides
			this.bindTexture(tile.getBlockTexture());
//			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, 10497.0F);
//			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, 10497.0F);
			worldrenderer.startDrawingQuads();						
			Block blockType = tile.getBlockType();
			IBlockState blockState = blockType.getStateFromMeta(tile.getBlockMetadata());
			float[] color ;
//			switch((Integer)blockState.getValue(BlockLink.NETWORK_ID)){
//			case 1:
//				color= EntitySheep.func_175513_a(EnumDyeColor.RED);
//				break;
//			case 2:
//				color= EntitySheep.func_175513_a(EnumDyeColor.GREEN);
//				break;
//			case 3:
//				color= EntitySheep.func_175513_a(EnumDyeColor.BLUE);
//				break;
//			case 0:
//			default:
				color= EntitySheep.func_175513_a(EnumDyeColor.WHITE);
//				break;
//			}
			
			color = EntitySheep.func_175513_a(EnumDyeColor.WHITE);
			worldrenderer.setColorRGBA_F(color[0], color[1], color[2], 0.125F);

			AxisAlignedBB bounds=AxisAlignedBB.fromBounds(0.20f, 0.20f, 0.20f, 0.80f, 0.80f, 0.80f);
			renderBox(worldrenderer, bounds);
//			for (int i = 0; i < sideQuads.length; i++) {
//				for (int j = 0; j < sideQuads[i].length; j++) {
//					worldrenderer.addVertexWithUV(sideQuads[i][j][0], sideQuads[i][j][1], sideQuads[i][j][2], sideQuads[i][j][3], sideQuads[i][j][4]);
//				}
//			}

			tessellator.draw();
		}
		{ // draw connetors
			this.bindTexture(tile.getConnectorTexture());
			worldrenderer.startDrawingQuads();
			World world = tile.getWorld();
			float f2 = (float) world.getTotalWorldTime() ;
			float[] color;
			
//			if( f2 % 64 <24){
//				color = EntitySheep.func_175513_a(EnumDyeColor.RED);
//			}else{
//				color = EntitySheep.func_175513_a(EnumDyeColor.GREEN);
//			}
			color= EntitySheep.func_175513_a(EnumDyeColor.WHITE);
			worldrenderer.setColorRGBA_F(color[0], color[1], color[2], 0.875F);
			//	0.125F
//			worldrenderer.setColorRGBA_F(color[0], color[1], color[2], (1+(f2 % 6))/8);

			BlockPos pos = tile.getPos();
			BlockPos sidepos ;
			Block blockType = tile.getBlockType();
			IBlockState blockState = blockType.getStateFromMeta(tile.getBlockMetadata());
			for(EnumFacing facing:EnumFacing.values()){
				sidepos = pos.offset(facing);
//				IBlockState sideBlockState = world.getBlockState(sidepos);
//				Block block = sideBlockState.getBlock();				
				if( tile.isConnected(pos,facing,sidepos)){
					renderConnector(worldrenderer, facing);	
				}
			}	
//			
//			renderConnector(worldrenderer, EnumFacing.NORTH);
//			renderConnector(worldrenderer, EnumFacing.SOUTH);
//			
//			renderConnector(worldrenderer, EnumFacing.EAST);
//			renderConnector(worldrenderer, EnumFacing.WEST);
			
//			for (int i = 0; i < quadVertexes.length; i++) {
//				for (int j = 0; j < quadVertexes[i].length; j++) {
//					worldrenderer.addVertexWithUV(quadVertexes[i][j][0], quadVertexes[i][j][1], quadVertexes[i][j][2], quadVertexes[i][j][3], quadVertexes[i][j][4]);
//				}
//			}

			tessellator.draw();
		}
//		GlStateManager.enableBlend();
//		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
//		GlStateManager.depthMask(false);
//		worldrenderer.startDrawingQuads();
////		worldrenderer.setColorRGBA_F(color[0], color[1], color[2], 0.125F);
//		for (int i = 0; i < quadVertexes.length; i++) {
//			for (int j = 0; j < quadVertexes[i].length; j++) {
//				worldrenderer.addVertexWithUV(quadVertexes[i][j][0], quadVertexes[i][j][1], quadVertexes[i][j][2], 1f, 1f);
//			}
//		}
//		tessellator.draw();
		
		GlStateManager.enableLighting();
		GlStateManager.enableTexture2D();
		GlStateManager.depthMask(true);
		GL11.glPopMatrix();
	}

	private void renderConnector(WorldRenderer worldrenderer, EnumFacing facing) {
		AxisAlignedBB bounds;
		switch (facing) {
		case UP:
			bounds=AxisAlignedBB.fromBounds(0.25f, 0.80f, 0.25f, 0.75f, 1.00f, 0.75f);
			renderNorthFace(bounds, worldrenderer, 1, 1);
			renderSouthFace(bounds, worldrenderer, 1, 1);
			renderEastFace(bounds, worldrenderer, 1, 1);
			renderWestFace(bounds, worldrenderer, 1, 1);
			break;
		case DOWN:
			bounds=AxisAlignedBB.fromBounds(0.25f, 0.00f, 0.25f, 0.75f, 0.20f, 0.75f);
			renderNorthFace(bounds, worldrenderer, 1, 1);
			renderSouthFace(bounds, worldrenderer, 1, 1);
			renderEastFace(bounds, worldrenderer, 1, 1);
			renderWestFace(bounds, worldrenderer, 1, 1);
			break;
		case NORTH:
			bounds=AxisAlignedBB.fromBounds(0.25f,  0.25f, 0.00f,0.75f, 0.75f, 0.20f);
			renderUpFace(bounds, worldrenderer, 1, 1);
			renderDownFace(bounds, worldrenderer, 1, 1);
			renderEastFace(bounds, worldrenderer, 1, 1);
			renderWestFace(bounds, worldrenderer, 1, 1);
			break;
		case SOUTH:
			bounds=AxisAlignedBB.fromBounds(0.25f,  0.25f, 0.80f,0.75f, 0.75f, 1.00f);
			renderUpFace(bounds, worldrenderer, 1, 1);
			renderDownFace(bounds, worldrenderer, 1, 1);
			renderEastFace(bounds, worldrenderer, 1, 1);
			renderWestFace(bounds, worldrenderer, 1, 1);
			break;
		case WEST:
			bounds=AxisAlignedBB.fromBounds(0.00f ,0.25f,  0.25f, 0.20f, 0.75f, 0.75f);
			renderUpFace(bounds, worldrenderer, 1, 1);
			renderDownFace(bounds, worldrenderer, 1, 1);
			renderNorthFace(bounds, worldrenderer, 1, 1);
			renderSouthFace(bounds, worldrenderer, 1, 1);
			break;
		case EAST:
			bounds=AxisAlignedBB.fromBounds(0.80f ,0.25f,  0.25f, 1.00f, 0.75f, 0.75f);
			renderUpFace(bounds, worldrenderer, 1, 1);
			renderDownFace(bounds, worldrenderer, 1, 1);
			renderNorthFace(bounds, worldrenderer, 1, 1);
			renderSouthFace(bounds, worldrenderer, 1, 1);
			break;
		default:
			break;
		}
		
	}

	private void renderBox(WorldRenderer worldrenderer, AxisAlignedBB bounds) {
		renderNorthFace(bounds, worldrenderer, 1, 1);
		renderSouthFace(bounds, worldrenderer, 1, 1);
		renderEastFace(bounds, worldrenderer, 1, 1);
		renderWestFace(bounds, worldrenderer, 1, 1);
		renderUpFace(bounds, worldrenderer, 1, 1);
		renderDownFace(bounds, worldrenderer, 1, 1);
	}

//	public void renderBeacon(AbstractTileDuct p_180536_1_, double p_180536_2_, double p_180536_4_, double p_180536_6_, float p_180536_8_, int p_180536_9_) {
//		float f1 = 1;
//		GlStateManager.alphaFunc(516, 0.1F);
//
//		if (f1 > 0.0F) {
//			Tessellator tessellator = Tessellator.getInstance();
//			WorldRenderer worldrenderer = tessellator.getWorldRenderer();
//
//			List list = new ArrayList();
//
//			list.add(new TileEntityBeacon.BeamSegment(EntitySheep.func_175513_a(EnumDyeColor.WHITE)));
//			// List list = p_180536_1_.func_174907_n();
//			int j = 0;
//
//			for (int k = 0; k < list.size(); ++k) {
//				TileEntityBeacon.BeamSegment beamsegment = (TileEntityBeacon.BeamSegment) list.get(k);
//				int l = j + beamsegment.func_177264_c();
//				this.bindTexture(beaconBeam);
//				GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, 10497.0F);
//				GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, 10497.0F);
//				GlStateManager.disableLighting();
//				GlStateManager.disableCull();
//				GlStateManager.disableBlend();
//				GlStateManager.depthMask(true);
//				GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0);
//				float f2 = (float) p_180536_1_.getWorld().getTotalWorldTime() + p_180536_8_;
//				float f3 = -f2 * 0.2F - (float) MathHelper.floor_float(-f2 * 0.1F);
//				double d3 = (double) f2 * 0.025D * -1.5D;
//				worldrenderer.startDrawingQuads();
//				double d4 = 0.2D;
//				double d5 = 0.5D + Math.cos(d3 + 2.356194490192345D) * d4;
//				double d6 = 0.5D + Math.sin(d3 + 2.356194490192345D) * d4;
//				double d7 = 0.5D + Math.cos(d3 + (Math.PI / 4D)) * d4;
//				double d8 = 0.5D + Math.sin(d3 + (Math.PI / 4D)) * d4;
//				double d9 = 0.5D + Math.cos(d3 + 3.9269908169872414D) * d4;
//				double d10 = 0.5D + Math.sin(d3 + 3.9269908169872414D) * d4;
//				double d11 = 0.5D + Math.cos(d3 + 5.497787143782138D) * d4;
//				double d12 = 0.5D + Math.sin(d3 + 5.497787143782138D) * d4;
//				double d13 = 0.0D;
//				double d14 = 1.0D;
//				double d15 = (double) (-1.0F + f3);
//				double d16 = (double) ((float) beamsegment.func_177264_c() * f1) * (0.5D / d4) + d15;
//				worldrenderer.setColorRGBA_F(beamsegment.func_177263_b()[0], beamsegment.func_177263_b()[1], beamsegment.func_177263_b()[2], 0.125F);
//				worldrenderer.addVertexWithUV(p_180536_2_ + d5, p_180536_4_ + (double) l, p_180536_6_ + d6, d14, d16);
//				worldrenderer.addVertexWithUV(p_180536_2_ + d5, p_180536_4_ + (double) j, p_180536_6_ + d6, d14, d15);
//				worldrenderer.addVertexWithUV(p_180536_2_ + d7, p_180536_4_ + (double) j, p_180536_6_ + d8, d13, d15);
//				worldrenderer.addVertexWithUV(p_180536_2_ + d7, p_180536_4_ + (double) l, p_180536_6_ + d8, d13, d16);
//				worldrenderer.addVertexWithUV(p_180536_2_ + d11, p_180536_4_ + (double) l, p_180536_6_ + d12, d14, d16);
//				worldrenderer.addVertexWithUV(p_180536_2_ + d11, p_180536_4_ + (double) j, p_180536_6_ + d12, d14, d15);
//				worldrenderer.addVertexWithUV(p_180536_2_ + d9, p_180536_4_ + (double) j, p_180536_6_ + d10, d13, d15);
//				worldrenderer.addVertexWithUV(p_180536_2_ + d9, p_180536_4_ + (double) l, p_180536_6_ + d10, d13, d16);
//				worldrenderer.addVertexWithUV(p_180536_2_ + d7, p_180536_4_ + (double) l, p_180536_6_ + d8, d14, d16);
//				worldrenderer.addVertexWithUV(p_180536_2_ + d7, p_180536_4_ + (double) j, p_180536_6_ + d8, d14, d15);
//				worldrenderer.addVertexWithUV(p_180536_2_ + d11, p_180536_4_ + (double) j, p_180536_6_ + d12, d13, d15);
//				worldrenderer.addVertexWithUV(p_180536_2_ + d11, p_180536_4_ + (double) l, p_180536_6_ + d12, d13, d16);
//				worldrenderer.addVertexWithUV(p_180536_2_ + d9, p_180536_4_ + (double) l, p_180536_6_ + d10, d14, d16);
//				worldrenderer.addVertexWithUV(p_180536_2_ + d9, p_180536_4_ + (double) j, p_180536_6_ + d10, d14, d15);
//				worldrenderer.addVertexWithUV(p_180536_2_ + d5, p_180536_4_ + (double) j, p_180536_6_ + d6, d13, d15);
//				worldrenderer.addVertexWithUV(p_180536_2_ + d5, p_180536_4_ + (double) l, p_180536_6_ + d6, d13, d16);
//				tessellator.draw();
//				GlStateManager.enableBlend();
//				GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
//				GlStateManager.depthMask(false);
//				worldrenderer.startDrawingQuads();
//				worldrenderer.setColorRGBA_F(beamsegment.func_177263_b()[0], beamsegment.func_177263_b()[1], beamsegment.func_177263_b()[2], 0.125F);
//				d3 = 0.2D;
//				d4 = 0.2D;
//				d5 = 0.8D;
//				d6 = 0.2D;
//				d7 = 0.2D;
//				d8 = 0.8D;
//				d9 = 0.8D;
//				d10 = 0.8D;
//				d11 = 0.0D;
//				d12 = 1.0D;
//				d13 = (double) (-1.0F + f3);
//				d14 = (double) ((float) beamsegment.func_177264_c() * f1) + d13;
//				worldrenderer.addVertexWithUV(p_180536_2_ + d3, p_180536_4_ + (double) l, p_180536_6_ + d4, d12, d14);
//				worldrenderer.addVertexWithUV(p_180536_2_ + d3, p_180536_4_ + (double) j, p_180536_6_ + d4, d12, d13);
//				worldrenderer.addVertexWithUV(p_180536_2_ + d5, p_180536_4_ + (double) j, p_180536_6_ + d6, d11, d13);
//				worldrenderer.addVertexWithUV(p_180536_2_ + d5, p_180536_4_ + (double) l, p_180536_6_ + d6, d11, d14);
//				worldrenderer.addVertexWithUV(p_180536_2_ + d9, p_180536_4_ + (double) l, p_180536_6_ + d10, d12, d14);
//				worldrenderer.addVertexWithUV(p_180536_2_ + d9, p_180536_4_ + (double) j, p_180536_6_ + d10, d12, d13);
//				worldrenderer.addVertexWithUV(p_180536_2_ + d7, p_180536_4_ + (double) j, p_180536_6_ + d8, d11, d13);
//				worldrenderer.addVertexWithUV(p_180536_2_ + d7, p_180536_4_ + (double) l, p_180536_6_ + d8, d11, d14);
//				worldrenderer.addVertexWithUV(p_180536_2_ + d5, p_180536_4_ + (double) l, p_180536_6_ + d6, d12, d14);
//				worldrenderer.addVertexWithUV(p_180536_2_ + d5, p_180536_4_ + (double) j, p_180536_6_ + d6, d12, d13);
//				worldrenderer.addVertexWithUV(p_180536_2_ + d9, p_180536_4_ + (double) j, p_180536_6_ + d10, d11, d13);
//				worldrenderer.addVertexWithUV(p_180536_2_ + d9, p_180536_4_ + (double) l, p_180536_6_ + d10, d11, d14);
//				worldrenderer.addVertexWithUV(p_180536_2_ + d7, p_180536_4_ + (double) l, p_180536_6_ + d8, d12, d14);
//				worldrenderer.addVertexWithUV(p_180536_2_ + d7, p_180536_4_ + (double) j, p_180536_6_ + d8, d12, d13);
//				worldrenderer.addVertexWithUV(p_180536_2_ + d3, p_180536_4_ + (double) j, p_180536_6_ + d4, d11, d13);
//				worldrenderer.addVertexWithUV(p_180536_2_ + d3, p_180536_4_ + (double) l, p_180536_6_ + d4, d11, d14);
//				tessellator.draw();
//				GlStateManager.enableLighting();
//				GlStateManager.enableTexture2D();
//				GlStateManager.depthMask(true);
//				j = l;
//			}
//		}
//	}


	public void renderBlock(IBlockAccess blockAccess, double x, double y, double z, BlockPos lightPos, boolean doLight, boolean doTessellating) {

		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer renderer = tessellator.getWorldRenderer();
		BlockRendererDispatcher renderBlocks = Minecraft.getMinecraft().getBlockRendererDispatcher();
		BlockPos pos = new BlockPos(x, y, z);
		renderer.startDrawingQuads();
		renderer.setVertexFormat(DefaultVertexFormats.BLOCK);
		renderer.setTranslation((double)(-pos.getX()), (double)(-pos.getY()), (double)(-pos.getZ()));

        GlStateManager.scale(0.5,0.5, 0.5);
		renderBlocks.renderBlock(blockAccess.getBlockState(pos), pos, blockAccess, renderer);
		tessellator.draw();
	}
	
	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float p_180535_8_, int p_180535_9_) {
		this.renderKnot((AbstractTileDuct) te, x, y, z, p_180535_8_, p_180535_9_);

	}
	
	protected void renderNorthFace(AxisAlignedBB bounds, WorldRenderer worldrenderer,float uScale, float vscale){		
		worldrenderer.addVertexWithUV(bounds.maxX, bounds.minY, bounds.minZ, 1.0f*uScale, 1.0*vscale);
		worldrenderer.addVertexWithUV(bounds.maxX, bounds.maxY, bounds.minZ, 0.0f*uScale, 1.0*vscale);
		worldrenderer.addVertexWithUV(bounds.minX, bounds.maxY, bounds.minZ, 0.0f*uScale, 0.0*vscale);
		worldrenderer.addVertexWithUV(bounds.minX, bounds.minY, bounds.minZ, 1.0f*uScale, 0.0*vscale);
	}
	
	protected void renderSouthFace(AxisAlignedBB bounds, WorldRenderer worldrenderer,float uScale, float vscale){		
		worldrenderer.addVertexWithUV(bounds.maxX, bounds.minY, bounds.maxZ, 1.0f*uScale, 1.0*vscale);
		worldrenderer.addVertexWithUV(bounds.maxX, bounds.maxY, bounds.maxZ, 0.0f*uScale, 1.0*vscale);
		worldrenderer.addVertexWithUV(bounds.minX, bounds.maxY, bounds.maxZ, 0.0f*uScale, 0.0*vscale);
		worldrenderer.addVertexWithUV(bounds.minX, bounds.minY, bounds.maxZ, 1.0f*uScale, 0.0*vscale);
	}
	
	protected void renderWestFace(AxisAlignedBB bounds, WorldRenderer worldrenderer,float uScale, float vscale){		
		worldrenderer.addVertexWithUV(bounds.minX, bounds.minY, bounds.minZ, 1.0f*uScale, 1.0*vscale);
		worldrenderer.addVertexWithUV(bounds.minX, bounds.maxY, bounds.minZ, 0.0f*uScale, 1.0*vscale);
		worldrenderer.addVertexWithUV(bounds.minX, bounds.maxY, bounds.maxZ, 0.0f*uScale, 0.0*vscale);
		worldrenderer.addVertexWithUV(bounds.minX, bounds.minY, bounds.maxZ, 1.0f*uScale, 0.0*vscale);
	}
	
	protected void renderEastFace(AxisAlignedBB bounds, WorldRenderer worldrenderer,float uScale, float vscale){		
		worldrenderer.addVertexWithUV(bounds.maxX, bounds.minY, bounds.minZ, 1.0f*uScale, 1.0*vscale);
		worldrenderer.addVertexWithUV(bounds.maxX, bounds.maxY, bounds.minZ, 0.0f*uScale, 1.0*vscale);
		worldrenderer.addVertexWithUV(bounds.maxX, bounds.maxY, bounds.maxZ, 0.0f*uScale, 0.0*vscale);
		worldrenderer.addVertexWithUV(bounds.maxX, bounds.minY, bounds.maxZ, 1.0f*uScale, 0.0*vscale);
	}
	
	protected void renderUpFace(AxisAlignedBB bounds, WorldRenderer worldrenderer,float uScale, float vscale){		
		worldrenderer.addVertexWithUV(bounds.maxX, bounds.maxY, bounds.minZ, 1.0f*uScale, 1.0*vscale);
		worldrenderer.addVertexWithUV(bounds.minX, bounds.maxY, bounds.minZ, 0.0f*uScale, 1.0*vscale);
		worldrenderer.addVertexWithUV(bounds.minX, bounds.maxY, bounds.maxZ, 0.0f*uScale, 0.0*vscale);
		worldrenderer.addVertexWithUV(bounds.maxX, bounds.maxY, bounds.maxZ, 1.0f*uScale, 0.0*vscale);
	}
	
	protected void renderDownFace(AxisAlignedBB bounds, WorldRenderer worldrenderer,float uScale, float vscale){		
		worldrenderer.addVertexWithUV(bounds.minX, bounds.minY, bounds.maxZ, 1.0f*uScale, 1.0*vscale);
		worldrenderer.addVertexWithUV(bounds.minX, bounds.minY, bounds.minZ, 0.0f*uScale, 1.0*vscale);
		worldrenderer.addVertexWithUV(bounds.maxX, bounds.minY, bounds.minZ, 0.0f*uScale, 0.0*vscale);
		worldrenderer.addVertexWithUV(bounds.maxX, bounds.minY, bounds.maxZ, 1.0f*uScale, 0.0*vscale);
	}

//	private float[][][] sideQuads = new float[][][] {
//			{ //NORTH SIDE
//				{ 0.80F, 0.20F, 0.20F, 4.00f, 4.00f },			
//				{ 0.80F, 0.80F, 0.20F, 0.00f, 4.00f },			
//				{ 0.20F, 0.80F, 0.20F, 0.00f, 0.00f },			
//				{ 0.20F, 0.20F, 0.20F, 4.00f, 0.00f }
//			},{ //SOUTH SIDE
//				{ 0.80F, 0.20F, 0.80F, 4.00f, 4.00f },			
//				{ 0.80F, 0.80F, 0.80F, 0.00f, 4.00f },			
//				{ 0.20F, 0.80F, 0.80F, 0.00f, 0.00f },			
//				{ 0.20F, 0.20F, 0.80F, 4.00f, 0.00f }
//			},{ // WEST SIDE
//				{ 0.20F, 0.20F, 0.20F, 4.00f, 4.00f },			
//				{ 0.20F, 0.80F, 0.20F, 0.00f, 4.00f },			
//				{ 0.20F, 0.80F, 0.80F, 0.00f, 0.00f },			
//				{ 0.20F, 0.20F, 0.80F, 4.00f, 0.00f }
//			},{ // EAST SIDE
//				{ 0.80F, 0.20F, 0.80F, 4.00f, 4.00f },			
//				{ 0.80F, 0.80F, 0.80F, 0.00f, 4.00f },			
//				{ 0.80F, 0.80F, 0.20F, 0.00f, 0.00f },			
//				{ 0.80F, 0.20F, 0.20F, 4.00f, 0.00f }
//			},{ // UP SIDE
//				{ 0.80F, 0.80F, 0.20F, 4.00f, 4.00f },			
//				{ 0.20F, 0.80F, 0.20F, 0.00f, 4.00f },			
//				{ 0.20F, 0.80F, 0.80F, 0.00f, 0.00f },			
//				{ 0.80F, 0.80F, 0.80F, 4.00f, 0.00f }
//			},{ // DOWN SIDE
//				{ 0.20F, 0.20F, 0.80F, 4.00f, 4.00f },			
//				{ 0.20F, 0.20F, 0.20F, 0.00f, 4.00f },			
//				{ 0.80F, 0.20F, 0.20F, 0.00f, 0.00f },			
//				{ 0.80F, 0.20F, 0.80F, 4.00f, 0.00f }
//			}
//			
//	};
	private float[][][] quadVertexes = new float[][][] {
		// UP CONNECTOR
		{ 
			{ 0.25F, 0.75F, 0.25F, 4.00f, 4.00f}, 
			{ 0.75F, 0.75F, 0.25F, 0.00f, 4.00f}, 
			{ 0.75F, 1.00F, 0.25F, 0.00f, 0.00f}, 
			{ 0.25F, 1.00F, 0.25F, 4.00f, 0.00f} 
		},
		{ 
			{ 0.25F, 0.75F, 0.25F, 4.00f, 4.00f }, 
			{ 0.25F, 0.75F, 0.75F, 0.00f, 4.00f }, 
			{ 0.25F, 1.00F, 0.75F, 0.00f, 0.00f }, 
			{ 0.25F, 1.00F, 0.25F, 4.00f, 0.00f } 
		},
		{ 
			{ 0.25F, 0.75F, 0.75F, 4.00f, 4.00f }, 
			{ 0.75F, 0.75F, 0.75F, 0.00f, 4.00f }, 
			{ 0.75F, 1.00F, 0.75F, 0.00f, 0.00f }, 
			{ 0.25F, 1.00F, 0.75F, 4.00f, 0.00f } 
		},
		{ 
			{ 0.75F, 0.75F, 0.25F, 4.00f, 4.00f }, 
			{ 0.75F, 0.75F, 0.75F, 0.00f, 4.00f }, 
			{ 0.75F, 1.00F, 0.75F, 0.00f, 0.00f }, 
			{ 0.75F, 1.00F, 0.25F, 4.00f, 0.00f } 
		},
		// DOWN CONNECTOR
		{ 
			{ 0.25F, 0.00F, 0.25F,  4.00f, 4.00f }, 
			{ 0.75F, 0.00F, 0.25F,  0.00f, 4.00f }, 
			{ 0.75F, 0.25F, 0.25F,  0.00f, 0.00f }, 
			{ 0.25F, 0.25F, 0.25F,  4.00f, 0.00f } 
		},
		{ 
			{ 0.25F, 0.00F, 0.25F, 4.00f, 4.00f },
			{ 0.25F, 0.00F, 0.75F, 0.00f, 4.00f }, 
			{ 0.25F, 0.25F, 0.75F, 0.00f, 0.00f },
			{ 0.25F, 0.25F, 0.25F, 4.00f, 0.00f } 
		},
		{ 
			{ 0.25F, 0.00F, 0.75F, 4.00f, 4.00f }, 
			{ 0.75F, 0.00F, 0.75F, 0.00f, 4.00f }, 
			{ 0.75F, 0.25F, 0.75F, 0.00f, 0.00f }, 
			{ 0.25F, 0.25F, 0.75F, 4.00f, 0.00f } 
		},
		{ 
			{ 0.75F, 0.00F, 0.25F, 4.00f, 4.00f }, 
			{ 0.75F, 0.00F, 0.75F, 0.00f, 4.00f },
			{ 0.75F, 0.25F, 0.75F, 0.00f, 0.00f },
			{ 0.75F, 0.25F, 0.25F, 4.00f, 0.00f } 
		},
		// WEST CONNECTOR
		{ 
			{ 0.00F, 0.25F, 0.25F, 4.00f, 4.00f }, 
			{ 0.00F, 0.75F, 0.25F, 0.00f, 4.00f }, 
			{ 0.25F, 0.75F, 0.25F, 0.00f, 0.00f }, 
			{ 0.25F, 0.25F, 0.25F, 4.00f, 0.00f }
		},
		{ 
			{ 0.00F, 0.25F, 0.25F, 4.00f, 4.00f }, 
			{ 0.00F, 0.25F, 0.75F, 0.00f, 4.00f }, 
			{ 0.25F, 0.25F, 0.75F, 0.00f, 0.00f }, 
			{ 0.25F, 0.25F, 0.25F, 4.00f, 0.00f } 
		},
		{ 
			{ 0.00F, 0.25F, 0.75F, 4.00f, 4.00f }, 
			{ 0.00F, 0.75F, 0.75F, 0.00f, 4.00f },
			{ 0.25F, 0.75F, 0.75F, 0.00f, 0.00f },
			{ 0.25F, 0.25F, 0.75F, 4.00f, 0.00f }
		},
		{ 
			{ 0.00F, 0.75F, 0.25F, 4.00f, 4.00f },
			{ 0.00F, 0.75F, 0.75F, 0.00f, 4.00f }, 
			{ 0.25F, 0.75F, 0.75F, 0.00f, 0.00f },
			{ 0.25F, 0.75F, 0.25F, 4.00f, 0.00f } 
		},
		// EAST CONNECTOR                                                                                                                                 
		{ 
			{ 0.75F, 0.25F, 0.25F, 4.00f, 4.00f },		
			{ 0.75F, 0.75F, 0.25F, 0.00f, 4.00f },
			{ 1.00F, 0.75F, 0.25F, 0.00f, 0.00f }, 
			{ 1.00F, 0.25F, 0.25F, 4.00f, 0.00f } 
		},
		{ 
			{ 0.75F, 0.25F, 0.25F, 4.00f, 4.00f }, 
			{ 0.75F, 0.25F, 0.75F, 0.00f, 4.00f }, 
			{ 1.00F, 0.25F, 0.75F, 0.00f, 0.00f }, 
			{ 1.00F, 0.25F, 0.25F, 4.00f, 0.00f }
		},
		{ 
			{ 0.75F, 0.25F, 0.75F, 4.00f, 4.00f }, 
			{ 0.75F, 0.75F, 0.75F, 0.00f, 4.00f }, 
			{ 1.00F, 0.75F, 0.75F, 0.00f, 0.00f }, 
			{ 1.00F, 0.25F, 0.75F, 4.00f, 0.00f } 
		},
		{ 
			{ 0.75F, 0.75F, 0.25F, 4.00f, 4.00f },
			{ 0.75F, 0.75F, 0.75F, 0.00f, 4.00f }, 
			{ 1.00F, 0.75F, 0.75F, 0.00f, 0.00f }, 
			{ 1.00F, 0.75F, 0.25F, 4.00f, 0.00f } 
		},
		// SOUTH CONNECTORw                                                                                                                               
		{ 
			{ 0.25F, 0.25F, 0.75F,  4.00f, 4.00f },
			{ 0.75F, 0.25F, 0.75F,  0.00f, 4.00f },
			{ 0.75F, 0.25F, 1.00F,  0.00f, 0.00f },
			{ 0.25F, 0.25F, 1.00F,  4.00f, 0.00f }
		},
		{ 
			{ 0.25F, 0.25F, 0.75F, 4.00f, 4.00f }, 
			{ 0.25F, 0.75F, 0.75F, 0.00f, 4.00f }, 
			{ 0.25F, 0.75F, 1.00F, 0.00f, 0.00f }, 
			{ 0.25F, 0.25F, 1.00F, 4.00f, 0.00f } 
		},
		{
			{ 0.25F, 0.75F, 0.75F, 4.00f, 4.00f }, 
			{ 0.75F, 0.75F, 0.75F, 0.00f, 4.00f },
			{ 0.75F, 0.75F, 1.00F, 0.00f, 0.00f }, 
			{ 0.25F, 0.75F, 1.00F, 4.00f, 0.00f } 
		},
		{
			{ 0.75F, 0.25F, 0.75F, 4.00f, 4.00f }, 
			{ 0.75F, 0.75F, 0.75F, 0.00f, 4.00f }, 
			{ 0.75F, 0.75F, 1.00F, 0.00f, 0.00f }, 
			{ 0.75F, 0.25F, 1.00F, 4.00f, 0.00f }
		},
		// NORTH CONNECTOR                                                                                                                                
		{ 
			{ 0.25F, 0.25F, 0.00F, 4.00f, 4.00f },
			{ 0.75F, 0.25F, 0.00F, 0.00f, 4.00f },
			{ 0.75F, 0.25F, 0.25F, 0.00f, 0.00f },
			{ 0.25F, 0.25F, 0.25F, 4.00f, 0.00f },
		},
		{
			{ 0.25F, 0.25F, 0.00F, 4.00f, 4.00f }, 
			{ 0.25F, 0.75F, 0.00F, 0.00f, 4.00f },
			{ 0.25F, 0.75F, 0.25F, 0.00f, 0.00f },
			{ 0.25F, 0.25F, 0.25F, 4.00f, 0.00f },
		},
		{
			{ 0.25F, 0.75F, 0.00F, 4.00f, 4.00f },
			{ 0.75F, 0.75F, 0.00F, 0.00f, 4.00f }, 
			{ 0.75F, 0.75F, 0.25F, 0.00f, 0.00f }, 
			{ 0.25F, 0.75F, 0.25F, 4.00f, 0.00f }, 
		},
		{ 
			{ 0.75F, 0.25F, 0.00F, 4.00f, 4.00f },
			{ 0.75F, 0.75F, 0.00F, 0.00f, 4.00f },
			{ 0.75F, 0.75F, 0.25F, 0.00f, 0.00f },
			{ 0.75F, 0.25F, 0.25F, 4.00f, 0.00f }, 
		}
	};
//	private float[][][] quadVertexes = new float[][][] {
//			// UP CONNECTOR
//			{ { 0.25F, 0.75F, 0.25F, 0.0f, 0.0f }, { 0.75F, 0.75F, 0.25F,  0.0f,  0.0f }, { 0.75F, 1.00F, 0.25F,  0.0f,  0.0f }, { 0.25F, 1.00F, 0.25F,  0.0f,  0.0f }, },
//			{ { 0.25F, 0.75F, 0.25F, 1.0f, 0.0f }, { 0.25F, 0.75F, 0.75F, 16.0f,  0.0f }, { 0.25F, 1.00F, 0.75F, 16.0f,  0.0f }, { 0.25F, 1.00F, 0.25F, 16.0f,  0.0f }, },
//			{ { 0.25F, 0.75F, 0.75F, 1.0f, 1.0f }, { 0.75F, 0.75F, 0.75F, 16.0f, 16.0f }, { 0.75F, 1.00F, 0.75F, 16.0f, 16.0f }, { 0.25F, 1.00F, 0.75F, 16.0f, 16.0f }, },
//			{ { 0.75F, 0.75F, 0.25F, 0.0f, 1.0f }, { 0.75F, 0.75F, 0.75F,  0.0f, 16.0f }, { 0.75F, 1.00F, 0.75F,  0.0f, 16.0f }, { 0.75F, 1.00F, 0.25F,  0.0f, 16.0f }, },
//			// DOWN CONNECTOR
//			{ { 0.25F, 0.00F, 0.25F, 0.0f, 0.0f }, { 0.75F, 0.00F, 0.25F, 0.0f, 0.0f }, { 0.75F, 0.25F, 0.25F, 0.0f, 0.0f }, { 0.25F, 0.25F, 0.25F, 0.0f, 0.0f }, },
//			{ { 0.25F, 0.00F, 0.25F, 1.0f, 0.0f }, { 0.25F, 0.00F, 0.75F, 1.0f, 0.0f }, { 0.25F, 0.25F, 0.75F, 1.0f, 0.0f }, { 0.25F, 0.25F, 0.25F, 1.0f, 0.0f }, },
//			{ { 0.25F, 0.00F, 0.75F, 1.0f, 1.0f }, { 0.75F, 0.00F, 0.75F, 1.0f, 1.0f }, { 0.75F, 0.25F, 0.75F, 1.0f, 1.0f }, { 0.25F, 0.25F, 0.75F, 1.0f, 1.0f }, },
//			{ { 0.75F, 0.00F, 0.25F, 0.0f, 1.0f }, { 0.75F, 0.00F, 0.75F, 0.0f, 1.0f }, { 0.75F, 0.25F, 0.75F, 0.0f, 1.0f }, { 0.75F, 0.25F, 0.25F, 0.0f, 1.0f }, },
//			// WEST CONNECTOR
//			{ { 0.00F, 0.25F, 0.25F, 0.0f, 0.0f }, { 0.00F, 0.75F, 0.25F, 0.0f, 0.0f }, { 0.25F, 0.75F, 0.25F, 0.0f, 0.0f }, { 0.25F, 0.25F, 0.25F, 0.0f, 0.0f }, },
//			{ { 0.00F, 0.25F, 0.25F, 1.0f, 0.0f }, { 0.00F, 0.25F, 0.75F, 1.0f, 0.0f }, { 0.25F, 0.25F, 0.75F, 1.0f, 0.0f }, { 0.25F, 0.25F, 0.25F, 1.0f, 0.0f }, },
//			{ { 0.00F, 0.25F, 0.75F, 1.0f, 1.0f }, { 0.00F, 0.75F, 0.75F, 1.0f, 1.0f }, { 0.25F, 0.75F, 0.75F, 1.0f, 1.0f }, { 0.25F, 0.25F, 0.75F, 1.0f, 1.0f }, },
//			{ { 0.00F, 0.75F, 0.25F, 0.0f, 1.0f }, { 0.00F, 0.75F, 0.75F, 0.0f, 1.0f }, { 0.25F, 0.75F, 0.75F, 0.0f, 1.0f }, { 0.25F, 0.75F, 0.25F, 0.0f, 1.0f }, },
//			// EAST CONNECTOR                                                                                                                                 
//			{ { 0.75F, 0.25F, 0.25F, 0.0f, 0.0f }, { 0.75F, 0.75F, 0.25F, 0.0f, 0.0f }, { 1.00F, 0.75F, 0.25F, 0.0f, 0.0f }, { 1.00F, 0.25F, 0.25F, 0.0f, 0.0f }, },
//			{ { 0.75F, 0.25F, 0.25F, 1.0f, 0.0f }, { 0.75F, 0.25F, 0.75F, 1.0f, 0.0f }, { 1.00F, 0.25F, 0.75F, 1.0f, 0.0f }, { 1.00F, 0.25F, 0.25F, 1.0f, 0.0f }, },
//			{ { 0.75F, 0.25F, 0.75F, 1.0f, 1.0f }, { 0.75F, 0.75F, 0.75F, 1.0f, 1.0f }, { 1.00F, 0.75F, 0.75F, 1.0f, 1.0f }, { 1.00F, 0.25F, 0.75F, 1.0f, 1.0f }, },
//			{ { 0.75F, 0.75F, 0.25F, 0.0f, 1.0f }, { 0.75F, 0.75F, 0.75F, 0.0f, 1.0f }, { 1.00F, 0.75F, 0.75F, 0.0f, 1.0f }, { 1.00F, 0.75F, 0.25F, 0.0f, 1.0f }, },
//			// SOUTH CONNECTORw                                                                                                                               
//			{ { 0.25F, 0.25F, 0.75F, 0.0f, 0.0f }, { 0.75F, 0.25F, 0.75F, 0.0f, 0.0f }, { 0.75F, 0.25F, 1.00F, 0.0f, 0.0f }, { 0.25F, 0.25F, 1.00F, 0.0f, 0.0f }, },
//			{ { 0.25F, 0.25F, 0.75F, 1.0f, 0.0f }, { 0.25F, 0.75F, 0.75F, 1.0f, 0.0f }, { 0.25F, 0.75F, 1.00F, 1.0f, 0.0f }, { 0.25F, 0.25F, 1.00F, 1.0f, 0.0f }, },
//			{ { 0.25F, 0.75F, 0.75F, 1.0f, 1.0f }, { 0.75F, 0.75F, 0.75F, 1.0f, 1.0f }, { 0.75F, 0.75F, 1.00F, 1.0f, 1.0f }, { 0.25F, 0.75F, 1.00F, 1.0f, 1.0f }, },
//			{ { 0.75F, 0.25F, 0.75F, 0.0f, 1.0f }, { 0.75F, 0.75F, 0.75F, 0.0f, 1.0f }, { 0.75F, 0.75F, 1.00F, 0.0f, 1.0f }, { 0.75F, 0.25F, 1.00F, 0.0f, 1.0f }, },
//			// NORTH CONNECTOR                                                                                                                                
//			{ { 0.25F, 0.25F, 0.00F, 0.0f, 0.0f }, { 0.75F, 0.25F, 0.00F, 0.0f, 0.0f }, { 0.75F, 0.25F, 0.25F, 0.0f, 0.0f }, { 0.25F, 0.25F, 0.25F, 0.0f, 0.0f }, },
//			{ { 0.25F, 0.25F, 0.00F, 1.0f, 0.0f }, { 0.25F, 0.75F, 0.00F, 1.0f, 0.0f }, { 0.25F, 0.75F, 0.25F, 1.0f, 0.0f }, { 0.25F, 0.25F, 0.25F, 1.0f, 0.0f }, },
//			{ { 0.25F, 0.75F, 0.00F, 1.0f, 1.0f }, { 0.75F, 0.75F, 0.00F, 1.0f, 1.0f }, { 0.75F, 0.75F, 0.25F, 1.0f, 1.0f }, { 0.25F, 0.75F, 0.25F, 1.0f, 1.0f }, },
//			{ { 0.75F, 0.25F, 0.00F, 0.0f, 1.0f }, { 0.75F, 0.75F, 0.00F, 0.0f, 1.0f }, { 0.75F, 0.75F, 0.25F, 0.0f, 1.0f }, { 0.75F, 0.25F, 0.25F, 0.0f, 1.0f }, }
//	};

}
