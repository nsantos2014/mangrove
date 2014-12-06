package net.minecraft.mangrove.mod.thrive.autobench.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.mangrove.core.gui.MGContainer;
import net.minecraft.mangrove.core.gui.slots.SlotBase;
import net.minecraft.mangrove.core.gui.slots.SlotCrafting;
import net.minecraft.mangrove.mod.thrive.autobench.TileEntityAutobench;

public class ContainerTemplate extends MGContainer
{
    /** The crafting matrix inventory (3x3). */
    public InventoryCrafting craftMatrix = new InventoryCrafting(this, 3, 3);
    public IInventory craftResult = new InventoryCraftResult();
	private TileEntityAutobench autobench;
    
    public ContainerTemplate(InventoryPlayer par1InventoryPlayer, TileEntityAutobench autobench){
    	super(autobench,par1InventoryPlayer,84,0);
        this.autobench = autobench;		
       
        final SlotCrafting slotTemplate = new SlotCrafting(par1InventoryPlayer.player, this.craftMatrix, this.craftResult, 0, 103, 36);
        
        this.addSlotToContainer(slotTemplate);
        int l;
        int i1;

        for (l = 0; l < 3; ++l)
        {
            for (i1 = 0; i1 < 3; ++i1)
            {
                final SlotBase slot = new SlotBase(this.craftMatrix, i1 + l * 3, 9 + i1 * 18, 18 + l * 18);                
				this.addSlotToContainer(slot);
            }
        }
        

//        for (l = 0; l < 3; ++l)
//        {
//            for (i1 = 0; i1 < 9; ++i1)
//            {
//                this.addSlotToContainer(new Slot(par1InventoryPlayer, i1 + l * 9 + 9, 8 + i1 * 18, 84 + l * 18));
//            }
//        }
//
//        for (l = 0; l < 9; ++l)
//        {
//            this.addSlotToContainer(new Slot(par1InventoryPlayer, l, 8 + l * 18, 142));
//        }
        
        

        this.onCraftMatrixChanged(this.craftMatrix);        
    }
    @Override
    protected void drawTileInventory(IInventory inventory, int inventorySize) {
    	
    }
    /**
     * Callback for when the crafting matrix is changed.
     */
    public void onCraftMatrixChanged(IInventory par1IInventory)
    {
        this.craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(this.craftMatrix, this.autobench.getWorldObj()));
    }

    /**
     * Called when the container is closed.
     */
    public void onContainerClosed(EntityPlayer par1EntityPlayer)
    {
        super.onContainerClosed(par1EntityPlayer);

        if (!this.autobench.getWorldObj().isRemote)
        {
            for (int i = 0; i < 9; ++i)
            {
                ItemStack itemstack = this.craftMatrix.getStackInSlotOnClosing(i);

                if (itemstack != null)
                {
                    par1EntityPlayer.dropPlayerItemWithRandomChoice(itemstack, false);
                }
            }
        }
    }

    public boolean canInteractWith(EntityPlayer par1EntityPlayer)
    {
//        return this.autobench.getBlockType() != MyMod.autobench /*? false : par1EntityPlayer.getDistanceSq((double)this.autobench.get + 0.5D, (double)this.posY + 0.5D, (double)this.posZ + 0.5D) <= 64.0D*/;
    	return true;
    }

    /**
     * Called when a player shift-clicks on a slot. You must override this or you will crash when someone does that.
     */
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(par2);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (par2 == 0)
            {
                if (!this.mergeItemStack(itemstack1, 10, 46, true))
                {
                    return null;
                }

                slot.onSlotChange(itemstack1, itemstack);
            }
            else if (par2 >= 10 && par2 < 37)
            {
                if (!this.mergeItemStack(itemstack1, 37, 46, false))
                {
                    return null;
                }
            }
            else if (par2 >= 37 && par2 < 46)
            {
                if (!this.mergeItemStack(itemstack1, 10, 37, false))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 10, 46, false))
            {
                return null;
            }

            if (itemstack1.stackSize == 0)
            {
                slot.putStack((ItemStack)null);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (itemstack1.stackSize == itemstack.stackSize)
            {
                return null;
            }

            slot.onPickupFromSlot(par1EntityPlayer, itemstack1);
        }

        return itemstack;
    }

    public boolean func_94530_a(ItemStack par1ItemStack, Slot par2Slot)
    {
        return par2Slot.inventory != this.craftResult && super.func_94530_a(par1ItemStack, par2Slot);
    }
}