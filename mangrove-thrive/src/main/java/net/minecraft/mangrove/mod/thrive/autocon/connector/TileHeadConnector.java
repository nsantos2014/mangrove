package net.minecraft.mangrove.mod.thrive.autocon.connector;

import net.minecraft.inventory.IInventory;
import net.minecraft.mangrove.mod.thrive.autocon.AbstractTileDuct;
import net.minecraft.mangrove.mod.thrive.autocon.harvester.AbstractTileHarvester;
import net.minecraft.mangrove.mod.thrive.autocon.itembroker.TileItemBroker;
import net.minecraft.mangrove.mod.thrive.autocon.junction.TileStorageJunction;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class TileHeadConnector extends AbstractTileDuct {
	private static final ResourceLocation glassCyan = new ResourceLocation("textures/blocks/wool_colored_cyan.png");
	private static final ResourceLocation commandBeam = new ResourceLocation("textures/blocks/comparator_on.png");
	
	@Override
	public boolean isConnected(BlockPos pos, EnumFacing facing, BlockPos sidepos) {
		boolean connected = super.isConnected(pos, facing, sidepos);
		if( !connected){
//			IBlockState sidestate = worldObj.getBlockState(sidepos);
			TileEntity sidetile = worldObj.getTileEntity(sidepos);
			if (sidetile instanceof TileStorageJunction) {
				TileStorageJunction tileItemBroker = (TileStorageJunction) sidetile;
				return /*tileItemBroker.isSideConnected(facing.getOpposite())*/true;
			}
			if (sidetile instanceof AbstractTileHarvester) {
				AbstractTileHarvester tileItemBroker = (AbstractTileHarvester) sidetile;
				return /*tileItemBroker.isSideConnected(facing.getOpposite())*/true;
			}
			
			return sidetile instanceof IInventory; 	
		}
		return connected;
	}
	
	@Override
	public ResourceLocation getBlockTexture() {
		// TODO Auto-generated method stub
		return commandBeam;
	}
	@Override
	public ResourceLocation getConnectorTexture() {
		// TODO Auto-generated method stub
		return glassCyan;
	}
}
