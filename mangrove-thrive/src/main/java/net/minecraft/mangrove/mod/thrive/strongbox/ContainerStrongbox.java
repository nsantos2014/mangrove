package net.minecraft.mangrove.mod.thrive.strongbox;

import java.awt.Point;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.mangrove.core.gui.MGContainer;
import net.minecraft.mangrove.core.gui.slots.SlotBase;

public class ContainerStrongbox extends MGContainer {
	private TileEntityStrongbox tile;
	private Point[] invPositions;
	public int start;
	public int size;
	private int slotOffset;
	
	public ContainerStrongbox(TileEntityStrongbox tile, InventoryPlayer inventory) {
		super(tile, inventory, 84, 0);
		this.tile = tile;

		this.onCraftMatrixChanged(this.tile);
		setPlayerInventoryOffset(22,120);
	}
	@Override
	protected void drawTileInventory(IInventory inventory, int inventorySize) {
		this.size=9;
		this.start=0;
		invPositions = new Point[9];
		int l;
		int i1;
		this.slotOffset=this.inventorySlots.size();
		for (l = 0; l < 3; ++l) {
			for (i1 = 0; i1 < 3; ++i1) {
				final int idx = i1 + l * 3;
				final int x = 22 + i1 * 18;
				final int y = 22 + l * 18;

				invPositions[idx] = new Point(x, y);

				final SlotBase slot = new SlotBase(inventory, idx, x, y);
				this.addSlotToContainer(slot);
			}
		}
		for( l=9; l< inventory.getSizeInventory(); l++){
			final SlotBase slot = new SlotBase(inventory, l, 6000, 6000);
			this.addSlotToContainer(slot);
		}
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
	}
	
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
		
		for(int i=0; i< size; i++){
			SlotBase slot = (SlotBase) inventorySlots.get(slotOffset+start+i);
			slot.xDisplayPosition=invPositions[i].x;
			slot.yDisplayPosition=invPositions[i].y;
		}
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
		for(int i=0; i< size; i++){
			SlotBase slot = (SlotBase) inventorySlots.get(slotOffset+start+i);
			slot.xDisplayPosition=invPositions[i].x;
			slot.yDisplayPosition=invPositions[i].y;
		}
		detectAndSendChanges();
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return tile.isUseableByPlayer(player);
	}

}
