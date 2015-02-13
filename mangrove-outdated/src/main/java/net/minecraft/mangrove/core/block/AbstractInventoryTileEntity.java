package net.minecraft.mangrove.core.block;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.mangrove.core.inventory.InventorySupport;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IChatComponent;

public  class AbstractInventoryTileEntity extends TileEntity implements IInventory {
	protected final InventorySupport inventorySupport;

	public AbstractInventoryTileEntity() {
		super();
		inventorySupport = new InventorySupport(5, "container", false);
	}

	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		inventorySupport.readFromNBT(tag);
	}

	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		inventorySupport.writeToNBT(tag);
	}

	/**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory(){
    	return this.inventorySupport.getSize();
    }

    /**
     * Returns the stack in slot i
     */
    public ItemStack getStackInSlot(int slot){
    	return this.inventorySupport.getStackInSlot(slot);
    }

    /**
     * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
     * new stack.
     */
    public ItemStack decrStackSize(int slot, int size){
    	return this.inventorySupport.decrStackSize(slot, size);
    }
    /**
     * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
     * like when you close a workbench GUI.
     */
    public ItemStack getStackInSlotOnClosing(int slot) {
    	ItemStack stack=this.inventorySupport.getStackInSlot(slot);
    	if( stack!=null){
    		this.inventorySupport.setSlotContents(slot, null);
    	}
    	return stack;
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int slot, ItemStack itemStack){
    	this.inventorySupport.setSlotContents(slot, itemStack);
    }

    /**
     * Returns the name of the inventory
     */
    public String getInventoryName(){
    	return inventorySupport.getInventoryName();
    }

    /**
     * Returns if the inventory is named
     */
    public boolean hasCustomInventoryName(){
    	return this.inventorySupport.hasCustomInventoryName();
    }

    public void setInventoryName(String inventoryName){
    	inventorySupport.setInventoryName(inventoryName);
    }

    /**
     * Returns the maximum stack size for a inventory slot.
     */
    public int getInventoryStackLimit(){
        return 64;
    }

    /**
     * Do not make give this method the name canInteractWith because it clashes with Container
     */
    public boolean isUseableByPlayer(EntityPlayer player) {
        return this.worldObj.getTileEntity(this.pos) != this ? false : player.getDistanceSq((double)this.pos.getX()+ 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
    }
        

    public void openInventory() {}

    public void closeInventory() {}

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot.
     */
    public boolean isItemValidForSlot(int slot, ItemStack itemStack){
        return true;
    }
    // NEW

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasCustomName() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IChatComponent getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void openInventory(EntityPlayer player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closeInventory(EntityPlayer player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getField(int id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setField(int id, int value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getFieldCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}
    
}
