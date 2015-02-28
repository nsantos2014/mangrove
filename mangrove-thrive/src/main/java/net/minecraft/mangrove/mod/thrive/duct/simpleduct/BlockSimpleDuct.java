package net.minecraft.mangrove.mod.thrive.duct.simpleduct;

import net.minecraft.mangrove.mod.thrive.duct.AbstractBlockDuct;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockSimpleDuct extends AbstractBlockDuct {

	public BlockSimpleDuct() {
		super("simpleduct");
	}
	@Override
	protected TileEntity createNewTileDuct(World worldIn, int meta) {
		return new TileSimpleDuct();
	}

}
