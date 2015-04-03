package net.minecraft.mangrove.mod.thrive.strongbox;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.mangrove.core.gui.MGContainer;
import net.minecraft.mangrove.core.gui.slots.IPhantomSlot;
import net.minecraft.mangrove.core.gui.slots.SlotBase;
import net.minecraft.mangrove.core.utils.StackHelper;

public class ContainerStrongbox2 extends Container {
	private int inventorySize;
	private int playerInvStartSlot;
	private int playerInvEndSlot;
	
	private TileEntityStrongbox tile;
	
	private final List<Point> invPositions=new ArrayList<Point>();
	public int start;
	public int size;
	private int slotOffset;
	private AtomicInteger counter=new AtomicInteger();
	
	public ContainerStrongbox2(TileEntityStrongbox tile, InventoryPlayer playerInventory) {
		this.tile = tile;
		this.tile = tile;
		int j=0;
		int k = 0;
		
		for (j = 0; j < 5; ++j) {
			for (k = 0; k < 8; ++k) {
				Point p=new Point(22 + k * 18, 22 + j * 18);				
				this.addSlotToContainer(new SlotBase(tile, counter.getAndIncrement(), p.x,p.y));
				invPositions.add(p);
			}
		}
		
		while(counter.get()<200 ) {
			this.addSlotToContainer(new SlotBase(tile, counter.getAndIncrement(), 60000,60000));
		}
		
		for (j = 0; j < 3; ++j) {
			for (k = 0; k < 9; ++k) {
				this.addSlotToContainer(new Slot(playerInventory, k + j * 9 + 9, 22 + k * 18, 120 + j * 18));
			}
		}

		for (j = 0; j < 9; ++j) {
			this.addSlotToContainer(new Slot(playerInventory, j, 22 + j * 18, 178));
		}
//		this.tile.getBomMatrix().addInventoryListener(this);
		
		this.onCraftMatrixChanged(this.tile);
	}

//	protected void drawTileInventory(IInventory inventory, int inventorySize) {
//		this.size=9;
//		this.start=0;
//		invPositions = new Point[9];
//		int l;
//		int i1;
//		this.slotOffset=this.inventorySlots.size();
//		for (l = 0; l < 3; ++l) {
//			for (i1 = 0; i1 < 3; ++i1) {
//				final int idx = i1 + l * 3;
//				final int x = 22 + i1 * 18;
//				final int y = 22 + l * 18;
//
//				invPositions[idx] = new Point(x, y);
//
//				final SlotBase slot = new SlotBase(inventory, idx, x, y);
//				this.addSlotToContainer(slot);
//			}
//		}
//		for( l=9; l< inventory.getSizeInventory(); l++){
//			final SlotBase slot = new SlotBase(inventory, l, 6000, 6000);
//			this.addSlotToContainer(slot);
//		}
//		
//		for (j = 0; j < 3; ++j) {
//			for (k = 0; k < 9; ++k) {
//				this.addSlotToContainer(new Slot(playerInventory, k + j * 9 + 9, 22 + k * 18, 120 + j * 18));
//			}
//		}
//
//		for (j = 0; j < 9; ++j) {
//			this.addSlotToContainer(new Slot(playerInventory, j, 22 + j * 18, 178));
//		}
//	}
	
	public int getStartPos() {
		return start+1;
	}
	public int getEndPos() {
		return start+size;
	}
	public void nextPage(){
		for(int i=0; i< size; i++){
			SlotBase slot = (SlotBase) inventorySlots.get(slotOffset+start+i);
			slot.xDisplayPosition=6000;
			slot.yDisplayPosition=6000;
		}
		int maxSize = tile.getSizeInventory();
		
		if( start+size>= maxSize){
			start=maxSize-size;
		}else{
			start+=size;
		}
		
//		for(int i=0; i< size; i++){
//			SlotBase slot = (SlotBase) inventorySlots.get(slotOffset+start+i);
//			slot.xDisplayPosition=invPositions[i].x;
//			slot.yDisplayPosition=invPositions[i].y;
//		}
		detectAndSendChanges();
	}
	
	public void previousPage(){
		for(int i=0; i< size; i++){
			SlotBase slot = (SlotBase) inventorySlots.get(slotOffset+start+i);
			slot.xDisplayPosition=6000;
			slot.yDisplayPosition=6000;
		}
		if( start-size< 0){
			start=0;
		}else{
			start-=size;
		}
//		for(int i=0; i< size; i++){
//			SlotBase slot = (SlotBase) inventorySlots.get(slotOffset+start+i);
//			slot.xDisplayPosition=invPositions[i].x;
//			slot.yDisplayPosition=invPositions[i].y;
//		}
		detectAndSendChanges();
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return tile.isUseableByPlayer(player);
	}

	public void setRange(int firstRow, int rowCount) {	
		for(int i=0; i< counter.get(); i++){
			SlotBase slot = (SlotBase) inventorySlots.get(i);
			slot.xDisplayPosition=6000;
			slot.yDisplayPosition=6000;
		}	
		int start=firstRow*8;
		for(int i=0; i<invPositions.size(); i++){
			SlotBase slot = (SlotBase) inventorySlots.get(start+i);
			Point p = invPositions.get(i);
			slot.xDisplayPosition=p.x;
			slot.yDisplayPosition=p.y;
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
}
