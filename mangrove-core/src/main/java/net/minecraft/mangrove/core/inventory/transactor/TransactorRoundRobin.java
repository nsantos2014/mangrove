/**
 * Copyright (c) 2011-2014, SpaceToad and the BuildCraft Team
 * http://www.mod-buildcraft.com
 *
 * BuildCraft is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package net.minecraft.mangrove.core.inventory.transactor;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.mangrove.core.inventory.iterator.InventoryIterator;
import net.minecraft.mangrove.core.inventory.iterator.InventoryIterator.IInvSlot;
import net.minecraft.mangrove.core.utils.StackHelper;
import net.minecraft.util.EnumFacing;

public class TransactorRoundRobin extends TransactorSimple {

	public TransactorRoundRobin(IInventory inventory) {
		super(inventory);
	}

	@Override
	public int inject(ItemStack stack, EnumFacing orientation, boolean doAdd) {

		int added = 0;

		for (int itemLoop = 0; itemLoop < stack.stackSize; ++itemLoop) { // add 1 item n times.

			int smallestStackSize = Integer.MAX_VALUE;
			IInvSlot minSlot = null;

			for (IInvSlot slot : InventoryIterator.getIterable(inventory, orientation)) {
				ItemStack stackInInventory = slot.getStackInSlot();

				if (stackInInventory == null) {
					continue;
				}

				if (stackInInventory.stackSize >= stackInInventory.getMaxStackSize()) {
					continue;
				}

				if (stackInInventory.stackSize >= inventory.getInventoryStackLimit()) {
					continue;
				}

				if (StackHelper.instance().canStacksMerge(stack, stackInInventory) && stackInInventory.stackSize < smallestStackSize) {
					smallestStackSize = stackInInventory.stackSize;
					minSlot = slot;
				}
				if (smallestStackSize <= 1) {
					break;
				}
			}

			if (minSlot != null) {
				added += addToSlot(minSlot, stack, stack.stackSize - 1, doAdd); // add 1 item n times, into the selected slot
			} else {
				break;
			}

		}

		return added;
	}
}
