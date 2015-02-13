package net.minecraft.mangrove.mod.thrive.robot.block;

import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.mangrove.mod.thrive.MGThriveForge;
import net.minecraft.mangrove.mod.thrive.proxy.CommonProxy;
import net.minecraft.mangrove.mod.thrive.robot.IRobotNode;
import net.minecraft.mangrove.mod.thrive.robot.entity.AbstractTileRobotNode;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public abstract class AbstractBlockNode extends BlockContainer implements IRobotNode{
    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    
    private boolean field_149996_a;
    private String name;

    public AbstractBlockNode(String name) {
        super(Material.wood);
        this.name = name;
        GameRegistry.registerBlock(this, name);
        setUnlocalizedName(MGThriveForge.ID + "_" + name);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        
//        setBlockTextureName("crafting_table");
        this.setCreativeTab(CreativeTabs.tabRedstone);
    }

     /**
     * If this block doesn't render as an ordinary block it will return False
     * (examples: signs, buttons, stairs, etc)
     */
    public boolean renderAsNormalBlock() {
        return false;        
    }
    
    public int getRenderType() {
        return 3;
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
     * Updates the blocks bounds based on its current state. Args: world, x, y,
     * z
     */
    public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int x,int y, int z) {
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }
    
    @Override
    public void onBlockAdded(World world, BlockPos blockPos,IBlockState iBlockState) {
//      System.out.println("BlockAdded:"+world);
        super.onBlockAdded(world, blockPos,iBlockState);
        this.setDefaultFacing(world, blockPos,iBlockState);
        updateMetadata(world, blockPos,iBlockState);
    }
    
    @Override
    public boolean canPlaceBlockAt(World world, BlockPos blockPos) {
        boolean canPlaceBlock = super.canPlaceBlockAt(world, blockPos);
        if(canPlaceBlock){
            final Set<BlockPos> controls = SystemUtils.findAllControl(world, blockPos);
            canPlaceBlock=controls.size()<=1;
            if( canPlaceBlock){
                canPlaceBlock=SystemUtils.checkAllNodes(getClass(),world, blockPos);
            }
//            canPlaceBlock=false;
//            for (int i1 = 0; !canPlaceBlock && (i1 < 6); ++i1){
//                Block j1 = world.getBlock(x + Facing.offsetsXForSide[i1], y + Facing.offsetsYForSide[i1], z + Facing.offsetsZForSide[i1]);
//                if( j1 instanceof IRobotComponent){
//                    IRobotComponent component=(IRobotComponent)j1;
//                    
//                    canPlaceBlock=true;
//                }
//            }
        }
        return canPlaceBlock;
    }
    @Override
        public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());            
        }
    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        
    }
