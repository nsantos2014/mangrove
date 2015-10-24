package net.minecraft.mangrove.mod.house.doors.drawbridge;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.mangrove.mod.house.MGHouseForge;
import net.minecraft.mangrove.mod.house.MGHouseItems;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockDrawbridge extends Block {
	public static boolean	              disableValidation	= false;
	public static final PropertyDirection	FACING	        = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	public static final PropertyBool	  OPEN	            = PropertyBool.create("open");
	public static final PropertyEnum	  TYPE	            = PropertyEnum.create("type", ComponentType.class);

	protected String	                  name	            = "drawbridge";

	public BlockDrawbridge() {
		super(Material.wood);
		setHardness(3.5F);
		setStepSound(Block.soundTypeGravel);

		GameRegistry.registerBlock(this, name);
//		GameRegistry.registerBlock(this, ItemDrawbridge.class, name);
		setUnlocalizedName(MGHouseForge.ID + "_" + name);

		 setCreativeTab(CreativeTabs.tabDecorations);
		setHarvestLevel("axe", 0);
		setLightLevel(1.0F);

		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(OPEN, Boolean.valueOf(false))
		        .withProperty(TYPE, ComponentType.HINGE));

		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);

	}


	
	public String getName() {
		return name;
	}

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

	public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
		return false;
	}

	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos) {
		this.setBlockBoundsBasedOnState(worldIn, pos);
		return super.getSelectedBoundingBox(worldIn, pos);
	}

	public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
		this.setBlockBoundsBasedOnState(worldIn, pos);
		return super.getCollisionBoundingBox(worldIn, pos, state);
	}

	public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
		this.setBounds(worldIn.getBlockState(pos));
	}

	public void setBlockBoundsForItemRender() {
		float f = 0.1875F;
		this.setBlockBounds(0.0F, 0.40625F, 0.0F, 1.0F, 0.59375F, 1.0F);
	}

	public void setBounds(IBlockState state) {
		if (state.getBlock() == this) {
			// boolean flag = state.getValue(HALF) == DoorHalf.TOP;
			Boolean obool = (Boolean) state.getValue(OPEN);
			EnumFacing enumfacing = (EnumFacing) state.getValue(FACING);
			ComponentType type = (ComponentType) state.getValue(TYPE);
			if (obool) {
				switch (enumfacing) {
				case NORTH:
					this.setBlockBounds(0.0F, 0.0F, 0.5F, 1.0F, 1.0F, 1.0F);
					break;
				case SOUTH:
					this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.5F);
					break;
				case EAST:
					this.setBlockBounds(0.0F, 0.0F, 0.0F, 0.5F, 1.0F, 1.0F);
					break;
				case WEST:
					this.setBlockBounds(0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
					break;
				default:
					this.setBlockBounds(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
					break;
				}
			} else if (type == ComponentType.HINGE) {
				this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
			} else {
				this.setBlockBounds(0.0F, 0.5F, 0.0F, 1.0F, 1.0F, 1.0F);
			}
			// float f = 0.1875F;
			//
			// switch (enumfacing) {
			// case NORTH:
			// this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 2.0F);
			// break;
			// default:
			// this.setBlockBounds(0.0F, 0.8125F, 0.0F, 1.0F, 1.0F, 1.0F);
			// break;
			// }
			//
			// // if (flag)
			// // {
			//
			// // }
			// // else
			// // {
			// // this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.1875F, 1.0F);
			// // }
			//
			// if (obool.booleanValue())
			// {
			// if (enumfacing == EnumFacing.NORTH)
			// {
			// this.setBlockBounds(0.0F, -1.0F, 0.8125F, 1.0F, 1.0F, 1.0F);
			// }
			//
			// if (enumfacing == EnumFacing.SOUTH)
			// {
			// this.setBlockBounds(0.0F, -1.0F, 0.0F, 1.0F, 1.0F, 0.1875F);
			// }
			//
			// if (enumfacing == EnumFacing.WEST)
			// {
			// this.setBlockBounds(0.8125F, -1.0F, 0.0F, 1.0F, 1.0F, 1.0F);
			// }
			//
			// if (enumfacing == EnumFacing.EAST)
			// {
			// this.setBlockBounds(0.0F, -1.0F, 0.0F, 0.1875F, 1.0F, 1.0F);
			// }
			// }
		}
	}

	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
		ComponentType type = (ComponentType) state.getValue(TYPE);
		EnumFacing enumfacing = (EnumFacing) state.getValue(FACING);
		boolean oldStateValue = (Boolean) state.getValue(OPEN);

		if (type == ComponentType.HINGE) {
			toggleBridge(worldIn, pos, state, !oldStateValue, new HashSet<BlockPos>());
			worldIn.playAuxSFXAtEntity(playerIn, ((Boolean) state.getValue(OPEN)).booleanValue() ? 1003 : 1006, pos, 0);

		} else {
			BlockPos blockpos1 = oldStateValue ? pos.down() : pos.offset(enumfacing.getOpposite());
			IBlockState iblockstate1 = worldIn.getBlockState(blockpos1);

			if (iblockstate1.getBlock() != this) {
				return false;
			}
			onBlockActivated(worldIn, blockpos1, iblockstate1, playerIn, side, hitX, hitY, hitZ);
		}
		return true;

	}

	public boolean toggleBridge(World worldIn, BlockPos pos, IBlockState state, boolean newOpenState, Set<BlockPos> handledBlocks) {
		ComponentType type = (ComponentType) state.getValue(TYPE);
		EnumFacing enumfacing = (EnumFacing) state.getValue(FACING);
		boolean oldStateValue = (Boolean) state.getValue(OPEN);
		handledBlocks.add(pos);
		if (type == ComponentType.HINGE) {

			if (oldStateValue && canLower(worldIn, pos, enumfacing)) {
				state = state.withProperty(OPEN, newOpenState);
				lowerBridge(worldIn, pos, enumfacing);
			} else if (canRaise(worldIn, pos, enumfacing)) {
				state = state.withProperty(OPEN, newOpenState);
				raiseBridge(worldIn, pos, enumfacing);
			} else {
				return false;
			}

			worldIn.setBlockState(pos, state, 2);
			// worldIn.markBlockRangeForRenderUpdate(blockpos1, pos);
			BlockPos blockposLeft = pos.offset(enumfacing.rotateYCCW());
			if (!handledBlocks.contains(blockposLeft)) {
				IBlockState iblockstateLeft = worldIn.getBlockState(blockposLeft);
				if (iblockstateLeft.getBlock() == this) {
					this.toggleBridge(worldIn, blockposLeft, iblockstateLeft, !oldStateValue, handledBlocks);
					// onBlockActivated(worldIn, blockposLeft, state, playerIn,
					// side, hitX, hitY, hitZ);
				}
			}
			BlockPos blockposRight = pos.offset(enumfacing.rotateY());

			if (!handledBlocks.contains(blockposRight)) {
				IBlockState iblockstateRight = worldIn.getBlockState(blockposRight);
				if (iblockstateRight.getBlock() == this) {
					this.toggleBridge(worldIn, blockposRight, iblockstateRight, !oldStateValue, handledBlocks);
					// onBlockActivated(worldIn, blockposRight, state, playerIn,
					// side, hitX, hitY, hitZ);
				}
			}
			handledBlocks.add(blockposLeft);
			handledBlocks.add(blockposRight);
		} else {
			return true;
		}
		return false;
	}

	private void lowerBridge(World worldIn, BlockPos pos, EnumFacing facing) {
		for (int i = 1; i < 4; i++) {
			BlockPos oldPos = pos.up(i);
			BlockPos newPos = pos.offset(facing, i);
			worldIn.setBlockToAir(oldPos);
			worldIn.notifyNeighborsOfStateChange(oldPos, this);
			IBlockState iblockstate = getDefaultState().withProperty(BlockDrawbridge.FACING, facing).withProperty(BlockDrawbridge.TYPE, BlockDrawbridge.ComponentType.PLANK)
			        .withProperty(BlockDrawbridge.OPEN, false);
			worldIn.setBlockState(newPos, iblockstate, 2);
			worldIn.notifyNeighborsOfStateChange(newPos, this);
		}
	}

	private void raiseBridge(World worldIn, BlockPos pos, EnumFacing facing) {
		for (int i = 1; i < 4; i++) {
			BlockPos oldPos = pos.offset(facing, i);
			BlockPos newPos = pos.up(i);
			worldIn.setBlockToAir(oldPos);
			worldIn.notifyNeighborsOfStateChange(oldPos, this);
			IBlockState iblockstate = getDefaultState().withProperty(BlockDrawbridge.FACING, facing).withProperty(BlockDrawbridge.TYPE, BlockDrawbridge.ComponentType.PLANK)
			        .withProperty(BlockDrawbridge.OPEN, true);
			worldIn.setBlockState(newPos, iblockstate, 2);

			worldIn.notifyNeighborsOfStateChange(newPos, this);
		}
	}

	private boolean canLower(World worldIn, BlockPos pos, EnumFacing facing) {
		for (int i = 1; i < 4; i++) {
			BlockPos checkPos = pos.offset(facing, i);
			if (!worldIn.isAirBlock(checkPos)) {
				return false;
			}
		}
		return true;
	}

	private boolean canRaise(World worldIn, BlockPos pos, EnumFacing facing) {
		for (int i = 1; i < 4; i++) {
			BlockPos checkPos = pos.up(i);
			if (!worldIn.isAirBlock(checkPos)) {
				return false;
			}
		}
		return true;
	}

