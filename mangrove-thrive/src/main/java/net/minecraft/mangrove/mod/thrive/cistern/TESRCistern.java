package net.minecraft.mangrove.mod.thrive.cistern;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

public class TESRCistern extends TileEntitySpecialRenderer {
	
//	private void renderConnector(WorldRenderer worldrenderer, EnumFacing facing) {
//		AxisAlignedBB bounds;
//		switch (facing) {
//		case UP:
//			bounds=AxisAlignedBB.fromBounds(0.25f, 0.80f, 0.25f, 0.75f, 1.00f, 0.75f);
//			renderNorthFace(bounds, worldrenderer, 1, 1);
//			renderSouthFace(bounds, worldrenderer, 1, 1);
//			renderEastFace(bounds, worldrenderer, 1, 1);
//			renderWestFace(bounds, worldrenderer, 1, 1);
//			break;
//		case DOWN:
//			bounds=AxisAlignedBB.fromBounds(0.25f, 0.00f, 0.25f, 0.75f, 0.20f, 0.75f);
//			renderNorthFace(bounds, worldrenderer, 1, 1);
//			renderSouthFace(bounds, worldrenderer, 1, 1);
//			renderEastFace(bounds, worldrenderer, 1, 1);
//			renderWestFace(bounds, worldrenderer, 1, 1);
//			break;
//		case NORTH:
//			bounds=AxisAlignedBB.fromBounds(0.25f,  0.25f, 0.00f,0.75f, 0.75f, 0.20f);
//			renderUpFace(bounds, worldrenderer, 1, 1);
//			renderDownFace(bounds, worldrenderer, 1, 1);
//			renderEastFace(bounds, worldrenderer, 1, 1);
//			renderWestFace(bounds, worldrenderer, 1, 1);
//			break;
//		case SOUTH:
//			bounds=AxisAlignedBB.fromBounds(0.25f,  0.25f, 0.80f,0.75f, 0.75f, 1.00f);
//			renderUpFace(bounds, worldrenderer, 1, 1);
//			renderDownFace(bounds, worldrenderer, 1, 1);
//			renderEastFace(bounds, worldrenderer, 1, 1);
//			renderWestFace(bounds, worldrenderer, 1, 1);
//			break;
//		case WEST:
//			bounds=AxisAlignedBB.fromBounds(0.00f ,0.25f,  0.25f, 0.20f, 0.75f, 0.75f);
//			renderUpFace(bounds, worldrenderer, 1, 1);
//			renderDownFace(bounds, worldrenderer, 1, 1);
//			renderNorthFace(bounds, worldrenderer, 1, 1);
//			renderSouthFace(bounds, worldrenderer, 1, 1);
//			break;
//		case EAST:
//			bounds=AxisAlignedBB.fromBounds(0.80f ,0.25f,  0.25f, 1.00f, 0.75f, 0.75f);
//			renderUpFace(bounds, worldrenderer, 1, 1);
//			renderDownFace(bounds, worldrenderer, 1, 1);
//			renderNorthFace(bounds, worldrenderer, 1, 1);
//			renderSouthFace(bounds, worldrenderer, 1, 1);
//			break;
//		default:
//			break;
//		}
//		
//	}
//
//	private void renderBox(WorldRenderer worldrenderer, AxisAlignedBB bounds) {
//		renderNorthFace(bounds, worldrenderer, 1, 1);
//		renderSouthFace(bounds, worldrenderer, 1, 1);
//		renderEastFace(bounds, worldrenderer, 1, 1);
//		renderWestFace(bounds, worldrenderer, 1, 1);
//		renderUpFace(bounds, worldrenderer, 1, 1);
//		renderDownFace(bounds, worldrenderer, 1, 1);
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
		this.bindTexture(TextureMap.locationBlocksTexture);
		BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
		World world = te.getWorld();
        BlockPos blockpos = te.getPos();
        IBlockState iblockstate = world.getBlockState(blockpos);
        
		GlStateManager.pushMatrix();
		RenderHelper.disableStandardItemLighting();
        GlStateManager.translate((float)x, (float)y, (float)z);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.startDrawingQuads();
        worldrenderer.setVertexFormat(DefaultVertexFormats.BLOCK);
        int i = blockpos.getX();
        int j = blockpos.getY();
        int k = blockpos.getZ();

        worldrenderer.setTranslation((double)((float)(-i)), (double)(-j), (double)((float)(-k) ));
        
