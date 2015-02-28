package net.minecraft.mangrove.mod.thrive.autocon;

import java.util.List;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.mangrove.mod.thrive.MGThriveForge;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class AbstractBlockDuct extends BlockContainer{
	private static final float CUBE_SIZE = 0.8F;
	private String name;

	public AbstractBlockDuct(String name) {
		super(Material.circuits);
		this.name = name;
		GameRegistry.registerBlock(this, name);
		setUnlocalizedName(MGThriveForge.ID + "_" + name);
		this.setBlockBounds(0.2f, 0.2f, 0.2f, CUBE_SIZE, CUBE_SIZE, CUBE_SIZE);
		this.setCreativeTab(CreativeTabs.tabRedstone);
		setHardness(2.0F);
		setResistance(8.0F);	
	}

	public String getName() {
		return name;
	}
	
	public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
		this.setBlockBounds(0.2f, 0.2f, 0.2f, CUBE_SIZE, CUBE_SIZE, CUBE_SIZE);
	}

	public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List list, Entity collidingEntity) {
//		this.setBlockBounds(0.2F, 0.2F, 0.2F, 0.8F, 0.625F, 0.8F);
//		super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
//		float f = 0.125F;
//		this.setBlockBounds(0.0F, 0.0F, 0.0F, f, 1.0F, 1.0F);
//		super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
//		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f);
//		super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
////		this.setBlockBounds(1.0F - f, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
//		this.setBlockBounds(0.2F - f, 0.2F, 0.2F, 8.0F, 8.0F, 8.0F);
//		super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
//		this.setBlockBounds(0.0F, 0.0F, 1.0F - f, 1.0F, 1.0F, 1.0F);
//		super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
////		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
//		this.setBlockBounds(0.2f, 0.2f, 0.2f, CUBE_SIZE, CUBE_SIZE, CUBE_SIZE);
		float b = 0.2F;
        float b2 = 1.0F - b;
//        float e = 0.375F;
//        float e2 = 1.0F - e;

        setBlockBounds(b, b, b, b2, b2, b2);
        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);

        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
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
	@Override
	public boolean canBeReplacedByLeaves(IBlockAccess world, BlockPos pos) {
		return false;
	}
	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		// TODO validate block placing
		return super.canPlaceBlockAt(worldIn, pos);
	}
	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
		super.onBlockAdded(worldIn, pos, state);
	}
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return createNewTileDuct(worldIn,meta);
	}

	protected abstract AbstractTileDuct createNewTileDuct(World worldIn, int meta);

}
