package net.minecraft.mangrove.mod.house.matplace;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public abstract class ItemMaterialPlacer extends Item{
	
	public ItemMaterialPlacer(Block block) {
    }

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
	    IBlockState blockstate = worldIn.getBlockState(pos);
	    System.out.println("Use Item on : "+blockstate);
	    if( blockstate.getBlock().getMaterial().isReplaceable()){
	    	return true;
	    }
	    
	    if( blockstate.getBlock()==Blocks.stone){
	    	IBlockState newState = Blocks.cobblestone.getDefaultState();
	    	worldIn.setBlockState(pos, newState, 2);
	    	return true;
	    }
		return false;
	}
	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 100;
	}
	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
	    return EnumAction.BLOCK;
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn,
			EntityPlayer playerIn) {
		System.out.println("onItemRightClick");
		playerIn.setItemInUse(itemStackIn, this.getMaxItemUseDuration(itemStackIn));
		return super.onItemRightClick(itemStackIn, worldIn, playerIn);
	}
}
