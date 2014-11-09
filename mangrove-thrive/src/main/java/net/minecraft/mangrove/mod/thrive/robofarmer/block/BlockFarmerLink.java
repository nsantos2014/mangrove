package net.minecraft.mangrove.mod.thrive.robofarmer.block;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemLead;
import net.minecraft.mangrove.core.cs.CSPoint3i;
import net.minecraft.mangrove.core.utils.BlockUtils;
import net.minecraft.mangrove.mod.thrive.MGThriveBlocks;
import net.minecraft.mangrove.mod.thrive.proxy.CommonProxy;
import net.minecraft.mangrove.mod.thrive.robofarmer.IRobotComponent;
import net.minecraft.mangrove.mod.thrive.robofarmer.IRobotConnection;
import net.minecraft.mangrove.mod.thrive.robofarmer.IRobotControl;
import net.minecraft.mangrove.mod.thrive.robofarmer.IRobotNode;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockFarmerLink extends Block implements IRobotConnection{
    private final String field_149827_a;

    public BlockFarmerLink() {
        super(Material.wood);
        setBlockName("farmer_link");
        this.field_149827_a = "planks_oak";
        setBlockTextureName("planks_oak");
        this.setCreativeTab(CreativeTabs.tabRedstone);
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        boolean canPlaceBlock = super.canPlaceBlockAt(world, x, y, z);
        if(canPlaceBlock){
//            canPlaceBlock=false;
            final Set<CSPoint3i> controls = SystemUtils.findAllControl(world, x, y, z);
            canPlaceBlock=controls.size()<=1;
//            System.out.println("What : "+controls.size());
//            IRobotControl iControl = SystemUtils.findFirstControl(world, x, y, z, found);
//            System.out.println("What : "+iControl+" : "+found);
            
//            for (int i1 = 0; !canPlaceBlock && (i1 < 6); ++i1){
//                Block j1 = world.getBlock(x + Facing.offsetsXForSide[i1], y + Facing.offsetsYForSide[i1], z + Facing.offsetsZForSide[i1]);
//                if( j1 instanceof IRobotComponent){
//                    IRobotComponent component=(IRobotComponent)j1;
//                    Set<CSPoint3i> found=new HashSet<CSPoint3i>();
//                    SystemUtils.findControl(world, x, y, z, found)
//                    canPlaceBlock=true;
//                }
//            }
        }
        return canPlaceBlock;
        
    }
    @Override
    public void onPostBlockPlaced(World world, int x, int y, int z, int meta) {
        super.onPostBlockPlaced(world, x, y, z, meta);
        System.out.println("Block placed at :"+x+","+y+","+z+" ("+meta+")");
    }
    @Override
    public void onNeighborBlockChange(World world, int x, int y,int z, Block block) {
        super.onNeighborBlockChange(world, x, y, z, block);
        System.out.println("Block neighbour changed at :"+x+","+y+","+z+" ("+block+")");
        updateMetadata(world, x, y, z);
    }
    @Override
    public void onBlockAdded(World p_149726_1_, int p_149726_2_, int p_149726_3_, int p_149726_4_) {
        // TODO Auto-generated method stub
        super.onBlockAdded(p_149726_1_, p_149726_2_, p_149726_3_, p_149726_4_);
        updateMetadata(p_149726_1_, p_149726_2_, p_149726_3_, p_149726_4_);
    }
    @Override
    public void onBlockPreDestroy(World wordl, int x, int y, int z, int meta) {
        // TODO Auto-generated method stub
        super.onBlockPreDestroy(wordl, x, y, z, meta);
        System.out.println("Block to be destroyed at :"+x+","+y+","+z+" ("+meta+")");
    }
    
    /**
     * Adds all intersecting collision boxes to a list. (Be sure to only add
     * boxes to the list if they intersect the mask.) Parameters: World, X, Y,
     * Z, mask, list, colliding entity
     */
    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB mask, List list, Entity entity) {
        boolean connectTop=canConnectTo(world, x , y+1, z);
        boolean connectBottom=canConnectTo(world, x , y-1, z);
        
        boolean connectLeft = canConnectTo(world, x - 1, y, z);
        boolean connectRight = canConnectTo(world, x + 1, y, z);
        
        boolean connectFront = canConnectTo(world, x, y, z - 1);
        boolean connectBack = canConnectTo(world, x, y, z + 1);
        
        float minX=0.25f;
        float minY=0.25f;
        float minZ=0.25f;
        
        float maxX=0.75f;
        float maxY=0.75f;
        float maxZ=0.75f;
        
        if( connectTop){
            maxY=1.0f;
        }
        if( connectBottom){
            minY=0.0f;
        }
        if( connectRight){
            maxX=1.0f;
        }
        if( connectLeft){
            minX=0.0f;
        }
        if( connectBack){
            maxZ=1.0f;
        }
        if( connectFront){
            minZ=0.0f;
        }
        
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.625F, 1.0F);
        super.addCollisionBoxesToList(world, x, y, z, mask,list, entity);
        float f = 0.125F;
        setBlockBounds(0.0F, 0.0F, 0.0F, f, 1.0F, 1.0F);
        super.addCollisionBoxesToList(world, x, y, z, mask,list, entity);
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f);
        super.addCollisionBoxesToList(world, x, y, z, mask,list, entity);
        setBlockBounds(1.0F - f, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        super.addCollisionBoxesToList(world, x, y, z, mask,list, entity);
        setBlockBounds(0.0F, 0.0F, 1.0F - f, 1.0F, 1.0F, 1.0F);
        super.addCollisionBoxesToList(world, x, y, z, mask,list, entity);
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        
        this.setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);
        
