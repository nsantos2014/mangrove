package net.minecraft.mangrove.mod.thrive.autocon.harvester.miner;

import java.util.List;

import net.minecraft.mangrove.mod.thrive.MGThriveBlocks;
import net.minecraft.mangrove.mod.thrive.autocon.SearchItem;
import net.minecraft.mangrove.mod.thrive.autocon.SearchUtil;
import net.minecraft.mangrove.mod.thrive.autocon.harvester.AbstractBlockHarvester;
import net.minecraft.mangrove.mod.thrive.autocon.harvester.AbstractTileHarvester;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockHarvesterMiner extends AbstractBlockHarvester{

	public BlockHarvesterMiner() {
		super("harvester_miner");
	}

	@Override
	protected AbstractTileHarvester createNewTileHarvester(World worldIn, int meta) {
		return new TileHarvesterMiner();
	}
	
	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		boolean canPlaceBlockAt = super.canPlaceBlockAt(worldIn, pos);
		if (canPlaceBlockAt) {
			List<SearchItem> result = SearchUtil.findAllBlockFrom(worldIn, pos,
					MGThriveBlocks.duct_connector,
					MGThriveBlocks.harvester_farmer);
			canPlaceBlockAt=result.isEmpty();
		}
		return canPlaceBlockAt;
	}

}
