package net.minecraft.mangrove.mod.thrive.autocon.junction;

import net.minecraft.mangrove.mod.thrive.autocon.AbstractBlockAutocon;
import net.minecraft.mangrove.mod.thrive.autocon.AbstractTileAutocon;
import net.minecraft.world.World;

public class BlockStorageJunction extends AbstractBlockAutocon{

	public BlockStorageJunction() {
		super("storage_junction");
	}

	@Override
	public AbstractTileAutocon createNewTileAutocon(World worldIn, int meta) {
		return new TileStorageJunction();
	}

}
