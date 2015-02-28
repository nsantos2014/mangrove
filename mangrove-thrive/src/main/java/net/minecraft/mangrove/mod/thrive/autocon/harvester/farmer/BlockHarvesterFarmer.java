package net.minecraft.mangrove.mod.thrive.autocon.harvester.farmer;

import net.minecraft.mangrove.mod.thrive.autocon.harvester.AbstractBlockHarvester;
import net.minecraft.mangrove.mod.thrive.autocon.harvester.AbstractTileHarvester;
import net.minecraft.world.World;

public class BlockHarvesterFarmer extends AbstractBlockHarvester{

	public BlockHarvesterFarmer() {
		super("harvester_farmer");
	}

	@Override
	protected AbstractTileHarvester createNewTileHarvester(World worldIn, int meta) {
		return new TileHarvesterFarmer();
	}

}
