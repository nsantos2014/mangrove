package net.minecraft.mangrove.mod.thrive.autocon.junction;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.mangrove.core.inventory.InventorySupport;
import net.minecraft.mangrove.mod.thrive.autocon.AbstractTileAutocon;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;

public class TileStorageJunction extends AbstractTileAutocon implements IInventory{

	
	private InventorySupport inventorySupport;

	public TileStorageJunction() {
		this.inventorySupport=new InventorySupport(180, "storageJunction");
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
    public String getCommandSenderName(){
    	return inventorySupport.getInventoryName();
    }

    /**
     * Returns if the inventory is named
     */
    public boolean hasCustomName(){
    	return this.inventorySupport.hasCustomInventoryName();
    }

    public void setName(String inventoryName){
    	inventorySupport.setInventoryName(inventoryName);
    }
    
    @Override
    public IChatComponent getDisplayName() {
        return (IChatComponent) (this.hasCustomName() ? new ChatComponentText(this.getCommandSenderName()) : new ChatComponentTranslation(this.getCommandSenderName(), new Object[0]));
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
        

    public void openInventory(EntityPlayer player) {}

    public void closeInventory(EntityPlayer player) {}

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot.
     */
    public boolean isItemValidForSlot(int slot, ItemStack itemStack){
        return true;
    }
    // NEW

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
