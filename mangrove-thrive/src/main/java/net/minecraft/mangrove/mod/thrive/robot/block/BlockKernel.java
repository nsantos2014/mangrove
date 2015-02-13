package net.minecraft.mangrove.mod.thrive.robot.block;

import java.util.List;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.mangrove.core.inventory.block.AbstractBlockInventory;
import net.minecraft.mangrove.core.inventory.transactor.ITransactor;
import net.minecraft.mangrove.core.inventory.transactor.TransactorSimple;
import net.minecraft.mangrove.mod.thrive.MGThriveForge;
import net.minecraft.mangrove.mod.thrive.proxy.CommonProxy;
import net.minecraft.mangrove.mod.thrive.robot.IRobotControl;
import net.minecraft.mangrove.mod.thrive.robot.entity.TileRobotKernel;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockKernel extends AbstractBlockInventory implements IRobotControl {
    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

    private int p_149727_2_;
    private String name;

    public BlockKernel() {
        super(Material.wood);
        this.name = "robot_kernel";
        GameRegistry.registerBlock(this, name);
        setUnlocalizedName(MGThriveForge.ID + "_" + name);
        setCreativeTab(CreativeTabs.tabRedstone);
        // setBlockName("robot_kernel");
        // setBlockTextureName("crafting_table");
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        setHardness(2.0F);
        setResistance(8.0F);
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

    // @Override
    // public void onPostBlockPlaced(World world, int x, int y, int z, int meta)
    // {
    // super.onPostBlockPlaced(world, x, y, z, meta);
    // System.out.println("Block placed at :" + x + "," + y + "," + z + " (" +
    // meta + ")");
    // updateMetadata(world, x, y, z);
    // }

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
        if (neighborBlock.canProvidePower()) {
            final TileEntity tile = worldIn.getTileEntity(pos);
            if (tile instanceof TileRobotKernel) {
                final TileRobotKernel tileFarmer = (TileRobotKernel) tile;
                tileFarmer.handlePower();
                // System.out.println("Block neighbour changed at :"+x+","+y+","+z+" ("+block+")");
            }
        }
        updateMetadata(worldIn, pos, state);
    }

    // @Override
    // public void onNeighborBlockChange(World world, int x, int y, int z, Block
    // block) {
    // super.onNeighborBlockChange(world, x, y, z, block);
    // if (block.canProvidePower()) {
    // final TileEntity tile = world.getTileEntity(x, y, z);
    // if (tile instanceof TileRobotKernel) {
    // final TileRobotKernel tileFarmer = (TileRobotKernel) tile;
    // tileFarmer.handlePower();
    // //
    // System.out.println("Block neighbour changed at :"+x+","+y+","+z+" ("+block+")");
    // }
    // }
    // updateMetadata(world, x, y, z);
    // }

    // @Override
    // public void onBlockPreDestroy(World wordl, int x, int y, int z, int meta)
    // {
    // // TODO Auto-generated method stub
    // super.onBlockPreDestroy(wordl, x, y, z, meta);
    // System.out.println("Block to be destroyed at :" + x + "," + y + "," + z +
    // " (" + meta + ")");
    // }

    // /**
    // * Gets the block's texture. Args: side, meta
    // */
    // @SideOnly(Side.CLIENT)
    // public IIcon getIcon(int side, int meta) {
    // return side == 1 ? this.blockIconTop : (side == 0 ?
    // Blocks.planks.getBlockTextureFromSide(side) : (side != 2 && side != 4 ?
    // this.blockIcon : this.blockIconFront));
    // }
    //
    // @SideOnly(Side.CLIENT)
    // public void registerBlockIcons(IIconRegister register) {
    // this.blockIcon = register.registerIcon("planks_birch");
    // this.blockIconTop = register.registerIcon("piston_top_normal");
    // this.blockIconFront = register.registerIcon("planks_birch");
    // }
    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        boolean canPlaceBlock = super.canPlaceBlockAt(worldIn, pos);
        if (canPlaceBlock) {
            final Set<BlockPos> controls = SystemUtils.findAllControl(worldIn, pos);
            canPlaceBlock = controls.isEmpty();
        }
        return canPlaceBlock;
    }

    // @Override
    // public boolean canPlaceBlockAt(World world, int x, int y, int z) {
    // boolean canPlaceBlock = super.canPlaceBlockAt(world, x, y, z);
    // if (canPlaceBlock) {
    // final Set<CSPoint3i> controls = SystemUtils.findAllControl(world, x, y,
    // z);
    // canPlaceBlock = controls.isEmpty();
    // }
    // return canPlaceBlock;
    // }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        super.onBlockActivated(worldIn, pos, state, playerIn, side, hitX, hitY, hitZ);
        if (playerIn.isSneaking()) {
            return false;
        }
        if (!worldIn.isRemote) {
            playerIn.openGui(MGThriveForge.instance, 0, worldIn, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }

    // public boolean onBlockActivated(World world, int x, int y, int z,
    // EntityPlayer player, int p_149727_6_, float p_149727_7_, float
    // p_149727_8_, float p_149727_9_) {
    // super.onBlockActivated(world, x, y, z, player, p_149727_6_, p_149727_7_,
    // p_149727_8_, p_149727_9_);
    // if (player.isSneaking()) {
    // return false;
    // }
    // if (!world.isRemote) {
    // player.openGui(MGThriveForge.instance, 0, world, x, y, z);
    // }
    // return true;
    // }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileRobotKernel();
    }

    // @SideOnly(Side.CLIENT)
    // public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int
    // par2,
    // int par3, int par4, int par5) {
    // return true;
    // }
    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
        super.onNeighborChange(world, pos, neighbor);
        updateMetadata((World) world, pos, world.getBlockState(pos));
    }

    // public void onNeighborChange(IBlockAccess world, int x, int y, int z,
    // int tileX, int tileY, int tileZ) {
    // updateMetadata(world, x, y, z);
    // }

    // private void updateMetadata(IBlockAccess par1World, int par2, int par3,
    // int par4) {
    // int l = par1World.getBlockMetadata(par2, par3, par4);
    // int i1 = BlockUtils.getDirectionFromMetadata(l);
    // boolean flag = !(((World)
    // par1World).isBlockIndirectlyGettingPowered(par2, par3, par4));
    // boolean flag1 = BlockUtils.getIsBlockNotPoweredFromMetadata(l);
    // System.out.println("Update metadata : "+flag+":"+flag1+":"+l);
    // if (flag == flag1)
    // return;
    // ((World) par1World).setBlockMetadataWithNotify(par2, par3, par4, i1 |
    // ((flag) ? 0 : 8), 2);
    // SystemUtils.updateNetwork(((World) par1World), par2, par3, par4);
    // }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        super.onBlockAdded(worldIn, pos, state);
        updateMetadata(worldIn, pos, state);
    }

    // public void onBlockAdded(World par1World, int par2, int par3, int par4) {
    // super.onBlockAdded(par1World, par2, par3, par4);
    // updateMetadata(par1World, par2, par3, par4);
    //
    // }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    // public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int
    // par2,
    // int par3, int par4) {
    // setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    // }
    @Override
    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List list, Entity collidingEntity) {
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.625F, 1.0F);
        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        float f = 0.125F;
        setBlockBounds(0.0F, 0.0F, 0.0F, f, 1.0F, 1.0F);
        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f);
        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        setBlockBounds(1.0F - f, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        setBlockBounds(0.0F, 0.0F, 1.0F - f, 1.0F, 1.0F, 1.0F);
        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    // public void addCollisionBoxesToList(World par1World, int par2, int par3,
    // int par4,
    // AxisAlignedBB par5AxisAlignedBB, List par6List, Entity par7Entity) {
    // setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.625F, 1.0F);
    // super.addCollisionBoxesToList(par1World, par2, par3, par4,
    // par5AxisAlignedBB,
    // par6List, par7Entity);
    // float f = 0.125F;
    // setBlockBounds(0.0F, 0.0F, 0.0F, f, 1.0F, 1.0F);
    // super.addCollisionBoxesToList(par1World, par2, par3, par4,
    // par5AxisAlignedBB,
    // par6List, par7Entity);
    // setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f);
    // super.addCollisionBoxesToList(par1World, par2, par3, par4,
    // par5AxisAlignedBB,
    // par6List, par7Entity);
    // setBlockBounds(1.0F - f, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    // super.addCollisionBoxesToList(par1World, par2, par3, par4,
    // par5AxisAlignedBB,
    // par6List, par7Entity);
    // setBlockBounds(0.0F, 0.0F, 1.0F - f, 1.0F, 1.0F, 1.0F);
    // super.addCollisionBoxesToList(par1World, par2, par3, par4,
    // par5AxisAlignedBB,
    // par6List, par7Entity);
    // setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    // }

    @Override
    public boolean isPowered(World world, BlockPos point) {
        TileEntity tile = world.getTileEntity(point);
        if (tile instanceof TileRobotKernel) {
            return world.isBlockIndirectlyGettingPowered(point) == 15;
        }
        return false;
    }

    @Override
    public ITransactor getTransactor(World world, BlockPos blockPos) {
        TileEntity tile = world.getTileEntity(blockPos);
        if (tile instanceof TileRobotKernel) {
            return TransactorSimple.getTransactorFor((TileRobotKernel) tile);
        }
        return null;
    }

    @Override
    public void updateNetwork(IBlockAccess world, BlockPos blockPos) {

    }

    private void setDefaultFacing(World worldIn, BlockPos pos, IBlockState state) {
        if (!worldIn.isRemote) {
            Block block = worldIn.getBlockState(pos.north()).getBlock();
            Block block1 = worldIn.getBlockState(pos.south()).getBlock();
            Block block2 = worldIn.getBlockState(pos.west()).getBlock();
            Block block3 = worldIn.getBlockState(pos.east()).getBlock();
            EnumFacing enumfacing = (EnumFacing) state.getValue(FACING);

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
