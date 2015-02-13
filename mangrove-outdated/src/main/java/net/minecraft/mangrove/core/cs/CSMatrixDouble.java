package net.minecraft.mangrove.core.cs;

import java.util.Arrays;

public class CSMatrixDouble {
	private final double [] matrix;
	private int size;
		
	public CSMatrixDouble(int size,double ... values) {
		this.size=size;
		this.matrix=new double[size*size];	
		if(values.length==this.matrix.length){
			System.arraycopy(values, 0, this.matrix, 0, this.matrix.length);
		}else{
			Arrays.fill(matrix, 0);
		}
	}
	
	public double get(int i,int j){
		if( i<0 || j<0 || i>=size|| j>=size){
			return -1.0;
		}
		return this.matrix[i+j*size];
	}
	public void set(int i,int j, double value){
		if( i<0 || j<0 || i>=size|| j>=size){
			return;
		}
		this.matrix[i+j*size]=value;
	}
}
