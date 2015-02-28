package net.minecraft.mangrove.mod.thrive.autocon.harvester.miner;

import net.minecraft.mangrove.mod.thrive.autocon.harvester.AbstractBlockHarvester;
import net.minecraft.mangrove.mod.thrive.autocon.harvester.AbstractTileHarvester;
import net.minecraft.world.World;

public class BlockHarvesterMiner extends AbstractBlockHarvester{

	public BlockHarvesterMiner() {
		super("harvester_miner");
	}

	@Override
	protected AbstractTileHarvester createNewTileHarvester(World worldIn, int meta) {
		return new TileHarvesterMiner();
	}

}
