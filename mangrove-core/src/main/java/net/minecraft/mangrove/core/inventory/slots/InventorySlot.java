package net.minecraft.mangrove.core.inventory.slots;

import net.minecraft.item.ItemStack;
import net.minecraft.mangrove.core.inventory.EnumPermission;
import net.minecraft.util.EnumFacing;

public class InventorySlot {
    private ItemStack itemStack=null;
    private EnumFacing face=EnumFacing.UP;
    private EnumPermission permission=EnumPermission.BOTH;
    
    public EnumFacing getFace() {
        return face;
    }
    public void setFace(EnumFacing face) {
        this.face = face;
    }
    public EnumPermission getPermission() {
        return permission;
    }
    public void setPermission(EnumPermission permission) {
        this.permission = permission;
    }
    public ItemStack  getItemStack(){
        return itemStack;
    }
    public void setItemStack(ItemStack newItemStack,int maxStackSize){
        this.itemStack=newItemStack;
        if (this.itemStack != null && this.itemStack.stackSize > maxStackSize){
            this.itemStack.stackSize = maxStackSize;
        }
    }
    public ItemStack removeItemStack(){
        final ItemStack returnStack = itemStack;
        itemStack=null;
        return returnStack;
    }
    
    public ItemStack decrStackSize(int count){
        final ItemStack result;
        if( itemStack.stackSize <= count){
              result=itemStack;
              itemStack=null;
        }else{
            result = itemStack.splitStack(count);
            if(itemStack.stackSize==0){
                itemStack=null;
            }
        }
        return result;
    }
    public boolean checkValid(ItemStack stack, int inventoryStackLimit) {
        return true;
    }
    public boolean canInsertItem(ItemStack itemStackIn, EnumFacing direction) {
        return (this.permission==EnumPermission.INSERT || this.permission==EnumPermission.BOTH);
    }
    public boolean canExtractItem(ItemStack itemStackOut, EnumFacing direction) {
        return (this.permission==EnumPermission.EXTRACT || this.permission==EnumPermission.BOTH);
    }
    
}
