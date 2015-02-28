package net.minecraft.mangrove.mod.thrive.autocon.itembroker;

import java.util.Arrays;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

public class ConnectionConfig {
	EnumFacing facing;
	EnumConnectionState state;
	final ItemStack[] items = new ItemStack[7];

	public ConnectionConfig(EnumFacing facing, EnumConnectionState state) {
		this.facing = facing;
		this.state = state;
	}

	public ItemStack getItemStack(int slot) {
		return this.items[slot];
	}

	public void setItemStack(byte slot, ItemStack itemStack) {
		if (itemStack != null) {
			itemStack.stackSize = 1;
		}
		this.items[slot] = itemStack;
	}

	public EnumFacing getFacing() {
		return facing;
	}

	protected void setFacing(EnumFacing facing) {
		this.facing = facing;
	}

	public EnumConnectionState getState() {
		return state;
	}

	protected void setState(EnumConnectionState state) {
		this.state = state;
	}

	protected void connect() {
		if (state == EnumConnectionState.DISCONNECTED) {
			state = EnumConnectionState.NONE;
			for (int i = 0; i < 7; i++) {
				this.items[i] = null;
			}
		}
	}

	protected void disconnect() {
		if (state != EnumConnectionState.DISCONNECTED) {
			state = EnumConnectionState.DISCONNECTED;
			for (int i = 0; i < 7; i++) {
				this.items[i] = null;
			}
		}
	}

	@Override
	public String toString() {
		return "ConnectionConfig [facing=" + facing + ", state=" + state + ", items=" + Arrays.toString(items) + "]";
	}

}