package net.minecraft.mangrove.mod.thrive.autobench.gui;

import java.util.ArrayList;
import java.util.List;

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

public class AutobenchContainer extends MGContainer
{
    /** The crafting matrix inventory (3x3). */
    public InventoryCrafting craftMatrix = new InventoryCrafting(this, 3, 3);
    public IInventory craftResult = new InventoryCraftResult();
	private TileEntityAutobench autobench;
	private List<SlotBase> templateSlots=new ArrayList<SlotBase>();
	private List<SlotBase> operationSlots=new ArrayList<SlotBase>();
	private SlotCrafting slotTemplate;
	private SlotBase slotOperate;
    
    public AutobenchContainer(InventoryPlayer par1InventoryPlayer, TileEntityAutobench autobench){
    	super(autobench,par1InventoryPlayer,84,0);
        this.autobench = autobench;		
       
        slotTemplate = new SlotCrafting(par1InventoryPlayer.player, this.craftMatrix, this.craftResult, 0, 103, 36);
        templateSlots.add(slotTemplate);
        this.addSlotToContainer(slotTemplate);
        int l;
        int i1;

        for (l = 0; l < 3; ++l)
        {
            for (i1 = 0; i1 < 3; ++i1)
            {
                final SlotBase slot = new SlotBase(this.craftMatrix, i1 + l * 3, 9 + i1 * 18, 18 + l * 18);
                templateSlots.add(slot);
				this.addSlotToContainer(slot);
            }
        }
        
//        int l;
//        int i1;
        operationSlots=new ArrayList<SlotBase>();
	  	for (l = 0; l < 3; ++l)
	      {
	          for (i1 = 0; i1 < 3; ++i1)
	          {
	              final SlotBase slot = new SlotBase(autobench, i1 + l * 3, 9 + i1 * 18, 18 + l * 18);
	              operationSlots.add(slot);
				  this.addSlotToContainer(slot);
	          }
	      }
	  	for (l = 0; l < 3; ++l)
	      {
	          for (i1 = 0; i1 < 3; ++i1)
	          {
	              final SlotBase slot = new SlotBase(autobench,9+ i1 + l * 3, 115 + i1 * 18, 18 + l * 18);
	              operationSlots.add(slot);
	              this.addSlotToContainer(slot);
	          }
	      }
//	  	for (int y = 0; y < 3; y++) {
//			for (int x = 0; x < 3; x++) {
//				this.addSlotToContainer(new SlotWorkbench(autobench.craftMatrix, x + y * 3, 30 + x * 18, 17 + y * 18));
//			}
//		}
//	  	for (l = 0; l < 3; ++l)
//	      {
//	          for (i1 = 0; i1 < 3; ++i1)
//	          {
//	              final SlotBase slot = new SlotBase(autobench,19+ i1 + l * 3, 225 + i1 * 18, 18 + l * 18);
//	              operationSlots.add(slot);
//	              this.addSlotToContainer(slot);
//	          }
//	      }
	    
	  	
	  	slotOperate = new SlotBase(autobench, 18,81, 17){
	  		@Override
	  		public boolean canTakeStack(EntityPlayer par1EntityPlayer) {
	  			return false;
	  		}
	  		@Override
	  		public boolean canShift() {
	  			return false;
	  		}
	  		@Override
	  		public ItemStack decrStackSize(int par1) {
	  			return null;
	  		}
	  		
	  	};
	  	operationSlots.add(slotOperate);
		this.addSlotToContainer(slotOperate);

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
        this.onCraftMatrixChanged(this.autobench); 
    }
    @Override
    protected void drawTileInventory(IInventory inventory, int inventorySize) {
    	
    }
    public void hideTemplateSlots(){
    	for(SlotBase slot:templateSlots){
    		slot.hide();
    	}
    }
    public void showTemplateSlots(){
    	for(SlotBase slot:templateSlots){
    		slot.show();
    	}
    }
    
    public void hideOperationSlots(){
    	for(SlotBase slot:operationSlots){
    		slot.hide();
    	}
    }
    public void showOperationSlots(){
    	for(SlotBase slot:operationSlots){
    		slot.show();
    	}
    }
    @Override
    public void detectAndSendChanges() {
    	// TODO Auto-generated method stub
    	super.detectAndSendChanges();
//    	ItemStack output = craftResult.getStackInSlot(0);
//		if (output != prevOutput) {
//			prevOutput = output;
//    	setTemplate();
//			onCraftMatrixChanged(autobench.craftMatrix);
//		}
    }
    
    /**
     * Callback for when the crafting matrix is changed.
     */
    public void onCraftMatrixChanged(IInventory par1IInventory)
    {
        this.craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(this.craftMatrix, this.autobench.getWorld()));
    }

    /**
     * Called when the container is closed.
     */
    public void onContainerClosed(EntityPlayer par1EntityPlayer)
    {
        super.onContainerClosed(par1EntityPlayer);

        if (!this.autobench.getWorld().isRemote)
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

    @Override
    public boolean canMergeSlot(ItemStack par1ItemStack, Slot par2Slot)
    {
        return par2Slot.inventory != this.craftResult && super.canMergeSlot(par1ItemStack, par2Slot);
    }
	public void setTemplate() {
		
		ItemStack itemStack = this.craftResult.getStackInSlot(0);
		if( itemStack==null){
			autobench.setTemplate(null,null);	
		}else{
			ItemStack itemStack1 = itemStack.copy();
			//slotOperate.putStack(itemStack.copy());
			//slotTemplate.decrStackSize(itemStack.stackSize);
			
			this.craftResult.decrStackSize(0, itemStack.stackSize);
			
			slotTemplate.onSlotChange(itemStack, itemStack1);
			
			List<ItemStack> bom=new ArrayList<ItemStack>();
			for( int i=0; i<craftMatrix.getSizeInventory(); i++){
				ItemStack iStack=craftMatrix.getStackInSlot(i);
				if( iStack!=null && iStack.stackSize>0){
					ItemStack copy = iStack.copy();
					copy.stackSize=1;
					bom.add(copy);
					craftMatrix.decrStackSize(i, 1);
					slotTemplate.onSlotChange(itemStack, copy);
				}
			}
			
			autobench.setTemplate(itemStack1,bom);
			slotOperate.putStack(itemStack1);
			//for(ItemStack )
		}
		craftResult.markDirty();
		craftMatrix.markDirty();
		
		slotOperate.onSlotChanged();
		slotTemplate.onSlotChanged();
//		detectAndSendChanges();
	}
}