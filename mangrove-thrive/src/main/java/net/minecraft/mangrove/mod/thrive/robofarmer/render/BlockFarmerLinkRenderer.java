package net.minecraft.mangrove.mod.thrive.robofarmer.render;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockHopper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.mangrove.core.utils.BlockUtils;
import net.minecraft.mangrove.mod.thrive.proxy.CommonProxy;
import net.minecraft.mangrove.mod.thrive.robofarmer.block.BlockFarmerLink;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class BlockFarmerLinkRenderer implements ISimpleBlockRenderingHandler {

    public final Minecraft minecraftRB;

    public boolean field_152631_f;

    //
    public BlockFarmerLinkRenderer() {
        this.minecraftRB = Minecraft.getMinecraft();
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
    }

    public boolean renderBlockFence(BlockFarmerLink block, int x, int y, int z, RenderBlocks renderer, IBlockAccess world) {

        Tessellator tessellator = Tessellator.instance;
        IIcon icon = block.getBlockTextureFromSide(1);

        boolean connectTop = block.canConnectTo(world, x, y + 1, z);
        boolean connectBottom = block.canConnectTo(world, x, y - 1, z);

        boolean connectLeft = block.canConnectTo(world, x - 1, y, z);
        boolean connectRight = block.canConnectTo(world, x + 1, y, z);

        boolean connectFront = block.canConnectTo(world, x, y, z - 1);
        boolean connectBack = block.canConnectTo(world, x, y, z + 1);

        boolean flag = false;

        double minY = connectBottom ? 0.0f : 0.25d;
        double maxY = connectTop ? 1.0f : 0.75d;

        // float f = 0.375F;
        // float f1 = 0.625F;
        double f = 0.25d;
        double f1 = 0.75d;
        
        int meta = world.getBlockMetadata(x, y, z);
//      int strongestIndirectPower = getStrongestIndirectPower(world,block,x, y, z);
//      System.out.println("Indirect power "+meta+":"+BlockUtils.getIsBlockNotPoweredFromMetadata(meta));        
        if(BlockUtils.getIsBlockNotPoweredFromMetadata(meta)){
          renderer.setOverrideBlockTexture(renderer.getBlockIcon(Blocks.coal_block));   
        }else{
          renderer.setOverrideBlockTexture(renderer.getBlockIcon(Blocks.redstone_block));            
        }
        
//        renderer.setOverrideBlockTexture(icon);

        renderer.setRenderBounds(f, f, f, f1, f1, f1);
        renderer.renderStandardBlock(block, x, y, z);

        if (connectTop) {
            renderer.setRenderBounds(f, f1, f, f1, 1.0, f1);
            renderer.renderStandardBlock(block, x, y, z);
        }
        if (connectBottom) {
            renderer.setRenderBounds(f, 0, f, f1, f, f1);
            renderer.renderStandardBlock(block, x, y, z);
        }

        if (connectLeft) {
            renderer.setRenderBounds(0, f, f, f, f1, f1);
            renderer.renderStandardBlock(block, x, y, z);
        }
        if (connectRight) {
            renderer.setRenderBounds(f1, f, f, 1.0, f1, f1);
            renderer.renderStandardBlock(block, x, y, z);
        }

        if (connectFront) {
            renderer.setRenderBounds(f, f, 0, f1, f1, f);
            renderer.renderStandardBlock(block, x, y, z);
        }
        if (connectBack) {
            renderer.setRenderBounds(f, f, f1, f1, f1, 1);
            renderer.renderStandardBlock(block, x, y, z);
        }
        flag = true;
        // boolean flag1 = false;
        // boolean flag2 = false;
        //
        // if (block.canConnectTo(world, x - 1, y, z) ||
        // block.canConnectTo(world, x + 1, y, z)) {
        // flag1 = true;
        // }
        //
        // if (block.canConnectTo(world, x, y, z - 1) ||
        // block.canConnectTo(world, x, y, z + 1)) {
        // flag2 = true;
        // }
        //
        // boolean flag3 = block.canConnectTo(world, x - 1, y, z);
        // boolean flag4 = block.canConnectTo(world, x + 1, y, z);
        // boolean flag5 = block.canConnectTo(world, x, y, z - 1);
        // boolean flag6 = block.canConnectTo(world, x, y, z + 1);
        //
        // if (!flag1 && !flag2) {
        // flag1 = true;
        // }
        //
        // // f = 0.4375F;
        // // f1 = 0.5625F;
        // float f2 = 0.75F;
        // float f3 = 0.9375F;
        // float f4 = flag3 ? 0.0F : (float)f;
        // float f5 = flag4 ? 1.0F : (float)f1;
        // float f6 = flag5 ? 0.0F : (float)f;
        // float f7 = flag6 ? 1.0F : (float)f1;
        // this.field_152631_f = true;
        //
        // if (flag1) {
        // renderer.setRenderBounds((double) f4, (double) f2, (double) f,
        // (double) f5, (double) f3, (double) f1);
        // renderer.renderStandardBlock(block, x, y, z);
        // flag = true;
        // }
        //
        // if (flag2) {
        // renderer.setRenderBounds((double) f, (double) f2, (double) f6,
        // (double) f1, (double) f3, (double) f7);
        // renderer.renderStandardBlock(block, x, y, z);
        // flag = true;
        // }
        /*
         * f2 = 0.375F; f3 = 0.5625F;
         * 
         * if (flag1) { renderer.setRenderBounds((double) f4, (double) f2,
         * (double) f, (double) f5, (double) f3, (double) f1);
         * renderer.renderStandardBlock(block, x, y, z); flag = true; }
         * 
         * if (flag2) { renderer.setRenderBounds((double) f, (double) f2,
         * (double) f6, (double) f1, (double) f3, (double) f7);
         * renderer.renderStandardBlock(block, x, y, z); flag = true; }
         */
        this.field_152631_f = false;
        block.setBlockBoundsBasedOnState(world, x, y, z);
        renderer.clearOverrideBlockTexture();
        return flag;
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        // world=world;
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z));
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
        return renderBlockFence((BlockFarmerLink) block, x, y, z, renderer, world);
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return false;
    }

    @Override
    public int getRenderId() {
        return CommonProxy.blockFarmerLinkRenderId;
    }

}
