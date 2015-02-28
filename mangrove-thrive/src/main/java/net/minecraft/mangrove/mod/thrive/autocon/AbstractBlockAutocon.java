package net.minecraft.mangrove.mod.thrive.autocon;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.mangrove.mod.thrive.MGThriveForge;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class AbstractBlockAutocon extends BlockContainer {

	private String name;

	protected AbstractBlockAutocon(String name) {
		super(Material.circuits);
		this.name = name;
		GameRegistry.registerBlock(this, name);
		setUnlocalizedName(MGThriveForge.ID + "_" + name);
		this.setCreativeTab(CreativeTabs.tabRedstone);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		setHardness(2.0F);
		setResistance(8.0F);
	}

	public int getRenderType() {
		return 3;
	}

	public boolean isFullCube() {
		return true;
	}

	public boolean isOpaqueCube() {
		return false;
	}

	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
		return true;
	}

	public String getName() {
		return name;
	}

	@Override
	public EnumWorldBlockLayer getBlockLayer() {
		return EnumWorldBlockLayer.CUTOUT_MIPPED;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return createNewTileAutocon(worldIn, meta);
	}

	public abstract AbstractTileAutocon createNewTileAutocon(World worldIn, int meta);
}
