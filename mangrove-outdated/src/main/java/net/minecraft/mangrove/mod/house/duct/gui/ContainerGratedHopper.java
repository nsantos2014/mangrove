package net.minecraft.mangrove.mod.house.duct.gui;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.mangrove.core.core.gui.MGContainer;

public class ContainerGratedHopper extends MGContainer {
	private final IInventory field_94538_a;

	public ContainerGratedHopper(IInventory par2IInventory,InventoryPlayer par1InventoryPlayer) {
	    super(par2IInventory,par1InventoryPlayer,82,par2IInventory.getSizeInventory());
		this.field_94538_a = par2IInventory;

//		par2IInventory.openInventory();
//		byte b0 = 82;
//
//		for (int i = 0; i < 5; ++i) {
//			addSlotToContainer(new Slot(par2IInventory, i, 44 + i * 18, 20));
//		}
//		for (int i = 5; i < 10; ++i) {
//			addSlotToContainer(new Slot(par2IInventory, i, 44 + (i - 5) * 18, 51));
//		}

//		for (int i = 0; i < 3; ++i) {
//			for (int j = 0; j < 9; ++j) {
//				addSlotToContainer(new Slot(par1InventoryPlayer, j + i * 9 + 9,
//						8 + j * 18, i * 18 + b0));
//			}
//		}
//
//		for (int i = 0; i < 9; ++i) {
//			addSlotToContainer(new Slot(par1InventoryPlayer, i, 8 + i * 18, 58 + b0));
//		}
	}
	
	@Override
	protected void drawTileInventory(IInventory inventory, int inventorySize) {
	    byte b0 = 82;

        for (int i = 0; i < 5; ++i) {
            addSlotToContainer(new Slot(inventory, i, 44 + i * 18, 20));
        }
        for (int i = 5; i < 10; ++i) {
            addSlotToContainer(new Slot(inventory, i, 44 + (i - 5) * 18, 51));
        }
	}

	public boolean canInteractWith(EntityPlayer par1EntityPlayer) {
		return this.field_94538_a.isUseableByPlayer(par1EntityPlayer);
	}

	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {
		ItemStack itemstack = null;
		Slot slot = (Slot) this.inventorySlots.get(par2);

		if ((slot != null) && (slot.getHasStack())) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (par2 < this.field_94538_a.getSizeInventory()) {
				if (!(mergeItemStack(itemstack1,
						this.field_94538_a.getSizeInventory(),
						this.inventorySlots.size(), true))) {
					return null;
				}
			} else if (!(mergeItemStack(itemstack1, 0,
					this.field_94538_a.getSizeInventory(), false))) {
				return null;
			}

			if (itemstack1.stackSize == 0) {
				slot.putStack((ItemStack) null);
			} else {
				slot.onSlotChanged();
			}
		}

		return itemstack;
	}

	public void onContainerClosed(EntityPlayer par1EntityPlayer) {
		super.onContainerClosed(par1EntityPlayer);

		this.field_94538_a.closeInventory();
	}

}
