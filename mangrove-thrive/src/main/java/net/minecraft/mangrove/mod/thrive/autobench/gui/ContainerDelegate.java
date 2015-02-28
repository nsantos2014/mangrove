package net.minecraft.mangrove.mod.thrive.autobench.gui;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.mangrove.core.gui.MGContainer;

public class ContainerDelegate extends Container{
	private MGContainer container;
	private ContainerTemplate template;
	private ContainerProcessing processing;

	public ContainerDelegate(ContainerProcessing processing, ContainerTemplate template) {
		this.processing = processing;
		this.template = template;
		this.container=processing;
	}
	public void setTemplateContainer(){
		this.container=template;
	}
	public void setProcessingContainer(){
		this.container=processing;
//		this.container=template;
	}
	
	public void onCraftGuiOpened(ICrafting par1iCrafting) {
		container.onCraftGuiOpened(par1iCrafting);
	}

	public List getInventory() {
		return container.getInventory();
	}

	public void removeCraftingFromCrafters(ICrafting par1iCrafting) {
		container.removeCraftingFromCrafters(par1iCrafting);
	}

	public void detectAndSendChanges() {
		container.detectAndSendChanges();
	}

	public boolean enchantItem(EntityPlayer par1EntityPlayer, int par2) {
		return container.enchantItem(par1EntityPlayer, par2);
	}

	public Slot getSlotFromInventory(IInventory par1iInventory, int par2) {
		return container.getSlotFromInventory(par1iInventory, par2);
	}

	public Slot getSlot(int par1) {
		return container.getSlot(par1);
	}

	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {
		return container.transferStackInSlot(par1EntityPlayer, par2);
	}

	public ItemStack slotClick(int par1, int par2, int par3,
			EntityPlayer par4EntityPlayer) {
		return container.slotClick(par1, par2, par3, par4EntityPlayer);
	}

	public boolean canMergeSlot(ItemStack par1ItemStack, Slot par2Slot) {
		return container.canMergeSlot(par1ItemStack, par2Slot);
	}

	public void onContainerClosed(EntityPlayer par1EntityPlayer) {
		container.onContainerClosed(par1EntityPlayer);
	}

	public void onCraftMatrixChanged(IInventory par1iInventory) {
		container.onCraftMatrixChanged(par1iInventory);
	}

	public void putStackInSlot(int par1, ItemStack par2ItemStack) {
		container.putStackInSlot(par1, par2ItemStack);
	}

	public void putStacksInSlots(ItemStack[] par1ArrayOfItemStack) {
		container.putStacksInSlots(par1ArrayOfItemStack);
	}

	public void updateProgressBar(int par1, int par2) {
		container.updateProgressBar(par1, par2);
	}

	public short getNextTransactionID(InventoryPlayer par1InventoryPlayer) {
		return container.getNextTransactionID(par1InventoryPlayer);
	}

//	public boolean isPlayerNotUsingContainer(EntityPlayer par1EntityPlayer) {
//		return container.isPlayerNotUsingContainer(par1EntityPlayer);
//	}
//
//	public void setPlayerIsPresent(EntityPlayer par1EntityPlayer, boolean par2) {
//		container.setPlayerIsPresent(par1EntityPlayer, par2);
//	}

	public boolean canInteractWith(EntityPlayer var1) {
		return container.canInteractWith(var1);
	}

	public boolean canDragIntoSlot(Slot par1Slot) {
		return container.canDragIntoSlot(par1Slot);
	}

}