        worldrenderer.setColorOpaque_F(1.0F, 1.0F, 1.0F);
        IBakedModel ibakedmodel = blockrendererdispatcher.getModelFromBlockState(iblockstate, world, blockpos);
        blockrendererdispatcher.getBlockModelRenderer().renderModel(world, ibakedmodel, iblockstate, blockpos, worldrenderer, true);
        
        worldrenderer.setTranslation(0.0D, 0.0D, 0.0D);
        tessellator.draw();
        
        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
        
        this.renderContent((TileCistern) te, x, y, z, p_180535_8_, p_180535_9_);
	}
	
//	private void renderFluidContent(TileCistern tile, double x, double y, double z, float p_180535_8_, int p_180535_9_){
////		int[] displayList = FluidRenderer.getFluidDisplayLists(liquid, tileentity.getWorld(), false);
////		if (displayList == null) {
////			return;
////		}
//
//		GL11.glPushMatrix();
//		GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
//		GL11.glEnable(GL11.GL_CULL_FACE);
//		GL11.glDisable(GL11.GL_LIGHTING);
//		GL11.glEnable(GL11.GL_BLEND);
//		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
//
////		bindTexture(TextureMap.locationBlocksTexture);
//		bindTexture(tile.getFluidTexture());
////		RenderUtils.setGLColorFromInt(color);
//
//		GL11.glTranslatef((float) x + 0.125F, (float) y + 0.5F, (float) z + 0.125F);
//		GL11.glScalef(0.75F, 0.999F, 0.75F);
//		GL11.glTranslatef(0, -0.5F, 0);
//		
//		Tessellator tessellator = Tessellator.getInstance();
//		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
//		 worldrenderer.startDrawingQuads();
//		 
//		 AxisAlignedBB bounds = AxisAlignedBB.fromBounds(0.125f, 0.75f, 0.125f, 0.875f, 0.75f, 0.875f);
//		 
//		worldrenderer.addVertexWithUV(bounds.maxX, bounds.maxY, bounds.minZ, 1.0f, 1.0);
//		worldrenderer.addVertexWithUV(bounds.minX, bounds.maxY, bounds.minZ, 0.0f, 1.0);
//		worldrenderer.addVertexWithUV(bounds.minX, bounds.maxY, bounds.maxZ, 0.0f, 0.0);
//		worldrenderer.addVertexWithUV(bounds.maxX, bounds.maxY, bounds.maxZ, 1.0f, 0.0);
//
////		GL11.glCallList(displayList[(int) ((float) liquid.amount / (float) (tank.tank.getCapacity()) * (FluidRenderer.DISPLAY_STAGES - 1))]);
//		tessellator.draw();
//		GL11.glPopAttrib();
//		GL11.glPopMatrix();
//	}
	
	private void renderContent(TileCistern tile, double x, double y, double z, float p_180535_8_, int p_180535_9_) {
		
		if( tile.getLevel()==0){
			return;
		}
		float bottom=0.25f;
		float level=((float)tile.getLevel())/((float)tile.getMaxLevel())*(1.0f-bottom);
		
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		
		GlStateManager.disableLighting();
		GlStateManager.disableCull();
		GlStateManager.disableBlend();
		GlStateManager.depthMask(true);
		GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0);

		GL11.glPushMatrix();
		GL11.glTranslatef((float) x, (float) y, (float) z);

		float scale = .2f;
		 worldrenderer.startDrawingQuads();
		this.bindTexture(tile.getFluidTexture());
		
		AxisAlignedBB bounds = AxisAlignedBB.fromBounds(0.125f, bottom+level, 0.125f, 0.875f, bottom+level, 0.875f);
		worldrenderer.setColorOpaque_F(1.0F, 1.0F, 0.1F);
		
		renderUpFace(bounds, worldrenderer, 1, .1f);
		
		tessellator.draw();

		GlStateManager.enableLighting();
		GlStateManager.enableTexture2D();
		GlStateManager.depthMask(true);
		GL11.glPopMatrix();
	}

