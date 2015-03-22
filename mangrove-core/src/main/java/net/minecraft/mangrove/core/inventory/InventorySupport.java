package net.minecraft.mangrove.core.inventory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import net.minecraft.item.ItemStack;
import net.minecraft.mangrove.core.INBTTagable;
import net.minecraft.mangrove.core.inventory.slots.InvSlot;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;

import com.google.common.collect.SetMultimap;
import com.google.common.collect.TreeMultimap;
import com.google.common.primitives.Ints;

public class InventorySupport implements INBTTagable{
	private String inventoryName;
	private boolean customInventoryName=false;
	
	private int size;
	private int maxSize;
	
	private final Map<Integer, ItemStack> slots;
	private SetMultimap<EnumFacing, Integer> slotsBySide=null;
	private Map<Integer, Map<EnumFacing,InvSlot>> slotsMetadata;
	
	
//	private ItemStack[] items;
//	private int[] allSlots ;
	
	private InventorySupport(){
		this.slots=new TreeMap<Integer,ItemStack>();
		this.slotsMetadata=new HashMap<Integer, Map<EnumFacing,InvSlot>>();
	}
	
	public InventorySupport(int size,String inventoryName) {
		this(size,inventoryName,inventoryName!=null && !inventoryName.isEmpty());		
	}
	
	public InventorySupport(int size,String inventoryName, boolean customInventoryName) {
		this();
		this.size = size;
		this.inventoryName = inventoryName;
		this.customInventoryName=customInventoryName;
		
	}

	public String getInventoryName() {
		return customInventoryName?inventoryName:"container";
	}
	public void setInventoryName(String inventoryName) {
		this.inventoryName = inventoryName;
		this.customInventoryName=inventoryName!=null && !inventoryName.isEmpty();
	}	
	public boolean hasCustomInventoryName() {
		return customInventoryName ;
	}	
	public int getSize() {
		return this.size;
	}
	
	public void defineSlotRange(int slotStart,int length,ItemStack itemStack,EnumPermission permission,EnumFacing... sides){
		for( int i=0; i<length; i++){
			defineSlot(slotStart+i, itemStack, permission, sides);
		}
		
	}
	public void defineSlot(int slot,ItemStack itemStack,EnumPermission permission,EnumFacing... sides){
		this.size=Math.max(size, slot+1);
		if( this.slotsBySide==null){
			this.slotsBySide=TreeMultimap.<EnumFacing, Integer>create();
		}
		for(EnumFacing sideId:sides){
			if( permission==EnumPermission.NONE){
				slotsBySide.remove(sideId, slot);
			}else{
				slotsBySide.put(sideId, slot);
			}
			Map<EnumFacing, InvSlot> slotMeta = this.slotsMetadata.get(slot);
			if( slotMeta==null){
				slotMeta=new HashMap<EnumFacing,InvSlot>();
				this.slotsMetadata.put(slot, slotMeta);
			}
			InvSlot sideSlot = slotMeta.get(sideId);
			if(sideSlot==null){
				sideSlot=new InvSlot(slot, sideId);
				slotMeta.put(sideId,sideSlot);
			}
			if( itemStack!=null){
				sideSlot.setItem(itemStack.getItem());
			}
			sideSlot.setPermission(permission);
		}
	}
	
	public int findNextAvailableSlot(ItemStack stack){
		if( stack==null){
			return -1;
		}
		int retval=findNextOpenSlot(stack);
		if( retval<0){
			return findNextFreeSlot();
		}
		return retval;
	}
	public int findNextOpenSlot(final ItemStack search){
		if( search==null){
			return -1;
		}
		for(Entry<Integer, ItemStack> slot:slots.entrySet()){
			if( search.isItemEqual(slot.getValue()) && slot.getValue().stackSize<search.getMaxStackSize()){
				return slot.getKey();
			}			
		}		
		return -1;
	}
	public int findNextFreeSlot(){
		int check=0;
		for(Integer slot:slots.keySet()){
			if(slot!=check){
				return check;
			}
			check++;
		}		
		return check<this.size?check:-1;
	}
	public ItemStack getStackInSlot(int slot) {
		return slots.get(slot);		
	}
	
	public ItemStack incStackSize(int slot, int delta) {
		if( delta>0 && slot>=0 && slot<size){
			
			ItemStack item = slots.get(slot);
			if( item==null)
				return null;
			int newSize = item.stackSize+delta;
			ItemStack retStack=null;
			if( newSize>item.getMaxStackSize()){
				retStack = item.copy();
				newSize=item.getMaxStackSize();
				retStack.stackSize=item.getMaxStackSize()-delta;							
			}
			item.stackSize=newSize;			
			return retStack;			
		}
		return null;
	}
	
	public ItemStack decrStackSize(int slot, int delta) {
		if( delta>0 && slot>=0 && slot<size){
			ItemStack item = slots.get(slot);
			if( item!=null){
				ItemStack retStack = item.copy();			
				retStack.stackSize=Math.min(delta,item.stackSize);
				item.stackSize-=delta;
				if( item.stackSize<=0){
					slots.remove(slot);
				}
				return retStack;
			}
		}
		return null;
	}
		
