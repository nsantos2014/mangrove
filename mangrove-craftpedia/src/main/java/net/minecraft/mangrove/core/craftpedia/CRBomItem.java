package net.minecraft.mangrove.core.craftpedia;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CRBomItem {

	private Item	                item;
	private int firstMeta =0;
	private Map<Integer, ItemStack>	itemStacks	= new HashMap<Integer, ItemStack>();

	private Map<Integer, String>	itemNames	= new HashMap<Integer, String>();

	public void inc(int meta) {
		if (itemStacks.containsKey(meta)) {
			itemStacks.get(meta).stackSize++;
		}
	}

	public void incAll() {
		for(ItemStack iStack:itemStacks.values()){
			iStack.stackSize++;
		}
	}
	
	public void inc() {
		inc(firstMeta);
	}

	public Item getItem() {
		return item;
	}

	public String getName(int meta) {
//		if (itemNames.containsKey(meta)) {
			return itemNames.get(meta);
//		}
	}
	
	public String getName(){
		return getName(firstMeta);
	}
	public ItemStack getItemStack() {
		return getItemStack(firstMeta);
	}
	public ItemStack getItemStack(int meta) {
		return itemStacks.get(meta);
    }

	public static CRBomItem of(Item item) {
		return of(item, Arrays.asList(new ItemStack(item)));
	}

	public static CRBomItem of(Item item, int itemDamage) {
		return of(item, Arrays.asList(new ItemStack(item, 1, itemDamage)));
	}

	public static CRBomItem of(Item item, List<ItemStack> subItems) {
		CRBomItem bomItem = new CRBomItem();
		bomItem.item = item;
//		bomItem.subItems = subItems;
		bomItem.firstMeta=-1;
		for (ItemStack iStack : subItems) {
			if( bomItem.firstMeta<0){
				bomItem.firstMeta=iStack.getMetadata();
			}
			ItemStack stack = iStack.copy();
			bomItem.itemStacks.put(stack.getMetadata(), stack);
			bomItem.itemNames.put(stack.getMetadata(), Util.resolveItemName(iStack));
		}
		return bomItem;
	}

	

}
