package net.minecraft.mangrove.mod.thrive.autobench.gui;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.mangrove.core.gui.MGContainer;
import net.minecraft.mangrove.core.gui.slots.SlotBase;
import net.minecraft.mangrove.mod.thrive.autobench.TileEntityAutobench;

public class ContainerProcessing extends MGContainer {
	private final TileEntityAutobench tile;
	private List<SlotBase> operationSlots;
	
	public ContainerProcessing(InventoryPlayer inventory,TileEntityAutobench tile) {
		super(tile,inventory,84,9);
		this.tile = tile;
		int l;
        int i1;
//        operationSlots=new ArrayList<SlotBase>();
    	for (l = 0; l < 3; ++l)
        {
            for (i1 = 0; i1 < 3; ++i1)
            {
                final SlotBase slot = new SlotBase(tile, i1 + l * 3, 9 + i1 * 18, 18 + l * 18);
//                operationSlots.add(slot);
				this.addSlotToContainer(slot);
            }
        }
	}
	
	protected void drawTileInventory(IInventory inventory, int inventorySize){
//		for (int i = 0; i < inventorySize; ++i) {
//			addSlotToContainer(new Slot(inventory, i, 80 + i * 18, 20));
//		}
		
		
	}

	 public void hideOperationSlots(){
	    for( SlotBase slot:operationSlots){
	    		slot.hide();
	    	}
	    }
	    public void showOperationSlots(){
	    	for( SlotBase slot:operationSlots){
	    		slot.show();
	    	}
	    }
	    
	public boolean canInteractWith(EntityPlayer player) {
		return this.tile.isUseableByPlayer(player);
	}

	public ItemStack transferStackInSlot(EntityPlayer player, int par2) {
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

		this.tile.closeInventory(par1EntityPlayer);
	}
}
