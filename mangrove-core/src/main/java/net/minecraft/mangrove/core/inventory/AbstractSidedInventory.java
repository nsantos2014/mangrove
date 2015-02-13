package net.minecraft.mangrove.core.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;

public class AbstractSidedInventory extends AbstractInventory implements ISidedInventory{

    public AbstractSidedInventory(String title, boolean customName) {
        super(title, customName,20,9,false);
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return null;
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        if( index>=0 && index <(pageCount*pageSize)){
            ensureSize();
            return this.slots.get(index).canInsertItem(itemStackIn,direction);
        }      
        return false;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack itemStackOut, EnumFacing direction) {
        if( index>=0 && index <(pageCount*pageSize)){
            ensureSize();
            return this.slots.get(index).canExtractItem(itemStackOut,direction);
        }
        return false;
    }

}
