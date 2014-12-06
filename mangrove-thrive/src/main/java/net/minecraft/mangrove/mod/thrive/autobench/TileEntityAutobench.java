package net.minecraft.mangrove.mod.thrive.autobench;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.vecmath.Point3d;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.mangrove.core.ITileUpdatable;
import net.minecraft.mangrove.core.block.AbstractSidedInventoryTileEntity;
import net.minecraft.mangrove.core.inventory.InvUtils;
import net.minecraft.mangrove.core.inventory.Permission;
import net.minecraft.mangrove.core.json.JSON;
import net.minecraft.mangrove.network.NetBus;
import net.minecraft.mangrove.network.TileEntityMessage;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class TileEntityAutobench extends AbstractSidedInventoryTileEntity implements ITileUpdatable {
    public int transferCooldown;
    private boolean working = true;

    // public boolean paused=false;

    public ItemStack template = null;
    public final List<ItemStack> bom = new ArrayList<ItemStack>();

    private String name;

    public TileEntityAutobench() {
        super();
        this.name = null;
        inventorySupport.defineSlotRange(0, 9, null, Permission.INSERT, 0, 2, 3, 4, 5);
        inventorySupport.defineSlotRange(9, 9, null, Permission.EXTRACT, 0, 2, 3, 4, 5);
        inventorySupport.defineSlot(18, null, Permission.BOTH, -1);

    }

    public String getName() {
        if (this.name == null) {
            this.name = String.format("AutoBench (%d,%d,%d)", xCoord, yCoord, zCoord);
        }
        return this.name;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        this.transferCooldown = tag.getInteger("TransferCooldown");
        this.working = tag.getBoolean("Working");
        // this.paused = tag.getBoolean("Paused");
        NBTTagCompound templateTag = tag.getCompoundTag("Template");
        this.template = null;
        this.bom.clear();
        if (templateTag.hasKey("Item")) {
            NBTTagCompound itemTag = templateTag.getCompoundTag("Item");
            this.template = ItemStack.loadItemStackFromNBT(itemTag);

            inventorySupport.setSlotContents(18, this.template);

            final NBTTagList nbttaglist = templateTag.getTagList("BOM", 10);
            // this.hopperItemStacks = new ItemStack[this.getSizeInventory()];
            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
                this.bom.add(ItemStack.loadItemStackFromNBT(nbttagcompound1));
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setInteger("TransferCooldown", this.transferCooldown);
        tag.setBoolean("Working", working);
        // tag.setBoolean("Paused", paused);
        NBTTagCompound templateTag = new NBTTagCompound();
        if (this.template != null) {
            NBTTagCompound itemtag = new NBTTagCompound();
            this.template.writeToNBT(itemtag);
            templateTag.setTag("Item", itemtag);
            NBTTagList nbttaglist = new NBTTagList();

            for (ItemStack iStack : bom) {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                iStack.writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
            templateTag.setTag("BOM", nbttaglist);
        }
        tag.setTag("Template", templateTag);

        // nbttagcompound1.setByte("Slot", slot.getKey().byteValue());
        // slot.getValue().writeToNBT(nbttagcompound1);

    }

    @Override
    public void updateEntity() {
        if ((this.worldObj == null) || (this.worldObj.isRemote)) {
            return;
        }
        this.transferCooldown--;

        if (isCoolingDown())
            return;

        setTransferCooldown(0);
        if (template != null && !bom.isEmpty()) {
            // System.out.println("Kelood : "+this.template+" :>"+this.bom);
            if (!checkAvailableMaterials()) {
                if (this.working) {
                    NetBus.notify(getName(), "Not enough materials");
                }
                this.working = false;
                // consumeAndCraft();
            } else if (!checkSpaceForCraft()) {
                if (this.working) {
                    NetBus.notify(getName(), "Not enough space for output");
                }
                this.working = false;
            } else {
                doConsume();
                doCrafting();
            }
        }
        // doStuffs();
        setTransferCooldown(100);
    }

    private boolean checkSpaceForCraft() {
        final ItemStack craft = template.copy();
        for (int i = 9; i < 18; i++) {
            final ItemStack iStack = inventorySupport.getStackInSlot(i);
            if (iStack == null) {
                if (craft.stackSize > craft.getMaxStackSize()) {
                    craft.splitStack(craft.getMaxStackSize());
                } else {
                    craft.stackSize = 0;
                    return true;
                }
            } else if (iStack.isItemEqual(craft)) {
                if (craft.stackSize + iStack.stackSize > iStack.getMaxStackSize()) {
                    craft.splitStack(iStack.getMaxStackSize() - iStack.stackSize);
                } else {
                    craft.stackSize = 0;
                }
            }
            if (craft.stackSize <= 0) {
                return true;
            }
        }
        return false;
    }

    private boolean checkAvailableMaterials() {
        final Map<Item, Integer> itemCount = new HashMap<Item, Integer>();
        for (final ItemStack bomItem : this.bom) {
            if (bomItem != null) {
                final Item item = bomItem.getItem();
                int count = bomItem.stackSize;
                if (itemCount.containsKey(item)) {
                    itemCount.put(item, itemCount.get(item) + count);
                } else {
                    itemCount.put(item, count);
                }
            }
        }
        if (!itemCount.isEmpty()) {
            for (int i = 0; i < 9; i++) {
                final ItemStack iStack = inventorySupport.getStackInSlot(i);
                if (iStack != null) {
                    final Item item = iStack.getItem();
                    if (itemCount.containsKey(item)) {
                        int count = itemCount.get(item);
                        if (count <= iStack.stackSize) {
                            itemCount.remove(item);
                        } else {
                            itemCount.put(item, count - iStack.stackSize);
                        }
                    }
                }
                if (itemCount.isEmpty()) {
                    return true;
                }

            }
        }
        return false;
    }

    private void doConsume() {
        final Map<Item, Integer> itemCount = new HashMap<Item, Integer>();
        for (final ItemStack bomItem : this.bom) {
            if (bomItem != null) {
                final Item item = bomItem.getItem();
                int count = bomItem.stackSize;
                if (itemCount.containsKey(item)) {
                    itemCount.put(item, itemCount.get(item) + count);
                } else {
                    itemCount.put(item, count);
                }
            }
        }

        if (!itemCount.isEmpty()) {
            for (int i = 0; i < 9; i++) {
                final ItemStack iStack = inventorySupport.getStackInSlot(i);
                if (iStack != null) {
                    final Item item = iStack.getItem();
                    if (itemCount.containsKey(item)) {
                        int count = itemCount.get(item);
                        if (count <= iStack.stackSize) {
                            itemCount.remove(item);
                            iStack.splitStack(count);
                            inventorySupport.setSlotContents(i, iStack);
                        } else {
                            itemCount.put(item, count - iStack.stackSize);
                            inventorySupport.setSlotContents(i, null);
                        }
                    }
                }
                if (itemCount.isEmpty()) {
                    break;
                }

            }
        }
    }

    private void doCrafting() {
        final ItemStack craft = template.copy();
        for (int i = 9; i < 18; i++) {
            final ItemStack iStack = inventorySupport.getStackInSlot(i);
            if (iStack == null) {
                if (craft.stackSize > craft.getMaxStackSize()) {
                    inventorySupport.setSlotContents(i, craft.splitStack(craft.getMaxStackSize()));
                } else {
                    inventorySupport.setSlotContents(i, craft);
                    break;
                }
            } else if (iStack.isItemEqual(craft)) {
                if (craft.stackSize + iStack.stackSize > iStack.getMaxStackSize()) {
                    craft.splitStack(iStack.getMaxStackSize() - iStack.stackSize);
                    iStack.stackSize = iStack.getMaxStackSize();
                } else {
                    iStack.stackSize = craft.stackSize + iStack.stackSize;
                    craft.stackSize = 0;
                }
                inventorySupport.setSlotContents(i, iStack);
            }
            if (craft.stackSize <= 0) {
                break;
            }
        }
    }

    public boolean isCoolingDown() {
        return (this.transferCooldown > 0);
    }

    public void setTransferCooldown(int par1) {
        this.transferCooldown = par1;
    }

    public void setTemplate(ItemStack itemStack, List<ItemStack> bom) {
        inventorySupport.setSlotContents(18, itemStack);
        InvUtils.packItemStackList(bom);
        int i = 19;
        for (ItemStack iStack : bom) {
            if (iStack != null) {
                inventorySupport.setSlotContents(i++, iStack);
            }
        }
        System.out.println(" > " + bom);
        markDirty();
        if (this.worldObj.isRemote) {
            final JsonObject evt = JSON.newObject();
            evt.add("template", itemStackToJson(template));
            JsonArray bomArray=new JsonArray();
            Iterator<ItemStack> it = bom.iterator();
            while(it.hasNext()){
                bomArray.add(itemStackToJson(it.next()));
            }
            evt.add("bom",bomArray);
            NetBus.sendToServer(new TileEntityMessage(this, evt));
//            MyMod.packetPipeline.sendToServer(new DataStatePacket<AutobenchData>(new Point3D(xCoord, yCoord, zCoord), new AutobenchData(itemStack, bom)));
        }
    }
    protected final void fireLifecycleEvent() {
        if (this.worldObj.isRemote) {
            
        }
    }
    @Override
    public JsonObject getTilePacketData() {
        final JsonObject data = JSON.newObject();

        return data;
    }

    private JsonObject itemStackToJson(ItemStack itemStack) {
        final JsonObject data = JSON.newObject();
        data.addProperty("itemId", Item.getIdFromItem(itemStack.getItem()));
        data.addProperty("stackSize", itemStack.stackSize);
        data.addProperty("damageValue", itemStack.getMaxDamage());
        return data;
    }

    private ItemStack jsonToItemStack(JsonObject data) {
        int itemId = data.get("itemId").getAsInt();
        int stackSize = data.get("stackSize").getAsInt();
        int damageValue = data.get("damageValue").getAsInt();

        return new ItemStack(Item.getItemById(itemId), stackSize, damageValue);
    }

    @Override
    public void handleClientUpdate(JsonObject data) throws IOException {

    }

    @Override
    public void handleServerUpdate(JsonObject data) throws IOException {
        JsonObject templateObj = data.get("template").getAsJsonObject();
        this.template = jsonToItemStack(templateObj);
        inventorySupport.setSlotContents(18, this.template);

        JsonArray bomArray = data.get("bom").getAsJsonArray();
        Iterator<JsonElement> it = bomArray.iterator();
        this.bom.clear();
        while (it.hasNext()) {
            JsonObject bomObj = it.next().getAsJsonObject();
            this.bom.add(jsonToItemStack(bomObj));
        }

    }

}