//    @Override
//    public void onBlockPlacedBy(World world, BlockPos blockPos,
//            EntityLivingBase player, ItemStack iStack) {
//
//        int playerOrientation = MathHelper.floor_double((double) (player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
//        ForgeDirection playerOr=ForgeDirection.UNKNOWN;
//        switch (playerOrientation) {
//        case 0:
//            playerOr=ForgeDirection.SOUTH.getOpposite();
////          world.setBlockMetadataWithNotify(x, y, z, 2, 2);
//            world.setBlockMetadataWithNotify(x, y, z, 3, 2);
//            break;
//        case 1:
//            playerOr=ForgeDirection.WEST.getOpposite();
////          world.setBlockMetadataWithNotify(x, y, z, 5, 2);
//            world.setBlockMetadataWithNotify(x, y, z, 4, 2);
//            break;
//        case 2:
//            playerOr=ForgeDirection.NORTH.getOpposite();
////          world.setBlockMetadataWithNotify(x, y, z, 3, 2);
//            world.setBlockMetadataWithNotify(x, y, z, 2, 2);
//            break;
//        case 3:
//            playerOr=ForgeDirection.EAST.getOpposite();
////          world.setBlockMetadataWithNotify(x, y, z, 4, 2);
//            world.setBlockMetadataWithNotify(x, y, z, 5, 2);
//            break;  
//        }
////      System.out.println("Direction :"+playerOrientation+":"+ForgeDirection.getOrientation(playerOrientation)+":"+playerOr+":"+playerOr.ordinal());
//        
//    }
    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
        if(neighborBlock.canProvidePower()){
            final TileEntity tile = worldIn.getTileEntity(pos);
            if (tile instanceof AbstractTileRobotNode) {
                final AbstractTileRobotNode tileFarmer = (AbstractTileRobotNode) tile;            
                if( worldIn.getStrongPower(pos)>0){
                    tileFarmer.doStart();
                }else{
                    tileFarmer.doStop();
                }
                //System.out.println("Block neighbour changed at :"+x+","+y+","+z+" ("+block+")");
            }
        }
        updateMetadata(worldIn, pos, worldIn.getBlockState(pos));
    }
   
    
    @Override
    public void updateNetwork(IBlockAccess world, BlockPos blockPos) {
//        System.out.println("Update network ("+x+","+y+","+z+") "+this);
        updateMetadata((World) world, blockPos,world.getBlockState(blockPos));
    }
    
    private void setDefaultFacing(World worldIn, BlockPos pos, IBlockState state){
        if (!worldIn.isRemote){
            Block block = worldIn.getBlockState(pos.north()).getBlock();
            Block block1 = worldIn.getBlockState(pos.south()).getBlock();
            Block block2 = worldIn.getBlockState(pos.west()).getBlock();
            Block block3 = worldIn.getBlockState(pos.east()).getBlock();
            EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);

            if (enumfacing == EnumFacing.NORTH && block.isFullBlock() && !block1.isFullBlock()) {
                enumfacing = EnumFacing.SOUTH;
            } else if (enumfacing == EnumFacing.SOUTH && block1.isFullBlock() && !block.isFullBlock()) {
                enumfacing = EnumFacing.NORTH;
            } else if (enumfacing == EnumFacing.WEST && block2.isFullBlock() && !block3.isFullBlock()) {
                enumfacing = EnumFacing.EAST;
            } else if (enumfacing == EnumFacing.EAST && block3.isFullBlock() && !block2.isFullBlock()) {
                enumfacing = EnumFacing.WEST;
            }

            worldIn.setBlockState(pos, state.withProperty(FACING, enumfacing), 2);
        }
    }
    private void updateMetadata(World world, BlockPos blockPos,IBlockState iBlockState) {
//        int l = world.getBlockMetadata(x, y, z);
//        int i1 = BlockUtils.getDirectionFromMetadata(l);
//        boolean flag1 = BlockUtils.getIsBlockNotPoweredFromMetadata(l);
//        //boolean flag = !(((World) par1World).isBlockIndirectlyGettingPowered(x, y, z));
//        
//        CSPoint3i controlPos = SystemUtils.findFirstControl((World) world, x, y, z);
//        boolean flag = false;
//        if(controlPos==null && flag1){
//            System.out.println("Update Metadata: No Control and "+flag1);
//            return;
//        }else if(controlPos!=null){
//            flag = !(((World) world).isBlockIndirectlyGettingPowered(controlPos.x, controlPos.y, controlPos.z));
//        }
//        if (flag == flag1){
//            System.out.println("Update Metadata: Same Value "+flag1);
//            return;
//        }
//        System.out.println("Update Metadata:"+flag);
//        ((World) world).setBlockMetadataWithNotify(x, y, z, i1 | ((flag) ? 0 : 8), 2);
        world.setBlockState(blockPos, iBlockState, 2);
    }
      
//
//    @SideOnly(Side.CLIENT)
//    @Override
//    public void registerBlockIcons(IIconRegister register) {
//        this.blockIcon = register.registerIcon("planks_birch");
//        this.blockIconTop = register.registerIcon("piston_top_normal");
//        this.blockIconFront = register.registerIcon("log_birch_top");
//    }
//
//    @SideOnly(Side.CLIENT)
//    public IIcon getIcon(int side, int meta) {
//        return this.blockIcon;
//    }
    
    public IBlockState getStateFromMeta(int meta){
        EnumFacing enumfacing = EnumFacing.getFront(meta);

        if (enumfacing.getAxis() == EnumFacing.Axis.Y){
            enumfacing = EnumFacing.NORTH;
        }

        return this.getDefaultState().withProperty(FACING, enumfacing);
    }

    public int getMetaFromState(IBlockState state){
        return ((EnumFacing)state.getValue(FACING)).getIndex();
    }

    protected BlockState createBlockState(){
        return new BlockState(this, new IProperty[] {FACING});
    }
}
