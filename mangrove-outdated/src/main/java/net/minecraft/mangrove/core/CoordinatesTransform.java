package net.minecraft.mangrove.core;


public class CoordinatesTransform {
	public static final CoordinatesTransform i = new CoordinatesTransform();

	public CoordinatesTransform() {
	}

	public synchronized Position translate(final Position pivot,
			final Position local) {
		final Position worldPosition = new Position();
		worldPosition.orientation = pivot.orientation;
		int[][] rotationMatrix;
		switch (pivot.orientation) {
		case UP:
			break;
		case DOWN:
			break;
		case EAST:
		case WEST:	
		case SOUTH:
		case NORTH:
			rotationMatrix=getMatrix(pivot.orientation);
			worldPosition.x=pivot.x+(rotationMatrix[0][0]*calcX(pivot, local)-rotationMatrix[0][1]*calcZ(pivot, local));
			worldPosition.y=pivot.y+local.y;
			worldPosition.z=pivot.z+(rotationMatrix[1][0]*calcX(pivot, local)-rotationMatrix[1][1]*calcZ(pivot, local));						
			break;		
		default:
			break;
		}

		return worldPosition;
	}

	private double calcZ(final Position pivot, final Position local) {
		switch(pivot.orientation){
		case NORTH:
			return local.z-1;
		case SOUTH:
			return local.z;
		case EAST:
			return local.z-1;
		case WEST:
			return local.z;
		default:
			break;
		}		
		return 0;
	}
	
	private double calcX(final Position pivot, final Position local) {
		switch(pivot.orientation){
		case NORTH:
			return local.x;
		case SOUTH:
			return local.x+1;
		case EAST:
			return local.x+1;
		case WEST:
			return local.x;
		default:
			break;
		}		
		return 0;
	}
	
//	private double calcX(final Position pivot, final Position local) {
//		switch(pivot.orientation){
//		case NORTH:
//			return local.x-1;
//		case SOUTH:
////			return local.x+1;
//			return local.x;
//		case EAST:
//			return local.x-1;
//		case WEST:
//			return local.x-2;
//		}		
//		return 0;
//	}
//	
//	private double calcZ(final Position pivot, final Position local) {
//		switch(pivot.orientation){
//		case NORTH:
////			return local.z+1;
//			return local.z;
//		case SOUTH:
//			return local.z+1;
//		case EAST:
//			return local.z-1;
//		case WEST:
//			return local.z;
//		}		
//		return 0;
//	}
	
	public float getXRotationAngle(ForgeDirection orientation) {
		switch (orientation) {
		case UP:
			break;
		case DOWN:
			break;
		case NORTH:
			return 0;
		case SOUTH:
			return 0;
		case EAST:
			return 0;
		case WEST:
			return 0;
		default:
			break;
		}
		return 0;
	}

	public float getYRotationAngle(ForgeDirection orientation) {
		switch (orientation) {
		case UP:
			break;
		case DOWN:
			break;
		case NORTH:
			return -180;			
		case SOUTH:
			return 0;
		case EAST:
			return 90;
		case WEST:
			return -90;
		default:
			break;
		}
		return 0;
	}

	public float getZRotationAngle(ForgeDirection orientation) {
		switch (orientation) {
		case UP:
			break;
		case DOWN:
			break;
		case NORTH:
			return 0;			
		case SOUTH:
			return 0;
		case EAST:
			return 0;
		case WEST:
			return 0;
		default:
			break;
		}
		return 0;
	}

//	public float getXRotationAngle(ForgeDirection orientation) {
//		switch (orientation) {
//		case UP:
//			break;
//		case DOWN:
//			break;
//		case NORTH:
//			return 180;
//		case SOUTH:
//			return 0;
//		case EAST:
//			return 0;
//		case WEST:
//			return 90;
//		default:
//			break;
//		}
//		return 0;
//	}
//
//	public float getYRotationAngle(ForgeDirection orientation) {
//		switch (orientation) {
//		case UP:
//			break;
//		case DOWN:
//			break;
//		case NORTH:
//			return 0;			
//		case SOUTH:
//			return 0;
//		case EAST:
//			return 90;
//		case WEST:
//			return 270;
//		default:
//			break;
//		}
//		return 0;
//	}
//
//	public float getZRotationAngle(ForgeDirection orientation) {
//		switch (orientation) {
//		case UP:
//			break;
//		case DOWN:
//			break;
//		case NORTH:
//			return 270;			
//		case SOUTH:
//			return 0;
//		case EAST:
//			return 90;
//		case WEST:
//			return 90;
//		default:
//			break;
//		}
//		return 0;
//	}
	
	public int[][] getMatrix(ForgeDirection dir){
		switch (dir) {
		case NORTH:
			return MATRIX_NORTH;
		case SOUTH:
			return MATRIX_SOUTH;
		case EAST:
			return MATRIX_EAST;
		case WEST:
			return MATRIX_WEST;
		default:
			break;
		}
		return MATRIX_NONE;
	}
	
	private int[][] MATRIX_NONE={
			{0,0},
			{0,0}
	};
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

//	public void add(ForgeDirection direction, Vector local, int delta) {
////		int[][] rotationMatrix;
//		switch(direction){
//		case EAST:
//			local.x+=delta;
//			break;
//		case WEST:
//			local.x-=delta;
//			break;
//		case SOUTH:
//			local.z+=delta;
//			break;
//		case NORTH:
//			local.z-=delta;
////			rotationMatrix=getMatrix(direction);
////			local.x=rotationMatrix[0][0]*(local.x+delta)-rotationMatrix[0][1]*(local.z+delta);			
////			local.z=rotationMatrix[1][0]*(local.x+delta)-rotationMatrix[1][1]*(local.z+delta);						
//			break;		
//		default:
//			break;
//		}
//	}
}
