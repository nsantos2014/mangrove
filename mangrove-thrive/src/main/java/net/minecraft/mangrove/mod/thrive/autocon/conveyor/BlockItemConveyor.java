package net.minecraft.mangrove.mod.thrive.autocon.conveyor;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.mangrove.mod.thrive.MGThriveBlocks;
import net.minecraft.mangrove.mod.thrive.autocon.AbstractBlockDuct;
import net.minecraft.mangrove.mod.thrive.autocon.AbstractTileDuct;
import net.minecraft.mangrove.mod.thrive.autocon.SearchItem;
import net.minecraft.mangrove.mod.thrive.autocon.SearchUtil;
import net.minecraft.mangrove.mod.thrive.autocon.itembroker.TileItemBroker;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockItemConveyor extends AbstractBlockDuct{

	public BlockItemConveyor() {
		super("duct_conveyor");
	}
	
//	@Override
//	public void onNeighborChange(IBlockAccess worldIn, BlockPos pos, BlockPos neighborPos) {
//		super.onNeighborChange(worldIn, pos, neighborPos);
////		System.out.println("Neighbor Block changed");
//		TileEntity tile = worldIn.getTileEntity(pos);
//		if (tile instanceof TileItemConveyor) {
//			TileItemConveyor itemBroker = (TileItemConveyor) tile;
//			
//			for(EnumFacing facing:EnumFacing.values()){
//				BlockPos offset = pos.offset(facing);
//				TileEntity testTile = worldIn.getTileEntity(offset);
//				Block testBlock = worldIn.getBlockState(neighborPos).getBlock();
//				if (testTile instanceof TileItemConveyor) {
//					TileItemConveyor testTransport = (TileItemConveyor) testTile;
//					itemBroker.updateConnectorStatus(facing,testBlock==MGThriveBlocks.duct_conveyor);
//					
//				}
//			}
//		}
//	}
	
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		boolean canPlaceBlockAt = super.canPlaceBlockAt(worldIn, pos);
		if( canPlaceBlockAt){
			List<SearchItem> result = SearchUtil.findAllBlockFrom(worldIn, pos, MGThriveBlocks.duct_conveyor, MGThriveBlocks.item_broker);			
			// TODO Send message informing that a broker cannot be added to a network with a broker 
			return result.size()<2;
		}
		return canPlaceBlockAt;
	}

	@Override
	protected AbstractTileDuct createNewTileDuct(World worldIn, int meta) {
		return new TileItemConveyor();
	}

}
