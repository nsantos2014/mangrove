package net.minecraft.mangrove.mod.thrive.robofarmer.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.mangrove.mod.thrive.robofarmer.entity.TileFarmerNode;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockFarmerNode extends BlockContainer {
	@SideOnly(Side.CLIENT)
	protected IIcon blockIcon;
	@SideOnly(Side.CLIENT)
	protected IIcon blockIconTop;
	@SideOnly(Side.CLIENT)
	protected IIcon blockIconFront;

	public BlockFarmerNode() {
		super(Material.wood);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		setBlockName("farmer_node");
		setBlockTextureName("crafting_table");
		this.setCreativeTab(CreativeTabs.tabRedstone);
	}

	/**
	 * Updates the blocks bounds based on its current state. Args: world, x, y,
	 * z
	 */
	public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int x,
			int y, int z) {
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}
	
	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
//		System.out.println("BlockAdded:"+world);
		super.onBlockAdded(world, x, y, z);
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z,
			EntityLivingBase player, ItemStack iStack) {

		int playerOrientation = MathHelper.floor_double((double) (player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		ForgeDirection playerOr=ForgeDirection.UNKNOWN;
		switch (playerOrientation) {
		case 0:
			playerOr=ForgeDirection.SOUTH;
			world.setBlockMetadataWithNotify(x, y, z, 2, 2);
			break;
		case 1:
			playerOr=ForgeDirection.WEST;
			world.setBlockMetadataWithNotify(x, y, z, 5, 2);
			break;
		case 2:
			playerOr=ForgeDirection.NORTH;
			world.setBlockMetadataWithNotify(x, y, z, 3, 2);
			break;
		case 3:
			playerOr=ForgeDirection.EAST;
			world.setBlockMetadataWithNotify(x, y, z, 4, 2);
			break;	
		}
		System.out.println("Direction :"+playerOrientation+":"+ForgeDirection.getOrientation(playerOrientation)+":"+playerOr+":"+playerOr.ordinal());
		
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
//		System.out.println("New Tile:"+world);
		return new TileFarmerNode();
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(IIconRegister register) {
		this.blockIcon = register.registerIcon("planks_birch");
		this.blockIconTop = register.registerIcon("piston_top_normal");
		this.blockIconFront = register.registerIcon("log_birch_top");
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		final ForgeDirection dir = ForgeDirection.getOrientation(meta);
		return side == 0 || side == 1
					? this.blockIconTop 
					: (side == dir.ordinal() 
						? this.blockIconFront
					    : this.blockIcon);

		// return side == 1 ? this.blockIconTop : (side == 0 ? Blocks.planks
		// .getBlockTextureFromSide(side)
		// : (side != 2 && side != 4 ? this.blockIcon
		// : this.blockIconFront));
	}
	
}
