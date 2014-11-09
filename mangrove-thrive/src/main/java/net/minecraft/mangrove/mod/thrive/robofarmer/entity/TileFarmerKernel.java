package net.minecraft.mangrove.mod.thrive.robofarmer.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
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

public class TileFarmerKernel extends AbstractSidedInventoryTileEntity {
	private String name;
	private int tick=0;

	
	public TileFarmerKernel() {
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
//		if( tag.hasKey("sid")){
//		    uuid=UUID.fromString(tag.getString("sid"));
//		}
		
	}
	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
//		if( uuid==null){
//		    tag.removeTag("sid");
//		}else{
//		    tag.setString("sid", uuid.toString());
//		}
	}
	
	@Override
	public void updateEntity() {
	    tick++;
	    if( !worldObj.isRemote){
	        if( tick % 512==0){
	            Block block = worldObj.getBlock(xCoord, yCoord, zCoord);
	            System.out.println("Is getting Power? "+worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord));
	        }
	    }
	        
	}
    public void handlePower() {
        System.out.println("Is getting Power? "+worldObj.getStrongestIndirectPower(xCoord, yCoord, zCoord));
    }
//    @SideOnly(Side.SERVER)
//    public UUID getSid() {
//        return uuid;
//    }
	
}
