package net.minecraft.mangrove.mod.thrive.autocon.conveyor;

import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.IInventory;
import net.minecraft.mangrove.mod.thrive.MGThriveBlocks;
import net.minecraft.mangrove.mod.thrive.autocon.AbstractTileDuct;
import net.minecraft.mangrove.mod.thrive.autocon.autobench.TileEntityAutobench;
import net.minecraft.mangrove.mod.thrive.autocon.itembroker.TileItemBroker;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class TileItemConveyor extends AbstractTileDuct {
//	private static final ResourceLocation beaconBeam = new ResourceLocation("textures/entity/beacon_beam.png");
	private static final ResourceLocation glassCyan = new ResourceLocation("textures/blocks/wool_colored_green.png");
	private static final ResourceLocation commandBeam = new ResourceLocation("textures/blocks/command_block.png");
	
	@Override
	public boolean isConnected(BlockPos pos, EnumFacing facing, BlockPos sidepos) {
		boolean connected = super.isConnected(pos, facing, sidepos);
		if( !connected){
//			IBlockState sidestate = worldObj.getBlockState(sidepos);
			TileEntity sidetile = worldObj.getTileEntity(sidepos);
			if (sidetile instanceof TileItemBroker) {
				TileItemBroker tileItemBroker = (TileItemBroker) sidetile;
				return tileItemBroker.isSideConnected(facing.getOpposite());
			}
			if (sidetile instanceof TileEntityAutobench) {
				TileEntityAutobench tileItemBroker = (TileEntityAutobench) sidetile;
				return tileItemBroker.isSideConnected(facing.getOpposite());
			}
			
			return sidetile instanceof IInventory; 	
		}
		return connected;
	}
	@Override
	public ResourceLocation getBlockTexture() {
		return commandBeam;
	}
	@Override
	public ResourceLocation getConnectorTexture(){
		return glassCyan;
	}
	
}
