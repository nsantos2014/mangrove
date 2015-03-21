package net.minecraft.mangrove.mod.thrive.autocon.autobench;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.mangrove.core.inventory.block.AbstractBlockInventory;
import net.minecraft.mangrove.mod.thrive.MGThriveBlocks;
import net.minecraft.mangrove.mod.thrive.MGThriveForge;
import net.minecraft.mangrove.mod.thrive.autocon.AbstractBlockProcess;
import net.minecraft.mangrove.mod.thrive.autocon.AbstractTileAutocon;
import net.minecraft.mangrove.mod.thrive.autocon.SearchItem;
import net.minecraft.mangrove.mod.thrive.autocon.SearchUtil;
import net.minecraft.mangrove.network.NetBus;
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

public class BlockAutobench extends AbstractBlockProcess {
	private int p_149727_2_;
	
	public BlockAutobench() {
		super("autobench");
//		this.name = ;
//		GameRegistry.registerBlock(this, name);
//		setUnlocalizedName(MGThriveForge.ID + "_" + name);
//		setCreativeTab(CreativeTabs.tabRedstone);
//		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
//		setHardness(2.0F);
//		setResistance(8.0F);
//		
//		this.setDefaultState(this.blockState.getBaseState()
//				.withProperty(FACING, EnumFacing.NORTH)
//				.withProperty(POWERED, false));
	}

//	public String getName() {
//		return name;
//	}

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
		return EnumWorldBlockLayer.SOLID;
	}

	public boolean isFullCube() {
		return true;
	}
	
	
//	@Override
//	public boolean isSideSolid(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
//		return 	worldIn.getBlockState(pos).getValue(FACING).equals(side);
//	}

	public int getRenderType() {
		return 3;
	}
	
	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		boolean canPlaceBlockAt = super.canPlaceBlockAt(worldIn, pos);
		if (canPlaceBlockAt) {
			List<SearchItem> result = SearchUtil.findAllBlockFrom(worldIn, pos, MGThriveBlocks.duct_conveyor, MGThriveBlocks.item_broker,MGThriveBlocks.autobench);

			boolean empty = result.isEmpty();
			if (!empty) {
				NetBus.notify(getName(), "Network already has a processor");
			}
			return empty;
		}
		return canPlaceBlockAt;
	}
	
	@Override
	public AbstractTileAutocon createNewTileAutocon(World worldIn, int meta) {
		return new TileEntityAutobench();
	}


	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
		super.onBlockActivated(worldIn, pos, state, playerIn, side, hitX, hitY, hitZ);
		if (playerIn.isSneaking()) {
			return false;
		}
		if (worldIn.isRemote) {
			worldIn.markBlockForUpdate(pos);
		}else{
			playerIn.openGui(MGThriveForge.instance, 0, worldIn, pos.getX(), pos.getY(), pos.getZ());

		}
		return true;
	}

	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileEntityAutobench((EnumFacing)getStateFromMeta(metadata).getValue(FACING));
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
		super.setBlockBoundsBasedOnState(worldIn, pos);
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

