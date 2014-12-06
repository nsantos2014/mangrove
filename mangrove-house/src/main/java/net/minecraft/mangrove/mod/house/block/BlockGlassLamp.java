package net.minecraft.mangrove.mod.house.block;

import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.mangrove.mod.house.proxy.CommonProxy;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class BlockGlassLamp extends Block {

	private boolean field_149996_a;
	private String field_149995_b;
	
	@SideOnly(Side.CLIENT)
    protected IIcon blockInsideIcon;
	
//	private static final IIcon[] field_149998_a = new IIcon[16];

	public BlockGlassLamp() {
		super(Material.glass);
		setHardness(3.5F);
		setStepSound(Block.soundTypeGlass);
		setBlockName("glass_lamp");
		setBlockTextureName("glass");
		setCreativeTab(CreativeTabs.tabDecorations);
		setHarvestLevel("pickaxe", 0);
		setLightLevel(1.0F);
	}

	/**
	 * Is this block (a) opaque and (b) a full 1m cube? This determines whether
	 * or not to render the shared face of two adjacent blocks and also whether
	 * the player can attach torches, redstone wire, etc to this block.
	 */
	public boolean isOpaqueCube() {
		return false;
	}

	/**
	 * Returns true if the given side of this block type should be rendered, if
	 * the adjacent block is at the given coordinates. Args: blockAccess, x, y,
	 * z, side
	 */
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess blockAccess, int x, int y,
			int z, int side) {
		Block block = blockAccess.getBlock(x, y, z);

		if (this == Blocks.glass /* || this == Blocks.stained_glass */) {
			if (blockAccess.getBlockMetadata(x, y, z) != blockAccess
					.getBlockMetadata(x - Facing.offsetsXForSide[side], y
							- Facing.offsetsYForSide[side], z
							- Facing.offsetsZForSide[side])) {
				return true;
			}

			if (block == this) {
				return false;
			}
		}

		return !this.field_149996_a && block == this ? false : super
				.shouldSideBeRendered(blockAccess, x, y, z, side);
	}

//	/**
//	 * Gets the block's texture. Args: side, meta
//	 */
//	@SideOnly(Side.CLIENT)
//	public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
//		return field_149998_a[p_149691_2_ % field_149998_a.length];
//	}
	
	/**
	 * Gets the block's texture. Args: side, meta
	 */
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
	    if( side==1){
	        return blockInsideIcon;
	    }
		return blockIcon;
	}

	/**
	 * Determines the damage on the item the block drops. Used in cloth and
	 * wood.
	 */
	public int damageDropped(int p_149692_1_) {
		return p_149692_1_;
	}

	/**
	 * Returns the quantity of items to drop on block destruction.
	 */
	public int quantityDropped(Random p_149745_1_) {
		return 1;
	}

	@SideOnly(Side.CLIENT)
	public static int func_149997_b(int p_149997_0_) {
		return ~p_149997_0_ & 15;
	}

//	/**
//	 * returns a list of blocks with the same ID, but different meta (eg: wood
//	 * returns 4 blocks)
//	 */
//	@SideOnly(Side.CLIENT)
//	public void getSubBlocks(Item p_149666_1_, CreativeTabs p_149666_2_,
//			List p_149666_3_) {
//		for (int i = 0; i < field_149998_a.length; ++i) {
//			p_149666_3_.add(new ItemStack(p_149666_1_, 1, i));
//		}
//	}

	/**
	 * Returns which pass should this block be rendered on. 0 for solids and 1
	 * for alpha
	 */
	@SideOnly(Side.CLIENT)
	public int getRenderBlockPass() {
		return 1;
	}

//	@SideOnly(Side.CLIENT)
//	public void registerBlockIcons(IIconRegister p_149651_1_) {
//		for (int i = 0; i < field_149998_a.length; ++i) {
//			field_149998_a[i] = p_149651_1_.registerIcon(this.getTextureName()
//					+ "_" + ItemDye.field_150921_b[func_149997_b(i)]);
//		}
//	}

	 @SideOnly(Side.CLIENT)
	 public void registerBlockIcons(IIconRegister p_149651_1_) {
	     this.blockIcon = p_149651_1_.registerIcon("glass");
	     this.blockInsideIcon = p_149651_1_.registerIcon("beacon");
	 }

	/**
	 * Return true if a player with Silk Touch can harvest this block directly,
	 * and not its normal drops.
	 */
	protected boolean canSilkHarvest() {
		return true;
	}

	/**
	 * If this block doesn't render as an ordinary block it will return False
	 * (examples: signs, buttons, stairs, etc)
	 */
	public boolean renderAsNormalBlock() {
		return false;
	}
	public int getRenderType() {
        return CommonProxy.blockGlassLamp;
    }
}
