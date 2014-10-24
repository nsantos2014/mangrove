package net.minecraft.mangrove.mod.thrive.robofarmer.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.mangrove.mod.thrive.MGThriveBlocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockFarmerLink extends Block {
	@SideOnly(Side.CLIENT)
	protected IIcon blockIcon;
	@SideOnly(Side.CLIENT)
	protected IIcon blockIconTop;
	@SideOnly(Side.CLIENT)
	protected IIcon blockIconFront;

	public BlockFarmerLink() {
		super(Material.iron);
		setBlockName("farmer_link");
		setBlockTextureName("crafting_table");
		this.setCreativeTab(CreativeTabs.tabRedstone);
	}

	@Override
	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		final Block blockUnder = world.getBlock(x, y - 1, z);
		if (blockUnder == Blocks.sand
				|| blockUnder == MGThriveBlocks.farmer_link) {
			return true;
		}
		final Block blockLeft = world.getBlock(x - 1, y, z);
		if (blockLeft == MGThriveBlocks.farmer_link) {
			return true;
		}
		final Block blockRight = world.getBlock(x + 1, y, z);
		if (blockRight == MGThriveBlocks.farmer_link) {
			return true;
		}
		final Block blockBack = world.getBlock(x, y, z + 1);
		if (blockBack == MGThriveBlocks.farmer_link) {
			return true;
		}
		final Block blockFront = world.getBlock(x, y, z - 1);
		if (blockFront == MGThriveBlocks.farmer_link) {
			return true;
		}
		// return super.canPlaceBlockAt(world, x, y, z);
		return false;
	}

	// World, X, Y, Z, side, hitX, hitY, hitZ, block metadata
	@Override
	public int onBlockPlaced(World world, int x, int y, int z, int side,
			float hitX, float hitY, float hitZ, int meta) {
		
		return super
				.onBlockPlaced(world, x, y, z, side, hitX, hitY, hitZ, meta);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(IIconRegister register) {
		this.blockIcon = register.registerIcon("planks_birch");
		this.blockIconTop = register.registerIcon("piston_top_normal");
		this.blockIconFront = register.registerIcon("planks_birch");
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		return side == 1 ? this.blockIconTop : (side == 0 ? Blocks.planks
				.getBlockTextureFromSide(side)
				: (side != 2 && side != 4 ? this.blockIcon
						: this.blockIconFront));
	}

}
