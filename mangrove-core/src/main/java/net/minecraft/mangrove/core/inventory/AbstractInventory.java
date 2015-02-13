package net.minecraft.mangrove.core.inventory;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.mangrove.core.inventory.slots.InventorySlot;
import net.minecraft.util.IChatComponent;

public abstract class AbstractInventory extends AbstractNameable implements IInventory{
    protected int pageCount;
    protected final int pageSize;    
    protected final List<InventorySlot> slots;

    public AbstractInventory(String title, boolean customName) {
        this(title, customName,1,9,false);
    }

    public AbstractInventory(String title, boolean customName,int pageCount) {
        this(title, customName,pageCount,9,false);
    }
    public AbstractInventory(String title, boolean customName,int pageCount, int pageSize) {
        this(title, customName,pageCount,pageSize,false);
    }
    public AbstractInventory(String title, boolean customName,int pageCount, int pageSize,boolean autoExpand) {
        super(title, customName);
        this.pageCount = pageCount;
        this.pageSize = pageSize;
        this.slots=new ArrayList<>(pageCount*pageSize);
        ensureSize();
    }

    public void grow(){
        this.pageCount++;
        ensureSize();
    }
    
    @Override
    public int getSizeInventory() {
        return pageCount*pageSize;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        if( index>=0 && index <(pageCount*pageSize)){
            ensureSize();
            return this.slots.get(index).getItemStack();
        }
        return null;
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        if( index>=0 && index <(pageCount*pageSize) && count>0){
            ensureSize();
            try{
                return this.slots.get(index).decrStackSize(count);
            }finally{
                this.markDirty();
            }
        }
        return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int index) {
        if( index>=0 && index <(pageCount*pageSize)){
            ensureSize();
            return  this.slots.get(index).removeItemStack();
        }
        return null;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        if( index>=0 && index <(pageCount*pageSize)){
            ensureSize();
            this.slots.get(index).setItemStack(stack,this.getInventoryStackLimit());
            this.markDirty();
        }        
    }

    protected final void ensureSize() {
        final int newSize = (pageCount*pageSize);               
        while( newSize > this.slots.size()){
            this.slots.add(createInventorySlot(this.slots.size()));
        }
    }

    private InventorySlot createInventorySlot(int index) {
        return new InventorySlot();
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void markDirty() {
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return true;
    }

    @Override
    public void openInventory(EntityPlayer player) {
    }

    @Override
    public void closeInventory(EntityPlayer player) {
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        if( index>=0 && index <(pageCount*pageSize)){
            return this.slots.get(index).checkValid(stack,getInventoryStackLimit());
        }
        return false;
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {
    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {
        this.slots.clear();
    }

}
