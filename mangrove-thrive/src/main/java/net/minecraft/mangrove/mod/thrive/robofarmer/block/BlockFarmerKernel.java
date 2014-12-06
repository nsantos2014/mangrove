package net.minecraft.mangrove.mod.thrive.robofarmer.block;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.mangrove.core.block.AbstractBlockInventory;
import net.minecraft.mangrove.core.cs.CSPoint3i;
import net.minecraft.mangrove.core.inventory.ITransactor;
import net.minecraft.mangrove.core.inventory.TransactorSimple;
import net.minecraft.mangrove.core.utils.BlockUtils;
import net.minecraft.mangrove.mod.thrive.MGThriveForge;
import net.minecraft.mangrove.mod.thrive.proxy.CommonProxy;
import net.minecraft.mangrove.mod.thrive.robofarmer.IRobotComponent;
import net.minecraft.mangrove.mod.thrive.robofarmer.IRobotControl;
import net.minecraft.mangrove.mod.thrive.robofarmer.entity.TileFarmerKernel;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockFarmerKernel extends AbstractBlockInventory implements IRobotControl {

    @SideOnly(Side.CLIENT)
    private IIcon blockIconTop;
    @SideOnly(Side.CLIENT)
    private IIcon blockIconFront;
    private int p_149727_2_;

    public BlockFarmerKernel() {
        super(Material.wood);
        setCreativeTab(CreativeTabs.tabRedstone);
        setBlockName("farmer_kernel");
        setBlockTextureName("crafting_table");
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
        return CommonProxy.blockFarmerKernelRenderId;
    }
    /**
     * Is this block (a) opaque and (b) a full 1m cube? This determines whether
     * or not to render the shared face of two adjacent blocks and also whether
     * the player can attach torches, redstone wire, etc to this block.
     */
    public boolean isOpaqueCube() {
        return false;
    }
    @Override
    public void onPostBlockPlaced(World world, int x, int y, int z, int meta) {
        super.onPostBlockPlaced(world, x, y, z, meta);
        System.out.println("Block placed at :" + x + "," + y + "," + z + " (" + meta + ")");
        updateMetadata(world, x, y, z);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
        super.onNeighborBlockChange(world, x, y, z, block);
        if (block.canProvidePower()) {
            final TileEntity tile = world.getTileEntity(x, y, z);
            if (tile instanceof TileFarmerKernel) {
                final TileFarmerKernel tileFarmer = (TileFarmerKernel) tile;
                tileFarmer.handlePower();
                // System.out.println("Block neighbour changed at :"+x+","+y+","+z+" ("+block+")");
            }
        }
        updateMetadata(world, x, y, z);
    }

    @Override
    public void onBlockPreDestroy(World wordl, int x, int y, int z, int meta) {
        // TODO Auto-generated method stub
        super.onBlockPreDestroy(wordl, x, y, z, meta);
        System.out.println("Block to be destroyed at :" + x + "," + y + "," + z + " (" + meta + ")");
    }

    /**
     * Gets the block's texture. Args: side, meta
     */
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return side == 1 ? this.blockIconTop : (side == 0 ? Blocks.planks.getBlockTextureFromSide(side) : (side != 2 && side != 4 ? this.blockIcon : this.blockIconFront));
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register) {
        this.blockIcon = register.registerIcon("planks_birch");
        this.blockIconTop = register.registerIcon("piston_top_normal");
        this.blockIconFront = register.registerIcon("planks_birch");
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        boolean canPlaceBlock = super.canPlaceBlockAt(world, x, y, z);
        if (canPlaceBlock) {
            final Set<CSPoint3i> controls = SystemUtils.findAllControl(world, x, y, z);
            canPlaceBlock = controls.isEmpty();
        }
        return canPlaceBlock;
    }

    @Override
    public void onBlockClicked(World world, int x,
            int y, int z, EntityPlayer player) {

        super.onBlockClicked(world, x, y, z, player);
    }

    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
        super.onBlockActivated(world, x, y, z, player, p_149727_6_, p_149727_7_, p_149727_8_, p_149727_9_);
        if (player.isSneaking()) {
            return false;
        }
        if (!world.isRemote) {
            player.openGui(MGThriveForge.instance, 0, world, x, y, z);
        }
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileFarmerKernel();
    }

//    @SideOnly(Side.CLIENT)
//    public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2,
//            int par3, int par4, int par5) {
//        return true;
//    }

    public void onNeighborChange(IBlockAccess world, int x, int y, int z,
            int tileX, int tileY, int tileZ) {
        updateMetadata(world, x, y, z);
    }

    private void updateMetadata(IBlockAccess par1World, int par2, int par3, int par4) {
        int l = par1World.getBlockMetadata(par2, par3, par4);
        int i1 = BlockUtils.getDirectionFromMetadata(l);
        boolean flag = !(((World) par1World).isBlockIndirectlyGettingPowered(par2, par3, par4));
        boolean flag1 = BlockUtils.getIsBlockNotPoweredFromMetadata(l);
        System.out.println("Update metadata : "+flag+":"+flag1+":"+l);
        if (flag == flag1)
            return;
        ((World) par1World).setBlockMetadataWithNotify(par2, par3, par4, i1 | ((flag) ? 0 : 8), 2);
        SystemUtils.updateNetwork(((World) par1World), par2, par3, par4);
    }

    public void onBlockAdded(World par1World, int par2, int par3, int par4) {
        super.onBlockAdded(par1World, par2, par3, par4);
        updateMetadata(par1World, par2, par3, par4);
        
    }

    public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2,
            int par3, int par4) {
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    public void addCollisionBoxesToList(World par1World, int par2, int par3, int par4,
            AxisAlignedBB par5AxisAlignedBB, List par6List, Entity par7Entity) {
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.625F, 1.0F);
        super.addCollisionBoxesToList(par1World, par2, par3, par4, par5AxisAlignedBB,
                par6List, par7Entity);
        float f = 0.125F;
        setBlockBounds(0.0F, 0.0F, 0.0F, f, 1.0F, 1.0F);
        super.addCollisionBoxesToList(par1World, par2, par3, par4, par5AxisAlignedBB,
                par6List, par7Entity);
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f);
        super.addCollisionBoxesToList(par1World, par2, par3, par4, par5AxisAlignedBB,
                par6List, par7Entity);
        setBlockBounds(1.0F - f, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        super.addCollisionBoxesToList(par1World, par2, par3, par4, par5AxisAlignedBB,
                par6List, par7Entity);
        setBlockBounds(0.0F, 0.0F, 1.0F - f, 1.0F, 1.0F, 1.0F);
        super.addCollisionBoxesToList(par1World, par2, par3, par4, par5AxisAlignedBB,
                par6List, par7Entity);
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public boolean isPowered(World world, int x, int y, int z) {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof TileFarmerKernel) {
            // TileFarmerKernel tileKernel = (TileFarmerKernel)tile;
            return world.isBlockIndirectlyGettingPowered(x, y, z);
            // return tileKernel.
        }
        return false;
    }

    @Override
    public boolean isPowered(World world, CSPoint3i point) {
        return isPowered(world, point.x, point.y, point.z);
    }
    
    @Override
    public ITransactor getTransactor(World world, CSPoint3i point) {
        return getTransactor(world, point.x, point.y, point.z);
    }
    
    @Override
    public ITransactor getTransactor(World world, int x, int y, int z) {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof TileFarmerKernel) {
            return TransactorSimple.getTransactorFor((TileFarmerKernel)tile);
        }
        return null;
    }
      
    @Override
    public void updateNetwork(IBlockAccess world, int x, int y, int z) {
     
    }
}
