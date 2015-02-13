package net.minecraft.mangrove.core.inventory.slots;

import net.minecraft.item.Item;
import net.minecraft.mangrove.core.inventory.EnumPermission;
import net.minecraft.util.EnumFacing;

public class InvSlot implements Comparable<InvSlot>{
	private int slotId;
	private EnumFacing sideId;
	private Item item;
	private EnumPermission permission;
	
	public InvSlot(int slotId, EnumFacing sideId) {
		super();
		this.slotId = slotId;
		this.sideId = sideId;
		this.item = null;
		this.permission =EnumPermission.BOTH;
	}
	
	public InvSlot(int slotId, EnumFacing sideId, Item item, EnumPermission permission) {
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
	public EnumFacing getSideId() {
		return sideId;
	}
	public void setSideId(EnumFacing sideId) {
		this.sideId = sideId;
	}
	public Item getItem() {
		return item;
	}
	public void setItem(Item item) {
		this.item = item;
	}
	public EnumPermission getPermission() {
		return permission;
	}
	public void setPermission(EnumPermission permission) {
		this.permission = permission;
	}

	@Override
	public int compareTo(InvSlot o) {
		int matchSlot = Integer.compare(o.slotId,slotId);
		if( matchSlot!=0)
			return matchSlot;
		return Integer.compare(o.sideId.ordinal(),sideId.ordinal());
	}
	
	
}
