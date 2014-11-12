package net.minecraft.mangrove.mod.thrive.feeder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.mangrove.core.block.AbstractSidedInventoryTileEntity;
import net.minecraft.mangrove.core.inventory.Permission;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;

public class TileEntityFeeder extends AbstractSidedInventoryTileEntity {
	private String name;
	private int tick=0;
	
	public TileEntityFeeder() {
		super();
		this.name=null;
		inventorySupport.defineSlotRange(0, 180, null, Permission.BOTH, 0,1,2,3,4,5);
	}
	public String getName(){
		if( this.name==null){
			this.name=String.format("Crate (%d,%d,%d)",xCoord,yCoord,zCoord);
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
	
	@Override
	public void updateEntity() {
	    tick++;	   
	}
	
}
