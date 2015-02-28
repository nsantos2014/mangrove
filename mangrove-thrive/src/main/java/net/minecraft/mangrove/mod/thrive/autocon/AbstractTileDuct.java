package net.minecraft.mangrove.mod.thrive.autocon;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public abstract class AbstractTileDuct extends TileEntity{

	public boolean isConnected(BlockPos pos, EnumFacing facing, BlockPos sidepos) {
		IBlockState sideBlockState = worldObj.getBlockState(sidepos);
		Block sideblock = sideBlockState.getBlock();		
		return sideblock==getBlockType();
	}

	public abstract ResourceLocation getBlockTexture();

	public abstract ResourceLocation getConnectorTexture();

}
