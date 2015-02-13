package net.minecraft.mangrove.mod.thrive.robot.block;

import java.util.List;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.mangrove.mod.thrive.MGThriveForge;
import net.minecraft.mangrove.mod.thrive.proxy.CommonProxy;
import net.minecraft.mangrove.mod.thrive.robot.IRobotComponent;
import net.minecraft.mangrove.mod.thrive.robot.IRobotConnection;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockLink extends Block implements IRobotConnection{
    private String name;

    public BlockLink() {
        super(Material.wood);
        this.name="robot_link";
        GameRegistry.registerBlock(this, name);
        setUnlocalizedName(MGThriveForge.ID + "_" + name);
        this.setCreativeTab(CreativeTabs.tabRedstone);
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        // TODO Auto-generated method stub
        boolean canPlaceBlock = super.canPlaceBlockAt(worldIn, pos);
        if(canPlaceBlock){
          final Set<BlockPos> controls = SystemUtils.findAllControl(worldIn, pos);
          canPlaceBlock=controls.size()<=1;
      }
      return canPlaceBlock;
    }
//    @Override
//    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
//        boolean canPlaceBlock = super.canPlaceBlockAt(world, x, y, z);
//        if(canPlaceBlock){
////            canPlaceBlock=false;
//            final Set<CSPoint3i> controls = SystemUtils.findAllControl(world, x, y, z);
//            canPlaceBlock=controls.size()<=1;
////            System.out.println("What : "+controls.size());
////            IRobotControl iControl = SystemUtils.findFirstControl(world, x, y, z, found);
////            System.out.println("What : "+iControl+" : "+found);
//            
////            for (int i1 = 0; !canPlaceBlock && (i1 < 6); ++i1){
////                Block j1 = world.getBlock(x + Facing.offsetsXForSide[i1], y + Facing.offsetsYForSide[i1], z + Facing.offsetsZForSide[i1]);
////                if( j1 instanceof IRobotComponent){
////                    IRobotComponent component=(IRobotComponent)j1;
////                    Set<CSPoint3i> found=new HashSet<CSPoint3i>();
////                    SystemUtils.findControl(world, x, y, z, found)
////                    canPlaceBlock=true;
////                }
////            }
//        }
//        return canPlaceBlock;
//        
//    }
    
    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        // TODO Auto-generated method stub
        super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
        updateMetadata(worldIn, pos, state);
    }
    
//    @Override
//    public void onNeighborBlockChange(World world, int x, int y,int z, Block block) {
//        super.onNeighborBlockChange(world, x, y, z, block);
//        System.out.println("Block neighbour changed at :"+x+","+y+","+z+" ("+block+")");
//        updateMetadata(world, x, y, z);
//    }
    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        // TODO Auto-generated method stub
        super.onBlockAdded(worldIn, pos, state);
        updateMetadata(worldIn, pos, state);
    }
