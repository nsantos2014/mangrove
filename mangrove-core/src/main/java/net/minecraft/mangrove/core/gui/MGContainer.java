package net.minecraft.mangrove.core.gui;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.mangrove.core.gui.slots.IPhantomSlot;
import net.minecraft.mangrove.core.gui.slots.SlotBase;
import net.minecraft.mangrove.core.gui.widgets.Widget;
import net.minecraft.mangrove.core.utils.StackHelper;
import net.minecraft.mangrove.network.GuiWidgetMessage;
import net.minecraft.mangrove.network.NetBus;

public class MGContainer extends Container {
	private List<Widget> widgets = new ArrayList<Widget>();
	private int inventorySize;
	private int playerInvStartSlot;
	private int playerInvEndSlot;

	public MGContainer(int inventorySize) {
		this.inventorySize = inventorySize;
	}

	public MGContainer(IInventory tile, InventoryPlayer inventory, int yOffset, int inventorySize) {
		this.inventorySize = inventorySize;

		inventory.openInventory(inventory.player);
		drawTileInventory(tile, inventorySize);
		//
		drawPlayerInventory(inventory, yOffset, getInventorySize());
	}

	@Override
	public boolean canInteractWith(EntityPlayer p_75145_1_) {
		return false;
	}

	public void setPlayerInventoryOffset(int xOffset, int yOffset) {
		final List<Slot> playerSlots = this.inventorySlots.subList(playerInvStartSlot, playerInvEndSlot);
		final Iterator<Slot> it = playerSlots.iterator();
		for (int i = 0; it.hasNext() && i < 3; ++i) {
			for (int j = 0; it.hasNext() && j < 9; ++j) {
				final Slot slot = it.next();
				slot.xDisplayPosition = xOffset + j * 18;
				slot.yDisplayPosition = yOffset + i * 18;
			}
		}
		for (int i = 0; it.hasNext() && i < 9; ++i) {
			final Slot slot = it.next();
			slot.xDisplayPosition = xOffset + i * 18;
			slot.yDisplayPosition = yOffset + 58;
		}
	}

