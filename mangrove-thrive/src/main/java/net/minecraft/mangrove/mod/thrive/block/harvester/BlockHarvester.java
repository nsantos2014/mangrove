package net.minecraft.mangrove.mod.thrive.block.harvester;

import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.mangrove.core.utils.BlockUtils;
import net.minecraft.mangrove.mod.thrive.MGThriveBlocks;
import net.minecraft.mangrove.mod.thrive.MGThriveForge;
import net.minecraft.mangrove.mod.thrive.proxy.CommonProxy;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockHarvester extends BlockContainer {
	private final Random field_149922_a = new Random();
	@SideOnly(Side.CLIENT)
	private IIcon iconOutside;
	@SideOnly(Side.CLIENT)
	private IIcon iconTop;
	@SideOnly(Side.CLIENT)
	private IIcon iconInside;

	public BlockHarvester() {
		super(Material.iron);
		this.setCreativeTab(CreativeTabs.tabRedstone);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		this.setLightLevel(1.0f);
		this.setBlockTextureName("mangrove:harvester");
	}

	public int getRenderType() {
		return CommonProxy.harvesterRendererID;
	}
	
	/**
	 * Updates the blocks bounds based on its current state. Args: world, x, y,
	 * z
	 */
	public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int x,
			int y, int z) {
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	/**
	 * Adds all intersecting collision boxes to a list. (Be sure to only add
	 * boxes to the list if they intersect the mask.) Parameters: World, X, Y,
	 * Z, mask, list, colliding entity
	 */
	public void addCollisionBoxesToList(World world, int x, int y, int z,AxisAlignedBB mask, List list, Entity entity) {
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.625F, 1.0F);
		super.addCollisionBoxesToList(world, x, y, z, mask, list, entity);
		float f = 0.125F;
		this.setBlockBounds(0.0F, 0.0F, 0.0F, f, 1.0F, 1.0F);
		super.addCollisionBoxesToList(world, x, y, z, mask, list, entity);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f);
		super.addCollisionBoxesToList(world, x, y, z, mask, list, entity);
		this.setBlockBounds(1.0F - f, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		super.addCollisionBoxesToList(world, x, y, z, mask, list, entity);
		this.setBlockBounds(0.0F, 0.0F, 1.0F - f, 1.0F, 1.0F, 1.0F);
		super.addCollisionBoxesToList(world, x, y, z, mask, list, entity);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	/**
	 * Called when a block is placed using its ItemBlock. Args: World, X, Y, Z,
	 * side, hitX, hitY, hitZ, block metadata
	 */
	public int onBlockPlaced(World world, int x, int y, int z, int side,float hitX, float hitY, float hitZ, int meta) {
		int j1 = Facing.oppositeSide[side];

		if (j1 == 0) {
			j1 = 1;
		}
		return j1;
//		return 1;
	}

	/**
	 * Returns a new instance of a block's tile entity class. Called on placing
	 * the block.
	 */
	public TileEntity createNewTileEntity(World world, int p_149915_2_) {
		return new TileEntityHarvester();
	}

	/**
	 * Called when the block is placed in the world.
	 */
	public void onBlockPlacedBy(World world, int x, int y, int z,
			EntityLivingBase entity, ItemStack itemStack) {
		super.onBlockPlacedBy(world, x, y, z, entity, itemStack);

		if (itemStack.hasDisplayName()) {
			TileEntityHarvester tileentityhopper = func_149920_e(world, x, y, z);
			tileentityhopper.setInventoryName(itemStack.getDisplayName());
		}
	}

	/**
	 * Called whenever the block is added into the world. Args: world, x, y, z
	 */
	public void onBlockAdded(World world, int x, int y, int z) {
		super.onBlockAdded(world, x, y, z);
		this.func_149919_e(world, x, y, z);
	}

	/**
	 * Called upon block activation (right click on the block.)
	 */
	public boolean onBlockActivated(World world, int x, int y, int z,EntityPlayer player, int p_149727_6_, float p_149727_7_,float p_149727_8_, float p_149727_9_) {
		if (world.isRemote) {
			return true;
		} else {
			TileEntityHarvester tileentityharvester = func_149920_e(world, x, y, z);

			if (tileentityharvester != null) {				
				player.openGui(MGThriveForge.instance, 0, world, x, y, z);
			}

			return true;
		}
	}

	/**
	 * Lets the block know when one of its neighbor changes. Doesn't know which
	 * neighbor changed (coordinates passed are their own) Args: x, y, z,
	 * neighbor Block
	 */
	public void onNeighborBlockChange(World world, int x,int y, int z, Block block) {
		this.func_149919_e(world, x, y, z);
	}

	private void func_149919_e(World p_149919_1_, int p_149919_2_,
			int p_149919_3_, int p_149919_4_) {
		int l = p_149919_1_.getBlockMetadata(p_149919_2_, p_149919_3_,
				p_149919_4_);
		int i1 = BlockUtils.getDirectionFromMetadata(l);
		boolean flag = !p_149919_1_.isBlockIndirectlyGettingPowered(
				p_149919_2_, p_149919_3_, p_149919_4_);
		boolean flag1 = BlockUtils.getIsBlockNotPoweredFromMetadata(l);

		if (flag != flag1) {
			p_149919_1_.setBlockMetadataWithNotify(p_149919_2_, p_149919_3_,
					p_149919_4_, i1 | (flag ? 0 : 8), 4);
		}
	}

	public void breakBlock(World p_149749_1_, int p_149749_2_, int p_149749_3_,
			int p_149749_4_, Block p_149749_5_, int p_149749_6_) {
		TileEntityHarvester tileentityhopper = (TileEntityHarvester) p_149749_1_
				.getTileEntity(p_149749_2_, p_149749_3_, p_149749_4_);

		if (tileentityhopper != null) {
			for (int i1 = 0; i1 < tileentityhopper.getSizeInventory(); ++i1) {
				ItemStack itemstack = tileentityhopper.getStackInSlot(i1);

				if (itemstack != null) {
					float f = this.field_149922_a.nextFloat() * 0.8F + 0.1F;
					float f1 = this.field_149922_a.nextFloat() * 0.8F + 0.1F;
					float f2 = this.field_149922_a.nextFloat() * 0.8F + 0.1F;

					while (itemstack.stackSize > 0) {
						int j1 = this.field_149922_a.nextInt(21) + 10;

						if (j1 > itemstack.stackSize) {
							j1 = itemstack.stackSize;
						}

						itemstack.stackSize -= j1;
						EntityItem entityitem = new EntityItem(p_149749_1_,
								(double) ((float) p_149749_2_ + f),
								(double) ((float) p_149749_3_ + f1),
								(double) ((float) p_149749_4_ + f2),
								new ItemStack(itemstack.getItem(), j1,
										itemstack.getItemDamage()));

						if (itemstack.hasTagCompound()) {
							entityitem.getEntityItem().setTagCompound(
									(NBTTagCompound) itemstack.getTagCompound()
											.copy());
						}

						float f3 = 0.05F;
						entityitem.motionX = (double) ((float) this.field_149922_a
								.nextGaussian() * f3);
						entityitem.motionY = (double) ((float) this.field_149922_a
								.nextGaussian() * f3 + 0.2F);
						entityitem.motionZ = (double) ((float) this.field_149922_a
								.nextGaussian() * f3);
						p_149749_1_.spawnEntityInWorld(entityitem);
					}
				}
			}

			p_149749_1_.func_147453_f(p_149749_2_, p_149749_3_, p_149749_4_,
					p_149749_5_);
		}

		super.breakBlock(p_149749_1_, p_149749_2_, p_149749_3_, p_149749_4_,
				p_149749_5_, p_149749_6_);
	}
	

	/**
	 * If this block doesn't render as an ordinary block it will return False
	 * (examples: signs, buttons, stairs, etc)
	 */
	public boolean renderAsNormalBlock() {
		return false;
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
	public boolean shouldSideBeRendered(IBlockAccess p_149646_1_,
			int p_149646_2_, int p_149646_3_, int p_149646_4_, int p_149646_5_) {
		return true;
	}

	/**
	 * Gets the block's texture. Args: side, meta
	 */
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
		return p_149691_1_ == 0 ? this.iconTop : this.iconOutside;
	}

//	public static int getDirectionFromMetadata(int p_149918_0_) {
//		return p_149918_0_ & 7;
//	}

//	public static boolean func_149917_c(int p_149917_0_) {
//		return (p_149917_0_ & 8) != 8;
//	}

	/**
	 * If this returns true, then comparators facing away from this block will
	 * use the value from getComparatorInputOverride instead of the actual
	 * redstone signal strength.
	 */
	public boolean hasComparatorInputOverride() {
		return true;
	}

	/**
	 * If hasComparatorInputOverride returns true, the return value from this is
	 * used instead of the redstone signal strength when this block inputs to a
	 * comparator.
	 */
	public int getComparatorInputOverride(World p_149736_1_, int p_149736_2_,
			int p_149736_3_, int p_149736_4_, int p_149736_5_) {
		return Container.calcRedstoneFromInventory(func_149920_e(p_149736_1_,
				p_149736_2_, p_149736_3_, p_149736_4_));
	}

	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister p_149651_1_) {
		this.iconOutside = p_149651_1_.registerIcon("hopper_outside");
		this.iconTop = p_149651_1_.registerIcon("hopper_top");
		this.iconInside = p_149651_1_.registerIcon("hopper_inside");
	}

	@SideOnly(Side.CLIENT)
	public static IIcon getHopperIcon(String p_149916_0_) {
		return p_149916_0_.equals("harvester_outside") ? MGThriveBlocks.harvester.iconOutside
				: (p_149916_0_.equals("harvester_inside") ? MGThriveBlocks.harvester.iconInside
						: null);
	}

	public static TileEntityHarvester func_149920_e(IBlockAccess blockAccess,
			int x, int y, int z) {
		return (TileEntityHarvester) blockAccess.getTileEntity(x, y, z);
	}

	/**
	 * Gets the icon name of the ItemBlock corresponding to this block. Used by
	 * hoppers.
	 */
	@SideOnly(Side.CLIENT)
	public String getItemIconName() {
		return "mangrove:harvester";
	}
}
