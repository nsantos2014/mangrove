package net.minecraft.mangrove.mod.house.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.mangrove.mod.house.MGHouseForge;
import net.minecraft.mangrove.mod.house.proxy.CommonProxy;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockGlassLamp extends Block {

	private boolean field_149996_a;
	private String field_149995_b;
	
	protected String name = "glass_lamp";
	
//	private static final IIcon[] field_149998_a = new IIcon[16];

	public BlockGlassLamp() {
		super(Material.glass);
		setHardness(3.5F);
		setStepSound(Block.soundTypeGlass);
		
        GameRegistry.registerBlock(this, name);
		setUnlocalizedName(MGHouseForge.ID + "_" + name);		   
		
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
    @SideOnly(Side.CLIENT)
    public EnumWorldBlockLayer getBlockLayer()
    {
        return EnumWorldBlockLayer.CUTOUT;
    }

    public boolean isFullCube()
    {
        return false;
    }
    public int getRenderType(){
        return 3;
    }
//
//	@Override
//	public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
//	    IBlockState blockState2 = worldIn.getBlockState(pos);
//        Block block = blockState2.getBlock();
//	    return super.shouldSideBeRendered(worldIn, pos, side);
//	}
	
	 @SideOnly(Side.CLIENT)
	    public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
	    {
	        IBlockState iblockstate = worldIn.getBlockState(pos);
	        Block block = iblockstate.getBlock();

	        if ((block == Blocks.glass) || (block == Blocks.stained_glass) )
	        {
	            if (worldIn.getBlockState(pos.offset(side.getOpposite())) != iblockstate)
	            {
	                return true;
	            }

	            if (block == this)
	            {
	                return false;
	            }
	        }

	        return /*!this.ignoreSimilarity &&*/ block == this ? false : super.shouldSideBeRendered(worldIn, pos, side);
	    }
//	/**
//	 * Returns true if the given side of this block type should be rendered, if
//	 * the adjacent block is at the given coordinates. Args: blockAccess, x, y,
//	 * z, side
//	 */
//	@SideOnly(Side.CLIENT)
//	public boolean shouldSideBeRendered(IBlockAccess blockAccess, int x, int y,
//			int z, int side) {
//		Block block = blockAccess.getBlock(x, y, z);
//
//		if (this == Blocks.glass /* || this == Blocks.stained_glass */) {
//			if (blockAccess.getBlockMetadata(x, y, z) != blockAccess
//					.getBlockMetadata(x - Facing.offsetsXForSide[side], y
//							- Facing.offsetsYForSide[side], z
//							- Facing.offsetsZForSide[side])) {
//				return true;
//			}
//
//			if (block == this) {
//				return false;
//			}
//		}
//
//		return !this.field_149996_a && block == this ? false : super
//				.shouldSideBeRendered(blockAccess, x, y, z, side);
//	}

//	/**
//	 * Gets the block's texture. Args: side, meta
//	 */
//	@SideOnly(Side.CLIENT)
//	public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
//		return field_149998_a[p_149691_2_ % field_149998_a.length];
//	}
	
//	/**
//	 * Gets the block's texture. Args: side, meta
//	 */
//	@SideOnly(Side.CLIENT)
//	public IIcon getIcon(int side, int meta) {
//	    if( side==1){
//	        return blockInsideIcon;
//	    }
//		return blockIcon;
//	}

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

//	/**
//	 * Returns which pass should this block be rendered on. 0 for solids and 1
//	 * for alpha
//	 */
//	@SideOnly(Side.CLIENT)
//	public int getRenderBlockPass() {
//		return 1;
//	}

//	@SideOnly(Side.CLIENT)
//	public void registerBlockIcons(IIconRegister p_149651_1_) {
//		for (int i = 0; i < field_149998_a.length; ++i) {
//			field_149998_a[i] = p_149651_1_.registerIcon(this.getTextureName()
//					+ "_" + ItemDye.field_150921_b[func_149997_b(i)]);
//		}
//	}
//
//	 @SideOnly(Side.CLIENT)
//	 public void registerBlockIcons(IIconRegister p_149651_1_) {
//	     this.blockIcon = p_149651_1_.registerIcon("glass");
//	     this.blockInsideIcon = p_149651_1_.registerIcon("beacon");
//	 }

	/**
	 * Return true if a player with Silk Touch can harvest this block directly,
	 * and not its normal drops.
	 */
	protected boolean canSilkHarvest() {
		return true;
	}

//	/**
//	 * If this block doesn't render as an ordinary block it will return False
//	 * (examples: signs, buttons, stairs, etc)
//	 */
//	public boolean renderAsNormalBlock() {
//		return false;
//	}
//	public int getRenderType() {
//        return CommonProxy.blockGlassLamp;
//    }
}
