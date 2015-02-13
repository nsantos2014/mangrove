package net.minecraft.mangrove.mod.house.block.crate;

import net.minecraft.mangrove.core.inventory.EnumPermission;
import net.minecraft.mangrove.core.inventory.tile.AbstractSidedInventoryTileEntity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public class TileEntityCrate extends AbstractSidedInventoryTileEntity {
	private String name;
	
	public TileEntityCrate() {
		super();
		this.name=null;
		inventorySupport.defineSlotRange(0, 180, null, EnumPermission.BOTH, EnumFacing.DOWN,EnumFacing.UP,EnumFacing.NORTH,EnumFacing.SOUTH,EnumFacing.WEST,EnumFacing.EAST);
	}
	public String getName(){
		if( this.name==null){
			this.name=String.format("Crate (%d,%d,%d)",pos.getX(),pos.getY(),pos.getZ());
		}
		return this.name;
	}
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
	}
	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
	}
	
//	@Override
	public void updateEntity() {
	}
	
}
