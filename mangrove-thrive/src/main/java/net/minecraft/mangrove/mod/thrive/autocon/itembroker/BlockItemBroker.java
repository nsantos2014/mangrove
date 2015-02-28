package net.minecraft.mangrove.mod.thrive.autocon.itembroker;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.mangrove.mod.thrive.MGThriveBlocks;
import net.minecraft.mangrove.mod.thrive.MGThriveForge;
import net.minecraft.mangrove.mod.thrive.autocon.AbstractBlockAutocon;
import net.minecraft.mangrove.mod.thrive.autocon.AbstractTileAutocon;
import net.minecraft.mangrove.mod.thrive.autocon.SearchItem;
import net.minecraft.mangrove.mod.thrive.autocon.SearchUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.World;

public class BlockItemBroker extends AbstractBlockAutocon {

	public BlockItemBroker() {
		super("item_broker");
	}

	@Override
	public AbstractTileAutocon createNewTileAutocon(World worldIn, int meta) {
		return new TileItemBroker();
	}
	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		boolean canPlaceBlockAt = super.canPlaceBlockAt(worldIn, pos);
		if( canPlaceBlockAt){
			List<SearchItem> result = SearchUtil.findAllBlockFrom(worldIn, pos, MGThriveBlocks.duct_conveyor, MGThriveBlocks.item_broker);
			// TODO Send message informing that a broker cannot be added to a network with a broker 
			return result.isEmpty();
		}
		return canPlaceBlockAt;
	}
	
	@Override
	public void onNeighborChange(IBlockAccess worldIn, BlockPos pos, BlockPos neighborPos) {
		super.onNeighborChange(worldIn, pos, neighborPos);
//		System.out.println("Neighbor Block changed");
		TileEntity tile = worldIn.getTileEntity(pos);
		if (tile instanceof TileItemBroker) {
			TileItemBroker itemBroker = (TileItemBroker) tile;
			Block neighborBlock = worldIn.getBlockState(neighborPos).getBlock();
			
			for(EnumFacing facing:EnumFacing.values()){
				if( pos.offset(facing).equals(neighborPos) ){
					itemBroker.updateConnectorStatus(facing,neighborBlock==MGThriveBlocks.duct_conveyor);
					return;
				}
			}
		}
	}

	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (worldIn.isRemote) {
			worldIn.markBlockForUpdate(pos); // Makes the server call getDescriptionPacket for a full data sync
//			markDirty(); // Marks the chunk as dirty, so that it is saved properly on changes. Not required for the sync specifically, but usually goes alongside the former.
			return true;
		} else {
			
			playerIn.openGui(MGThriveForge.instance, 0, worldIn, pos.getX(), pos.getY(), pos.getZ());
			return true;
		}
	}
}
