package net.minecraft.mangrove.mod.thrive.autocon;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class SearchUtil {
	
	
	public static List<SearchItem> findAllBlockFrom(final World world, final BlockPos blockPos,final AbstractBlockDuct networktype,final Block... blocks){
		return findAllBlockFrom(world,1,blockPos,networktype,new HashSet<BlockPos>(),blocks);
	}

	private static List<SearchItem> findAllBlockFrom(final World world, int weight, final BlockPos blockPos,final AbstractBlockDuct networktype, final HashSet<BlockPos> uniqueBlockPos,final Block... blockMatches) {
		final List<SearchItem> blockList=new ArrayList<SearchItem>();
		for(EnumFacing facing:EnumFacing.values()){
			final BlockPos offset = blockPos.offset(facing);
			if(uniqueBlockPos.contains(offset)){
				continue;
			}
			uniqueBlockPos.add(offset);
			final Block block = world.getBlockState(offset).getBlock();
			for(Block blockMatch:blockMatches){
				if( blockMatch==block){				
					blockList.add(new SearchItem(offset, facing, weight));
				}
			}
			if( block==networktype){
				blockList.addAll(findAllBlockFrom(world, weight+1,offset, networktype, uniqueBlockPos,blockMatches));
			}
		}
		return blockList;
	}
	
	public static List<SearchItem> findAllTileFrom(final World world, final BlockPos blockPos,final AbstractBlockDuct networktype,final Class<?> clazz){
		return findAllTileFrom(world,1,blockPos,networktype,clazz,new HashSet<BlockPos>());
	}

	private static List<SearchItem> findAllTileFrom(final World world, int weight,final BlockPos blockPos,final AbstractBlockDuct networktype,final Class<?> clazz, final HashSet<BlockPos> uniqueBlockPos) {
		final List<SearchItem> blockList=new ArrayList<SearchItem>();
		for(EnumFacing facing:EnumFacing.values()){
			final BlockPos offset = blockPos.offset(facing);
			if(uniqueBlockPos.contains(offset)){
				continue;
			}
			uniqueBlockPos.add(offset);
			final Block block = world.getBlockState(offset).getBlock();
			TileEntity tile = world.getTileEntity(offset);
			if( clazz.isInstance(tile)){				
				blockList.add(new SearchItem(offset, facing, weight));
			}
			if( block==networktype){
				blockList.addAll(findAllTileFrom(world,weight+1, offset, networktype, clazz,uniqueBlockPos));
			}
		}
		return blockList;
	}
	
	public static SearchItem findFirstTileFrom(final World world, final BlockPos blockPos,final AbstractBlockDuct networktype,final Class<?> clazz){
		return findFirstTileFrom(world,1,blockPos,networktype,clazz,new HashSet<BlockPos>());
	}

	private static SearchItem findFirstTileFrom(final World world, int weight,final BlockPos blockPos,final AbstractBlockDuct networktype,final Class<?> clazz, final HashSet<BlockPos> uniqueBlockPos) {
		for(EnumFacing facing:EnumFacing.values()){
			final BlockPos offset = blockPos.offset(facing);
			if(uniqueBlockPos.contains(offset)){
				continue;
			}
			uniqueBlockPos.add(offset);
			final Block block = world.getBlockState(offset).getBlock();
			TileEntity tile = world.getTileEntity(offset);
			if( clazz.isInstance(tile)){				
				return new SearchItem(offset, facing, weight);
				
			}
			if( block==networktype){
				return findFirstTileFrom(world,weight+1, offset, networktype, clazz,uniqueBlockPos);
			}
		}
		return null;
	}
}
