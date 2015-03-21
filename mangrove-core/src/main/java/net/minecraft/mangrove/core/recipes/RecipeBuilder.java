package net.minecraft.mangrove.core.recipes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class RecipeBuilder {

	private boolean mirrored=true;
	private List<Object> recipeCollection=new ArrayList<Object>();
	private Map<ItemStack,Character> charMap=new HashMap<>();
	private char c='!';
	private ItemStack itemStack;
	private char[][] lines=new char[][]{{' ',' ',' '},{' ',' ',' '},{' ',' ',' '}}; 
	int line=0;
	
	public RecipeBuilder of(Block block){
		this.itemStack=new ItemStack(block);
		return this;
	}
	public RecipeBuilder of(Block block,int amount){
		this.itemStack=new ItemStack(block,amount);
		return this;
	}
	public RecipeBuilder of(Block block,int amount,int meta){
		this.itemStack=new ItemStack(block,amount,meta);
		return this;
	}

	public RecipeBuilder of(Item item){
		this.itemStack=new ItemStack(item);
		return this;
	}
	public RecipeBuilder of(Item item,int amount){
		this.itemStack=new ItemStack(item,amount);
		return this;
	}
	public RecipeBuilder of(Item item,int amount,int meta){
		this.itemStack=new ItemStack(item,amount,meta);
		return this;
	}
	public RecipeBuilder mirror(){
		this.mirrored=true;
		return this;
	}
	public RecipeBuilder nonmirror(){
		this.mirrored=false;
		return this;
	}
	public RecipeBuilder line(Object col1,Object col2,Object col3){		
		if (line<3){
			if( !empty(col1)){
				lines[line][0]=resolveChar(resolveItemStack(col1));
			}
			if( !empty(col2)){
				lines[line][1]=resolveChar(resolveItemStack(col2));
			}
			if( !empty(col3)){
				lines[line][2]=resolveChar(resolveItemStack(col3));
			}
			line++;
		}
		return this;
	}
	
	private boolean empty(Object col1) {
		if( col1==null) {
			return true;
		}
		if( col1 instanceof String){
			return ((String)col1).trim().isEmpty();
		}
		if(col1 instanceof Character){
			return ((Character)col1).equals('\0')||Character.isWhitespace((Character)col1);
		}
		if(col1 instanceof Number){
			return ((Number)col1).byteValue()==0;
		}
		return false;
	}
	protected ItemStack resolveItemStack(Object in){
		 if (in instanceof ItemStack){
             return ((ItemStack)in).copy();
         } else if (in instanceof Item) {
             return new ItemStack((Item)in);
         } else if (in instanceof Block) {
             return new ItemStack((Block)in, 1, OreDictionary.WILDCARD_VALUE);
         } else if (in instanceof String) {
             return OreDictionary.getOres((String)in).get(0);
         } else {
             throw new RuntimeException();
         }
	}
	
	protected char resolveChar(ItemStack itemStack){
		if( charMap.containsKey(itemStack)){
			return charMap.get(itemStack);
		}
		charMap.put(itemStack, c);
		recipeCollection.add(c);
		recipeCollection.add(itemStack);
		
		return c++;
	}
	
	public ShapedOreRecipe build(){
		recipeCollection.add(0,mirrored);
		recipeCollection.add(1,new String(lines[0]));
		recipeCollection.add(2,new String(lines[1]));
		recipeCollection.add(3,new String(lines[2]));
		return new ShapedOreRecipe(itemStack, recipeCollection.toArray());
	}

	
	protected RecipeBuilder() {
	}
	
	
	public static RecipeBuilder newRecipe(){
		return new RecipeBuilder();
	}
}
