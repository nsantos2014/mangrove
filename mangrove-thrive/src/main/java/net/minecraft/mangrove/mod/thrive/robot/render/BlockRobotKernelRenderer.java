package net.minecraft.mangrove.mod.thrive.robot.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.mangrove.core.utils.BlockUtils;
import net.minecraft.mangrove.mod.thrive.proxy.CommonProxy;
import net.minecraft.util.Facing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class BlockRobotKernelRenderer implements ISimpleBlockRenderingHandler {

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
               
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        float f = 0.1875F;
        renderer.setOverrideBlockTexture(renderer.getBlockIcon(Blocks.glass));
        renderer.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
        renderer.renderStandardBlock(block, x, y, z);
        renderer.renderAllFaces = true;
        renderer.setOverrideBlockTexture(renderer.getBlockIcon(Blocks.obsidian));
        renderer.setRenderBounds(0.125D, 0.0062500000931322575D, 0.125D, 0.875D, (double)f, 0.875D);
        renderer.renderStandardBlock(block, x, y, z);
        int meta = world.getBlockMetadata(x, y, z);
//        int strongestIndirectPower = getStrongestIndirectPower(world,block,x, y, z);
//        System.out.println("Indirect power "+meta+":"+BlockUtils.getIsBlockNotPoweredFromMetadata(meta));        
        if(BlockUtils.getIsBlockNotPoweredFromMetadata(meta)){
            renderer.setOverrideBlockTexture(renderer.getBlockIcon(Blocks.coal_block));   
        }else{
            renderer.setOverrideBlockTexture(renderer.getBlockIcon(Blocks.redstone_block));            
        }
        
        renderer.setRenderBounds(0.1875D, (double)f, 0.1875D, 0.8125D, 0.875D, 0.8125D);
        renderer.renderStandardBlock(block, x, y, z);
        renderer.renderAllFaces = false;
        renderer.clearOverrideBlockTexture();
        return true;
    }

    /**
     * Is this block powering in the specified direction Args: x, y, z, direction
     */
    public int isBlockProvidingPowerTo(IBlockAccess world,Block block,int x, int y, int z, int direction)
    {
        return block.isProvidingStrongPower(world, x, y, z, direction);
    }
    
    /**
     * Returns the highest redstone signal strength powering the given block. Args: X, Y, Z.
     */
    public int getBlockPowerInput(IBlockAccess world,Block block, int x, int y, int z)
    {
        byte b0 = 0;
        int l = Math.max(b0, this.isBlockProvidingPowerTo(world,block,x, y - 1, z, 0));

        if (l >= 15)
        {
            return l;
        }
        else
        {
            l = Math.max(l, this.isBlockProvidingPowerTo(world,block,x, y + 1, z, 1));

            if (l >= 15)
            {
                return l;
            }
            else
            {
                l = Math.max(l, this.isBlockProvidingPowerTo(world,block,x, y, z - 1, 2));

                if (l >= 15)
                {
                    return l;
                }
                else
                {
                    l = Math.max(l, this.isBlockProvidingPowerTo(world,block,x, y, z + 1, 3));

                    if (l >= 15)
                    {
                        return l;
                    }
                    else
                    {
                        l = Math.max(l, this.isBlockProvidingPowerTo(world,block,x - 1, y, z, 4));

                        if (l >= 15)
                        {
                            return l;
                        }
                        else
                        {
                            l = Math.max(l, this.isBlockProvidingPowerTo(world,block,x + 1, y, z, 5));
                            return l >= 15 ? l : l;
                        }
                    }
                }
            }
        }
    }

//    /**
//     * Returns the indirect signal strength being outputted by the given block in the *opposite* of the given direction.
//     * Args: X, Y, Z, direction
//     */
//    public boolean getIndirectPowerOutput(int p_94574_1_, int p_94574_2_, int p_94574_3_, int p_94574_4_)
//    {
//        return this.getIndirectPowerLevelTo(p_94574_1_, p_94574_2_, p_94574_3_, p_94574_4_) > 0;
//    }

    /**
     * Gets the power level from a certain block face.  Args: x, y, z, direction
     * @param world 
     * @param block2 
     */
    public int getIndirectPowerLevelTo(IBlockAccess world, Block block, int p_72878_1_, int p_72878_2_, int p_72878_3_, int p_72878_4_)
    {
        return /*block.shouldCheckWeakPower(world, p_72878_1_, p_72878_2_, p_72878_3_, p_72878_4_) ? this.getBlockPowerInput(world,block,p_72878_1_, p_72878_2_, p_72878_3_) :*/ block.isProvidingWeakPower(world, p_72878_1_, p_72878_2_, p_72878_3_, p_72878_4_);
    }

//    /**
//     * Used to see if one of the blocks next to you or your block is getting power from a neighboring block. Used by
//     * items like TNT or Doors so they don't have redstone going straight into them.  Args: x, y, z
//     */
//    public boolean isBlockIndirectlyGettingPowered(int p_72864_1_, int p_72864_2_, int p_72864_3_)
//    {
//        return this.getIndirectPowerLevelTo(p_72864_1_, p_72864_2_ - 1, p_72864_3_, 0) > 0 ? true : (this.getIndirectPowerLevelTo(p_72864_1_, p_72864_2_ + 1, p_72864_3_, 1) > 0 ? true : (this.getIndirectPowerLevelTo(p_72864_1_, p_72864_2_, p_72864_3_ - 1, 2) > 0 ? true : (this.getIndirectPowerLevelTo(p_72864_1_, p_72864_2_, p_72864_3_ + 1, 3) > 0 ? true : (this.getIndirectPowerLevelTo(p_72864_1_ - 1, p_72864_2_, p_72864_3_, 4) > 0 ? true : this.getIndirectPowerLevelTo(p_72864_1_ + 1, p_72864_2_, p_72864_3_, 5) > 0))));
//    }

    public int getStrongestIndirectPower(IBlockAccess world, Block block, int x, int y, int z)
    {
        int l = 0;

        for (int i1 = 0; i1 < 6; ++i1)
        {
            int j1 = this.getIndirectPowerLevelTo(world,block,x + Facing.offsetsXForSide[i1], y + Facing.offsetsYForSide[i1], z + Facing.offsetsZForSide[i1], i1);

            if (j1 >= 15)
            {
                return 15;
            }

            if (j1 > l)
            {
                l = j1;
            }
        }

        return l;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        // TODO Auto-generated method stub
        return false;
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