//	protected void renderNorthFace(AxisAlignedBB bounds, WorldRenderer worldrenderer,float uScale, float vscale){		
//		worldrenderer.addVertexWithUV(bounds.maxX, bounds.minY, bounds.minZ, 1.0f*uScale, 1.0*vscale);
//		worldrenderer.addVertexWithUV(bounds.maxX, bounds.maxY, bounds.minZ, 0.0f*uScale, 1.0*vscale);
//		worldrenderer.addVertexWithUV(bounds.minX, bounds.maxY, bounds.minZ, 0.0f*uScale, 0.0*vscale);
//		worldrenderer.addVertexWithUV(bounds.minX, bounds.minY, bounds.minZ, 1.0f*uScale, 0.0*vscale);
//	}
//	
//	protected void renderSouthFace(AxisAlignedBB bounds, WorldRenderer worldrenderer,float uScale, float vscale){		
//		worldrenderer.addVertexWithUV(bounds.maxX, bounds.minY, bounds.maxZ, 1.0f*uScale, 1.0*vscale);
//		worldrenderer.addVertexWithUV(bounds.maxX, bounds.maxY, bounds.maxZ, 0.0f*uScale, 1.0*vscale);
//		worldrenderer.addVertexWithUV(bounds.minX, bounds.maxY, bounds.maxZ, 0.0f*uScale, 0.0*vscale);
//		worldrenderer.addVertexWithUV(bounds.minX, bounds.minY, bounds.maxZ, 1.0f*uScale, 0.0*vscale);
//	}
//	
//	protected void renderWestFace(AxisAlignedBB bounds, WorldRenderer worldrenderer,float uScale, float vscale){		
//		worldrenderer.addVertexWithUV(bounds.minX, bounds.minY, bounds.minZ, 1.0f*uScale, 1.0*vscale);
//		worldrenderer.addVertexWithUV(bounds.minX, bounds.maxY, bounds.minZ, 0.0f*uScale, 1.0*vscale);
//		worldrenderer.addVertexWithUV(bounds.minX, bounds.maxY, bounds.maxZ, 0.0f*uScale, 0.0*vscale);
//		worldrenderer.addVertexWithUV(bounds.minX, bounds.minY, bounds.maxZ, 1.0f*uScale, 0.0*vscale);
//	}
//	
//	protected void renderEastFace(AxisAlignedBB bounds, WorldRenderer worldrenderer,float uScale, float vscale){		
//		worldrenderer.addVertexWithUV(bounds.maxX, bounds.minY, bounds.minZ, 1.0f*uScale, 1.0*vscale);
//		worldrenderer.addVertexWithUV(bounds.maxX, bounds.maxY, bounds.minZ, 0.0f*uScale, 1.0*vscale);
//		worldrenderer.addVertexWithUV(bounds.maxX, bounds.maxY, bounds.maxZ, 0.0f*uScale, 0.0*vscale);
//		worldrenderer.addVertexWithUV(bounds.maxX, bounds.minY, bounds.maxZ, 1.0f*uScale, 0.0*vscale);
//	}
//	
	protected void renderUpFace(AxisAlignedBB bounds, WorldRenderer worldrenderer,float uScale, float vscale){		
		worldrenderer.addVertexWithUV(bounds.maxX, bounds.maxY, bounds.minZ, 1.0f*uScale, 1.0*vscale);
		worldrenderer.addVertexWithUV(bounds.minX, bounds.maxY, bounds.minZ, 0.0f*uScale, 1.0*vscale);
		worldrenderer.addVertexWithUV(bounds.minX, bounds.maxY, bounds.maxZ, 0.0f*uScale, 0.0*vscale);
		worldrenderer.addVertexWithUV(bounds.maxX, bounds.maxY, bounds.maxZ, 1.0f*uScale, 0.0*vscale);
	}
