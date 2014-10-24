package net.minecraft.mangrove.core.inventory;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class InvSlot implements Comparable<InvSlot>{
	private int slotId;
	private int sideId;
	private Item item;
	private Permission permission;
	
	public InvSlot(int slotId, int sideId) {
		super();
		this.slotId = slotId;
		this.sideId = sideId;
		this.item = null;
		this.permission =Permission.BOTH;
	}
	
	public InvSlot(int slotId, int sideId, Item item, Permission permission) {
		super();
		this.slotId = slotId;
		this.sideId = sideId;
		this.item = item;
		this.permission = permission;
	}
	
	public int getSlotId() {
		return slotId;
	}
	public void setSlotId(int slotId) {
		this.slotId = slotId;
	}
	public int getSideId() {
		return sideId;
	}
	public void setSideId(int sideId) {
		this.sideId = sideId;
	}
	public Item getItem() {
		return item;
	}
	public void setItem(Item item) {
		this.item = item;
	}
	public Permission getPermission() {
		return permission;
	}
	public void setPermission(Permission permission) {
		this.permission = permission;
	}

	@Override
	public int compareTo(InvSlot o) {
		int matchSlot = Integer.compare(o.slotId,slotId);
		if( matchSlot!=0)
			return matchSlot;
		return Integer.compare(o.sideId,sideId);
	}
	
	
}
