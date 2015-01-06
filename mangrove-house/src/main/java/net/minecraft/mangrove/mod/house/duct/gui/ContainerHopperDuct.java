package net.minecraft.mangrove.mod.house.duct.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.mangrove.core.gui.MGContainer;
import net.minecraft.mangrove.mod.house.duct.AbstractTileEntityDuct;

public class ContainerHopperDuct extends MGContainer {
	private final AbstractTileEntityDuct tile;

	public ContainerHopperDuct(AbstractTileEntityDuct tile,InventoryPlayer inventory) {
//		super(tile.getSizeInventory());
		super(tile, inventory,51,tile.getSizeInventory());
		this.tile = tile;
//		byte b0 = 51;
//		
//		inventory.openInventory();
//		
//
//		for (int i = 0; i < this.tile.getSizeInventory(); ++i) {
//			addSlotToContainer(new Slot(this.tile, i, 80 + i * 18, 20));
//		}
//
//		for (int i = 0; i < 3; ++i) {
//			for (int j = 0; j < 9; ++j) {
//				addSlotToContainer(new Slot(inventory, j + i * 9 + 9,
//						8 + j * 18, i * 18 + b0));
//			}
//		}
//
//		for (int i = 0; i < 9; ++i) {
//			addSlotToContainer(new Slot(inventory, i, 8 + i * 18, 58 + b0));
//		}
	}
	protected void drawTileInventory(IInventory inventory, int inventorySize){
		for (int i = 0; i < inventorySize; ++i) {
			addSlotToContainer(new Slot(inventory, i, 80 + i * 18, 20));
		}
	}

	public boolean canInteractWith(EntityPlayer par1EntityPlayer) {
		return this.tile.isUseableByPlayer(par1EntityPlayer);
	}

	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {
		ItemStack itemstack = null;
		Slot slot = (Slot) this.inventorySlots.get(par2);

		if ((slot != null) && (slot.getHasStack())) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (par2 < this.tile.getSizeInventory()) {
				if (!(mergeItemStack(itemstack1,
						this.tile.getSizeInventory(),
						this.inventorySlots.size(), true))) {
					return null;
				}
			} else if (!(mergeItemStack(itemstack1, 0,
					this.tile.getSizeInventory(), false))) {
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

		this.tile.closeInventory();
	}
}