//    @Override
//    public void onBlockAdded(World p_149726_1_, int p_149726_2_, int p_149726_3_, int p_149726_4_) {
//        // TODO Auto-generated method stub
//        super.onBlockAdded(p_149726_1_, p_149726_2_, p_149726_3_, p_149726_4_);
//        updateMetadata(p_149726_1_, p_149726_2_, p_149726_3_, p_149726_4_);
//    }    
     
    /**
     * Adds all intersecting collision boxes to a list. (Be sure to only add
     * boxes to the list if they intersect the mask.) Parameters: World, X, Y,
     * Z, mask, list, colliding entity
     */
    public void addCollisionBoxesToList(World world, BlockPos blockPos, IBlockState iBlockState, AxisAlignedBB mask, List list, Entity entity) {
        boolean connectTop=canConnectTo(world, blockPos.up());
        boolean connectBottom=canConnectTo(world, blockPos.down());
        
        boolean connectLeft = canConnectTo(world, blockPos.west() );
        boolean connectRight = canConnectTo(world, blockPos.east());
        
        boolean connectFront = canConnectTo(world, blockPos.north());
        boolean connectBack = canConnectTo(world, blockPos.south());
        
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
        super.addCollisionBoxesToList(world, blockPos,iBlockState, mask,list, entity);
        float f = 0.125F;
        setBlockBounds(0.0F, 0.0F, 0.0F, f, 1.0F, 1.0F);
        super.addCollisionBoxesToList(world, blockPos,iBlockState, mask,list, entity);
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f);
        super.addCollisionBoxesToList(world, blockPos,iBlockState, mask,list, entity);
        setBlockBounds(1.0F - f, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        super.addCollisionBoxesToList(world, blockPos,iBlockState, mask,list, entity);
        setBlockBounds(0.0F, 0.0F, 1.0F - f, 1.0F, 1.0F, 1.0F);
        super.addCollisionBoxesToList(world, blockPos,iBlockState, mask,list, entity);
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
    public void setBlockBoundsBasedOnState(IBlockAccess world, BlockPos blockPos) {
        
        boolean connectTop=canConnectTo(world, blockPos.up());
        boolean connectBottom=canConnectTo(world, blockPos.down());
        
        boolean connectLeft = canConnectTo(world, blockPos.west() );
        boolean connectRight = canConnectTo(world, blockPos.east());
        
        boolean connectFront = canConnectTo(world, blockPos.north());
        boolean connectBack = canConnectTo(world, blockPos.south());
        
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
        return 3;
    }

    /**
     * Returns true if the specified block can be connected by a fence
     */
    public boolean canConnectTo(IBlockAccess world, BlockPos blockPos) {
        final Block block = world.getBlockState(blockPos).getBlock();
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

//    @SideOnly(Side.CLIENT)
//    public void registerBlockIcons(IIconRegister iconRegister) {
//        this.blockIcon = iconRegister.registerIcon(this.field_149827_a);
//    }
    
    @Override
    public void updateNetwork(IBlockAccess world, BlockPos blockPos) {
        updateMetadata((World) world, blockPos,world.getBlockState(blockPos));        
    }

//    private void updateMetadata(IBlockAccess world, int x, int y, int z) {
//        int l = world.getBlockMetadata(x, y, z);
//        int i1 = BlockUtils.getDirectionFromMetadata(l);
////        boolean flag = !(((World) par1World).isBlockIndirectlyGettingPowered(par2, par3, par4));
//        boolean flag1 = BlockUtils.getIsBlockNotPoweredFromMetadata(l);
//
////        if (flag == flag1)
////            return;
////        ((World) par1World).setBlockMetadataWithNotify(par2, par3, par4, i1 | ((flag) ? 0 : 8), 2);
//        CSPoint3i controlPos = SystemUtils.findFirstControl((World) world, x, y, z);
//        boolean flag = false;
//        if(controlPos==null && flag1){
//            System.out.println("Update Metadata: No Control and "+flag1);
//            return;
//        }else if(controlPos!=null){
//            flag = !(((World) world).isBlockIndirectlyGettingPowered(controlPos.x, controlPos.y, controlPos.z));
//        }
////        boolean flag = !(((World) world).isBlockIndirectlyGettingPowered(controlPos.x, controlPos.y, controlPos.z));
//        if (flag == flag1){
//            System.out.println("Update Metadata: Same Value "+flag1);
//            return;
//        }
//        System.out.println("Update Metadata:"+flag);
//        ((World) world).setBlockMetadataWithNotify(x, y, z, i1 | ((flag) ? 0 : 8), 2);
//    }

    
    public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int side){
//        final Set<CSPoint3i> controls = SystemUtils.findAllControl(world, x, y, z);
//        if( controls.size()==1){
//            CSPoint3i point = controls.iterator().next();
//            return (world.getBlockMetadata(point.x, point.y, point.z) & 8) > 0 ? 15 : 0;    
//        }
        return 0;
        
    }

    public int isProvidingStrongPower(IBlockAccess p_149748_1_, int p_149748_2_, int p_149748_3_, int p_149748_4_, int p_149748_5_)
    {
//        int i1 = p_149748_1_.getBlockMetadata(p_149748_2_, p_149748_3_, p_149748_4_);
//
//        if ((i1 & 8) == 0)
//        {
//            return 0;
//        }
//        else
//        {
//            int j1 = i1 & 7;
//            return j1 == 0 && p_149748_5_ == 0 ? 15 : (j1 == 7 && p_149748_5_ == 0 ? 15 : (j1 == 6 && p_149748_5_ == 1 ? 15 : (j1 == 5 && p_149748_5_ == 1 ? 15 : (j1 == 4 && p_149748_5_ == 2 ? 15 : (j1 == 3 && p_149748_5_ == 3 ? 15 : (j1 == 2 && p_149748_5_ == 4 ? 15 : (j1 == 1 && p_149748_5_ == 5 ? 15 : 0)))))));
//        }
        return 0;
    }

    /**
     * Can this block provide power. Only wire currently seems to have this change based on its state.
     */
    public boolean canProvidePower()
    {
        return false;
    }
    
    private void updateMetadata(World world, BlockPos blockPos, IBlockState iBlockState) {
        // int l = world.getBlockMetadata(x, y, z);
        // int i1 = BlockUtils.getDirectionFromMetadata(l);
        // boolean flag1 = BlockUtils.getIsBlockNotPoweredFromMetadata(l);
        // //boolean flag = !(((World)
        // par1World).isBlockIndirectlyGettingPowered(x, y, z));
        //
        // CSPoint3i controlPos = SystemUtils.findFirstControl((World) world, x,
        // y, z);
        // boolean flag = false;
        // if(controlPos==null && flag1){
        // System.out.println("Update Metadata: No Control and "+flag1);
        // return;
        // }else if(controlPos!=null){
        // flag = !(((World)
        // world).isBlockIndirectlyGettingPowered(controlPos.x, controlPos.y,
        // controlPos.z));
        // }
        // if (flag == flag1){
        // System.out.println("Update Metadata: Same Value "+flag1);
        // return;
        // }
        // System.out.println("Update Metadata:"+flag);
        // ((World) world).setBlockMetadataWithNotify(x, y, z, i1 | ((flag) ? 0
        // : 8), 2);
        world.setBlockState(blockPos, iBlockState, 2);
    }
}
