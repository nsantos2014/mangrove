package net.minecraft.mangrove.mod.thrive.strongbox;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.mangrove.core.inventory.EnumPermission;
import net.minecraft.mangrove.core.inventory.tile.AbstractSidedInventoryTileEntity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.LockCode;

public class TileEntityStrongbox extends AbstractSidedInventoryTileEntity{
	private String name;

	public TileEntityStrongbox() {
		super();
		this.name = null;
		inventorySupport.defineSlotRange(0, 200, null, EnumPermission.BOTH, EnumFacing.DOWN, EnumFacing.UP, EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.EAST);
	}

	public String getName() {
		if (this.name == null) {
			this.name = String.format("Crate (%d,%d,%d)", pos.getX(), pos.getY(), pos.getZ());
		}
		return this.name;
	}

}
