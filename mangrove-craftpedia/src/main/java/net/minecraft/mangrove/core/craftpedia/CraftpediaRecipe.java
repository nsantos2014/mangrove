package net.minecraft.mangrove.core.craftpedia;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.StatCollector;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class CraftpediaRecipe {
	private IRecipe	       recipe;
	private EnumRecipeType	type;
	private String	       outputName;
	private CRBom bom;

	private CraftpediaRecipe() {
		// TODO Auto-generated constructor stub
	}

	public static CraftpediaRecipe of(IRecipe recipe) {
		CraftpediaRecipe craftpediaRecipe = new CraftpediaRecipe();
		craftpediaRecipe.recipe = recipe;
		final ItemStack currentIcon = recipe.getRecipeOutput();

		craftpediaRecipe.outputName = Util.resolveItemName(currentIcon);

		craftpediaRecipe.type = craftpediaRecipe.resolveType(recipe);

		craftpediaRecipe.bom = craftpediaRecipe.type.resolve(recipe);

		return craftpediaRecipe;

	}

	public EnumRecipeType resolveType(IRecipe recipe) {
		
		if (recipe instanceof ShapedOreRecipe) {
			if (Util.i.isMirrored(recipe)) {
				return EnumRecipeType.MirroredShapedOreRecipe;
			} else {
				return EnumRecipeType.NonMirroredShapedOreRecipe;
			}
		} else if (recipe instanceof ShapedRecipes) {
			return EnumRecipeType.ShapedRecipes;
		} else if (recipe instanceof ShapelessOreRecipe) {
			return EnumRecipeType.ShapelessOreRecipe;
		} else  if (recipe instanceof ShapelessRecipes) {
			return EnumRecipeType.ShapelessRecipes;
	
		}
		return EnumRecipeType.Other;

	}

	public IRecipe getRecipe() {
		return recipe;
	}

	public EnumRecipeType getType() {
		return type;
	}

	public String getOutputName() {
		return outputName;
	}

	public CRBom getBom() {
	    return this.bom;
    }

	
}
