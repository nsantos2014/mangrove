package net.minecraft.mangrove.mod.thrive.robofarmer.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockHopper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.mangrove.mod.thrive.proxy.CommonProxy;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class BlockFarmerLinkRenderer implements ISimpleBlockRenderingHandler{
	
//	private IBlockAccess blockAccess;
	public final Minecraft minecraftRB;
	
	public boolean field_152631_f;
//	public boolean partialRenderBounds;
//	public boolean lockBlockBounds;
//	/** The minimum X value for rendering (default 0.0). */
//    public double renderMinX;
//    /** The maximum X value for rendering (default 1.0). */
//    public double renderMaxX;
//    /** The minimum Y value for rendering (default 0.0). */
//    public double renderMinY;
//    /** The maximum Y value for rendering (default 1.0). */
//    public double renderMaxY;
//    /** The minimum Z value for rendering (default 0.0). */
//    public double renderMinZ;
//    /** The maximum Z value for rendering (default 1.0). */
//    public double renderMaxZ;
    
    public BlockFarmerLinkRenderer() {
    	this.minecraftRB = Minecraft.getMinecraft();
	}
	
	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId,RenderBlocks renderer) {
	}

	public boolean renderBlockFence(BlockFarmerLink block, int x, int y, int z, RenderBlocks renderer, IBlockAccess world)
    {
		
		Tessellator tessellator = Tessellator.instance;
		IIcon icon = block.getBlockTextureFromSide(1);
		
        boolean flag = false;
        float f = 0.375F;
        float f1 = 0.625F;
        renderer.setOverrideBlockTexture(icon);
        renderer.setRenderBounds((double)f, 0.0D, (double)f, (double)f1, 1.0D, (double)f1);
        renderer.renderStandardBlock(block, x, y, z);
        
        
        flag = true;
        boolean flag1 = false;
        boolean flag2 = false;

        if (block.canConnectTo(world, x - 1, y, z) || block.canConnectTo(world, x + 1, y, z))
        {
            flag1 = true;
        }

        if (block.canConnectTo(world, x, y, z - 1) || block.canConnectTo(world, x, y, z + 1))
        {
            flag2 = true;
        }

        boolean flag3 = block.canConnectTo(world, x - 1, y, z);
        boolean flag4 = block.canConnectTo(world, x + 1, y, z);
        boolean flag5 = block.canConnectTo(world, x, y, z - 1);
        boolean flag6 = block.canConnectTo(world, x, y, z + 1);

        if (!flag1 && !flag2)
        {
            flag1 = true;
        }

        f = 0.4375F;
        f1 = 0.5625F;
        float f2 = 0.75F;
        float f3 = 0.9375F;
        float f4 = flag3 ? 0.0F : f;
        float f5 = flag4 ? 1.0F : f1;
        float f6 = flag5 ? 0.0F : f;
        float f7 = flag6 ? 1.0F : f1;
        this.field_152631_f = true;

        if (flag1)
        {
        	renderer.setRenderBounds((double)f4, (double)f2, (double)f, (double)f5, (double)f3, (double)f1);
            renderer.renderStandardBlock(block, x, y, z);
            flag = true;
        }

        if (flag2)
        {
        	renderer.setRenderBounds((double)f, (double)f2, (double)f6, (double)f1, (double)f3, (double)f7);
        	renderer.renderStandardBlock(block, x, y, z);
            flag = true;
        }

        f2 = 0.375F;
        f3 = 0.5625F;

        if (flag1)
        {
        	renderer.setRenderBounds((double)f4, (double)f2, (double)f, (double)f5, (double)f3, (double)f1);
        	renderer.renderStandardBlock(block, x, y, z);
            flag = true;
        }

        if (flag2)
        {
        	renderer.setRenderBounds((double)f, (double)f2, (double)f6, (double)f1, (double)f3, (double)f7);
        	renderer.renderStandardBlock(block, x, y, z);
            flag = true;
        }

        this.field_152631_f = false;
        block.setBlockBoundsBasedOnState(world, x, y, z);
        renderer.clearOverrideBlockTexture();
        return flag;
    }
	
	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z,Block block, int modelId, RenderBlocks renderer) {		
//		world=world;
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
        return renderBlockFence((BlockFarmerLink)block, x, y, z,renderer,world);
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return false;
	}

	@Override
	public int getRenderId() {
		return CommonProxy.blockFarmerLinkRenderId;
	}

//	 public void setRenderBounds(double p_147782_1_, double p_147782_3_, double p_147782_5_, double p_147782_7_, double p_147782_9_, double p_147782_11_)
//	    {
//	        if (!this.lockBlockBounds)
//	        {
//	            this.renderMinX = p_147782_1_;
//	            this.renderMaxX = p_147782_7_;
//	            this.renderMinY = p_147782_3_;
//	            this.renderMaxY = p_147782_9_;
//	            this.renderMinZ = p_147782_5_;
//	            this.renderMaxZ = p_147782_11_;
//	            this.partialRenderBounds = this.minecraftRB.gameSettings.ambientOcclusion >= 2 && (this.renderMinX > 0.0D || this.renderMaxX < 1.0D || this.renderMinY > 0.0D || this.renderMaxY < 1.0D || this.renderMinZ > 0.0D || this.renderMaxZ < 1.0D);
//	        }
//	    }
//	 
//	 /**
//	     * Renders a standard cube block at the given coordinates
//	     */
//	    public boolean renderStandardBlock(Block p_147784_1_, int p_147784_2_, int p_147784_3_, int p_147784_4_)
//	    {
//	        int l = p_147784_1_.colorMultiplier(world, p_147784_2_, p_147784_3_, p_147784_4_);
//	        float f = (float)(l >> 16 & 255) / 255.0F;
//	        float f1 = (float)(l >> 8 & 255) / 255.0F;
//	        float f2 = (float)(l & 255) / 255.0F;
//
//	        if (EntityRenderer.anaglyphEnable)
//	        {
//	            float f3 = (f * 30.0F + f1 * 59.0F + f2 * 11.0F) / 100.0F;
//	            float f4 = (f * 30.0F + f1 * 70.0F) / 100.0F;
//	            float f5 = (f * 30.0F + f2 * 70.0F) / 100.0F;
//	            f = f3;
//	            f1 = f4;
//	            f2 = f5;
//	        }
//
//	        return Minecraft.isAmbientOcclusionEnabled() && p_147784_1_.getLightValue() == 0 ? (this.partialRenderBounds ? renderer.renderStandardBlockWithAmbientOcclusionPartial(p_147784_1_, p_147784_2_, p_147784_3_, p_147784_4_, f, f1, f2) : renderer.renderStandardBlockWithAmbientOcclusion(p_147784_1_, p_147784_2_, p_147784_3_, p_147784_4_, f, f1, f2)) : renderer.renderStandardBlockWithColorMultiplier(p_147784_1_, p_147784_2_, p_147784_3_, p_147784_4_, f, f1, f2);
//	    }
	    
	    
}