//	
//	protected void renderDownFace(AxisAlignedBB bounds, WorldRenderer worldrenderer,float uScale, float vscale){		
//		worldrenderer.addVertexWithUV(bounds.minX, bounds.minY, bounds.maxZ, 1.0f*uScale, 1.0*vscale);
//		worldrenderer.addVertexWithUV(bounds.minX, bounds.minY, bounds.minZ, 0.0f*uScale, 1.0*vscale);
//		worldrenderer.addVertexWithUV(bounds.maxX, bounds.minY, bounds.minZ, 0.0f*uScale, 0.0*vscale);
//		worldrenderer.addVertexWithUV(bounds.maxX, bounds.minY, bounds.maxZ, 1.0f*uScale, 0.0*vscale);
//	}

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
//	private float[][][] quadVertexes = new float[][][] {
//		// UP CONNECTOR
//		{ 
//			{ 0.25F, 0.75F, 0.25F, 4.00f, 4.00f}, 
//			{ 0.75F, 0.75F, 0.25F, 0.00f, 4.00f}, 
//			{ 0.75F, 1.00F, 0.25F, 0.00f, 0.00f}, 
//			{ 0.25F, 1.00F, 0.25F, 4.00f, 0.00f} 
//		},
//		{ 
//			{ 0.25F, 0.75F, 0.25F, 4.00f, 4.00f }, 
//			{ 0.25F, 0.75F, 0.75F, 0.00f, 4.00f }, 
//			{ 0.25F, 1.00F, 0.75F, 0.00f, 0.00f }, 
//			{ 0.25F, 1.00F, 0.25F, 4.00f, 0.00f } 
//		},
//		{ 
//			{ 0.25F, 0.75F, 0.75F, 4.00f, 4.00f }, 
//			{ 0.75F, 0.75F, 0.75F, 0.00f, 4.00f }, 
//			{ 0.75F, 1.00F, 0.75F, 0.00f, 0.00f }, 
//			{ 0.25F, 1.00F, 0.75F, 4.00f, 0.00f } 
//		},
//		{ 
//			{ 0.75F, 0.75F, 0.25F, 4.00f, 4.00f }, 
//			{ 0.75F, 0.75F, 0.75F, 0.00f, 4.00f }, 
//			{ 0.75F, 1.00F, 0.75F, 0.00f, 0.00f }, 
//			{ 0.75F, 1.00F, 0.25F, 4.00f, 0.00f } 
//		},
//		// DOWN CONNECTOR
//		{ 
//			{ 0.25F, 0.00F, 0.25F,  4.00f, 4.00f }, 
//			{ 0.75F, 0.00F, 0.25F,  0.00f, 4.00f }, 
//			{ 0.75F, 0.25F, 0.25F,  0.00f, 0.00f }, 
//			{ 0.25F, 0.25F, 0.25F,  4.00f, 0.00f } 
//		},
//		{ 
//			{ 0.25F, 0.00F, 0.25F, 4.00f, 4.00f },
//			{ 0.25F, 0.00F, 0.75F, 0.00f, 4.00f }, 
//			{ 0.25F, 0.25F, 0.75F, 0.00f, 0.00f },
//			{ 0.25F, 0.25F, 0.25F, 4.00f, 0.00f } 
//		},
//		{ 
//			{ 0.25F, 0.00F, 0.75F, 4.00f, 4.00f }, 
//			{ 0.75F, 0.00F, 0.75F, 0.00f, 4.00f }, 
//			{ 0.75F, 0.25F, 0.75F, 0.00f, 0.00f }, 
//			{ 0.25F, 0.25F, 0.75F, 4.00f, 0.00f } 
//		},
//		{ 
//			{ 0.75F, 0.00F, 0.25F, 4.00f, 4.00f }, 
//			{ 0.75F, 0.00F, 0.75F, 0.00f, 4.00f },
//			{ 0.75F, 0.25F, 0.75F, 0.00f, 0.00f },
//			{ 0.75F, 0.25F, 0.25F, 4.00f, 0.00f } 
//		},
//		// WEST CONNECTOR
//		{ 
//			{ 0.00F, 0.25F, 0.25F, 4.00f, 4.00f }, 
//			{ 0.00F, 0.75F, 0.25F, 0.00f, 4.00f }, 
//			{ 0.25F, 0.75F, 0.25F, 0.00f, 0.00f }, 
//			{ 0.25F, 0.25F, 0.25F, 4.00f, 0.00f }
//		},
//		{ 
//			{ 0.00F, 0.25F, 0.25F, 4.00f, 4.00f }, 
//			{ 0.00F, 0.25F, 0.75F, 0.00f, 4.00f }, 
//			{ 0.25F, 0.25F, 0.75F, 0.00f, 0.00f }, 
//			{ 0.25F, 0.25F, 0.25F, 4.00f, 0.00f } 
//		},
//		{ 
//			{ 0.00F, 0.25F, 0.75F, 4.00f, 4.00f }, 
//			{ 0.00F, 0.75F, 0.75F, 0.00f, 4.00f },
//			{ 0.25F, 0.75F, 0.75F, 0.00f, 0.00f },
//			{ 0.25F, 0.25F, 0.75F, 4.00f, 0.00f }
//		},
//		{ 
//			{ 0.00F, 0.75F, 0.25F, 4.00f, 4.00f },
//			{ 0.00F, 0.75F, 0.75F, 0.00f, 4.00f }, 
//			{ 0.25F, 0.75F, 0.75F, 0.00f, 0.00f },
//			{ 0.25F, 0.75F, 0.25F, 4.00f, 0.00f } 
//		},
//		// EAST CONNECTOR                                                                                                                                 
//		{ 
//			{ 0.75F, 0.25F, 0.25F, 4.00f, 4.00f },		
//			{ 0.75F, 0.75F, 0.25F, 0.00f, 4.00f },
//			{ 1.00F, 0.75F, 0.25F, 0.00f, 0.00f }, 
//			{ 1.00F, 0.25F, 0.25F, 4.00f, 0.00f } 
//		},
//		{ 
//			{ 0.75F, 0.25F, 0.25F, 4.00f, 4.00f }, 
//			{ 0.75F, 0.25F, 0.75F, 0.00f, 4.00f }, 
//			{ 1.00F, 0.25F, 0.75F, 0.00f, 0.00f }, 
//			{ 1.00F, 0.25F, 0.25F, 4.00f, 0.00f }
//		},
//		{ 
//			{ 0.75F, 0.25F, 0.75F, 4.00f, 4.00f }, 
//			{ 0.75F, 0.75F, 0.75F, 0.00f, 4.00f }, 
//			{ 1.00F, 0.75F, 0.75F, 0.00f, 0.00f }, 
//			{ 1.00F, 0.25F, 0.75F, 4.00f, 0.00f } 
//		},
//		{ 
//			{ 0.75F, 0.75F, 0.25F, 4.00f, 4.00f },
//			{ 0.75F, 0.75F, 0.75F, 0.00f, 4.00f }, 
//			{ 1.00F, 0.75F, 0.75F, 0.00f, 0.00f }, 
//			{ 1.00F, 0.75F, 0.25F, 4.00f, 0.00f } 
//		},
//		// SOUTH CONNECTORw                                                                                                                               
//		{ 
//			{ 0.25F, 0.25F, 0.75F,  4.00f, 4.00f },
//			{ 0.75F, 0.25F, 0.75F,  0.00f, 4.00f },
//			{ 0.75F, 0.25F, 1.00F,  0.00f, 0.00f },
//			{ 0.25F, 0.25F, 1.00F,  4.00f, 0.00f }
//		},
//		{ 
//			{ 0.25F, 0.25F, 0.75F, 4.00f, 4.00f }, 
//			{ 0.25F, 0.75F, 0.75F, 0.00f, 4.00f }, 
//			{ 0.25F, 0.75F, 1.00F, 0.00f, 0.00f }, 
//			{ 0.25F, 0.25F, 1.00F, 4.00f, 0.00f } 
//		},
//		{
//			{ 0.25F, 0.75F, 0.75F, 4.00f, 4.00f }, 
//			{ 0.75F, 0.75F, 0.75F, 0.00f, 4.00f },
//			{ 0.75F, 0.75F, 1.00F, 0.00f, 0.00f }, 
//			{ 0.25F, 0.75F, 1.00F, 4.00f, 0.00f } 
//		},
//		{
//			{ 0.75F, 0.25F, 0.75F, 4.00f, 4.00f }, 
//			{ 0.75F, 0.75F, 0.75F, 0.00f, 4.00f }, 
//			{ 0.75F, 0.75F, 1.00F, 0.00f, 0.00f }, 
//			{ 0.75F, 0.25F, 1.00F, 4.00f, 0.00f }
//		},
//		// NORTH CONNECTOR                                                                                                                                
//		{ 
//			{ 0.25F, 0.25F, 0.00F, 4.00f, 4.00f },
//			{ 0.75F, 0.25F, 0.00F, 0.00f, 4.00f },
//			{ 0.75F, 0.25F, 0.25F, 0.00f, 0.00f },
//			{ 0.25F, 0.25F, 0.25F, 4.00f, 0.00f },
//		},
//		{
//			{ 0.25F, 0.25F, 0.00F, 4.00f, 4.00f }, 
//			{ 0.25F, 0.75F, 0.00F, 0.00f, 4.00f },
//			{ 0.25F, 0.75F, 0.25F, 0.00f, 0.00f },
//			{ 0.25F, 0.25F, 0.25F, 4.00f, 0.00f },
//		},
//		{
//			{ 0.25F, 0.75F, 0.00F, 4.00f, 4.00f },
//			{ 0.75F, 0.75F, 0.00F, 0.00f, 4.00f }, 
//			{ 0.75F, 0.75F, 0.25F, 0.00f, 0.00f }, 
//			{ 0.25F, 0.75F, 0.25F, 4.00f, 0.00f }, 
//		},
//		{ 
//			{ 0.75F, 0.25F, 0.00F, 4.00f, 4.00f },
//			{ 0.75F, 0.75F, 0.00F, 0.00f, 4.00f },
//			{ 0.75F, 0.75F, 0.25F, 0.00f, 0.00f },
//			{ 0.75F, 0.25F, 0.25F, 4.00f, 0.00f }, 
//		}
//	};
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
