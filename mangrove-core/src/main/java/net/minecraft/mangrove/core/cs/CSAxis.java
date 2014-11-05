package net.minecraft.mangrove.core.cs;

import net.minecraftforge.common.util.ForgeDirection;

public enum CSAxis {
	Other(ForgeDirection.UNKNOWN,new CSMatrixInt(2),new CSMatrixDouble(2)),
	Easting(ForgeDirection.EAST,new CSMatrixInt(2,  1,  0,  0,  1),new CSMatrixDouble(2,  1.0,  0.0,  0.0,  1.0)),
	Northing(ForgeDirection.NORTH,new CSMatrixInt(2,  0,  -1, -1,  0),new CSMatrixDouble(2,  0.0,  0.0,  -1.0,  -1.0)),
	Westing(ForgeDirection.WEST,new CSMatrixInt(2, -1,  0,  0, -1),new CSMatrixDouble(2,  -1.0,  0.0,  0.0,  -1.0)),
	Southing(ForgeDirection.SOUTH,new CSMatrixInt(2,  0, 1,  1,  0),new CSMatrixDouble(2,  0.0,  1.0,  1.0,  0.0));
	
	private final ForgeDirection direction;
	private final CSMatrixInt matrixInt;
	private final CSMatrixDouble matrixd;
	
	private CSAxis(ForgeDirection direction,CSMatrixInt matrixi,CSMatrixDouble matrixd) {
		this.direction = direction;
		this.matrixInt = matrixi;
		this.matrixd = matrixd;			
	}
	
	public ForgeDirection getDirection() {
		return direction;
	}
	public CSMatrixInt getMatrixInt() {
		return matrixInt;
	}
	public CSMatrixDouble getMatrixDouble() {
		return matrixd;
	}
	
	public static CSAxis fromDirection(ForgeDirection direction){
		switch (direction) {
		case EAST:
			return Easting;
		case NORTH:
			return Northing;
		case SOUTH:
			return Southing;
		case WEST:
			return Westing;
		default:
			break;
		}
		return Other;
	}
}
