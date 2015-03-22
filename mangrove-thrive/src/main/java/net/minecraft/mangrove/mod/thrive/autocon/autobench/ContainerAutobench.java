package net.minecraft.mangrove.mod.thrive.autocon.autobench;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.mangrove.core.gui.slots.SlotCrafting;
import net.minecraft.mangrove.core.gui.slots.SlotPhantom;
import net.minecraft.mangrove.core.inventory.AdvInventoryCrafting;
import net.minecraft.mangrove.core.inventory.listeners.InventoryListener;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class ContainerAutobench extends Container implements InventoryListener{
//	public InventoryCrafting craftMatrix = new InventoryCrafting(this, 3, 3);
//	public IInventory craftResult = new InventoryCraftResult();
//	public IInventory activeResult = new InventoryCraftResult();
	private World worldObj;
	private BlockPos pos;
	private TileEntityAutobench autobench;

	public ContainerAutobench(InventoryPlayer playerInventory, World worldIn, BlockPos posIn,TileEntityAutobench autobench) {
		this.worldObj = worldIn;
		this.pos = posIn;
		this.autobench = autobench;
		
		AdvInventoryCrafting bomMatrix = autobench.getBomMatrix();
		SlotCrafting slotIn = new SlotCrafting(playerInventory.player, bomMatrix, autobench.getCraftResult(), 0, 138, 46);
		this.addSlotToContainer(slotIn);
//		this.addSlotToContainer(new SlotPhantom(this.activeResult, 0, 124, 55));
//		this.activeResult.setInventorySlotContents(0, new ItemStack(Blocks.acacia_stairs));
		int i=0;
		int j=0;
		int k = 0;

		for (i = 0; i < 3; ++i) {
			for (j = 0; j < 3; ++j) {
				int index = j + i * 3;
				int xPosition = 44 + j * 18;
				int yPosition = 28 + i * 18;
				
				Slot slotIn2 ;
				if( autobench.isPowered()){
					slotIn2 = new SlotPhantom(bomMatrix, index, xPosition, yPosition);
				}else{
					slotIn2 = new Slot(bomMatrix, index, xPosition, yPosition);
				}
				
				this.addSlotToContainer(slotIn2);
			}
		}

		for (j = 0; j < 3; ++j) {
			for (k = 0; k < 9; ++k) {
				this.addSlotToContainer(new Slot(playerInventory, k + j * 9 + 9, 22 + k * 18, 120 + j * 18));
			}
		}

		for (j = 0; j < 9; ++j) {
			this.addSlotToContainer(new Slot(playerInventory, j, 22 + j * 18, 178));
		}
		this.autobench.getBomMatrix().addInventoryListener(this);
		this.onCraftMatrixChanged(this.autobench.getBomMatrix());
	}

	@Override
	public void onInventoryChanged(IInventory inventory) {	
		this.onCraftMatrixChanged(inventory);
	}
	public void onCraftMatrixChanged(IInventory inventoryIn) {
		this.autobench.getCraftResult().setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(autobench.getBomMatrix(), this.worldObj));
	}
@Override
	public void onCraftGuiOpened(ICrafting listener) {
		super.onCraftGuiOpened(listener);
		
	}
	public void onContainerClosed(EntityPlayer playerIn) {
		super.onContainerClosed(playerIn);
		this.autobench.getBomMatrix().removeInventoryListener(this);
//
//		if (!this.worldObj.isRemote) {
//			for (int i = 0; i < 9; ++i) {
//				ItemStack itemstack = autobench.getBomMatrix().getStackInSlotOnClosing(i);
//
//				if (itemstack != null) {
//					playerIn.dropPlayerItemWithRandomChoice(itemstack, false);
//				}
//			}
//		}
	}

	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
//		return this.worldObj.getBlockState(this.pos).getBlock() != Blocks.crafting_table ? false : playerIn.getDistanceSq((double) this.pos.getX() + 0.5D,
//				(double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) <= 64.0D;
	}

	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		ItemStack itemstack = null;
		Slot slot = (Slot) this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (index == 0) {
				if (!this.mergeItemStack(itemstack1, 10, 46, true)) {
					return null;
				}

				slot.onSlotChange(itemstack1, itemstack);
			} else if (index >= 10 && index < 37) {
				if (!this.mergeItemStack(itemstack1, 37, 46, false)) {
					return null;
				}
			} else if (index >= 37 && index < 46) {
				if (!this.mergeItemStack(itemstack1, 10, 37, false)) {
					return null;
				}
			} else if (!this.mergeItemStack(itemstack1, 10, 46, false)) {
				return null;
			}

			if (itemstack1.stackSize == 0) {
				slot.putStack((ItemStack) null);
			} else {
				slot.onSlotChanged();
			}

			if (itemstack1.stackSize == itemstack.stackSize) {
				return null;
			}

			slot.onPickupFromSlot(playerIn, itemstack1);
		}

		return itemstack;
	}

	public boolean canMergeSlot(ItemStack stack, Slot p_94530_2_) {
		return p_94530_2_.inventory != autobench.getCraftResult() && super.canMergeSlot(stack, p_94530_2_);
	}
}