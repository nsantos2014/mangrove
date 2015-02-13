package net.minecraft.mangrove.core.inventory.tile;

import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

public class AbstractSidedInventoryTileEntity extends AbstractInventoryTileEntity implements ISidedInventory{
	private static final int[] EMPTY = new int[0];
	
	@Override
    public boolean canExtractItem(int slot, ItemStack item, EnumFacing side) {
		return inventorySupport.canExtractItem(slot, item, side);
    }
    @Override
    public boolean canInsertItem(int slot, ItemStack item, EnumFacing side) {
    	return inventorySupport.canInsertItem(slot, item, side);
    }
    @Override
    public int[] getSlotsForFace(EnumFacing side) {
    	int[] slotArray = inventorySupport.getSlotArray(side);
		return slotArray==null?EMPTY:slotArray;
    }

}
