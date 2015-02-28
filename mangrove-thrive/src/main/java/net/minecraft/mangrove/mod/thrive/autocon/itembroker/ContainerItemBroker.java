package net.minecraft.mangrove.mod.thrive.autocon.itembroker;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

public class ContainerItemBroker extends Container {
	private int numRows;
	private TileItemBroker tileItemBroker;

	public ContainerItemBroker(TileItemBroker tileItemBroker, IInventory playerInventory, EntityPlayer player) {
		this.tileItemBroker = tileItemBroker;
		this.numRows = 0;

		// chestInventory.openInventory(player);
		// int i = (this.numRows - 4) * 18;
		int j = 0;
		int k = 0;
		// int rows = 0;

		for (EnumFacing facing : EnumFacing.values()) {

			ConnectionConfig connectorConfig = tileItemBroker.getConnectorConfig(facing);
			// if (connectorConfig.getState() != State.DISCONNECTED) {
			int y = facing.ordinal();

			for (int x = 0; x < 7; x++) {
				int index = x + y * 7;
//				ItemStack itemStack = connectorConfig.getItemStack(x);
				this.addSlotToContainer(new Slot(tileItemBroker.getConnectorInventory(), index, 58 + x * 18, 8 + y * 18));
			}

			// }
		}

		// for (j = 0; j < this.numRows; ++j) {
		// for (k = 0; k < 9; ++k) {
		// this.addSlotToContainer(new Slot(chestInventory, k + j * 9, 8 + k *
		// 18, 18 + j * 18));
		// }
		// }

		for (j = 0; j < 3; ++j) {
			for (k = 0; k < 9; ++k) {
				this.addSlotToContainer(new Slot(playerInventory, k + j * 9 + 9, 22 + k * 18, 120 + j * 18));
			}
		}

		for (j = 0; j < 9; ++j) {
			this.addSlotToContainer(new Slot(playerInventory, j, 22 + j * 18, 178));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}

//	@Override
//	public void onCraftMatrixChanged(IInventory inventoryIn) {
//		for (EnumFacing facing : EnumFacing.values()) {
//			int y = facing.ordinal();
//
//			for (int x = 0; x < 7; x++) {
//				int index = x + y * 7;
//				ItemStack itemStack = craftMatrix.getStackInSlot(index);
//				tileItemBroker.setFilteredItemStack(facing,x,itemStack);
//			}
//
//			// }
//		}
//		super.onCraftMatrixChanged(inventoryIn);
//	}

	@Override
	public void onContainerClosed(EntityPlayer playerIn) {
		tileItemBroker.markDirty();
		super.onContainerClosed(playerIn);
	}
	
	// public boolean canInteractWith(EntityPlayer playerIn) {
	// return this.lowerChestInventory.isUseableByPlayer(playerIn);
	// }

	// public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
	// ItemStack itemstack = null;
	// Slot slot = (Slot) this.inventorySlots.get(index);
	//
	// if (slot != null && slot.getHasStack()) {
	// ItemStack itemstack1 = slot.getStack();
	// itemstack = itemstack1.copy();
	//
	// if (index < this.numRows * 9) {
	// if (!this.mergeItemStack(itemstack1, this.numRows * 9,
	// this.inventorySlots.size(), true)) {
	// return null;
	// }
	// } else if (!this.mergeItemStack(itemstack1, 0, this.numRows * 9, false))
	// {
	// return null;
	// }
	//
	// if (itemstack1.stackSize == 0) {
	// slot.putStack((ItemStack) null);
	// } else {
	// slot.onSlotChanged();
	// }
	// }
	//
	// return itemstack;
	// }

	// public void onContainerClosed(EntityPlayer playerIn) {
	// super.onContainerClosed(playerIn);
	// this.lowerChestInventory.closeInventory(playerIn);
	// }
	//
	// public IInventory getLowerChestInventory() {
	// return this.lowerChestInventory;
	// }
}