//        boolean flag = this.canConnectTo(world, x, y, z - 1);
//        boolean flag1 = this.canConnectTo(world, x, y, z + 1);
//        boolean flag2 = this.canConnectTo(world, x - 1, y, z);
//        boolean flag3 = this.canConnectTo(world, x + 1, y, z);
//        float f = 0.375F;
//        float f1 = 0.625F;
//        float f2 = 0.375F;
//        float f3 = 0.625F;
//
//        if (flag) {
//            f2 = 0.0F;
//        }
//
//        if (flag1) {
//            f3 = 1.0F;
//        }
//
//        if (flag || flag1) {
//            this.setBlockBounds(f, 0.0F, f2, f1, 1.5F, f3);
//            super.addCollisionBoxesToList(world, x, y, z, mask, list, entity);
//        }
//
//        f2 = 0.375F;
//        f3 = 0.625F;
//
//        if (connectLeft) {
//            f = 0.0F;
//        }
//
//        if (flag3) {
//            f1 = 1.0F;
//        }
//
//        if (connectLeft || flag3 || !flag && !flag1) {
//            this.setBlockBounds(f, 0.0F, f2, f1, 1.5F, f3);
//            super.addCollisionBoxesToList(world, x, y, z, mask, list, entity);
//        }
//
//        if (flag) {
//            f2 = 0.0F;
//        }
//
//        if (flag1) {
//            f3 = 1.0F;
//        }
//
//        this.setBlockBounds(f, 0.0F, f2, f1, 1.0F, f3);
    }

    /**
     * Updates the blocks bounds based on its current state. Args: world, x, y,
     * z
     */
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
        
        boolean connectTop=canConnectTo(world, x , y+1, z);
        boolean connectBottom=canConnectTo(world, x , y-1, z);
        
        boolean connectLeft = canConnectTo(world, x - 1, y, z);
        boolean connectRight = canConnectTo(world, x + 1, y, z);
        
        boolean connectFront = canConnectTo(world, x, y, z - 1);
        boolean connectBack = canConnectTo(world, x, y, z + 1);
        
        float minX=0.25f;
        float minY=0.25f;
        float minZ=0.25f;
        
        float maxX=0.75f;
        float maxY=0.75f;
        float maxZ=0.75f;
        
        if( connectTop){
            maxY=1.0f;
        }
        if( connectBottom){
            minY=0.0f;
        }
        if( connectRight){
            maxX=1.0f;
        }
        if( connectLeft){
            minX=0.0f;
        }
        if( connectBack){
            maxZ=1.0f;
        }
        if( connectFront){
            minZ=0.0f;
        }
        this.setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);
        
//        boolean flag = this.canConnectTo(world, x, y, z - 1);
//        boolean flag1 = this.canConnectTo(world, x, y, z + 1);
//        boolean flag2 = this.canConnectTo(world, x - 1, y, z);
//        boolean flag3 = this.canConnectTo(world, x + 1, y, z);
//        float f = 0.375F;
//        float f1 = 0.625F;
//        float f2 = 0.375F;
//        float f3 = 0.625F;
//
//        if (flag) {
//            f2 = 0.0F;
//        }
//
//        if (flag1) {
//            f3 = 1.0F;
//        }
//
//        if (flag2) {
//            f = 0.0F;
//        }
//
//        if (flag3) {
//            f1 = 1.0F;
//        }
//
//        this.setBlockBounds(f, 0.0F, f2, f1, 1.0F, f3);
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
        final Block block = world.getBlock(x, y, z);
        return /*block != this &&*/ block instanceof IRobotComponent;
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

    private void updateMetadata(IBlockAccess par1World, int par2, int par3, int par4) {
        int l = par1World.getBlockMetadata(par2, par3, par4);
        int i1 = BlockUtils.getDirectionFromMetadata(l);
        boolean flag = !(((World) par1World).isBlockIndirectlyGettingPowered(par2, par3, par4));
        boolean flag1 = BlockUtils.getIsBlockNotPoweredFromMetadata(l);

        if (flag == flag1)
            return;
        ((World) par1World).setBlockMetadataWithNotify(par2, par3, par4, i1 | ((flag) ? 0 : 8), 4);
    }
    
    
//    /**
//     * Called upon block activation (right click on the block.)
//     */
//    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_,float p_149727_8_, float p_149727_9_) {
//       // return world.isRemote ? true : ItemLead.func_150909_a(player, world, x, y, z);
//        return true;
//    }

//    @Override
    @SideOnly(Side.SERVER)
    public UUID getSid(World world, int x, int y, int z) {
        
        for (int i1 = 0; (i1 < 6); ++i1){
            Block j1 = world.getBlock(x + Facing.offsetsXForSide[i1], y + Facing.offsetsYForSide[i1], z + Facing.offsetsZForSide[i1]);
            
            if( j1 instanceof IRobotComponent){
                final IRobotComponent component=(IRobotComponent)j1;
                
                
            }
        }        
        return null;
    }
}
