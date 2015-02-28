package net.minecraft.mangrove.mod.thrive.duct;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
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

import com.google.common.base.Predicate;

public abstract class AbstractBlockDuct extends BlockContainer {
	public static final PropertyDirection INPUT = PropertyDirection.create("input");
	public static final PropertyDirection OUTPUT = PropertyDirection.create("output");
	public static final PropertyBool ENABLED = PropertyBool.create("enabled");
//	public static final IUnlistedProperty<Object> ENABLED = Properties.toUnlisted(PropertyBool.create("enabled"));
	
	private static final String __OBFID = "CL_00000257";
	private String name;
	private static final float UNIT=0.0625f;

	public AbstractBlockDuct(String name) {
		super(Material.iron);
		this.name = name;
		GameRegistry.registerBlock(this, name);
		setUnlocalizedName(MGThriveForge.ID + "_" + name);
        
		this.setDefaultState(this.blockState.getBaseState().withProperty(INPUT, EnumFacing.DOWN)
				.withProperty(OUTPUT, EnumFacing.UP)
				.withProperty(ENABLED, Boolean.valueOf(true)));
		this.setCreativeTab(CreativeTabs.tabRedstone);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		setHardness(2.0F);
		setResistance(8.0F);
	}

	public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
		this.setBlockBounds(UNIT * 0, UNIT * 0, UNIT * 0, UNIT * 16, UNIT * 16, UNIT * 16);
	}

	public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List list, Entity collidingEntity) {
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.625F, 1.0F);
		super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
		float f = 0.125F;
		this.setBlockBounds(0.0F, 0.0F, 0.0F, f, 1.0F, 1.0F);
		super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f);
		super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
		this.setBlockBounds(1.0F - f, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
		this.setBlockBounds(0.0F, 0.0F, 1.0F - f, 1.0F, 1.0F, 1.0F);
		super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		EnumFacing enumfacing1 = facing.getOpposite();
		System.out.println("onBlockPlaced: output="+facing+" input="+enumfacing1);
//		if (enumfacing1 == EnumFacing.UP) {
//			enumfacing1 = EnumFacing.DOWN;
//		}

		IBlockState defaultState = this.getDefaultState();
		
		return defaultState.withProperty(INPUT, enumfacing1).withProperty(OUTPUT, facing).withProperty(ENABLED, Boolean.valueOf(true));
	}

	public TileEntity createNewTileEntity(World worldIn, int meta) {
		System.out.println("createNewTileEntity");
		return createNewTileDuct(worldIn,meta);
	}

	protected abstract TileEntity createNewTileDuct(World worldIn, int meta);

	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		System.out.println("onBlockPlacedBy");
		if (stack.hasDisplayName()) {
			TileEntity tileentity = worldIn.getTileEntity(pos);
			
			if (tileentity instanceof AbstractTileDuct) {
				AbstractTileDuct ductTile = (AbstractTileDuct) tileentity;
				ductTile.setCustomName(stack.getDisplayName());
				ductTile.setInputFace((EnumFacing) state.getValue(INPUT));
				ductTile.setOutputFace((EnumFacing) state.getValue(OUTPUT));
			}
		}
	}

	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
		this.updateState(worldIn, pos, state);
	}

	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (worldIn.isRemote) {
			return true;
		} else {
			TileEntity tileentity = worldIn.getTileEntity(pos);

			if (tileentity instanceof AbstractTileDuct) {
				playerIn.displayGUIChest((AbstractTileDuct) tileentity);
			}

			return true;
		}
	}

	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
		this.updateState(worldIn, pos, state);
	}

	private void updateState(World worldIn, BlockPos pos, IBlockState state) {
		boolean flag = !worldIn.isBlockPowered(pos);

		if (flag != ((Boolean) state.getValue(ENABLED)).booleanValue()) {
			worldIn.setBlockState(pos, state.withProperty(ENABLED, Boolean.valueOf(flag)), 4);
		}
	}

	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		TileEntity tileentity = worldIn.getTileEntity(pos);

		if (tileentity instanceof AbstractTileDuct) {
			InventoryHelper.dropInventoryItems(worldIn, pos, (AbstractTileDuct) tileentity);
			worldIn.updateComparatorOutputLevel(pos, this);
		}

		super.breakBlock(worldIn, pos, state);
	}

	public int getRenderType() {
		return 2;
	}

	public boolean isFullCube() {
		return false;
	}

	public boolean isOpaqueCube() {
		return false;
	}

	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
		return true;
	}

	public static EnumFacing getFacing(int meta) {
		return EnumFacing.getFront(meta & 7);
	}

	public static boolean isEnabled(int meta) {
		return (meta & 8) != 8;
	}

	public boolean hasComparatorInputOverride() {
		return true;
	}

	public int getComparatorInputOverride(World worldIn, BlockPos pos) {
		return Container.calcRedstone(worldIn.getTileEntity(pos));
	}

	@SideOnly(Side.CLIENT)
	public EnumWorldBlockLayer getBlockLayer() {
		return EnumWorldBlockLayer.CUTOUT_MIPPED;
	}

	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(INPUT, getFacing(meta)).withProperty(ENABLED, Boolean.valueOf(isEnabled(meta)));
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		TileEntity tileentity = worldIn.getTileEntity(pos);
		IBlockState actualState = super.getActualState(state, worldIn, pos);
		if (tileentity instanceof AbstractTileDuct) {
			AbstractTileDuct tileDuct = (AbstractTileDuct) tileentity;
			
			actualState.withProperty(OUTPUT, tileDuct.getOutputFace());
		}
		System.out.println("State  : input="+actualState.getValue(INPUT)+" output="+actualState.getValue(OUTPUT));
		return actualState;
	}

	public int getMetaFromState(IBlockState state) {
		byte b0 = 0;
		int i = b0 | ((EnumFacing) state.getValue(INPUT)).getIndex();

		if (!((Boolean) state.getValue(ENABLED)).booleanValue()) {
			i |= 8;
		}

		return i;
	}

	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { INPUT, OUTPUT,ENABLED });
	}

}
