package net.minecraft.mangrove.mod.thrive.cistern;

import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class TileCistern extends TileEntity {

	private static final int CONTAINER_MAX_SIZE = 64;
	private static final ResourceLocation water = new ResourceLocation("textures/blocks/water_still.png");
	private static final ResourceLocation lava = new ResourceLocation("textures/blocks/lava_still.png");
	private static final ResourceLocation milk = new ResourceLocation("textures/blocks/wool_colored_white.png");
	private final ItemStack itemStack = new ItemStack(Blocks.air, 0);

	public TileCistern() {
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		readSyncData(compound);
	}

	private void readSyncData(NBTTagCompound compound) {
		if (compound.hasKey("Item")) {
			NBTTagCompound itemTag = compound.getCompoundTag("Item");

			ItemStack templateStack = ItemStack.loadItemStackFromNBT(itemTag);
			if (templateStack != null) {
				itemStack.setItem(templateStack.getItem());
				itemStack.stackSize = templateStack.stackSize;
			}
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		writeSyncData(compound);
	}

	private void writeSyncData(NBTTagCompound compound) {
		NBTTagCompound templateTag = new NBTTagCompound();
		itemStack.writeToNBT(templateTag);
		compound.setTag("Item", templateTag);
	}

	public ResourceLocation getFluidTexture() {
		if (itemStack.getItem() == Items.lava_bucket) {
			return lava;
		}
		if (itemStack.getItem() == Items.milk_bucket) {
			return milk;
		}
		return water;
	}

	public int getLevel() {
		return itemStack.stackSize;
	}
	
	public int getMaxLevel(){
		return CONTAINER_MAX_SIZE;
	}

	public boolean canRetrieve() {
		return itemStack.stackSize > 0;
	}

	public Item retrieve() {
		if (itemStack.stackSize > 0) {
			itemStack.stackSize--;
			System.out.println("StackSize:" + itemStack.stackSize);
			fireSync();
			return itemStack.getItem();
		}
		return Items.bucket;
	}

	public boolean canPlace(ItemBucket item) {
		return itemStack.stackSize == 0 || (itemStack.stackSize < CONTAINER_MAX_SIZE && itemStack.getItem() == item);
	}

	public Item place(ItemBucket item) {

		if (itemStack.stackSize == 0) {
			itemStack.setItem(item);
			itemStack.stackSize = 1;
//			System.out.println("StackSize:" + itemStack.stackSize);
			fireSync();
			return Items.bucket;
		}
		if (itemStack.getItem() != item) {
			return item;
		}
		if (itemStack.stackSize >= CONTAINER_MAX_SIZE) {
			return item;
		}

		itemStack.stackSize++;
//		System.out.println("StackSize:" + itemStack.stackSize);
		fireSync();
		return Items.bucket;

	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound syncData = new NBTTagCompound();
		this.writeSyncData(syncData);
		return new S35PacketUpdateTileEntity(pos, 1, syncData);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		readSyncData(pkt.getNbtCompound());
	}

	private void fireSync() {
		worldObj.markBlockForUpdate(pos); // Makes the server call
											// getDescriptionPacket for a
											// full data sync
		markDirty(); // Marks the chunk as dirty, so that it is saved properly
						// on changes. Not required for the sync specifically,
						// but usually goes alongside the former.

	}
}
