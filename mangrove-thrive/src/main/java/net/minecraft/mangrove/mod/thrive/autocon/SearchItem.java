package net.minecraft.mangrove.mod.thrive.autocon;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class SearchItem {
	private final BlockPos pos;
	private final EnumFacing facing;
	private final int pathWeight;
	
	
	public SearchItem(BlockPos pos, EnumFacing facing, int pathWeight) {
		super();
		this.pos = pos;
		this.facing = facing;
		this.pathWeight = pathWeight;
	}
	
	public BlockPos getPos() {
		return pos;
	}
	public EnumFacing getFacing() {
		return facing;
	}
	public int getPathWeight() {
		return pathWeight;
	}
	
	@Override
	public String toString() {
		return "SearchItem [pos=" + pos + ", facing=" + facing + ", pathWeight=" + pathWeight + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((facing == null) ? 0 : facing.hashCode());
		result = prime * result + pathWeight;
		result = prime * result + ((pos == null) ? 0 : pos.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SearchItem other = (SearchItem) obj;
		if (facing != other.facing)
			return false;
		if (pathWeight != other.pathWeight)
			return false;
		if (pos == null) {
			if (other.pos != null)
				return false;
		} else if (!pos.equals(other.pos))
			return false;
		return true;
	}
	
}