	private void drawPlayerInventory(InventoryPlayer inventory, int yOffset, int slotOffset) {
		this.playerInvStartSlot = inventorySlots.size();
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				int slotIndex = j + i * 9 + 9;
				int x = 8 + j * 18;
				int y = i * 18 + yOffset;
				addSlotToContainer(new Slot(inventory, slotIndex, x, y));
			}
		}
		for (int i = 0; i < 9; ++i) {
			addSlotToContainer(new Slot(inventory, i, 8 + i * 18, 58 + yOffset));
		}
		this.playerInvEndSlot = inventorySlots.size();
	}

	protected void drawTileInventory(IInventory inventory, int inventorySize) {

	}

	public List<Widget> getWidgets() {
		return widgets;
	}

	public void addSlot(Slot slot) {
		addSlotToContainer(slot);
	}

	public void addWidget(Widget widget) {
		widget.addToContainer(this);
		widgets.add(widget);
	}

	public void sendWidgetDataToClient(Widget widget, ICrafting player, byte[] data) {
		// PacketGuiWidget pkt = new PacketGuiWidget(windowId,
		// widgets.indexOf(widget), data);
		// BuildCraftCore.instance.sendToPlayer((EntityPlayer) player, pkt);
		NetBus.sendToServer(new GuiWidgetMessage(windowId, widgets.indexOf(widget), data));
	}

	public void handleWidgetClientData(int widgetId, byte[] data) {
		final InputStream input = new ByteArrayInputStream(data);
		final DataInputStream stream = new DataInputStream(input);

		try {
			widgets.get(widgetId).handleClientPacketData(stream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onCraftGuiOpened(ICrafting listener) {
		// TODO Auto-generated method stub
		super.onCraftGuiOpened(listener);
		for (Widget widget : widgets) {
			widget.initWidget(listener);
		}
	}

	// @Override
	// public void addCraftingToCrafters(ICrafting player) {
	// super.addCraftingToCrafters(player);
	// for (Widget widget : widgets) {
	// widget.initWidget(player);
	// }
	// }

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		for (Widget widget : widgets) {
			for (ICrafting player : (List<ICrafting>) crafters) {
				widget.updateWidget(player);
			}
		}
	}

	@Override
	public ItemStack slotClick(int slotNum, int mouseButton, int modifier, EntityPlayer player) {
		Slot slot = slotNum < 0 ? null : (Slot) this.inventorySlots.get(slotNum);
		if (slot instanceof IPhantomSlot) {
			return slotClickPhantom(slot, mouseButton, modifier, player);
		}
		return super.slotClick(slotNum, mouseButton, modifier, player);
	}

	private ItemStack slotClickPhantom(Slot slot, int mouseButton, int modifier, EntityPlayer player) {
		ItemStack stack = null;

		if (mouseButton == 2) {
			if (((IPhantomSlot) slot).canAdjust()) {
				slot.putStack(null);
			}
		} else if (mouseButton == 0 || mouseButton == 1) {
			InventoryPlayer playerInv = player.inventory;
			slot.onSlotChanged();
			ItemStack stackSlot = slot.getStack();
			ItemStack stackHeld = playerInv.getItemStack();

			if (stackSlot != null) {
				stack = stackSlot.copy();
			}

			if (stackSlot == null) {
				if (stackHeld != null && slot.isItemValid(stackHeld)) {
					fillPhantomSlot(slot, stackHeld, mouseButton, modifier);
				}
			} else if (stackHeld == null) {
				adjustPhantomSlot(slot, mouseButton, modifier);
				slot.onPickupFromSlot(player, playerInv.getItemStack());
			} else if (slot.isItemValid(stackHeld)) {
				if (StackHelper.instance().canStacksMerge(stackSlot, stackHeld)) {
					adjustPhantomSlot(slot, mouseButton, modifier);
				} else {
					fillPhantomSlot(slot, stackHeld, mouseButton, modifier);
				}
			}
		}
		return stack;
	}

	protected void adjustPhantomSlot(Slot slot, int mouseButton, int modifier) {
		if (!((IPhantomSlot) slot).canAdjust()) {
			return;
		}
		ItemStack stackSlot = slot.getStack();
		int stackSize;
		if (modifier == 1) {
			stackSize = mouseButton == 0 ? (stackSlot.stackSize + 1) / 2 : stackSlot.stackSize * 2;
		} else {
			stackSize = mouseButton == 0 ? stackSlot.stackSize - 1 : stackSlot.stackSize + 1;
		}

		if (stackSize > slot.getSlotStackLimit()) {
			stackSize = slot.getSlotStackLimit();
		}

		stackSlot.stackSize = stackSize;

		if (stackSlot.stackSize <= 0) {
			slot.putStack((ItemStack) null);
		}
	}

	protected void fillPhantomSlot(Slot slot, ItemStack stackHeld, int mouseButton, int modifier) {
		if (!((IPhantomSlot) slot).canAdjust()) {
			return;
		}
		int stackSize = mouseButton == 0 ? stackHeld.stackSize : 1;
		if (stackSize > slot.getSlotStackLimit()) {
			stackSize = slot.getSlotStackLimit();
		}
		ItemStack phantomStack = stackHeld.copy();
		phantomStack.stackSize = stackSize;

		slot.putStack(phantomStack);
	}

	protected boolean shiftItemStack(ItemStack stackToShift, int start, int end) {
		boolean changed = false;
		if (stackToShift.isStackable()) {
			for (int slotIndex = start; stackToShift.stackSize > 0 && slotIndex < end; slotIndex++) {
				Slot slot = (Slot) inventorySlots.get(slotIndex);
				ItemStack stackInSlot = slot.getStack();
				if (stackInSlot != null && StackHelper.instance().canStacksMerge(stackInSlot, stackToShift)) {
					int resultingStackSize = stackInSlot.stackSize + stackToShift.stackSize;
					int max = Math.min(stackToShift.getMaxStackSize(), slot.getSlotStackLimit());
					if (resultingStackSize <= max) {
						stackToShift.stackSize = 0;
						stackInSlot.stackSize = resultingStackSize;
						slot.onSlotChanged();
						changed = true;
					} else if (stackInSlot.stackSize < max) {
						stackToShift.stackSize -= max - stackInSlot.stackSize;
						stackInSlot.stackSize = max;
						slot.onSlotChanged();
						changed = true;
					}
				}
			}
		}
		if (stackToShift.stackSize > 0) {
			for (int slotIndex = start; stackToShift.stackSize > 0 && slotIndex < end; slotIndex++) {
				Slot slot = (Slot) inventorySlots.get(slotIndex);
				ItemStack stackInSlot = slot.getStack();
				if (stackInSlot == null) {
					int max = Math.min(stackToShift.getMaxStackSize(), slot.getSlotStackLimit());
					stackInSlot = stackToShift.copy();
					stackInSlot.stackSize = Math.min(stackToShift.stackSize, max);
					stackToShift.stackSize -= stackInSlot.stackSize;
					slot.putStack(stackInSlot);
					slot.onSlotChanged();
					changed = true;
				}
			}
		}
		return changed;
	}

	private boolean tryShiftItem(ItemStack stackToShift, int numSlots) {
		for (int machineIndex = 0; machineIndex < numSlots - 9 * 4; machineIndex++) {
			Slot slot = (Slot) inventorySlots.get(machineIndex);
			if (slot instanceof SlotBase && !((SlotBase) slot).canShift()) {
				continue;
			}
			if (slot instanceof IPhantomSlot) {
				continue;
			}
			if (!slot.isItemValid(stackToShift)) {
				continue;
			}
			if (shiftItemStack(stackToShift, machineIndex, machineIndex + 1)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {
		ItemStack originalStack = null;
		Slot slot = (Slot) inventorySlots.get(slotIndex);
		int numSlots = inventorySlots.size();
		if (slot != null && slot.getHasStack()) {
			ItemStack stackInSlot = slot.getStack();
			originalStack = stackInSlot.copy();
			if (slotIndex >= numSlots - 9 * 4 && tryShiftItem(stackInSlot, numSlots)) {
				// NOOP
			} else if (slotIndex >= numSlots - 9 * 4 && slotIndex < numSlots - 9) {
				if (!shiftItemStack(stackInSlot, numSlots - 9, numSlots)) {
					return null;
				}
			} else if (slotIndex >= numSlots - 9 && slotIndex < numSlots) {
				if (!shiftItemStack(stackInSlot, numSlots - 9 * 4, numSlots - 9)) {
					return null;
				}
			} else if (!shiftItemStack(stackInSlot, numSlots - 9 * 4, numSlots)) {
				return null;
			}
			slot.onSlotChange(stackInSlot, originalStack);
			if (stackInSlot.stackSize <= 0) {
				slot.putStack(null);
			} else {
				slot.onSlotChanged();
			}
			if (stackInSlot.stackSize == originalStack.stackSize) {
				return null;
			}
			slot.onPickupFromSlot(player, stackInSlot);
		}
		return originalStack;
	}

	public int getInventorySize() {
		return inventorySize;
	}
}
