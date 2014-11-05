package net.minecraft.mangrove.core.cs;

import net.minecraftforge.common.util.ForgeDirection;

public class CS {
	public static final CS world=new CS(new CSPosition3i(),new CSPosition3d());
	private CSPosition3i position;
	private CSPosition3d positionDouble;
	private CSAxis axis;	
	static{
		world.position.direction=ForgeDirection.SOUTH;
		world.axis=CSAxis.Southing;
	}
	public static CS subSystem(CSPosition3i position) {
		return new CS(position,new CSPosition3d(position.x, position.y, position.z, position.direction));
	}
	public static CS subSystem(CSPosition3d position) {
		return new CS(new CSPosition3i(toInt(position.x), toInt(position.y), toInt(position.z), position.direction),position);	
	}
	
	public static double toDouble(int i){
		return i;
	}
	public static int toInt(double d){
		return (int)Math.round(d);
	}
	
	private CS(CSPosition3i position, CSPosition3d csPosition3d){
		this.position = position;
		this.positionDouble = csPosition3d;
		this.axis=CSAxis.fromDirection(position.direction);
	}


	public CSPosition3i toWorld(CSPosition3i local) {
		final CSPosition3i worldPos=new CSPosition3i(this.position);
		final CSMatrixInt mat = axis.getMatrixInt();
		worldPos.x+=mat.get(0,0)*local.x+mat.get(0,1)*local.z;
		worldPos.y+=local.y;
		worldPos.z+=mat.get(1,0)*local.x+mat.get(1,1)*local.z;
		return worldPos;
	}
	
	public CSPosition3d toWorld(CSPosition3d local) {
		final CSPosition3d worldPos=new CSPosition3d(this.positionDouble);
		final CSMatrixDouble mat = axis.getMatrixDouble();
		worldPos.x+=mat.get(0,0)*local.x+mat.get(0,1)*local.z;
		worldPos.y+=local.y;
		worldPos.z+=mat.get(1,0)*local.x+mat.get(1,1)*local.z;
		return worldPos;
	}
}
