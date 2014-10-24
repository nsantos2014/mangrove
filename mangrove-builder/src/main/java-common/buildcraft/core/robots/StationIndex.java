/**
 * Copyright (c) 2011-2014, SpaceToad and the BuildCraft Team
 * http://www.mod-buildcraft.com
 *
 * BuildCraft is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package buildcraft.core.robots;

import net.minecraft.nbt.NBTTagCompound;

import net.minecraftforge.common.util.ForgeDirection;

import buildcraft.api.core.BlockIndex;
import buildcraft.api.robots.IDockingStation;

public class StationIndex {

	public BlockIndex index = new BlockIndex();
	public ForgeDirection side = ForgeDirection.UNKNOWN;

	protected StationIndex() {
	}

	public StationIndex(ForgeDirection iSide, int x, int y, int z) {
		side = iSide;
		index = new BlockIndex(x, y, z);
	}

	public StationIndex(IDockingStation station) {
		side = station.side();
		index = station.index();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj.getClass() != getClass()) {
			return false;
		}

		StationIndex compareId = (StationIndex) obj;

		return index.equals(compareId.index)
				&& side == compareId.side;
	}

	@Override
	public int hashCode() {
		return (index.hashCode() * 37) + side.ordinal();
	}

	public void writeToNBT(NBTTagCompound nbt) {
		NBTTagCompound indexNBT = new NBTTagCompound();
		index.writeTo(indexNBT);
		nbt.setTag("index", indexNBT);
		nbt.setByte("side", (byte) side.ordinal());
	}

	protected void readFromNBT(NBTTagCompound nbt) {
		index = new BlockIndex(nbt.getCompoundTag("index"));
		side = ForgeDirection.values()[nbt.getByte("side")];
	}
}