//	public void updateState(World worldIn, BlockPos pos) {
//		IBlockState state = worldIn.getBlockState(pos);
//		int powered = worldIn.isBlockIndirectlyGettingPowered(pos);
//
//	}
	
	
//	public IBlockState onBlockPlaced(World worldIn, BlockPos pos,
//			EnumFacing facing, float hitX, float hitY, float hitZ, int meta,
//			EntityLivingBase placer) {
//		return this
//				.getDefaultState()
//				.withProperty(FACING,
//						placer.getHorizontalFacing().getOpposite())
//				.withProperty(POWERED, worldIn.isBlockPowered(pos));
//	}
//
//	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state,
//			EntityLivingBase placer, ItemStack stack) {
//		worldIn.setBlockState(pos, state.withProperty(FACING, placer
//				.getHorizontalFacing().getOpposite()).withProperty(POWERED, worldIn.isBlockPowered(pos)), 2);
//	}
//	
//	@Override
//	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
//		super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
//		boolean flag = worldIn.isBlockPowered(pos);
//
//		if ((flag || neighborBlock.canProvidePower()) && neighborBlock != this) {
//			worldIn.setBlockState(pos,
//					state.withProperty(POWERED, Boolean.valueOf(flag)), 2);
//
//		}
//	}
//	private void setDefaultFacing(World worldIn, BlockPos pos, IBlockState state) {
//		if (!worldIn.isRemote) {
//			Block block = worldIn.getBlockState(pos.north()).getBlock();
//			Block block1 = worldIn.getBlockState(pos.south()).getBlock();
//			Block block2 = worldIn.getBlockState(pos.west()).getBlock();
//			Block block3 = worldIn.getBlockState(pos.east()).getBlock();
//			EnumFacing enumfacing = (EnumFacing) state.getValue(FACING);
//
//			if (enumfacing == EnumFacing.NORTH && block.isFullBlock()
//					&& !block1.isFullBlock()) {
//				enumfacing = EnumFacing.SOUTH;
//			} else if (enumfacing == EnumFacing.SOUTH && block1.isFullBlock()
//					&& !block.isFullBlock()) {
//				enumfacing = EnumFacing.NORTH;
//			} else if (enumfacing == EnumFacing.WEST && block2.isFullBlock()
//					&& !block3.isFullBlock()) {
//				enumfacing = EnumFacing.EAST;
//			} else if (enumfacing == EnumFacing.EAST && block3.isFullBlock()
//					&& !block2.isFullBlock()) {
//				enumfacing = EnumFacing.WEST;
//			}
//
//			worldIn.setBlockState(pos, state.withProperty(FACING, enumfacing),
//					2);
//		}
//	}
//	
//	@SideOnly(Side.CLIENT)
//	public IBlockState getStateForEntityRender(IBlockState state) {
//		return this.getDefaultState().withProperty(FACING, EnumFacing.SOUTH)
//				.withProperty(POWERED, true);
//	}
//
//	public IBlockState getStateFromMeta(int meta) {
//		EnumFacing enumfacing = EnumFacing.getFront(meta);
//
//		if (enumfacing.getAxis() == EnumFacing.Axis.Y) {
//			enumfacing = EnumFacing.NORTH;
//		}
//
//		return this.getDefaultState().withProperty(FACING, enumfacing)
//				.withProperty(POWERED, (meta & 8) == 8);
//	}
//
//	public int getMetaFromState(IBlockState state) {
//		int powered = state.getValue(POWERED).compareTo(true) == 0 ? 8 : 0;
//		int faceIndex = ((EnumFacing) state.getValue(FACING)).getIndex();
//		return powered | faceIndex;
//	}
//
//	protected BlockState createBlockState() {
//		return new BlockState(this, new IProperty[] { FACING, POWERED });
//	}
//
//	public static void setState(boolean active, World worldIn, BlockPos pos) {
//		IBlockState iblockstate = worldIn.getBlockState(pos);
//		TileEntity tileentity = worldIn.getTileEntity(pos);
//		if (tileentity != null) {
//			tileentity.validate();
//			worldIn.setTileEntity(pos, tileentity);
//		}
//	}
//
//	@SideOnly(Side.CLIENT)
//	static final class SwitchEnumFacing {
//		static final int[] FACING_LOOKUP = new int[EnumFacing.values().length];
//		private static final String __OBFID = "CL_00002111";
//
//		static {
//			try {
//				FACING_LOOKUP[EnumFacing.WEST.ordinal()] = 1;
//			} catch (NoSuchFieldError var4) {
//				;
//			}
//
//			try {
//				FACING_LOOKUP[EnumFacing.EAST.ordinal()] = 2;
//			} catch (NoSuchFieldError var3) {
//				;
//			}
//
//			try {
//				FACING_LOOKUP[EnumFacing.NORTH.ordinal()] = 3;
//			} catch (NoSuchFieldError var2) {
//				;
//			}
//
//			try {
//				FACING_LOOKUP[EnumFacing.SOUTH.ordinal()] = 4;
//			} catch (NoSuchFieldError var1) {
//				;
//			}
//		}
//	}
}
