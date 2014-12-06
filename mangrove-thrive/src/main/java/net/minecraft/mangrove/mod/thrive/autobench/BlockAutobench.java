package net.minecraft.mangrove.mod.thrive.autobench;

import java.util.List;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.mangrove.core.block.AbstractBlockInventory;
import net.minecraft.mangrove.core.utils.BlockUtils;
import net.minecraft.mangrove.mod.thrive.MGThriveForge;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockAutobench extends AbstractBlockInventory{

	@SideOnly(Side.CLIENT)
    private IIcon blockIconTop;
    @SideOnly(Side.CLIENT)
    private IIcon blockIconFront;
	private int p_149727_2_;
    
	public BlockAutobench() {
		super(Material.wood);
		setCreativeTab(CreativeTabs.tabRedstone);
		setBlockName("autobench");
		setBlockTextureName("crafting_table");
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		setHardness(2.0F);
		setResistance(8.0F);
	}
	
	 /**
     * Gets the block's texture. Args: side, meta
     */
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta){
        return side == 1 ? this.blockIconTop : (side == 0 ? Blocks.planks.getBlockTextureFromSide(side) : (side != 2 && side != 4 ? this.blockIcon : this.blockIconFront));
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register){
        this.blockIcon = register.registerIcon(this.getTextureName() + "_side");
        this.blockIconTop = register.registerIcon(this.getTextureName() + "_top");
        this.blockIconFront = register.registerIcon(this.getTextureName() + "_front");
    }
    
    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
    	return super.canPlaceBlockAt(world, x, y, z);
    }
    
	@Override
	public void onBlockClicked(World world, int x,
			int y, int z, EntityPlayer player) {
	
		super.onBlockClicked(world, x, y, z,player);		
	}

	
	
//    /**
//	 * Called when a block is placed using its ItemBlock. Args: World, X, Y, Z,
//	 * side, hitX, hitY, hitZ, block metadata
//	 */
//	public int onBlockPlaced(World world, int x, int y, int z, int side,float hitX, float hitY, float hitZ, int meta) {
//		int j1 = Facing.oppositeSide[side];
//
//		if (j1 == 0 || j1==1) {
//			j1 = 2;
//		}
//		return j1;
////		return 1;
//	}
//	
//	/**
//	 * Called when the block is placed in the world.
//	 */
//	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemStack) {
//		super.onBlockPlacedBy(world, x, y, z, entity, itemStack);
//		int l = MathHelper.floor_double((double)(entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
//
//        if (l == 0){
//            world.setBlockMetadataWithNotify(x, y, z, 2, 2);
//        }
//
//        if (l == 1){
//            world.setBlockMetadataWithNotify(x, y, z, 5, 2);
//        }
//
//        if (l == 2){
//            world.setBlockMetadataWithNotify(x, y, z, 3, 2);
//        }
//
//        if (l == 3){
//            world.setBlockMetadataWithNotify(x, y, z, 4, 2);
//        }	
//	}
    /**
     * Called upon block activation (right click on the block.)
     */
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_){
    	super.onBlockActivated(world, x, y, z, player, p_149727_6_, p_149727_7_, p_149727_8_, p_149727_9_);
//        this.p_149727_2_ = x;
    	if (player.isSneaking()) {
			return false;
		}
		if (!world.isRemote){
//			TileEntity entity = world.getTileEntity(x, y, z);			
        	player.openGui(MGThriveForge.instance, 0, world,x, y, z);
            
        }
		return true;
    }
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileEntityAutobench();
    }

	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2,
			int par3, int par4, int par5) {
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
	public void onBlockAdded(World par1World, int par2, int par3, int par4) {
		super.onBlockAdded(par1World, par2, par3, par4);
		updateMetadata(par1World, par2, par3, par4);
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
}
