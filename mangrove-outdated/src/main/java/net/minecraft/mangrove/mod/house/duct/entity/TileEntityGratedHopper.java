package net.minecraft.mangrove.mod.house.duct.entity;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockHopper;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.mangrove.mod.house.MGHouseForge;
import net.minecraft.mangrove.mod.house.duct.AbstractTileEntityDuct;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Facing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class TileEntityGratedHopper extends AbstractTileEntityDuct implements ISidedInventory {
	private ItemStack[] hopperItemStacks;
	private String inventoryName;
	private int transferCooldown;
	private static final int[] slots_inventory = { 0, 1, 2, 3, 4 };
	private static final int[] slots_filter = { 5, 6, 7, 8, 9 };

	public TileEntityGratedHopper() {
		this.hopperItemStacks = new ItemStack[10];
		this.transferCooldown = -1;
	}

	public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readFromNBT(par1NBTTagCompound);
		NBTTagList nbttaglist = par1NBTTagCompound.getTagList("Items", 10);
		this.hopperItemStacks = new ItemStack[10];

		if (par1NBTTagCompound.hasKey("CustomName")) {
			this.inventoryName = par1NBTTagCompound.getString("CustomName");
		}

		this.transferCooldown = par1NBTTagCompound.getInteger("TransferCooldown");

		for (int i = 0; i < nbttaglist.tagCount(); ++i) {
			NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
			byte b0 = nbttagcompound1.getByte("Slot");

			if ((b0 < 0) || (b0 >= this.hopperItemStacks.length))
				continue;
			this.hopperItemStacks[b0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
		}
	}

	public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeToNBT(par1NBTTagCompound);

		NBTTagList nbttaglist = new NBTTagList();
		for (int i = 0; i < this.hopperItemStacks.length; ++i) {
			if (this.hopperItemStacks[i] == null)
				continue;
			NBTTagCompound nbttagcompound1 = new NBTTagCompound();
			nbttagcompound1.setByte("Slot", (byte) i);
			this.hopperItemStacks[i].writeToNBT(nbttagcompound1);
			nbttaglist.appendTag(nbttagcompound1);
		}

		par1NBTTagCompound.setTag("Items", nbttaglist);

		par1NBTTagCompound.setInteger("TransferCooldown",this.transferCooldown);

		if (!(isInvNameLocalized()))
			return;
		par1NBTTagCompound.setString("CustomName", this.inventoryName);
	}

	public int getSizeInventory() {
		return 5;
	}

	public ItemStack getStackInSlot(int par1) {
		return this.hopperItemStacks[par1];
	}

	public ItemStack decrStackSize(int par1, int par2) {
		if (this.hopperItemStacks[par1] != null) {
			if (this.hopperItemStacks[par1].stackSize <= par2) {
				ItemStack itemstack = this.hopperItemStacks[par1];
				this.hopperItemStacks[par1] = null;
				return itemstack;
			}

			ItemStack itemstack = this.hopperItemStacks[par1]
					.splitStack(par2);

			if (this.hopperItemStacks[par1].stackSize == 0) {
				this.hopperItemStacks[par1] = null;
			}

			return itemstack;
		}

		return null;
	}

	public ItemStack getStackInSlotOnClosing(int par1) {
		if (this.hopperItemStacks[par1] != null) {
			ItemStack itemstack = this.hopperItemStacks[par1];
			this.hopperItemStacks[par1] = null;
			return itemstack;
		}

		return null;
	}

	public void setInventorySlotContents(int par1, ItemStack par2ItemStack) {
		this.hopperItemStacks[par1] = par2ItemStack;

		if ((par2ItemStack == null)
				|| (par2ItemStack.stackSize <= getInventoryStackLimit()))
			return;
		par2ItemStack.stackSize = getInventoryStackLimit();
	}

	public String getInvName() {
		return ((isInvNameLocalized()) ? this.inventoryName
				: "container.hopper");
	}

	public boolean isInvNameLocalized() {
		return ((this.inventoryName != null) && (this.inventoryName.length() > 0));
	}

	public void setInventoryName(String par1Str) {
		this.inventoryName = par1Str;
	}

	public int getInventoryStackLimit() {
		return 64;
	}

	public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer) {
		return (this.worldObj.getTileEntity(this.xCoord,
				this.yCoord, this.zCoord) == this);
	}

	public void openChest() {
	}

	public void closeChest() {
	}

	public boolean isItemValidForSlot(int par1, ItemStack par2ItemStack) {
		return true;
	}

	public void updateEntity() {
		if ((this.worldObj == null) || (this.worldObj.isRemote))
			return;
		this.transferCooldown -= 1;

		if (isCoolingDown())
			return;
		setTransferCooldown(0);
		func_98045_j();
	}

	public boolean func_98045_j() {
		if ((this.worldObj != null)
				&& (!(this.worldObj.isRemote))) {
			if ((!(isCoolingDown()))&& (BlockHopper.func_149917_c(getBlockMetadata()))) {
				boolean flag = insertItemToInventory();
				flag = (suckItemsIntoHopper(this)) || (flag);

				if (flag) {
					setTransferCooldown(MGHouseForge.instance.cooldownTime);

					markDirty();
					return true;
				}
			}

			return false;
		}

		return false;
	}

	private boolean insertItemToInventory() {
		IInventory iinventory = getOutputInventory();

		if (iinventory == null) {
			return false;
		}

		for (int i = 0; i < getSizeInventory(); ++i) {
			if ((getStackInSlot(i) == null)
					|| (!(isValidFilterItem(getStackInSlot(i)))))
				continue;
			ItemStack itemstack = getStackInSlot(i).copy();
			ItemStack itemstack1 = insertStack(iinventory, decrStackSize(i, 1),
					Facing.oppositeSide[BlockHopper.getDirectionFromMetadata(getBlockMetadata())]);

			if ((itemstack1 == null) || (itemstack1.stackSize == 0)) {
				iinventory.markDirty();
				return true;
			}

			setInventorySlotContents(i, itemstack);
		}

		return false;
	}

	public static boolean suckItemsIntoHopper(TileEntityGratedHopper par0Hopper) {
		IInventory iinventory = getInventoryAboveHopper(par0Hopper);

		if (iinventory != null) {
			byte b0 = 0;

			if ((iinventory instanceof ISidedInventory) && (b0 > -1)) {
				ISidedInventory isidedinventory = (ISidedInventory) iinventory;
				int[] aint = isidedinventory.getAccessibleSlotsFromSide(b0);

				for (int i = 0; i < aint.length; ++i) {
					if (func_102012_a(par0Hopper, iinventory, aint[i], b0)) {
						return true;
					}
				}
			} else {
				int j = iinventory.getSizeInventory();

				for (int k = 0; k < j; ++k) {
					if (func_102012_a(par0Hopper, iinventory, k, b0)) {
						return true;
					}
				}
			}
		} else {
			EntityItem entityitem = func_96119_a(par0Hopper.getWorldObj(),
					par0Hopper.getXPos(), par0Hopper.getYPos() + 1.0D,
					par0Hopper.getZPos());

			if (entityitem != null) {
				return func_96114_a(par0Hopper, entityitem);
			}
		}

		return false;
	}

	private static boolean func_102012_a(TileEntityGratedHopper par0Hopper,
			IInventory par1IInventory, int par2, int par3) {
		ItemStack itemstack = par1IInventory.getStackInSlot(par2);

		if ((itemstack != null)
				&& (canExtractItemFromInventory(par1IInventory, itemstack,
						par2, par3))) {
			ItemStack itemstack1 = itemstack.copy();
			ItemStack itemstack2 = insertStack(par0Hopper,
					par1IInventory.decrStackSize(par2, 1), -1);

			if ((itemstack2 == null) || (itemstack2.stackSize == 0)) {
				par1IInventory.markDirty();
				return true;
			}

			par1IInventory.setInventorySlotContents(par2, itemstack1);
		}

		return false;
	}

	public static boolean func_96114_a(IInventory par0IInventory,
			EntityItem par1EntityItem) {
		boolean flag = false;

		if (par1EntityItem == null) {
			return false;
		}

		ItemStack itemstack = par1EntityItem.getEntityItem().copy();
		ItemStack itemstack1 = insertStack(par0IInventory, itemstack, -1);

		if ((itemstack1 != null) && (itemstack1.stackSize != 0)) {
			par1EntityItem.setEntityItemStack(itemstack1);
		} else {
			flag = true;
			par1EntityItem.setDead();
		}

		return flag;
	}

	public static ItemStack insertStack(IInventory par0IInventory,
			ItemStack par1ItemStack, int par2) {
		if ((par0IInventory instanceof ISidedInventory) && (par2 > -1)) {
			ISidedInventory isidedinventory = (ISidedInventory) par0IInventory;
			int[] aint = isidedinventory.getAccessibleSlotsFromSide(par2);

			for (int j = 0; (j < aint.length) && (par1ItemStack != null)
					&& (par1ItemStack.stackSize > 0); ++j) {
				par1ItemStack = func_102014_c(par0IInventory, par1ItemStack,
						aint[j], par2);
			}
		} else {
			int k = par0IInventory.getSizeInventory();

			for (int l = 0; (l < k) && (par1ItemStack != null)
					&& (par1ItemStack.stackSize > 0); ++l) {
				par1ItemStack = func_102014_c(par0IInventory, par1ItemStack, l,
						par2);
			}
		}

		if ((par1ItemStack != null) && (par1ItemStack.stackSize == 0)) {
			par1ItemStack = null;
		}

		return par1ItemStack;
	}

	private static boolean canInsertItemToInventory(IInventory par0IInventory,
			ItemStack par1ItemStack, int par2, int par3) {
		return (par0IInventory.isItemValidForSlot(par2, par1ItemStack));
	}

	private static boolean canExtractItemFromInventory(
			IInventory par0IInventory, ItemStack par1ItemStack, int par2,
			int par3) {
		return ((!(par0IInventory instanceof ISidedInventory)) || (((ISidedInventory) par0IInventory)
				.canExtractItem(par2, par1ItemStack, par3)));
	}

	private static ItemStack func_102014_c(IInventory par0IInventory,
			ItemStack par1ItemStack, int par2, int par3) {
		ItemStack itemstack1 = par0IInventory.getStackInSlot(par2);

		if (canInsertItemToInventory(par0IInventory, par1ItemStack, par2, par3)) {
			boolean flag = false;

			if (itemstack1 == null) {
				par0IInventory.setInventorySlotContents(par2, par1ItemStack);
				par1ItemStack = null;
				flag = true;
			} else if (areItemStacksEqualItem(itemstack1, par1ItemStack)) {
				int k = par1ItemStack.getMaxStackSize() - itemstack1.stackSize;
				int l = Math.min(par1ItemStack.stackSize, k);
				par1ItemStack.stackSize -= l;
				itemstack1.stackSize += l;
				flag = l > 0;
			}

			if (flag) {
				if (par0IInventory instanceof TileEntityHopper) {
					((TileEntityHopper) par0IInventory)
							.func_145896_c(MGHouseForge.instance.cooldownTime);

					par0IInventory.markDirty();
				}

				par0IInventory.markDirty();
			}
		}

		return par1ItemStack;
	}

	private IInventory getOutputInventory() {
		int i = BlockHopper.getDirectionFromMetadata(getBlockMetadata());
		return getInventoryAtLocation(getWorldObj(), this.xCoord
				+ Facing.offsetsXForSide[i],
				this.yCoord
						+ Facing.offsetsYForSide[i],
				this.zCoord
						+ Facing.offsetsZForSide[i]);
	}

	public static IInventory getInventoryAboveHopper(
			TileEntityGratedHopper par0Hopper) {
		return getInventoryAtLocation(par0Hopper.getWorldObj(),
				par0Hopper.getXPos(), par0Hopper.getYPos() + 1.0D,
				par0Hopper.getZPos());
	}

	public static EntityItem func_96119_a(World par0World, double par1,
			double par3, double par5) {
		List list = par0World.selectEntitiesWithinAABB(
				EntityItem.class,
				AxisAlignedBB.getBoundingBox(par1, par3, par5,
						par1 + 1.0D, par3 + 1.0D, par5 + 1.0D),
				IEntitySelector.selectAnything);
		return ((list.size() > 0) ? (EntityItem) list.get(0) : null);
	}

	public static IInventory getInventoryAtLocation(World par0World,
			double par1, double par3, double par5) {
		IInventory iinventory = null;
		int i = MathHelper.floor_double(par1);
		int j = MathHelper.floor_double(par3);
		int k = MathHelper.floor_double(par5);
		TileEntity tileentity = par0World.getTileEntity(i, j, k);

		if ((tileentity != null) && (tileentity instanceof IInventory)) {
			iinventory = (IInventory) tileentity;

			if (iinventory instanceof TileEntityChest) {
				Block block = par0World.getBlock(i, j, k);

				if (block instanceof BlockChest) {
					iinventory = ((BlockChest) block).func_149951_m(par0World,
							i, j, k);
				}
			}

		}

		if (iinventory == null) {
			List list = par0World.getEntitiesWithinAABBExcludingEntity(
					(Entity) null,
					AxisAlignedBB.getBoundingBox(par1, par3, par5,
							par1 + 1.0D, par3 + 1.0D, par5 + 1.0D),
					IEntitySelector.selectInventories);

			if ((list != null) && (list.size() > 0)) {
				iinventory = (IInventory) list.get(par0World.rand
						.nextInt(list.size()));
			}
		}

		return iinventory;
	}

	private static boolean areItemStacksEqualItem(ItemStack par0ItemStack,
			ItemStack par1ItemStack) {
		return ((par0ItemStack.stackSize > par0ItemStack.getMaxStackSize()) ? false
				: (par0ItemStack.getItemDamage() != par1ItemStack.getItemDamage()) ? false
						: (par0ItemStack.getItem() != par1ItemStack
								.getItem()) ? false : ItemStack
								.areItemStackTagsEqual(par0ItemStack, par1ItemStack));
	}

	public double getXPos() {
		return this.xCoord;
	}

	public double getYPos() {
		return this.yCoord;
	}

	public double getZPos() {
		return this.zCoord;
	}

	public void setTransferCooldown(int par1) {
		this.transferCooldown = par1;
	}

	public boolean isCoolingDown() {
		return (this.transferCooldown > 0);
	}

	public int[] getAccessibleSlotsFromSide(int var1) {
		return slots_inventory;
	}

	private boolean isValidFilterItem(ItemStack itemstack) {
		for (int n = 5; n < 10; ++n) {
			if (this.hopperItemStacks[n] == null)
				continue;
			if ((this.hopperItemStacks[n].getItem() == itemstack
					.getItem())
					&& (this.hopperItemStacks[n].getItemDamage() == itemstack
							.getItemDamage())) {
				return true;
			}
		}

		return false;
	}

	public boolean canInsertItem(int slot, ItemStack itemstack, int side) {
		if ((side == -1) || (side == 1)) {
			return (isValidFilterItem(itemstack));
		}

		return true;
	}

	public boolean canExtractItem(int i, ItemStack itemstack, int j) {
		return (!(isValidFilterItem(itemstack)));
	}

	public String getInventoryName() {
		return ((hasCustomInventoryName()) ? this.inventoryName
				: "container.gratedhopper");
	}

	public boolean hasCustomInventoryName() {
		return ((this.inventoryName != null) && (this.inventoryName.length() > 0));
	}

	public void openInventory() {
	}

	public void closeInventory() {
	}
}
