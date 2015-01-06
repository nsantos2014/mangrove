package net.minecraft.mangrove.mod.thrive.robot.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.mangrove.core.utils.BlockUtils;
import net.minecraft.mangrove.mod.thrive.proxy.CommonProxy;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class BlockRobotNodeRenderer implements ISimpleBlockRenderingHandler {

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        renderer.uvRotateTop = getUVTopForFront(ForgeDirection.EAST.ordinal());

        Tessellator tessellator = Tessellator.instance;
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, -1.0F, 0.0F);
        renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(0, metadata));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(0, metadata));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(0, metadata));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, -1.0F);
        renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(0, metadata));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(0, metadata));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(-1.0F, 0.0F, 0.0F);
        renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(0, metadata));
        tessellator.draw();
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        
        GL11.glPushMatrix();
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        GL11.glScaled(0.8125D, 0.875D, 0.8125D);
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, -1.0F, 0.0F);
        renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(1, metadata));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(1, metadata));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(1, metadata));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, -1.0F);
        renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(1, metadata));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(1, metadata));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(-1.0F, 0.0F, 0.0F);
        renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(1, metadata));
        tessellator.draw();
        GL11.glPopMatrix();        
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        float f = 0.1875F;
        renderer.renderAllFaces = true;
        renderer.setOverrideBlockTexture(renderer.getBlockIcon(Blocks.glass));
        renderer.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
        renderer.renderStandardBlock(block, x, y, z);
                
        renderer.setOverrideBlockTexture(renderer.getBlockIcon(Blocks.log));
        
        int meta = world.getBlockMetadata(x, y, z);
        ForgeDirection orientation = ForgeDirection.getOrientation(meta & 0x07);
        switch(orientation){
        case NORTH:
            renderer.setRenderBounds(0.125D,0.125D, 0.0062500000931322575D, 0.875D,0.875D,  (double)f);
            break;
        case SOUTH:
            renderer.setRenderBounds(0.125D,0.125D,1.0-(double)f, 0.875D,0.875D,  1.0-0.0062500000931322575D);
            break;
        case WEST:
            renderer.setRenderBounds(0.0062500000931322575D, 0.125D,0.125D,  (double)f, 0.875D,0.875D);
            break;
        case EAST:
            renderer.setRenderBounds(1.0-(double)f, 0.125D,0.125D, 1.0-0.0062500000931322575D, 0.875D,0.875D);
            break;
        default:
            System.out.println("Direction : "+meta+":"+orientation);
            renderer.setRenderBounds(0.125D, 0.0062500000931322575D, 0.125D, 0.875D, (double)f, 0.875D);   
        }
        
        renderer.renderStandardBlock(block, x, y, z);
        if(BlockUtils.getIsBlockNotPoweredFromMetadata(meta)){
            renderer.setOverrideBlockTexture(renderer.getBlockIcon(Blocks.coal_block));   
        }else{
            renderer.setOverrideBlockTexture(renderer.getBlockIcon(Blocks.redstone_block));            
        }
        
//        renderer.setOverrideBlockTexture(renderer.getBlockIcon(Blocks.beacon));
        renderer.setRenderBounds(0.1875D, (double)f, 0.1875D, 0.8125D, 0.875D, 0.8125D);
        renderer.renderStandardBlock(block, x, y, z);
        renderer.renderAllFaces = false;
        renderer.clearOverrideBlockTexture();
        return true;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }

    @Override
    public int getRenderId() {
        return CommonProxy.blockFarmerNodeRenderId;
    }

    public static int getUVTopForFront(int front) {
        switch (front) {
            case 2:
                return 3;
            case 3:
                return 0;
            case 4:
                return 1;
            case 5:
                return 2;
            default:
                return 0;
        }
    }
}
