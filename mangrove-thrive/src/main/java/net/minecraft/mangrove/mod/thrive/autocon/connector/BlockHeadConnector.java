package net.minecraft.mangrove.mod.thrive.autocon.connector;

import java.util.List;

import net.minecraft.mangrove.mod.thrive.MGThriveBlocks;
import net.minecraft.mangrove.mod.thrive.autocon.AbstractBlockDuct;
import net.minecraft.mangrove.mod.thrive.autocon.AbstractTileDuct;
import net.minecraft.mangrove.mod.thrive.autocon.SearchItem;
import net.minecraft.mangrove.mod.thrive.autocon.SearchUtil;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockHeadConnector extends AbstractBlockDuct{

	public BlockHeadConnector() {
		super("duct_connector");
	}

	@Override
	protected AbstractTileDuct createNewTileDuct(World worldIn, int meta) {
		return new TileHeadConnector();
	}
	
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		boolean canPlaceBlockAt = super.canPlaceBlockAt(worldIn, pos);
		if( canPlaceBlockAt){
			List<SearchItem> result = SearchUtil.findAllBlockFrom(worldIn, pos, MGThriveBlocks.duct_connector, MGThriveBlocks.storage_junction);			
			// TODO Send message informing that a broker cannot be added to a network with a broker 
			return result.size()<2;
		}
		return canPlaceBlockAt;
	}

}