//	@Override
//	public Item getItem(World worldIn, BlockPos pos) {
//		return MGHouseItems.drawbridge;
//	}
//
//	@Override
//	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
////		return state.getValue(TYPE) == ComponentType.HINGE ? MGHouseItems.drawbridge : null;
//		return MGHouseItems.drawbridge;
//	}
	

	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
		EnumFacing facing = (EnumFacing) state.getValue(FACING);
		if (player.capabilities.isCreativeMode && state.getValue(TYPE) == ComponentType.HINGE && worldIn.getBlockState(pos).getBlock() == this) {
			for (int i = 1; i < 4; i++) {
				worldIn.setBlockToAir(pos.up(i));
				worldIn.setBlockToAir(pos.offset(facing, i));
			}
		}
		if (state.getValue(TYPE) == ComponentType.PLANK) {
			for (int i = 1; i < 4; i++) {
				BlockPos blockpos1 = getPreviousPos(state, i, pos);
				IBlockState iblockstate1 = worldIn.getBlockState(blockpos1);
				if (iblockstate1.getBlock() == this && iblockstate1.getValue(TYPE) == ComponentType.HINGE) {
					dropBlockAsItem(worldIn, blockpos1, iblockstate1, 0);
					worldIn.setBlockToAir(blockpos1);
					break;
				}
				if (iblockstate1.getBlock() != this) {
					worldIn.setBlockToAir(pos);
					return;
				}
				worldIn.setBlockToAir(blockpos1);
			}
		}
	}

	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
		EnumFacing facing = (EnumFacing) state.getValue(FACING);

		if (state.getValue(TYPE) == ComponentType.HINGE) {
			for (int i = 1; i < 4; i++) {
				BlockPos blockpos1 = getNextPos(state, i, pos);
				IBlockState iblockstate1 = worldIn.getBlockState(blockpos1);
				if (iblockstate1.getBlock() != this) {
					// worldIn.setBlockToAir(pos);
					return;
				}
			}
		} else if (state.getValue(TYPE) == ComponentType.PLANK) {
			for (int i = 1; i < 4; i++) {
				BlockPos blockpos1 = getPreviousPos(state, i, pos);
				IBlockState iblockstate1 = worldIn.getBlockState(blockpos1);
				if (iblockstate1.getBlock() == this && iblockstate1.getValue(TYPE) == ComponentType.HINGE) {
					break;
				}
				if (iblockstate1.getBlock() != this) {
					worldIn.setBlockToAir(pos);
					return;
				}
			}
		}

		// if (state.getValue(TYPE) == ComponentType.PLANK){
		// BlockPos blockpos1=null;
		// IBlockState iblockstate1=null;
		// if( state.getValue(OPEN)==Boolean.TRUE){
		// for(int i=1; i< 4; i++){
		// blockpos1 = pos.down(i);
		// iblockstate1= worldIn.getBlockState(blockpos1);
		// if( iblockstate1.getValue(TYPE) == ComponentType.PLANK){
		// break;
		// }
		// iblockstate1= null;
		// }
		// } else{
		// for(int i=1; i< 4; i++){
		// blockpos1 = pos.offset(facing.getOpposite(),i);
		// iblockstate1= worldIn.getBlockState(blockpos1);
		// if( iblockstate1.getValue(TYPE) == ComponentType.PLANK){
		// break;
		// }
		// if(iblockstate1.getBlock()!=this){
		// worldIn.setBlockToAir(pos);
		// }
		// iblockstate1= null;
		// }
		// }
		// if( iblockstate1!=null){
		//
		//
		// if (iblockstate1.getBlock() != this)
		// {
		// worldIn.setBlockToAir(pos);
		// }
		// else if (neighborBlock != this)
		// {
		// this.onNeighborBlockChange(worldIn, blockpos1, iblockstate1,
		// neighborBlock);
		// }
		// }
		// }

		// if (!worldIn.isRemote) {
		// EnumFacing direction = (EnumFacing) state.getValue(FACING);
		// BlockPos blockpos1 = pos.offset(((EnumFacing)
		// state.getValue(FACING)).getOpposite());
		//
		// if
		// (!(isValidSupportBlock(worldIn.getBlockState(blockpos1).getBlock())
		// ||
		// worldIn.isSideSolid(blockpos1, direction, true))) {
		// worldIn.setBlockToAir(pos);
		// this.dropBlockAsItem(worldIn, pos, state, 0);
		// } else {
		//
		// ComponentType type = (ComponentType) state.getValue(TYPE);
		// boolean oldStateValue = (Boolean) state.getValue(OPEN);
		//
		//
		// if (type == ComponentType.HINGE) {
		// boolean flag = worldIn.isBlockPowered(pos);
		//
		// if (flag || neighborBlock.canProvidePower()) {
		// boolean flag1 = ((Boolean) state.getValue(OPEN)).booleanValue();
		// //
		// if (flag1 != flag) {
		// // worldIn.setBlockState(pos, state.withProperty(OPEN,
		// // Boolean.valueOf(flag)), 2);
		// if (toggleBridge(worldIn, pos, state, flag1, new
		// HashSet<BlockPos>())) {
		// worldIn.playAuxSFXAtEntity((EntityPlayer) null, flag ? 1003 : 1006,
		// pos,
		// 0);
		// }
		// }
		// }
		// }else{
		// BlockPos blockpos2 = oldStateValue ? pos.down() :
		// pos.offset(direction);
		// IBlockState iblockstate1 = worldIn.getBlockState(blockpos2);
		//
		// if (iblockstate1.getBlock() != this) {
		// return;
		// }
		// onNeighborBlockChange(worldIn,blockpos2,iblockstate1,neighborBlock);
		// }
		//
		//
		// }
		// }
	}

	private BlockPos getNextPos(IBlockState state, int i, BlockPos pos) {
		if (state.getValue(OPEN) == Boolean.TRUE) {
			return pos.up(i);
		}
		EnumFacing facing = (EnumFacing) state.getValue(FACING);
		return pos.offset(facing, i);
	}

	private BlockPos getPreviousPos(IBlockState state, int i, BlockPos pos) {
		if (state.getValue(OPEN) == Boolean.TRUE) {
			return pos.down(i);
		}
		EnumFacing facing = (EnumFacing) state.getValue(FACING);
		return pos.offset(facing.getOpposite(), i);
	}

	public MovingObjectPosition collisionRayTrace(World worldIn, BlockPos pos, Vec3 start, Vec3 end) {
		this.setBlockBoundsBasedOnState(worldIn, pos);
		return super.collisionRayTrace(worldIn, pos, start, end);
	}

	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		IBlockState iblockstate = this.getDefaultState();

		EnumFacing face = facing;
		if (facing.getAxis().isVertical()) {
			face = placer.getHorizontalFacing();

		}
		iblockstate = iblockstate.withProperty(FACING, face).withProperty(OPEN, Boolean.valueOf(false)).withProperty(TYPE, ComponentType.HINGE);
		return iblockstate;
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		boolean canPlaceBlockAt = super.canPlaceBlockAt(worldIn, pos);
		if (canPlaceBlockAt) {
			for (int i = 1; i < 4; i++) {
				BlockPos checkPos = pos.up(i);
				if (!super.canPlaceBlockAt(worldIn, checkPos)) {
					return false;
				}
			}
		}
		return canPlaceBlockAt;
	}

	public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
		return side != EnumFacing.DOWN;
		// if (disableValidation) return true;
		// EnumFacing dir = side.getOpposite();
		// pos = pos.offset(dir);
		// return !side.getAxis().isVertical() &&
		// (isValidSupportBlock(worldIn.getBlockState(pos).getBlock()) ||
		// worldIn.isSideSolid(pos, side, true));
	}

	protected static EnumFacing getFacing(int meta) {
		switch (meta & 3) {
		case 0:
			return EnumFacing.NORTH;
		case 1:
			return EnumFacing.SOUTH;
		case 2:
			return EnumFacing.WEST;
		case 3:
		default:
			return EnumFacing.EAST;
		}
	}

	protected static int getMetaForFacing(EnumFacing facing) {
		switch (SwitchEnumFacing.FACING_LOOKUP[facing.ordinal()]) {
		case 1:
			return 0;
		case 2:
			return 1;
		case 3:
			return 2;
		case 4:
		default:
			return 3;
		}
	}

	private static boolean isValidSupportBlock(Block blockIn) {
		return true;
		// if (disableValidation) return true;
		// return blockIn.getMaterial().isOpaque() && blockIn.isFullCube() ||
		// blockIn == Blocks.glowstone || blockIn instanceof BlockSlab ||
		// blockIn instanceof BlockStairs;
	}

	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(FACING, getFacing(meta)).withProperty(OPEN, Boolean.valueOf((meta & 4) != 0))
		        .withProperty(TYPE, (meta & 8) != 0 ? ComponentType.HINGE : ComponentType.PLANK);
	}

	public int getMetaFromState(IBlockState state) {
		byte b0 = 0;
		int i = b0 | getMetaForFacing((EnumFacing) state.getValue(FACING));

		if (((Boolean) state.getValue(OPEN)).booleanValue()) {
			i |= 4;
		}

		if (state.getValue(TYPE) == ComponentType.HINGE) {
			i |= 8;
		}

		return i;
	}

	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { FACING, TYPE, OPEN });
	}

	public static enum ComponentType implements IStringSerializable {
		HINGE("hinge"), PLANK("plank");
		private final String		name;

		private static final String	__OBFID	= "CL_00002051";

		private ComponentType(String name) {
			this.name = name;
		}

		public String toString() {
			return this.name;
		}

		public String getName() {
			return this.name;
		}
	}

	static final class SwitchEnumFacing {
		static final int[]		    FACING_LOOKUP	= new int[EnumFacing.values().length];
		private static final String	__OBFID		  = "CL_00002052";

		static {
			try {
				FACING_LOOKUP[EnumFacing.NORTH.ordinal()] = 1;
			} catch (NoSuchFieldError var4) {
				;
			}

			try {
				FACING_LOOKUP[EnumFacing.SOUTH.ordinal()] = 2;
			} catch (NoSuchFieldError var3) {
				;
			}

			try {
				FACING_LOOKUP[EnumFacing.WEST.ordinal()] = 3;
			} catch (NoSuchFieldError var2) {
				;
			}

			try {
				FACING_LOOKUP[EnumFacing.EAST.ordinal()] = 4;
			} catch (NoSuchFieldError var1) {
				;
			}
		}
	}
}
