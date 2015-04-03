package net.minecraft.mangrove.mod.thrive.cistern;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
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

public class BlockCistern extends Block implements ITileEntityProvider {

	private String name = "cistern";

	public BlockCistern() {
		super(Material.iron);
		setCreativeTab(CreativeTabs.tabRedstone);

		GameRegistry.registerBlock(this, name);
		setUnlocalizedName(MGThriveForge.ID + "_" + name);
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		setHardness(2.0F);
		setResistance(8.0F);
	}

	public String getName() {
		return name;
	}

	public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List list, Entity collidingEntity) {
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.3125F, 1.0F);
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
		this.setBlockBoundsForItemRender();
	}

	public void setBlockBoundsForItemRender() {
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public boolean isFullCube() {
		return false;
	}
	
	public int getRenderType() {
		return 2;
	}
	@Override
	public EnumWorldBlockLayer getBlockLayer() {
		 return EnumWorldBlockLayer.TRANSLUCENT;
	}
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
		return true;
	}
	@Override
	public boolean canBeReplacedByLeaves(IBlockAccess world, BlockPos pos) {
		return false;
	}
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
		// int i = ((Integer) state.getValue(LEVEL)).intValue();
		// float f = (float) pos.getY() + (6.0F + (float) (3 * i)) / 16.0F;
		//
		// if (!worldIn.isRemote && entityIn.isBurning() && i > 0 &&
		// entityIn.getEntityBoundingBox().minY <= (double) f) {
		// entityIn.extinguish();
		// this.setWaterLevel(worldIn, pos, state, i - 1);
		// }
	}

	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (worldIn.isRemote) {
			return true;
		} else {
			TileEntity tile = worldIn.getTileEntity(pos);
			if (tile instanceof TileCistern) {
				TileCistern tileCistern = (TileCistern) tile;
				ItemStack itemstack = playerIn.inventory.getCurrentItem();
				Item item = itemstack.getItem();
				if (item instanceof ItemBucket) {
					ItemBucket itemBucket = (ItemBucket) item;
					if( itemBucket==Items.bucket){
						//check if can retrieve 
						if(tileCistern.canRetrieve()){
							playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, new ItemStack(tileCistern.retrieve()));	
						}
						 
					}else{
						if(tileCistern.canPlace(itemBucket)){
							playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, new ItemStack(tileCistern.place(itemBucket)));	
						}
					}
				}
				return true;
			}
						
		}
		return false;
	}

	public void fillWithRain(World worldIn, BlockPos pos) {
		if (worldIn.rand.nextInt(20) == 1) {
			IBlockState iblockstate = worldIn.getBlockState(pos);

		}
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileCistern();
	}

}
