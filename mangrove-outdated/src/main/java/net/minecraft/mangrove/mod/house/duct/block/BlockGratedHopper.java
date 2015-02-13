package net.minecraft.mangrove.mod.house.duct.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.mangrove.core.core.utils.BlockUtils;
import net.minecraft.mangrove.mod.house.MGHouseBlocks;
import net.minecraft.mangrove.mod.house.MGHouseForge;
import net.minecraft.mangrove.mod.house.duct.AbstractBlockDuct;
import net.minecraft.mangrove.mod.house.duct.entity.TileEntityGratedHopper;
import net.minecraft.mangrove.mod.house.proxy.CommonProxy;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockGratedHopper extends AbstractBlockDuct {
	private final Random field_94457_a = new Random();

//	@SideOnly(Side.CLIENT)
//	private IIcon hopperIcon;
//
//	@SideOnly(Side.CLIENT)
//	private IIcon hopperTopIcon;
//
//	@SideOnly(Side.CLIENT)
//	private IIcon hopperInsideIcon;

	public BlockGratedHopper() {
		super();

		setBlockName("gratedhopper");
//		setCreativeTab(CreativeTabs.tabRedstone);
//		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
//		setHardness(2.0F);
//		setResistance(8.0F);
	}

	public int getRenderType() {
		return CommonProxy.ductRendererID;
	}

	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntityGratedHopper();
	}

	public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2,
			int par3, int par4) {
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	public void addCollisionBoxesToList(World par1World, int par2, int par3, int par4,
			AxisAlignedBB par5AxisAlignedBB, List par6List, Entity par7Entity) {
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.625F, 1.0F);
		super.addCollisionBoxesToList(par1World, par2, par3, par4, par5AxisAlignedBB,
				par6List, par7Entity);
		float f = 0.125F;
		setBlockBounds(0.0F, 0.0F, 0.0F, f, 1.0F, 1.0F);
		super.addCollisionBoxesToList(par1World, par2, par3, par4, par5AxisAlignedBB,
				par6List, par7Entity);
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f);
		super.addCollisionBoxesToList(par1World, par2, par3, par4, par5AxisAlignedBB,
				par6List, par7Entity);
		setBlockBounds(1.0F - f, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		super.addCollisionBoxesToList(par1World, par2, par3, par4, par5AxisAlignedBB,
				par6List, par7Entity);
		setBlockBounds(0.0F, 0.0F, 1.0F - f, 1.0F, 1.0F, 1.0F);
		super.addCollisionBoxesToList(par1World, par2, par3, par4, par5AxisAlignedBB,
				par6List, par7Entity);
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	public int 	onBlockPlaced(World par1World, int par2, int par3, int par4,
			int par5, float par6, float par7, float par8, int par9) {
		int j1 = Facing.oppositeSide[par5];

		if (j1 == 1) {
			j1 = 0;
		}

		return j1;
	}

	public void onBlockPlacedBy(World par1World, int par2, int par3, int par4,
			EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack) {
		super.onBlockPlacedBy(par1World, par2, par3, par4, par5EntityLivingBase,
				par6ItemStack);

		if (!(par6ItemStack.hasDisplayName()))
			return;
		TileEntityGratedHopper tileentityhopper = getGratedHopperTile(
				par1World, par2, par3, par4);
		tileentityhopper.setInventoryName(par6ItemStack.getDisplayName());
	}

	public void onBlockAdded(World par1World, int par2, int par3, int par4) {
		super.onBlockAdded(par1World, par2, par3, par4);
		updateMetadata(par1World, par2, par3, par4);
	}

	public boolean onBlockActivated(World par1World, int par2, int par3, int par4,
			EntityPlayer par5EntityPlayer, int par6, float par7, float par8,
			float par9) {
		if (par1World.isRemote) {
			return true;
		}

		if (par5EntityPlayer.getHeldItem() != null) {
			Item held = par5EntityPlayer.getHeldItem().getItem();

			if (held == Item.getItemFromBlock(this))
				return false;
			if (held == Item.getItemFromBlock(MGHouseBlocks.duct))
				return false;
			if (held == Item.getItemFromBlock(Blocks.hopper))
				return false;

		}

		TileEntityGratedHopper tileentityhopper = getGratedHopperTile(
				par1World, par2, par3, par4);

		if (tileentityhopper != null) {
			par5EntityPlayer.openGui(MGHouseForge.instance, 0, par1World,
					par2, par3, par4);
		}

		return true;
	}

	public void onNeighborChange(IBlockAccess world, int x, int y, int z,
			int tileX, int tileY, int tileZ) {
		updateMetadata(world, x, y, z);
	}

	private void updateMetadata(IBlockAccess par1World, int par2, int par3,
			int par4) {
		int l = par1World.getBlockMetadata(par2, par3, par4);
		int i1 = BlockUtils.getDirectionFromMetadata(l);
		boolean flag = !(((World) par1World).isBlockIndirectlyGettingPowered(par2, par3, par4));
		boolean flag1 = BlockUtils.getIsBlockNotPoweredFromMetadata(l);

		if (flag == flag1)
			return;
		((World) par1World).setBlockMetadataWithNotify(par2, par3, par4, i1
				| ((flag) ? 0 : 8), 4);
	}

	public void breakBlock(World par1World, int par2, int par3, int par4, Block par5, int par6) {
		TileEntityGratedHopper tileentityhopper = (TileEntityGratedHopper) par1World
				.getTileEntity(par2, par3, par4);

		if (tileentityhopper != null) {
			for (int j1 = 0; j1 < 10; ++j1) {
				ItemStack itemstack = tileentityhopper.getStackInSlot(j1);

				if (itemstack == null)
					continue;
				float f = this.field_94457_a.nextFloat() * 0.8F + 0.1F;
				float f1 = this.field_94457_a.nextFloat() * 0.8F + 0.1F;
				float f2 = this.field_94457_a.nextFloat() * 0.8F + 0.1F;

				while (itemstack.stackSize > 0) {
					int k1 = this.field_94457_a.nextInt(21) + 10;

					if (k1 > itemstack.stackSize) {
						k1 = itemstack.stackSize;
					}

					itemstack.stackSize -= k1;
					EntityItem entityitem = new EntityItem(par1World, par2 + f,
							par3 + f1, par4 + f2, new ItemStack(
									itemstack.getItem(), k1,
									itemstack.getItemDamage()));

					if (itemstack.hasTagCompound()) {
						entityitem.getEntityItem().setTagCompound(
								(NBTTagCompound) itemstack.getTagCompound()
										.copy());
					}

					float f3 = 0.05F;
					entityitem.motionX = ((float) this.field_94457_a
							.nextGaussian() * f3);
					entityitem.motionY = ((float) this.field_94457_a
							.nextGaussian() * f3 + 0.2F);
					entityitem.motionZ = ((float) this.field_94457_a
							.nextGaussian() * f3);
					par1World.spawnEntityInWorld(entityitem);
				}

			}

			par1World.func_147453_f(par2, par3, par4, par5);
		}

		super.breakBlock(par1World, par2, par3, par4, par5, par6);
	}

	public boolean renderAsNormalBlock() {
		return false;
	}

	public boolean isOpaqueCube() {
		return false;
	}

//	public static int getDirectionFromMetadata(int par0) {
//		return (par0 & 0x7);
//	}

	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2,
			int par3, int par4, int par5) {
		return true;
	}

