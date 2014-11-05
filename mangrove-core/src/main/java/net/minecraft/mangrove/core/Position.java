package net.minecraft.mangrove.core;

import net.minecraft.mangrove.core.utils.BlockUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;


public class Position {
	public double x, y, z;
	public ForgeDirection orientation;

	public Position() {
		x = 0;
		y = 0;
		z = 0;
		orientation = ForgeDirection.UNKNOWN;
	}

	public Position(double ci, double cj, double ck) {
		x = ci;
		y = cj;
		z = ck;
		orientation = ForgeDirection.UNKNOWN;
	}

	public Position(double ci, double cj, double ck, ForgeDirection corientation) {
		x = ci;
		y = cj;
		z = ck;
		orientation = corientation;

		if (orientation == null) {
			orientation = ForgeDirection.UNKNOWN;
		}
	}

	public Position(Position p) {
		x = p.x;
		y = p.y;
		z = p.z;
		orientation = p.orientation;
	}

	public Position(NBTTagCompound nbttagcompound) {
		readFromNBT(nbttagcompound);
	}

	public Position(TileEntity tile) {
		x = tile.xCoord;
		y = tile.yCoord;
		z = tile.zCoord;
		orientation = BlockUtils.getForgeDirectionFromMetadata(tile.getBlockMetadata());
	}
		
	public Position moveRight(double step) {
		switch (orientation) {
		case SOUTH:
			x = x - step;
			break;
		case NORTH:
			x = x + step;
			break;
		case EAST:
			z = z + step;
			break;
		case WEST:
			z = z - step;
			break;
		default:
		}
		return this;
	}
		

	public Position moveLeft(double step) {
		moveRight(-step);
		return this;
	}

	public Position moveForwards(double step) {
		switch (orientation) {
		case UP:
			y = y + step;
			break;
		case DOWN:
			y = y - step;
			break;
		case SOUTH:
			z = z + step;
			break;
		case NORTH:
			z = z - step;
			break;
		case EAST:
			x = x + step;
			break;
		case WEST:
			x = x - step;
			break;
		default:
		}
		return this;
	}

	public Position moveBackwards(double step) {
		moveForwards(-step);
		return this;
	}

	public Position moveUp(double step) {
		switch (orientation) {
		case SOUTH:
		case NORTH:
		case EAST:
		case WEST:
			y = y + step;
			break;
		default:
		}
		return this;
	}

	public Position moveDown(double step) {
		moveUp(-step);
		return this;
	}

	public void writeToNBT(NBTTagCompound nbttagcompound) {
		if (orientation == null) {
			orientation = ForgeDirection.UNKNOWN;
		}

		nbttagcompound.setDouble("x", x);
		nbttagcompound.setDouble("y", y);
		nbttagcompound.setDouble("z", z);
		nbttagcompound.setByte("orientation", (byte) orientation.ordinal());
	}

	public void readFromNBT(NBTTagCompound nbttagcompound) {
		x = nbttagcompound.getDouble("x");
		y = nbttagcompound.getDouble("y");
		z = nbttagcompound.getDouble("z");
		orientation = ForgeDirection.values() [nbttagcompound.getByte("orientation")];
	}

	@Override
	public String toString() {
		return "{" + x + ", " + y + ", " + z + "}";
	}

	public Position min(Position p) {
		return new Position(p.x > x ? x : p.x, p.y > y ? y : p.y, p.z > z ? z : p.z);
	}

	public Position max(Position p) {
		return new Position(p.x < x ? x : p.x, p.y < y ? y : p.y, p.z < z ? z : p.z);
	}

	public boolean isClose(Position newPosition, float f) {
		double dx = x - newPosition.x;
		double dy = y - newPosition.y;
		double dz = z - newPosition.z;

		double sqrDis = dx * dx + dy * dy + dz * dz;

		return !(sqrDis > f * f);
	}
	public Position clone(){
		return new Position(this);
	}
	
	

	private static final int[][] MATRIX_NORTH={
		{ 0, 1},
		{-1, 0}
	};
	private static final int[][] MATRIX_SOUTH={
		{ 0,-1},
		{ 1, 0}
	};
	private static final int[][] MATRIX_EAST={
		{ 1, 0},
		{ 0, 1}
	};
	private static final int[][] MATRIX_WEST={
		{ -1,  0},
		{  0, -1}
	};
	
}
