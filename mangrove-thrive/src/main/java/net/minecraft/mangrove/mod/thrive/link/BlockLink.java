package net.minecraft.mangrove.mod.thrive.link;

import java.util.List;

import com.mojang.authlib.properties.PropertyMap;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.mangrove.mod.thrive.MGThriveForge;
import net.minecraft.mangrove.mod.thrive.duct.AbstractTileDuct;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockLink extends BlockContainer{
	public static final PropertyInteger NETWORK_ID = PropertyInteger.create("networkid",0,3);	
	private String name;

	public BlockLink() {
		super(Material.circuits);
		this.name = "link";
		GameRegistry.registerBlock(this, name);
		setUnlocalizedName(MGThriveForge.ID + "_" + name);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		this.setCreativeTab(CreativeTabs.tabRedstone);
		setHardness(2.0F);
		setResistance(8.0F);	
		
		 
		this.setDefaultState(this.blockState.getBaseState().withProperty(NETWORK_ID, 0));
	}

	public String getName() {
		return name;
	}
	
	public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
		this.setBlockBounds(0f, 0f, 0f, 1f, 1f, 1f);
	}

	public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List list, Entity collidingEntity) {
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.625F, 1.0F);
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
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list)
    {
        for (int i = 0; i < 4; ++i)
        {
        	ItemBlock itemBlock = new ItemBlock(this);
        	itemBlock.setUnlocalizedName(getUnlocalizedName()+"_0");
            list.add(new ItemStack(itemBlock, 1, i));
        }
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
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
		super.onBlockAdded(worldIn, pos, state);
		System.out.println("State :"+state.getValue(NETWORK_ID));
	}
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityLink();
	}
	
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(NETWORK_ID,meta &0x07);
	}


	public int getMetaFromState(IBlockState state) {
		return (Integer) state.getValue(NETWORK_ID);
	}

	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { NETWORK_ID });
	}
}