//	@SideOnly(Side.CLIENT)
//	public IIcon getIcon(int par1, int par2) {
//		return ((par1 == 1) ? this.hopperTopIcon : this.hopperIcon);
//	}

//	public static boolean getIsBlockNotPoweredFromMetadata(int par0) {
//		return ((par0 & 0x8) != 8);
//	}

	public boolean hasComparatorInputOverrideM() {
		return true;
	}

	public int getComparatorInputOverride(World par1World, int par2, int par3, int par4,
			int par5) {
		return Container.calcRedstoneFromInventory(getGratedHopperTile(par1World, par2,
				par3, par4));
	}

//	@SideOnly(Side.CLIENT)
//	public void registerBlockIcons(IIconRegister par1IconRegister) {
//		this.hopperIcon = par1IconRegister.registerIcon("hopper_outside");
//		this.hopperTopIcon = par1IconRegister.registerIcon("hopper_top");
//		this.hopperInsideIcon = par1IconRegister.registerIcon("hopper_inside");
//	}

//	@SideOnly(Side.CLIENT)
//	public static IIcon getHopperIcon(String par0Str) {
//		return ((par0Str.equals("hopper_inside")) ? MyMod.instance.gratedhopper.hopperInsideIcon
//				: (par0Str.equals("hopper_outside")) ? MyMod.instance.gratedhopper.hopperIcon
//						: null);
//	}

	@SideOnly(Side.CLIENT)
	public String getItemIconName() {
		return "mangrove:gratedhopper";
	}

	public static TileEntityGratedHopper getGratedHopperTile(
			IBlockAccess par0IBlockAccess, int par1, int par2, int par3) {
		return ((TileEntityGratedHopper) par0IBlockAccess.getTileEntity(par1,
				par2, par3));
	}
}
