package net.minecraft.mangrove.core.inventory;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.mangrove.core.inventory.listeners.InventoryListener;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

public class AdvInventoryCrafting extends InventoryCrafting implements IInventory {
	private final List<InventoryListener> inventoryListeners=new ArrayList<InventoryListener>();
	private final ItemStack[] stackList;
	private final int inventoryWidth;
	private final int inventoryHeight;
	

	public AdvInventoryCrafting(int width, int height) {
		super(null,width,height);
		int k = width * height;
		this.stackList = new ItemStack[k];
		this.inventoryWidth = width;
		this.inventoryHeight = height;
	}
	
	public void addInventoryListener(InventoryListener listener){
		this.inventoryListeners.add(listener);
	}
	
	public void removeInventoryListener(InventoryListener listener){
		this.inventoryListeners.add(listener);
	}

	protected final void fireInventoryChanged(){
		for(InventoryListener listener:this.inventoryListeners){
			listener.onInventoryChanged(this);
		}
	}

	public int getSizeInventory() {
		return this.stackList.length;
	}

	public ItemStack getStackInSlot(int index) {
		return index >= this.getSizeInventory() ? null : this.stackList[index];
	}

	public ItemStack getStackInRowAndColumn(int row, int column) {
		return row >= 0 && row < this.inventoryWidth && column >= 0
				&& column <= this.inventoryHeight ? this.getStackInSlot(row
				+ column * this.inventoryWidth) : null;
	}

	public String getCommandSenderName() {
		return "container.crafting";
	}

	public boolean hasCustomName() {
		return false;
	}

	public IChatComponent getDisplayName() {
		return (IChatComponent) (this.hasCustomName() ? new ChatComponentText(
				this.getCommandSenderName()) : new ChatComponentTranslation(
				this.getCommandSenderName(), new Object[0]));
	}

	public ItemStack getStackInSlotOnClosing(int index) {
		if (this.stackList[index] != null) {
			ItemStack itemstack = this.stackList[index];
			this.stackList[index] = null;
			return itemstack;
		} else {
			return null;
		}
	}

	public ItemStack decrStackSize(int index, int count) {
		if (this.stackList[index] != null) {
			ItemStack itemstack;

			if (this.stackList[index].stackSize <= count) {
				itemstack = this.stackList[index];
				this.stackList[index] = null;
//				this.eventHandler.onCraftMatrixChanged(this);
				fireInventoryChanged();
				return itemstack;
			} else {
				itemstack = this.stackList[index].splitStack(count);

				if (this.stackList[index].stackSize == 0) {
					this.stackList[index] = null;
				}

//				this.eventHandler.onCraftMatrixChanged(this);
				fireInventoryChanged();
				return itemstack;
			}
		} else {
			return null;
		}
	}

	public void setInventorySlotContents(int index, ItemStack stack) {
		this.stackList[index] = stack;
//		this.eventHandler.onCraftMatrixChanged(this);
		fireInventoryChanged();
	}

	public int getInventoryStackLimit() {
		return 1;
	}

	public void markDirty() {
	}

	public boolean isUseableByPlayer(EntityPlayer player) {
		return true;
	}

	public void openInventory(EntityPlayer player) {
	}

	public void closeInventory(EntityPlayer player) {
	}

	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return true;
	}

	public int getField(int id) {
		return 0;
	}

	public void setField(int id, int value) {
	}

	public int getFieldCount() {
		return 0;
	}

	public void clear() {
		for (int i = 0; i < this.stackList.length; ++i) {
			this.stackList[i] = null;
		}
	}

	public int getHeight() {
		return this.inventoryHeight;
	}

	public int getWidth() {
		return this.inventoryWidth;
	}

}
