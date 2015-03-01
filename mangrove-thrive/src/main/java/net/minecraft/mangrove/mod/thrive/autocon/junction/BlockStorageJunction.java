package net.minecraft.mangrove.mod.thrive.autocon.junction;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.mangrove.mod.thrive.MGThriveBlocks;
import net.minecraft.mangrove.mod.thrive.MGThriveForge;
import net.minecraft.mangrove.mod.thrive.autocon.AbstractBlockAutocon;
import net.minecraft.mangrove.mod.thrive.autocon.AbstractTileAutocon;
import net.minecraft.mangrove.mod.thrive.autocon.SearchItem;
import net.minecraft.mangrove.mod.thrive.autocon.SearchUtil;
import net.minecraft.mangrove.mod.thrive.autocon.itembroker.TileItemBroker;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockStorageJunction extends AbstractBlockAutocon {
	public static final PropertyDirection FACING = PropertyDirection.create(
			"facing", EnumFacing.Plane.HORIZONTAL);
	public static final PropertyBool POWERED = PropertyBool.create("powered");

	public BlockStorageJunction() {
		super("storage_junction");
		this.setDefaultState(this.blockState.getBaseState()
				.withProperty(FACING, EnumFacing.NORTH)
				.withProperty(POWERED, false));
	}

	private void setDefaultFacing(World worldIn, BlockPos pos, IBlockState state) {
		if (!worldIn.isRemote) {
			Block block = worldIn.getBlockState(pos.north()).getBlock();
			Block block1 = worldIn.getBlockState(pos.south()).getBlock();
			Block block2 = worldIn.getBlockState(pos.west()).getBlock();
			Block block3 = worldIn.getBlockState(pos.east()).getBlock();
			EnumFacing enumfacing = (EnumFacing) state.getValue(FACING);

			if (enumfacing == EnumFacing.NORTH && block.isFullBlock()
					&& !block1.isFullBlock()) {
				enumfacing = EnumFacing.SOUTH;
			} else if (enumfacing == EnumFacing.SOUTH && block1.isFullBlock()
					&& !block.isFullBlock()) {
				enumfacing = EnumFacing.NORTH;
			} else if (enumfacing == EnumFacing.WEST && block2.isFullBlock()
					&& !block3.isFullBlock()) {
				enumfacing = EnumFacing.EAST;
			} else if (enumfacing == EnumFacing.EAST && block3.isFullBlock()
					&& !block2.isFullBlock()) {
				enumfacing = EnumFacing.WEST;
			}

			worldIn.setBlockState(pos, state.withProperty(FACING, enumfacing),
					2);
		}
	}

	@Override
	public AbstractTileAutocon createNewTileAutocon(World worldIn, int meta) {
		return new TileStorageJunction();
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		boolean canPlaceBlockAt = super.canPlaceBlockAt(worldIn, pos);
		if (canPlaceBlockAt) {
			List<SearchItem> result = SearchUtil.findAllBlockFrom(worldIn, pos,
					MGThriveBlocks.duct_connector,
					MGThriveBlocks.storage_junction);
			// TODO Send message informing that a broker cannot be added to a
			// network with a broker
			return result.isEmpty();
		}
		return canPlaceBlockAt;
	}

	public IBlockState onBlockPlaced(World worldIn, BlockPos pos,
			EnumFacing facing, float hitX, float hitY, float hitZ, int meta,
			EntityLivingBase placer) {
		return this
				.getDefaultState()
				.withProperty(FACING,
						placer.getHorizontalFacing().getOpposite())
				.withProperty(POWERED, worldIn.isBlockPowered(pos));
	}

	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state,
			EntityLivingBase placer, ItemStack stack) {
		worldIn.setBlockState(pos, state.withProperty(FACING, placer
				.getHorizontalFacing().getOpposite()).withProperty(POWERED, worldIn.isBlockPowered(pos)), 2);

		if (stack.hasDisplayName()) {
			TileEntity tileentity = worldIn.getTileEntity(pos);

			if (tileentity instanceof TileStorageJunction) {
				((TileStorageJunction) tileentity).setCustomInventoryName(stack
						.getDisplayName());
			}
		}
	}

	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
		super.onBlockAdded(worldIn, pos, state);
		TileEntity tile = worldIn.getTileEntity(pos);
		if (tile instanceof TileItemBroker) {
			TileItemBroker itemBroker = (TileItemBroker) tile;

			for (EnumFacing xfacing : EnumFacing.values()) {
				BlockPos offset = pos.offset(xfacing);
				Block neighborBlock = worldIn.getBlockState(offset).getBlock();

				itemBroker.updateConnectorStatus(xfacing,
						neighborBlock == MGThriveBlocks.duct_connector);
			}
		}
	}

	@Override
	public void onNeighborChange(IBlockAccess worldIn, BlockPos pos,
			BlockPos neighborPos) {
		super.onNeighborChange(worldIn, pos, neighborPos);
		// System.out.println("Neighbor Block changed");
		TileEntity tile = worldIn.getTileEntity(pos);
		if (tile instanceof TileItemBroker) {
			TileItemBroker itemBroker = (TileItemBroker) tile;
			Block neighborBlock = worldIn.getBlockState(neighborPos).getBlock();

			for (EnumFacing facing : EnumFacing.values()) {
				if (pos.offset(facing).equals(neighborPos)) {
					itemBroker.updateConnectorStatus(facing,
							neighborBlock == MGThriveBlocks.duct_connector);
					break;
				}
			}
		}

	}

	@Override
	public void onNeighborBlockChange(World worldIn, BlockPos pos,
			IBlockState state, Block neighborBlock) {
		// TODO Auto-generated method stub
		super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
		// BlockPos blockpos2 = pos.up();
		// IBlockState iblockstate2 = worldIn.getBlockState(blockpos2);

		boolean flag = worldIn.isBlockPowered(pos);

		if ((flag || neighborBlock.canProvidePower()) && neighborBlock != this) {
			worldIn.setBlockState(pos,
					state.withProperty(POWERED, Boolean.valueOf(flag)), 2);

		}
	}

	@Override
	public boolean isSideSolid(IBlockAccess world, BlockPos pos, EnumFacing side) {
		// return super.isSideSolid(world, pos, side);
		return true;
	}

	public boolean onBlockActivated(World worldIn, BlockPos pos,
			IBlockState state, EntityPlayer playerIn, EnumFacing side,
			float hitX, float hitY, float hitZ) {
		if (worldIn.isRemote) {
			worldIn.markBlockForUpdate(pos); // Makes the server call
												// getDescriptionPacket for a
												// full data sync
			// markDirty(); // Marks the chunk as dirty, so that it is saved
			// properly on changes. Not required for the sync specifically, but
			// usually goes alongside the former.
			return true;
		} else {

			playerIn.openGui(MGThriveForge.instance, 0, worldIn, pos.getX(),
					pos.getY(), pos.getZ());
			return true;
		}
	}

	@SideOnly(Side.CLIENT)
	public IBlockState getStateForEntityRender(IBlockState state) {
		return this.getDefaultState().withProperty(FACING, EnumFacing.SOUTH)
				.withProperty(POWERED, true);
	}

	public IBlockState getStateFromMeta(int meta) {
		EnumFacing enumfacing = EnumFacing.getFront(meta);

		if (enumfacing.getAxis() == EnumFacing.Axis.Y) {
			enumfacing = EnumFacing.NORTH;
		}

		return this.getDefaultState().withProperty(FACING, enumfacing)
				.withProperty(POWERED, (meta & 8) == 8);
	}

	public int getMetaFromState(IBlockState state) {
		int powered = state.getValue(POWERED).compareTo(true) == 0 ? 8 : 0;
		int faceIndex = ((EnumFacing) state.getValue(FACING)).getIndex();
		return powered | faceIndex;
	}

	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { FACING, POWERED });
	}

	public static void setState(boolean active, World worldIn, BlockPos pos) {
		IBlockState iblockstate = worldIn.getBlockState(pos);
		TileEntity tileentity = worldIn.getTileEntity(pos);
		if (tileentity != null) {
			tileentity.validate();
			worldIn.setTileEntity(pos, tileentity);
		}
	}

	@SideOnly(Side.CLIENT)
	static final class SwitchEnumFacing {
		static final int[] FACING_LOOKUP = new int[EnumFacing.values().length];
		private static final String __OBFID = "CL_00002111";

		static {
			try {
				FACING_LOOKUP[EnumFacing.WEST.ordinal()] = 1;
			} catch (NoSuchFieldError var4) {
				;
			}

			try {
				FACING_LOOKUP[EnumFacing.EAST.ordinal()] = 2;
			} catch (NoSuchFieldError var3) {
				;
			}

			try {
				FACING_LOOKUP[EnumFacing.NORTH.ordinal()] = 3;
			} catch (NoSuchFieldError var2) {
				;
			}

			try {
				FACING_LOOKUP[EnumFacing.SOUTH.ordinal()] = 4;
			} catch (NoSuchFieldError var1) {
				;
			}
		}
	}
}
