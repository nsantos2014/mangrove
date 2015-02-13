package net.minecraft.mangrove.mod.thrive.autobench;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.mangrove.core.inventory.block.AbstractBlockInventory;
import net.minecraft.mangrove.mod.thrive.MGThriveForge;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockAutobench extends AbstractBlockInventory {

	// @SideOnly(Side.CLIENT)
	// private IIcon blockIconTop;
	// @SideOnly(Side.CLIENT)
	// private IIcon blockIconFront;
	private int p_149727_2_;
	private String name;

	public BlockAutobench() {
		super(Material.wood);
		this.name = "autobench";
		GameRegistry.registerBlock(this, name);
		setUnlocalizedName(MGThriveForge.ID + "_" + name);
		setCreativeTab(CreativeTabs.tabRedstone);
		// setBlockName("autobench");
		// setBlockTextureName("crafting_table");
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		setHardness(2.0F);
		setResistance(8.0F);
	}

	public String getName() {
		return name;
	}

	/**
	 * Is this block (a) opaque and (b) a full 1m cube? This determines whether
	 * or not to render the shared face of two adjacent blocks and also whether
	 * the player can attach torches, redstone wire, etc to this block.
	 */
	public boolean isOpaqueCube() {
		return false;
	}

	@SideOnly(Side.CLIENT)
	public EnumWorldBlockLayer getBlockLayer() {
		return EnumWorldBlockLayer.CUTOUT;
	}

	public boolean isFullCube() {
		return false;
	}

	public int getRenderType() {
		return 3;
	}

	// /**
	// * Gets the block's texture. Args: side, meta
	// */
	// @SideOnly(Side.CLIENT)
	// public IIcon getIcon(int side, int meta){
	// return side == 1 ? this.blockIconTop : (side == 0 ?
	// Blocks.planks.getBlockTextureFromSide(side) : (side != 2 && side != 4 ?
	// this.blockIcon : this.blockIconFront));
	// }
	//
	// @SideOnly(Side.CLIENT)
	// public void registerBlockIcons(IIconRegister register){
	// this.blockIcon = register.registerIcon(this.getTextureName() + "_side");
	// this.blockIconTop = register.registerIcon(this.getTextureName() +
	// "_top");
	// this.blockIconFront = register.registerIcon(this.getTextureName() +
	// "_front");
	// }

	@Override
	public boolean canPlaceBlockAt(World world, BlockPos blockPos) {
		return super.canPlaceBlockAt(world, blockPos);
	}

	@Override
	public void onBlockClicked(World world, BlockPos blockPos, EntityPlayer player) {

		super.onBlockClicked(world, blockPos, player);
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
		super.onBlockActivated(worldIn, pos, state, playerIn, side, hitX, hitY, hitZ);
		if (playerIn.isSneaking()) {
			return false;
		}
		if (!worldIn.isRemote) {
			// TileEntity entity = world.getTileEntity(x, y, z);
			playerIn.openGui(MGThriveForge.instance, 0, worldIn, pos.getX(), pos.getY(), pos.getZ());

		}
		return true;
	}

	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileEntityAutobench();
	}

	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
		return true;
	}

	@Override
	public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
		super.onNeighborChange(world, pos, neighbor);
		updateState((World) world, pos);
	}

	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
		super.onBlockAdded(worldIn, pos, state);
		updateState(worldIn, pos);
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
		// super.setBlockBoundsBasedOnState(worldIn, pos);
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

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

	public void updateState(World worldIn, BlockPos pos) {
		IBlockState state = worldIn.getBlockState(pos);
		int powered = worldIn.isBlockIndirectlyGettingPowered(pos);

	}
	// private void updateMetadata(IBlockAccess World, int par2, int par3, int
	// par4) {
	// int l = par1World.getBlockMetadata(par2, par3, par4);
	// int i1 = BlockUtils.getDirectionFromMetadata(l);
	// boolean flag = !(((World)
	// par1World).isBlockIndirectlyGettingPowered(par2, par3, par4));
	// boolean flag1 = BlockUtils.getIsBlockNotPoweredFromMetadata(l);
	//
	// if (flag == flag1)
	// return;
	// ((World) par1World).setBlockMetadataWithNotify(par2, par3, par4, i1
	// | ((flag) ? 0 : 8), 4);
	// }

}
