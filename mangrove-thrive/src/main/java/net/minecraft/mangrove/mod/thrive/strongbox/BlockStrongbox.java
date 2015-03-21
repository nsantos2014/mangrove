package net.minecraft.mangrove.mod.thrive.strongbox;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.mangrove.core.inventory.block.AbstractBlockInventory;
import net.minecraft.mangrove.mod.thrive.MGThriveForge;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockStrongbox extends AbstractBlockInventory {
    private final Random field_149933_a = new Random();
    private int p_149727_2_;
    private String name = "strongbox";

    public BlockStrongbox() {
        super(Material.wood);
        setCreativeTab(CreativeTabs.tabRedstone);

        GameRegistry.registerBlock(this, name);
        setUnlocalizedName(MGThriveForge.ID + "_" + name);
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        setHardness(2.0F);
        setResistance(8.0F);
    }


    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        super.onBlockAdded(worldIn, pos, state);

    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (playerIn.isSneaking()) {
            return false;
        }
        if (!worldIn.isRemote) {
            playerIn.openGui(MGThriveForge.instance, 0, worldIn, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileEntityStrongbox();
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
        return true;
    }
    
    @SideOnly(Side.CLIENT)
    public EnumWorldBlockLayer getBlockLayer(){
      return EnumWorldBlockLayer.SOLID; 
   }
    
    public int getRenderType(){
        return 3;
    }
    
    @Override
    public boolean isFullBlock() {
        return true;
    }

    @Override
    public boolean isOpaqueCube() {
        return true;
    }


	public String getName() {
		return this.name;
	}
	    
}
