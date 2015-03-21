package net.minecraft.mangrove.mod.thrive.autocon.conveyor;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.mangrove.mod.thrive.MGThriveBlocks;
import net.minecraft.mangrove.mod.thrive.autocon.AbstractBlockDuct;
import net.minecraft.mangrove.mod.thrive.autocon.AbstractTileDuct;
import net.minecraft.mangrove.mod.thrive.autocon.SearchItem;
import net.minecraft.mangrove.mod.thrive.autocon.SearchUtil;
import net.minecraft.mangrove.mod.thrive.autocon.itembroker.TileItemBroker;
import net.minecraft.mangrove.network.NetBus;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockItemConveyor extends AbstractBlockDuct{

	public BlockItemConveyor() {
		super("duct_conveyor");
	}
	
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		boolean canPlaceBlockAt = super.canPlaceBlockAt(worldIn, pos);
		if( canPlaceBlockAt){
			List<SearchItem> result = SearchUtil.findAllBlockFrom(worldIn, pos, MGThriveBlocks.duct_conveyor, MGThriveBlocks.item_broker,MGThriveBlocks.autobench);
			if( result.size()>1){
				NetBus.notify(getName(), "Cannot connect two networks");
			}
			return result.size()<2;
		}
		return canPlaceBlockAt;
	}

	@Override
	protected AbstractTileDuct createNewTileDuct(World worldIn, int meta) {
		return new TileItemConveyor();
	}

}
