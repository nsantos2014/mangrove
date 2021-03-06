package net.minecraft.mangrove.mod.house.doors.drawbridge;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.mangrove.mod.house.MGHouseForge;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemDrawbridge extends ItemBlock {
	private final String	name;
	private Block	block;

	public ItemDrawbridge(Block block) {
		super(block);
		this.name="drawbridge_item";
		this.block = block;
		GameRegistry.registerItem(this, name, MGHouseForge.ID);
		this.setCreativeTab(CreativeTabs.tabRedstone);
	}
	
	public String getName() {
	    return name;
    }

	public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (side != EnumFacing.UP) {
			return false;
		} else {
			IBlockState iblockstate = worldIn.getBlockState(pos);
			Block block = iblockstate.getBlock();

			if (!block.isReplaceable(worldIn, pos)) {
				pos = pos.offset(side);
			}

			if (!playerIn.canPlayerEdit(pos, side, stack)) {
				return false;
			} else if (!this.block.canPlaceBlockAt(worldIn, pos)) {
				return false;
			} else {
				placeDoor(worldIn, pos, EnumFacing.fromAngle((double) playerIn.rotationYaw), this.block);
				--stack.stackSize;
				return true;
			}
		}
	}

	public static void placeDoor(World worldIn, BlockPos pos, EnumFacing facing, Block door) {
//		BlockPos blockpos1 = pos.offset(facing.rotateY());
//		BlockPos blockpos2 = pos.offset(facing.rotateYCCW());
//		int i = (worldIn.getBlockState(blockpos2).getBlock().isNormalCube() ? 1 : 0) + (worldIn.getBlockState(blockpos2.up()).getBlock().isNormalCube() ? 1 : 0);
//		int j = (worldIn.getBlockState(blockpos1).getBlock().isNormalCube() ? 1 : 0) + (worldIn.getBlockState(blockpos1.up()).getBlock().isNormalCube() ? 1 : 0);
//		boolean flag = worldIn.getBlockState(blockpos2).getBlock() == door || worldIn.getBlockState(blockpos2.up()).getBlock() == door;
//		boolean flag1 = worldIn.getBlockState(blockpos1).getBlock() == door || worldIn.getBlockState(blockpos1.up()).getBlock() == door;
//		boolean flag2 = false;
//
//		if (flag && !flag1 || j > i) {
//			flag2 = true;
//		}

		IBlockState iblockstate = door.getDefaultState().withProperty(BlockDrawbridge.FACING, facing).withProperty(BlockDrawbridge.TYPE, BlockDrawbridge.ComponentType.HINGE)
		        .withProperty(BlockDrawbridge.OPEN, true);
		worldIn.setBlockState(pos, iblockstate, 2);
		worldIn.notifyNeighborsOfStateChange(pos, door);
		for( int i=1; i<4; i++){
			BlockPos blockpos3 = pos.up(i);
			worldIn.setBlockState(blockpos3, iblockstate.withProperty(BlockDrawbridge.TYPE, BlockDrawbridge.ComponentType.PLANK), 2);		
			worldIn.notifyNeighborsOfStateChange(blockpos3, door);
			
//			blockpos3 = pos.offset(facing,i);
//			worldIn.setBlockState(blockpos3, iblockstate.withProperty(BlockDrawbridge.TYPE, BlockDrawbridge.ComponentType.PLANK), 2);		
//			worldIn.notifyNeighborsOfStateChange(blockpos3, door);
		}
	}
}
