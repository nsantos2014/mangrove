/**
 * Copyright (c) 2011-2014, SpaceToad and the BuildCraft Team
 * http://www.mod-buildcraft.com
 *
 * BuildCraft is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package net.minecraft.mangrove.core.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;

public class StackHelper {

	private static StackHelper instance;

	public static StackHelper instance() {
		if (instance == null) {
			instance = new StackHelper();
		}
		return instance;
	}

	public static void setInstance(StackHelper inst) {
		instance = inst;
	}

	protected StackHelper() {
	}

	/* STACK MERGING */
	/**
	 * Checks if two ItemStacks are identical enough to be merged
	 *
	 * @param stack1 - The first stack
	 * @param stack2 - The second stack
	 * @return true if stacks can be merged, false otherwise
	 */
	public boolean canStacksMerge(ItemStack stack1, ItemStack stack2) {
		if (stack1 == null || stack2 == null)
			return false;
		if (!stack1.isItemEqual(stack2))
			return false;
		if (!ItemStack.areItemStackTagsEqual(stack1, stack2))
			return false;
		return true;

	}

	/**
	 * Merges mergeSource into mergeTarget
	 *
	 * @param mergeSource - The stack to merge into mergeTarget, this stack is
	 * not modified
	 * @param mergeTarget - The target merge, this stack is modified if doMerge
	 * is set
	 * @param doMerge - To actually do the merge
	 * @return The number of items that was successfully merged.
	 */
	public int mergeStacks(ItemStack mergeSource, ItemStack mergeTarget, boolean doMerge) {
		if (!canStacksMerge(mergeSource, mergeTarget))
			return 0;
		int mergeCount = Math.min(mergeTarget.getMaxStackSize() - mergeTarget.stackSize, mergeSource.stackSize);
		if (mergeCount < 1)
			return 0;
		if (doMerge) {
			mergeTarget.stackSize += mergeCount;
		}
		return mergeCount;
	}

	/* ITEM COMPARISONS */
	/**
	 * Determines whether the given ItemStack should be considered equivalent
	 * for crafting purposes.
	 *
	 * @param base The stack to compare to.
	 * @param comparison The stack to compare.
	 * @param oreDictionary true to take the Forge OreDictionary into account.
	 * @return true if comparison should be considered a crafting equivalent for
	 * base.
	 */
	public boolean isCraftingEquivalent(ItemStack base, ItemStack comparison, boolean oreDictionary) {
		if (isMatchingItem(base, comparison, true, false))
			return true;

		if (oreDictionary) {
			int[] idBases = OreDictionary.getOreIDs(base);
			for(int idBase:idBases){
				if (idBase >= 0) {
					final String oreName = OreDictionary.getOreName(idBase);
					for (ItemStack itemstack : OreDictionary.getOres(oreName)) {
						if (comparison.getItem() == itemstack.getItem() && (itemstack.getItemDamage() == OreDictionary.WILDCARD_VALUE || comparison.getItemDamage() == itemstack.getItemDamage()))
							return true;
					}
				}	
			}
			
		}

		return false;
	}

	public boolean isCraftingEquivalent(int oreID, ItemStack comparison) {
		if (oreID >= 0) {
			final String oreName = OreDictionary.getOreName(oreID);
			for (ItemStack itemstack : OreDictionary.getOres(oreName)) {
				if (comparison.getItem() == itemstack.getItem() && (itemstack.getItemDamage() == OreDictionary.WILDCARD_VALUE || comparison.getItemDamage() == itemstack.getItemDamage()))
					return true;
			}
		}

		return false;
	}

	/**
	 * Compares item id, damage and NBT. Accepts wildcard damage. Ignores damage
	 * entirely if the item doesn't have subtypes.
	 *
	 * @param base The stack to compare to.
	 * @param comparison The stack to compare.
	 * @return true if id, damage and NBT match.
	 */
	public boolean isMatchingItem(ItemStack base, ItemStack comparison) {
		return isMatchingItem(base, comparison, true, true);
	}

	/**
	 * Compares item id, and optionally damage and NBT. Accepts wildcard damage.
	 * Ignores damage entirely if the item doesn't have subtypes.
	 *
	 * @param a ItemStack
	 * @param b ItemStack
	 * @param matchDamage
	 * @param matchNBT
	 * @return true if matches
	 */
	public boolean isMatchingItem(final ItemStack a, final ItemStack b, final boolean matchDamage, final boolean matchNBT) {
		if (a == null || b == null) {
			return false;
		}
		if (a.getItem() != b.getItem()) {
			return false;
		}
		if (matchDamage && a.getHasSubtypes()) {
			if (!isWildcard(a) && !isWildcard(b)) {
				if (a.getItemDamage() != b.getItemDamage()) {
					return false;
				}
			}
		}
		if (matchNBT) {
			NBTTagCompound aTagCompound = a.getTagCompound();
			NBTTagCompound bTagCompound = b.getTagCompound();
			if (aTagCompound != null && !aTagCompound.equals(bTagCompound)) {
				return false;
			}
		}
		return true;
	}

	public boolean isWildcard(ItemStack stack) {
		return isWildcard(stack.getItemDamage());
	}

	public boolean isWildcard(int damage) {
		return damage == -1 || damage == OreDictionary.WILDCARD_VALUE;
	}
	
	public static void packItemStackList(final List<ItemStack> stackList) {
        if( stackList==null){
            return;
        }
        for( int i=0; i< stackList.size(); i++) {
            final ItemStack item=stackList.get(i);
            final List<ItemStack> nextItems = stackList.subList(i+1, stackList.size());
            final Iterator<ItemStack> it = nextItems.iterator();
            final List<ItemStack> toRemove=new ArrayList<ItemStack>();
            while(it.hasNext()){
                final ItemStack next = it.next();
                if( item.isItemEqual(next)){
                    if( item.stackSize+next.stackSize > item.getMaxStackSize()){
                        next.stackSize = item.getMaxStackSize()-item.stackSize;
                        item.stackSize=item.getMaxStackSize();
                    }else{
                        item.stackSize+=next.stackSize;
                        toRemove.add(next);
                    }
                    if( item.stackSize==64){
                        break;
                    }
                }
            }
            if( !toRemove.isEmpty()){
                stackList.removeAll(toRemove);
            }
        }
    }
}