	public void setSlotContents(int slot, ItemStack item) {
		if( slot>=0 && slot<size){
			if( item==null || item.stackSize<=0){
				slots.remove(slot);
			}else{
				slots.put(slot,item);
			}
		}
	}
	public ItemStack mergeStack(ItemStack from){
		if( from!=null){
			ItemStack ret=from;
			while(ret!=null){
				int slot=findNextAvailableSlot(ret);
				if( slot<0){
					return ret;
				}
				if(getStackInSlot(slot)==null){
					setSlotContents(slot, ret);
					ret=null;
				}else{
					ret = incStackSize(slot, ret.stackSize);
				}
			}			
		}
		return null;
	}
	
	public ItemStack mergeStack(int start,int end,ItemStack from){
		if( from!=null){
			ItemStack ret=from;
			while(ret!=null){
				int slot=findNextAvailableSlot(ret);
				if( slot<0){
					return ret;
				}
				if(getStackInSlot(slot)==null){
					setSlotContents(slot, ret);
					ret=null;
				}else{
					ret = incStackSize(slot, ret.stackSize);
				}
			}			
		}
		return null;
	}
	public ItemStack[] mergeAll(ItemStack... from){
		ItemStack[] retList=null;
		for(ItemStack next:from){
			ItemStack ret=mergeStack(next);
			if( ret!=null){
				retList=grow(retList,1);
				retList[retList.length-1]=ret;
			}
		}
		return retList;
	}
	public ItemStack[] mergeAll(List<ItemStack> from){
		ItemStack[] retList=null;
		for(ItemStack next:from){
			ItemStack ret=mergeStack(next);
			if( ret!=null){
				retList=grow(retList,1);
				retList[retList.length-1]=ret;
			}
		}
		return retList;
	}

	private ItemStack[] grow(ItemStack[] a1, int i) {
		int size=a1==null?0:a1.length;
		ItemStack[] a2=new ItemStack[size+i];
		if (a1!=null){
			System.arraycopy(a1, 0,a2, 0, size);
		}
		return a2;
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		final NBTTagList nbttaglist = tag.getTagList("Items", 10);
        //this.hopperItemStacks = new ItemStack[this.getSizeInventory()];        
        for (int i = 0; i < nbttaglist.tagCount(); ++i){
            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
            int slot = nbttagcompound1.getInteger("Slot");
//            ensureSize(this.hopperItemStacks,slot);
            setSlotContents(slot,ItemStack.loadItemStackFromNBT(nbttagcompound1));            
        }
        if (tag.hasKey("CustomName", 8)){
            this.inventoryName = tag.getString("CustomName");
            this.customInventoryName=true;
        }else{
        	this.customInventoryName=false;
        	this.inventoryName = null;
        }
	}

	

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		NBTTagList nbttaglist = new NBTTagList();

		for (int i = 0; i < size; ++i) {
			for(Entry<Integer, ItemStack> slot:slots.entrySet()){
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setInteger("Slot", slot.getKey().intValue());
				slot.getValue().writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}			
		}
		tag.setTag("Items", nbttaglist);
		if (this.customInventoryName){
             tag.setString("CustomName",this.inventoryName);            
        }
	}

	public int[] getSlotArray(EnumFacing side) {
		if( this.slotsBySide==null){
			int[] allSlots=new int[size];
			for( int i=0; i<size;i++){
				allSlots[i]=i;
			}
			return allSlots;
		}
		//final List<Integer> list = this.slotsBySide.get(side);
		return Ints.toArray(this.slotsBySide.get(side));
//		return ArrayUtils.toPrimitive(list.toArray(new Integer[list.size()]));
	}

	public boolean canInsertItem(int slot, ItemStack item, EnumFacing side) {
		if( slotsBySide==null){
			return true;
		}
		if(!slotsBySide.get(side).contains(slot)){
			return false;
		}
		Map<EnumFacing, InvSlot> slotMeta = slotsMetadata.get(slot);
		if( slotMeta!=null){
			InvSlot slotSideMeta = slotMeta.get(side);
			if( slotSideMeta!=null){
				if( slotSideMeta.getPermission()==EnumPermission.NONE||slotSideMeta.getPermission()==EnumPermission.EXTRACT ){
					return false;
				}
				if(item==null ){
					return slotSideMeta.getItem()==null;
				}
				
				if(slotSideMeta.getItem()!=null&& !item.getItem().equals(slotSideMeta.getItem())){
					return false;
				}
				final ItemStack iStack=getStackInSlot(slot);
				if( iStack!=null ){
					if(!iStack.isItemEqual(item)){
						return false;
					}
					if( iStack.stackSize+item.stackSize>iStack.getMaxStackSize()){
						return false;
					}
				}
				
			}
		}
		
		return true;		
	}

	public boolean canExtractItem(int slot, ItemStack item, EnumFacing side) {
		if( slotsBySide==null){
			return true;
		}
		if(!slotsBySide.get(side).contains(slot)){
			return false;
		}
		Map<EnumFacing, InvSlot> slotMeta = slotsMetadata.get(slot);
		if( slotMeta!=null){
			InvSlot slotSideMeta = slotMeta.get(side);
			if( slotSideMeta!=null){
				if( slotSideMeta.getPermission()==EnumPermission.NONE||slotSideMeta.getPermission()==EnumPermission.INSERT){
					return false;
				}
				if(item==null ){
					return slotSideMeta.getItem()==null;
				}
				
				if(slotSideMeta.getItem()!=null&& !item.getItem().equals(slotSideMeta.getItem())){
					return false;
				}
			}
		}
		return true;
	}

}
