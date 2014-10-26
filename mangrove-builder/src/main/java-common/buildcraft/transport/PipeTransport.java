/**
 * Copyright (c) 2011-2014, SpaceToad and the BuildCraft Team
 * http://www.mod-buildcraft.com
 *
 * BuildCraft is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package buildcraft.transport;

import java.util.BitSet;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import net.minecraftforge.common.util.ForgeDirection;

import buildcraft.api.transport.IPipeTile.PipeType;

public abstract class PipeTransport {

	public TileGenericPipe container;

	protected boolean[] inputsOpen = new boolean[ForgeDirection.VALID_DIRECTIONS.length];
	protected boolean[] outputsOpen = new boolean[ForgeDirection.VALID_DIRECTIONS.length];

	public PipeTransport() {
		for (int b = 0; b < ForgeDirection.VALID_DIRECTIONS.length; b++) {
			inputsOpen[b] = true;
			outputsOpen[b] = true;
		}
	}

	public abstract PipeType getPipeType();

	public World getWorld() {
		return container.getWorldObj();
	}

	public void readFromNBT(NBTTagCompound nbt) {
	    if (nbt.hasKey("inputOpen")) {
			BitSet inputBuf = BitSet.valueOf(new byte [] {nbt.getByte("inputOpen")});
			BitSet outputBuf = BitSet.valueOf(new byte[] {nbt.getByte("outputOpen")});

			for (int b = 0; b < ForgeDirection.VALID_DIRECTIONS.length; b++) {
				inputsOpen[b] = inputBuf.get(b);
				outputsOpen[b] = outputBuf.get(b);
			}
	    }
	}

	public void writeToNBT(NBTTagCompound nbt) {
		BitSet inputBuf = new BitSet();
		BitSet outputBuf = new BitSet();

	    for (int b = 0; b < ForgeDirection.VALID_DIRECTIONS.length; b++) {
			if (inputsOpen[b]) {
				inputBuf.set(b, true);
			} else {
				inputBuf.set(b, false);
			}

			if (outputsOpen[b]) {
				outputBuf.set(b, true);
			} else {
				outputBuf.set(b, false);
			}
	    }

		nbt.setByte("inputOpen", inputBuf.toByteArray()[0]);
		nbt.setByte("outputOpen", outputBuf.toByteArray()[0]);
	}

	public void updateEntity() {
	}

	public void setTile(TileGenericPipe tile) {
	    this.container = tile;
	}

	public boolean canPipeConnect(TileEntity tile, ForgeDirection side) {
	    return true;
	}

	public void onNeighborBlockChange(int blockId) {
	}

	public void onBlockPlaced() {
	}

	public void initialize() {
	}

	public boolean inputOpen(ForgeDirection from) {
	    return inputsOpen[from.ordinal()];
	}

	public boolean outputOpen(ForgeDirection to) {
	    return outputsOpen[to.ordinal()];
	}

	public void allowInput(ForgeDirection from, boolean allow) {
		if (from != ForgeDirection.UNKNOWN) {
			inputsOpen[from.ordinal()] = allow;
		}
	}

	public void allowOutput(ForgeDirection to, boolean allow) {
		if (to != ForgeDirection.UNKNOWN) {
			outputsOpen[to.ordinal()] = allow;
		}
	}

	public void dropContents() {
	}

	public void sendDescriptionPacket() {
	}

	public boolean delveIntoUnloadedChunks() {
		return false;
	}
}