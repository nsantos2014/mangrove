package net.minecraft.mangrove.core.craftpedia;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;

public class CRPattern {
	Map<String,List<ItemStack>> pattern=new HashMap<String, List<ItemStack>>();
	private int rows;
	private int cols;
	
	public void setDimension(int rows,int cols){
		this.rows = rows;
		this.cols = cols;
	}
	
	public int getCols() {
	    return cols;
    }
	public int getRows() {
	    return rows;
    }
	public List<ItemStack> getCell(int rowId,int colId) {
	    List<ItemStack> list = pattern.get(String.format("%d,%d", rowId,colId));
		return list==null?Collections.emptyList():list;
    }
	public void addCell(int rowId, int colId, List<ItemStack> items) {
		pattern.put(String.format("%d,%d", rowId,colId), items);
    }

}
