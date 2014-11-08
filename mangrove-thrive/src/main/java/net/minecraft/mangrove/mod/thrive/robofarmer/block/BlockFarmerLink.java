package net.minecraft.mangrove.mod.thrive.robofarmer.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemLead;
import net.minecraft.mangrove.mod.thrive.MGThriveBlocks;
import net.minecraft.mangrove.mod.thrive.proxy.CommonProxy;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockFarmerLink extends Block implements IConnection{
    private final String field_149827_a;

    public BlockFarmerLink() {
        super(Material.wood);
        setBlockName("farmer_link");
        this.field_149827_a = "planks_oak";
        setBlockTextureName("planks_oak");
        this.setCreativeTab(CreativeTabs.tabRedstone);
    }

    /**
     * Adds all intersecting collision boxes to a list. (Be sure to only add
     * boxes to the list if they intersect the mask.) Parameters: World, X, Y,
     * Z, mask, list, colliding entity
     */
    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB mask, List list, Entity entity) {
        boolean flag = this.canConnectTo(world, x, y, z - 1);
        boolean flag1 = this.canConnectTo(world, x, y, z + 1);
        boolean flag2 = this.canConnectTo(world, x - 1, y, z);
        boolean flag3 = this.canConnectTo(world, x + 1, y, z);
        float f = 0.375F;
        float f1 = 0.625F;
        float f2 = 0.375F;
        float f3 = 0.625F;

        if (flag) {
            f2 = 0.0F;
        }

        if (flag1) {
            f3 = 1.0F;
        }

        if (flag || flag1) {
            this.setBlockBounds(f, 0.0F, f2, f1, 1.5F, f3);
            super.addCollisionBoxesToList(world, x, y, z, mask, list, entity);
        }

        f2 = 0.375F;
        f3 = 0.625F;

        if (flag2) {
            f = 0.0F;
        }

        if (flag3) {
            f1 = 1.0F;
        }

        if (flag2 || flag3 || !flag && !flag1) {
            this.setBlockBounds(f, 0.0F, f2, f1, 1.5F, f3);
            super.addCollisionBoxesToList(world, x, y, z, mask, list, entity);
        }

        if (flag) {
            f2 = 0.0F;
        }

        if (flag1) {
            f3 = 1.0F;
        }

        this.setBlockBounds(f, 0.0F, f2, f1, 1.0F, f3);
    }

    /**
     * Updates the blocks bounds based on its current state. Args: world, x, y,
     * z
     */
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
        boolean flag = this.canConnectTo(world, x, y, z - 1);
        boolean flag1 = this.canConnectTo(world, x, y, z + 1);
        boolean flag2 = this.canConnectTo(world, x - 1, y, z);
        boolean flag3 = this.canConnectTo(world, x + 1, y, z);
        float f = 0.375F;
        float f1 = 0.625F;
        float f2 = 0.375F;
        float f3 = 0.625F;

        if (flag) {
            f2 = 0.0F;
        }

        if (flag1) {
            f3 = 1.0F;
        }

        if (flag2) {
            f = 0.0F;
        }

        if (flag3) {
            f1 = 1.0F;
        }

        this.setBlockBounds(f, 0.0F, f2, f1, 1.0F, f3);
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube? This determines whether
     * or not to render the shared face of two adjacent blocks and also whether
     * the player can attach torches, redstone wire, etc to this block.
     */
    public boolean isOpaqueCube() {
        return false;
    }

    /**
     * If this block doesn't render as an ordinary block it will return False
     * (examples: signs, buttons, stairs, etc)
     */
    public boolean renderAsNormalBlock() {
        return false;
    }

    public boolean getBlocksMovement(IBlockAccess blockAccess, int x, int y, int z) {
        return false;
    }

    /**
     * The type of render function that is called for this block
     */
    public int getRenderType() {
        return CommonProxy.blockFarmerLinkRenderId;
    }

    /**
     * Returns true if the specified block can be connected by a fence
     */
    public boolean canConnectTo(IBlockAccess world, int x, int y, int z) {
        Block block = world.getBlock(x, y, z);
        return /*block != this &&*/ block instanceof IConnection;
    }

//    public static boolean func_149825_a(Block block) {
//        return block == Blocks.fence || block == Blocks.nether_brick_fence;
//    }

    /**
     * Returns true if the given side of this block type should be rendered, if
     * the adjacent block is at the given coordinates. Args: blockAccess, x, y,
     * z, side
     */
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess blockAccess, int x, int y, int z, int side) {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        this.blockIcon = iconRegister.registerIcon(this.field_149827_a);
    }

    /**
     * Called upon block activation (right click on the block.)
     */
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_,float p_149727_8_, float p_149727_9_) {
       // return world.isRemote ? true : ItemLead.func_150909_a(player, world, x, y, z);
        return true;
    }

}
