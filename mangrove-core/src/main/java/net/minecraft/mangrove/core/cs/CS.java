package net.minecraft.mangrove.core.cs;


import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;


public class CS {
	public static final CS world=new CS(new Vec3i(0,0,0),new Vec3(0.0,0.0,0.0),EnumFacing.SOUTH);
	private EnumFacing direction;
	private Vec3i position;
	private Vec3 positionDouble;
	private Axis axis;	
	
	public static CS subSystem(Vec3i position,EnumFacing direction) {
		return new CS(position,new Vec3(position.getX(), position.getY(), position.getZ()),direction);
	}
	public static CS subSystem(Vec3 position,EnumFacing direction) {
		return new CS(new Vec3i(toInt(position.xCoord), toInt(position.yCoord), toInt(position.zCoord)),position,direction);	
	}
	
	public static double toDouble(int i){
		return i;
	}
	public static int toInt(double d){
		return (int)Math.round(d);
	}
	
	private CS(Vec3i position, Vec3 csPosition3d,EnumFacing direction){
		this.position = position;
		this.positionDouble = csPosition3d;
		this.direction=direction;
		this.axis=direction.getAxis();
	}
	public BlockPos toWorldBlockPos(Vec3i local) {
	    Vec3i vec = toWorld(local);
	    return new BlockPos(vec);
	}
	public BlockPos toWorldBlockPos(BlockPos local) {
        Vec3i vec = toWorld(new Vec3i(local.getX(), local.getY(), local.getZ()));
        return new BlockPos(vec);
    }

	public Vec3i toWorld(Vec3i local) {
		if( axis.isHorizontal() ){
		    double[][] matrix;
		    if( axis==Axis.X){
		        matrix=T_MATRIX_X;
		    }else {
		        matrix=T_MATRIX_Z;
		    }
		    int x = toInt(matrix[0][0])*local.getX()+toInt(matrix[0][1])*local.getZ();
	        int y = local.getY();
	        int z = toInt(matrix[1][0])*local.getX()+toInt(matrix[1][1])*local.getZ();
	        return new Vec3i(x, y, z);
		}
		
		return local;
	}
	
	public Vec3 toWorld(Vec3 local) {
	    if( axis.isHorizontal() ){
            double[][] matrix;
            if( axis==Axis.X){
                matrix=T_MATRIX_X;
            }else {
                matrix=T_MATRIX_Z;
            }
            double x = matrix[0][0]*local.xCoord+matrix[0][1]*local.zCoord;
            double y = local.yCoord;
            double z = matrix[1][0]*local.xCoord+matrix[1][1]*local.zCoord;
            return new Vec3(x, y, z);
        }
        
        return local;
	}
	
	double[][] T_MATRIX_X={
	        {0.0,  1.0},  
	         {1.0,  0.0}
	        
	};
	double[][] T_MATRIX_Z={
	        {  1.0,  0.0},  
	        { 0.0,  1.0}     
    };
}
