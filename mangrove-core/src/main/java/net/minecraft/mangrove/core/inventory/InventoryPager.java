/**
 * Copyright (c) 2011-2014, SpaceToad and the BuildCraft Team
 * http://www.mod-buildcraft.com
 *
 * BuildCraft is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package net.minecraft.mangrove.core.inventory;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

/**
 * Wrapper class used to specify part of an existing inventory to be treated as
 * a complete inventory. Used primarily to map a side of an ISidedInventory, but
 * it is also helpful for complex inventories such as the Tunnel Bore.
 */
public class InventoryPager implements IInventory {

    private final IInventory inv;
    private int start;
    private final int pageSize;
    
    private int stackSizeLimit = -1;
    private boolean checkItems = true;

    /**
     * Creates a new InventoryMapper
     *
     * @param inv The backing inventory
     * @param start The starting index
     * @param size The size of the new inventory, take care not to exceed the
     * end of the backing inventory
     */
    public InventoryPager(IInventory inv, int start, int pageSize) {
        this(inv, start, pageSize, true);
    }

    public InventoryPager(IInventory inv, int start, int pageSize, boolean checkItems) {
        this.inv = inv;
        this.start = start;
        this.pageSize = pageSize;
        
        this.checkItems = checkItems;
    }
    
    public int getStart() {
		return start;
	}
    public void setStart(int start) {
		this.start = start;
	}
    @SideOnly(Side.CLIENT)
    public boolean hasNextPage(){
    	return this.start+pageSize<inv.getSizeInventory();
    }
    @SideOnly(Side.CLIENT)
    public void nextPage(){
    	if( this.start+pageSize>=inv.getSizeInventory() ){
    		this.start=inv.getSizeInventory()-pageSize;
    	}else {
    		this.start+=pageSize;
    	}
    }
    public boolean hasPreviousPage(){
    	return this.start>=pageSize;
    }
    
    public void previousPage(){
    	if( this.start<pageSize){
    		this.start=0;
    	}else {
    		this.start-=pageSize;
    	}
    }
    

    public IInventory getBaseInventory() {
        return inv;
    }

    @Override
    public int getSizeInventory() {
        return pageSize;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return inv.getStackInSlot(start + slot);
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount) {
        return inv.decrStackSize(start + slot, amount);
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack itemstack) {
        inv.setInventorySlotContents(start + slot, itemstack);
    }

    @Override
    public String getInventoryName() {
        return inv.getInventoryName();
    }

    public void setStackSizeLimit(int limit) {
        stackSizeLimit = limit;
    }

    @Override
    public int getInventoryStackLimit() {
        return stackSizeLimit > 0 ? stackSizeLimit : inv.getInventoryStackLimit();
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer) {
        return inv.isUseableByPlayer(entityplayer);
    }

    @Override
    public void openInventory() {
        inv.openInventory();
    }

    @Override
    public void closeInventory() {
        inv.closeInventory();
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        return inv.getStackInSlotOnClosing(start + slot);
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        if (checkItems) {
            return inv.isItemValidForSlot(start + slot, stack);
        }
        return true;
    }

	@Override
	public boolean hasCustomInventoryName() {
		return inv.hasCustomInventoryName();
	}

	@Override
	public void markDirty() {
		inv.markDirty();
	}
}
