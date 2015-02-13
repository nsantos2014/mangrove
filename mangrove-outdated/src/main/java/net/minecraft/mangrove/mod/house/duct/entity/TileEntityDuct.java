package net.minecraft.mangrove.mod.house.duct.entity;

import net.minecraft.mangrove.mod.house.duct.AbstractTileEntityDuct;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.IHopper;

public class TileEntityDuct extends AbstractTileEntityDuct implements IHopper {

	public TileEntityDuct() {
		super();
		setInventoryName("Duct Filter");
	}
	
	public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readFromNBT(par1NBTTagCompound);		
	}

	public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeToNBT(par1NBTTagCompound);		
	}
	

}
