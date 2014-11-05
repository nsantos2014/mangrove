package net.minecraft.mangrove.core.cs;

import java.util.Arrays;

public class CSMatrixInt {
	private final int [] matrix;
	private int size;
		
	public CSMatrixInt(int size,int ... values) {
		this.size=size;
		this.matrix=new int[size*size];	
		if(values.length==this.matrix.length){
			System.arraycopy(values, 0, this.matrix, 0, this.matrix.length);
		}else{
			Arrays.fill(matrix, 0);
		}
	}
	
	public int get(int i,int j){
		if( i<0 || j<0 || i>=size|| j>=size){
			return -1;
		}
		return this.matrix[i+j*size];
	}
	public void set(int i,int j, int value){
		if( i<0 || j<0 || i>=size|| j>=size){
			return;
		}
		this.matrix[i+j*size]=value;
	}
}
