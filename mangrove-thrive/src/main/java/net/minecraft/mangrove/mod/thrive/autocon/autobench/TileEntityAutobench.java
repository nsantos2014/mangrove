package net.minecraft.mangrove.mod.thrive.autocon.autobench;

import java.io.IOException;
import java.util.List;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.item.ItemStack;
import net.minecraft.mangrove.core.ITileUpdatable;
import net.minecraft.mangrove.core.inventory.AdvInventoryCrafting;
import net.minecraft.mangrove.core.json.JSON;
import net.minecraft.mangrove.mod.thrive.MGThriveBlocks;
import net.minecraft.mangrove.mod.thrive.autocon.AbstractTileAutocon;
import net.minecraft.mangrove.mod.thrive.autocon.SearchItem;
import net.minecraft.mangrove.mod.thrive.autocon.SearchUtil;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.util.EnumFacing;

import com.google.gson.JsonObject;

public class TileEntityAutobench extends AbstractTileAutocon implements
		ITileUpdatable, IUpdatePlayerListBox {
	public int transferCooldown;
	private boolean working = true;

	private AdvInventoryCrafting bomMatrix = new AdvInventoryCrafting(3, 3);
	private IInventory craftResult = new InventoryCraftResult();
	private CraftingProcessor craftingProcessor = new CraftingProcessor();
	private int coolDown = 100;

	// public ItemStack template = null;
	// public final List<ItemStack> bom = new ArrayList<ItemStack>();

	private String name;
	private EnumFacing enumFacing;

	public TileEntityAutobench(EnumFacing enumFacing) {
		super();
		this.enumFacing = enumFacing;
		this.name = null;
	}

	public TileEntityAutobench() {
		super();
	}

	public String getName() {
		if (this.name == null) {
			this.name = String.format("AutoBench (%d,%d,%d)", pos.getX(),
					pos.getY(), pos.getZ());
		}
		return this.name;
	}

	public boolean isSideConnected(EnumFacing facing) {
		EnumFacing tileFacing = (EnumFacing) worldObj.getBlockState(pos)
				.getValue(BlockAutobench.FACING);
		return facing != tileFacing;
	}

	public boolean isPowered() {
		return (Boolean) worldObj.getBlockState(pos).getValue(
				BlockAutobench.POWERED);
	}

	public AdvInventoryCrafting getBomMatrix() {
		return bomMatrix;
	}

	public IInventory getCraftResult() {
		return craftResult;
	}

	@Override
	public void update() {
		if ((this.worldObj == null) || (this.worldObj.isRemote)) {
			return;
		}

		if (!isPowered() || !isCraftingDefined()) {
			return;
		}

		List<SearchItem> inventories = SearchUtil.findAllTileFrom(worldObj,
				pos, MGThriveBlocks.duct_conveyor, IInventory.class);

		if (inventories.isEmpty()) {
			return;
		}
		this.coolDown--;
		if (this.coolDown <= 0) {
			craftingProcessor.startProcessing();
			System.out.println("Template : "
					+ this.craftResult.getStackInSlot(0));
			
			for (int i=0 ; i< this.bomMatrix.getSizeInventory(); i++) {
				ItemStack iStack = this.bomMatrix.getStackInSlot(i);
				if( iStack!=null && iStack.stackSize>0){
					craftingProcessor.addInlet(iStack, inventories);
				}
			}
			ItemStack templateStack = this.craftResult.getStackInSlot(0);
			if( templateStack!=null && templateStack.stackSize>0){
				craftingProcessor.setOutlet(templateStack, inventories);
			}
			if( craftingProcessor.canProcess()){
				craftingProcessor.process(worldObj);
			}else{
				System.out.println("Cannot process!!!!");
			}
			craftingProcessor.endProcessing();
			this.coolDown = 100;
		}

		// // TODO Iterate over B.O.M. and trying to withdraw needs and keeping
		// when successfull
		// for(SearchItem invLocation:inventories){
		// IInventory entity = (IInventory)
		// worldObj.getTileEntity(invLocation.getPos());
		//
		// for(IInvSlot slot: InventoryIterator.getIterable(entity,
		// invLocation.getFacing())){
		// slot.canTakeStackFromSlot(stack)
		// }
		//
		// }

		// TODO try to append crafting result and keep if successful

		// TODO commit the transaction : remove BOM and add craft

		// this.transferCooldown--;
		//
		// if (isCoolingDown())
		// return;
		//
		// setTransferCooldown(0);
		// if (template != null && !bom.isEmpty()) {
		// // System.out.println("Kelood : "+this.template+" :>"+this.bom);
		// if (!checkAvailableMaterials()) {
		// if (this.working) {
		// NetBus.notify(getName(), "Not enough materials");
		// }
		// this.working = false;
		// // consumeAndCraft();
		// } else if (!checkSpaceForCraft()) {
		// if (this.working) {
		// NetBus.notify(getName(), "Not enough space for output");
		// }
		// this.working = false;
		// } else {
		// doConsume();
		// doCrafting();
		// }
		// }
		// // doStuffs();
		// setTransferCooldown(100);
	}

	@Override
	public JsonObject getTilePacketData() {
		return JSON.newObject();
	}

	@Override
	public void handleClientUpdate(JsonObject data) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleServerUpdate(JsonObject data) throws IOException {
		// TODO Auto-generated method stub

	}

	// private boolean checkSpaceForCraft() {
	// final ItemStack craft = template.copy();
	// for (int i = 9; i < 18; i++) {
	// final ItemStack iStack = inventorySupport.getStackInSlot(i);
	// if (iStack == null) {
	// if (craft.stackSize > craft.getMaxStackSize()) {
	// craft.splitStack(craft.getMaxStackSize());
	// } else {
	// craft.stackSize = 0;
	// return true;
	// }
	// } else if (iStack.isItemEqual(craft)) {
	// if (craft.stackSize + iStack.stackSize > iStack.getMaxStackSize()) {
	// craft.splitStack(iStack.getMaxStackSize() - iStack.stackSize);
	// } else {
	// craft.stackSize = 0;
	// }
	// }
	// if (craft.stackSize <= 0) {
	// return true;
	// }
	// }
	// return false;
	// }
	//
	// private boolean checkAvailableMaterials() {
	// final Map<Item, Integer> itemCount = new HashMap<Item, Integer>();
	// for (final ItemStack bomItem : this.bom) {
	// if (bomItem != null) {
	// final Item item = bomItem.getItem();
	// int count = bomItem.stackSize;
	// if (itemCount.containsKey(item)) {
	// itemCount.put(item, itemCount.get(item) + count);
	// } else {
	// itemCount.put(item, count);
	// }
	// }
	// }
	// if (!itemCount.isEmpty()) {
	// for (int i = 0; i < 9; i++) {
	// final ItemStack iStack = inventorySupport.getStackInSlot(i);
	// if (iStack != null) {
	// final Item item = iStack.getItem();
	// if (itemCount.containsKey(item)) {
	// int count = itemCount.get(item);
	// if (count <= iStack.stackSize) {
	// itemCount.remove(item);
	// } else {
	// itemCount.put(item, count - iStack.stackSize);
	// }
	// }
	// }
	// if (itemCount.isEmpty()) {
	// return true;
	// }
	//
	// }
	// }
	// return false;
	// }
	//
	// private void doConsume() {
	// final Map<Item, Integer> itemCount = new HashMap<Item, Integer>();
	// for (final ItemStack bomItem : this.bom) {
	// if (bomItem != null) {
	// final Item item = bomItem.getItem();
	// int count = bomItem.stackSize;
	// if (itemCount.containsKey(item)) {
	// itemCount.put(item, itemCount.get(item) + count);
	// } else {
	// itemCount.put(item, count);
	// }
	// }
	// }
	//
	// if (!itemCount.isEmpty()) {
	// for (int i = 0; i < 9; i++) {
	// final ItemStack iStack = inventorySupport.getStackInSlot(i);
	// if (iStack != null) {
	// final Item item = iStack.getItem();
	// if (itemCount.containsKey(item)) {
	// int count = itemCount.get(item);
	// if (count <= iStack.stackSize) {
	// itemCount.remove(item);
	// iStack.splitStack(count);
	// inventorySupport.setSlotContents(i, iStack);
	// } else {
	// itemCount.put(item, count - iStack.stackSize);
	// inventorySupport.setSlotContents(i, null);
	// }
	// }
	// }
	// if (itemCount.isEmpty()) {
	// break;
	// }
	//
	// }
	// }
	// }
	//
	// private void doCrafting() {
	// final ItemStack craft = template.copy();
	// for (int i = 9; i < 18; i++) {
	// final ItemStack iStack = inventorySupport.getStackInSlot(i);
	// if (iStack == null) {
	// if (craft.stackSize > craft.getMaxStackSize()) {
	// inventorySupport.setSlotContents(i,
	// craft.splitStack(craft.getMaxStackSize()));
	// } else {
	// inventorySupport.setSlotContents(i, craft);
	// break;
	// }
	// } else if (iStack.isItemEqual(craft)) {
	// if (craft.stackSize + iStack.stackSize > iStack.getMaxStackSize()) {
	// craft.splitStack(iStack.getMaxStackSize() - iStack.stackSize);
	// iStack.stackSize = iStack.getMaxStackSize();
	// } else {
	// iStack.stackSize = craft.stackSize + iStack.stackSize;
	// craft.stackSize = 0;
	// }
	// inventorySupport.setSlotContents(i, iStack);
	// }
	// if (craft.stackSize <= 0) {
	// break;
	// }
	// }
	// }
	//
	// public boolean isCoolingDown() {
	// return (this.transferCooldown > 0);
	// }
	//
	// public void setTransferCooldown(int par1) {
	// this.transferCooldown = par1;
	// }
	//
	// public void setTemplate(ItemStack itemStack, List<ItemStack> bom) {
	// inventorySupport.setSlotContents(18, itemStack);
	// StackHelper.packItemStackList(bom);
	// int i = 19;
	// for (ItemStack iStack : bom) {
	// if (iStack != null) {
	// inventorySupport.setSlotContents(i++, iStack);
	// }
	// }
	// System.out.println(" > " + bom);
	// markDirty();
	// if (this.worldObj.isRemote) {
	// final JsonObject evt = JSON.newObject();
	// evt.add("template", itemStackToJson(itemStack));
	// JsonArray bomArray = new JsonArray();
	// Iterator<ItemStack> it = bom.iterator();
	// while (it.hasNext()) {
	// bomArray.add(itemStackToJson(it.next()));
	// }
	// evt.add("bom", bomArray);
	// NetBus.sendToServer(new TileEntityMessage(this, evt));
	// // MyMod.packetPipeline.sendToServer(new
	// // DataStatePacket<AutobenchData>(new Point3D(xCoord, yCoord,
	// // zCoord), new AutobenchData(itemStack, bom)));
	// }
	// }
	//
	// protected final void fireLifecycleEvent() {
	// if (this.worldObj.isRemote) {
	//
	// }
	// }
	//
	// @Override
	// public JsonObject getTilePacketData() {
	// final JsonObject data = JSON.newObject();
	//
	// return data;
	// }
	//
	// private JsonObject itemStackToJson(ItemStack itemStack) {
	// final JsonObject data = JSON.newObject();
	// ResourceLocation resourcelocation = (ResourceLocation)
	// Item.itemRegistry.getNameForObject(itemStack.getItem());
	// data.addProperty("itemId", resourcelocation == null ? "minecraft:air" :
	// resourcelocation.toString());
	// data.addProperty("stackSize", itemStack.stackSize);
	// data.addProperty("damageValue", itemStack.getMaxDamage());
	// return data;
	// }
	//
	// private ItemStack jsonToItemStack(JsonObject data) {
	// String itemId = data.get("itemId").getAsString();
	//
	// int stackSize = data.get("stackSize").getAsInt();
	// int damageValue = data.get("damageValue").getAsInt();
	//
	// return new ItemStack(Item.getByNameOrId(itemId), stackSize, damageValue);
	// }
	//
	// @Override
	// public void handleClientUpdate(JsonObject data) throws IOException {
	//
	// }
	//
	// @Override
	// public void handleServerUpdate(JsonObject data) throws IOException {
	// JsonObject templateObj = data.get("template").getAsJsonObject();
	// this.template = jsonToItemStack(templateObj);
	// inventorySupport.setSlotContents(18, this.template);
	//
	// JsonArray bomArray = data.get("bom").getAsJsonArray();
	// Iterator<JsonElement> it = bomArray.iterator();
	// this.bom.clear();
	// while (it.hasNext()) {
	// JsonObject bomObj = it.next().getAsJsonObject();
	// this.bom.add(jsonToItemStack(bomObj));
	// }
	//
	// }
	//
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		this.coolDown = tag.getInteger("Cooldown");
		// this.working = tag.getBoolean("Working");
		// this.paused = tag.getBoolean("Paused");
		NBTTagCompound templateTag = tag.getCompoundTag("Template");

		this.bomMatrix.clear();
		this.craftResult.clear();

		if (templateTag.hasKey("Item")) {
			NBTTagCompound itemTag = templateTag.getCompoundTag("Item");
			final NBTTagList nbttaglist = templateTag.getTagList("BOM", 10);

			ItemStack templateStack = ItemStack.loadItemStackFromNBT(itemTag);
			if (templateStack != null) {
				for (int i = 0; i < nbttaglist.tagCount(); ++i) {
					NBTTagCompound nbttagcompound1 = nbttaglist
							.getCompoundTagAt(i);
					int slot = nbttagcompound1.getByte("Slot");
					this.bomMatrix.setInventorySlotContents(slot,
							ItemStack.loadItemStackFromNBT(nbttagcompound1));
				}
				craftResult.setInventorySlotContents(0, templateStack);
			}

			// this.hopperItemStacks = new ItemStack[this.getSizeInventory()];

		}
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		tag.setInteger("Cooldown", this.coolDown);
		// tag.setBoolean("Working", working);
		// tag.setBoolean("Paused", paused);
		NBTTagCompound templateTag = new NBTTagCompound();

		ItemStack templateStack = this.craftResult.getStackInSlot(0);
		if (templateStack != null) {
			NBTTagCompound itemtag = new NBTTagCompound();
			templateStack.writeToNBT(itemtag);
			templateTag.setTag("Item", itemtag);
			NBTTagList nbttaglist = new NBTTagList();

			for (int i = 0; i < this.bomMatrix.getSizeInventory(); ++i) {
				ItemStack iStack = this.bomMatrix.getStackInSlot(i);
				if (iStack != null) {
					NBTTagCompound nbttagcompound1 = new NBTTagCompound();
					nbttagcompound1.setByte("Slot", (byte) i);
					iStack.writeToNBT(nbttagcompound1);
					nbttaglist.appendTag(nbttagcompound1);
				}
			}

			templateTag.setTag("BOM", nbttaglist);
		}
		tag.setTag("Template", templateTag);

	}

	public boolean isCraftingDefined() {
		ItemStack stackInSlot = craftResult.getStackInSlot(0);
		return stackInSlot != null && stackInSlot.stackSize > 0;
	}

}
