package net.minecraft.mangrove.mod.thrive.block.harvester.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.mangrove.core.gui.MGContainer;

public class ContainerHarvester extends MGContainer {
	private final IInventory inventory;

	public ContainerHarvester(IInventory inventory,InventoryPlayer player) {
		super(inventory,player,82,inventory.getSizeInventory());
		this.inventory = inventory;

//		inventory.openInventory();
//		byte b0 = 82;
//
//		for (int i = 0; i < 5; ++i) {
//			addSlotToContainer(new Slot(inventory, i, 44 + i * 18, 20));
//		}
//		for (int i = 5; i < 10; ++i) {
//			addSlotToContainer(new Slot(inventory, i, 44 + (i - 5) * 18, 51));
//		}
//
//		for (int i = 0; i < 3; ++i) {
//			for (int j = 0; j < 9; ++j) {
//				addSlotToContainer(new Slot(player, j + i * 9 + 9,
//						8 + j * 18, i * 18 + b0));
//			}
//		}
//
//		for (int i = 0; i < 9; ++i) {
//			addSlotToContainer(new Slot(player, i, 8 + i * 18, 58 + b0));
//		}
	}
	@Override
	protected void drawTileInventory(IInventory inventory, int inventorySize) {
		for (int i = 0; i < 5; ++i) {
			addSlotToContainer(new Slot(inventory, i, 44 + i * 18, 20));
		}
//		for (int i = 5; i < 10; ++i) {
//			addSlotToContainer(new Slot(inventory, i, 44 + (i - 5) * 18, 51));
//		}
	}
	

	public boolean canInteractWith(EntityPlayer par1EntityPlayer) {
		return this.inventory.isUseableByPlayer(par1EntityPlayer);
	}
//
//	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {
//		ItemStack itemstack = null;
//		Slot slot = (Slot) this.inventorySlots.get(par2);
//
//		if ((slot != null) && (slot.getHasStack())) {
//			ItemStack itemstack1 = slot.getStack();
//			itemstack = itemstack1.copy();
//
//			if (par2 < this.inventory.getSizeInventory()) {
//				if (!(mergeItemStack(itemstack1,
//						this.inventory.getSizeInventory(),
//						this.inventorySlots.size(), true))) {
//					return null;
//				}
//			} else if (!(mergeItemStack(itemstack1, 0,
//					this.inventory.getSizeInventory(), false))) {
//				return null;
//			}
//
//			if (itemstack1.stackSize == 0) {
//				slot.putStack((ItemStack) null);
//			} else {
//				slot.onSlotChanged();
//			}
//		}
//
//		return itemstack;
//	}
//
//	public void onContainerClosed(EntityPlayer par1EntityPlayer) {
//		super.onContainerClosed(par1EntityPlayer);
//
//		this.inventory.closeInventory();
//	}

}
