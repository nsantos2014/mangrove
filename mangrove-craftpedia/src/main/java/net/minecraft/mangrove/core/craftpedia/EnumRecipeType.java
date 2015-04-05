package net.minecraft.mangrove.core.craftpedia;

import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.ShapedOreRecipe;

public enum EnumRecipeType {
	ShapedRecipes {
		@Override
		public CRBom resolve(IRecipe recipe) {
			// TODO Auto-generated method stub
			return null;
		}

	},
	MirroredShapedOreRecipe {
		@Override
		public CRBom resolve(IRecipe recipe) {
			final ShapedOreRecipe sRecipe = (ShapedOreRecipe) recipe;
			return CRBom.of(Util.i.resolveBOM(sRecipe.getInput()),Util.i.resolvePattern(sRecipe.getRecipeSize(),sRecipe.getInput()),true);
		}
	},
	NonMirroredShapedOreRecipe {
		@Override
		public CRBom resolve(IRecipe recipe) {
			ShapedOreRecipe sRecipe = (ShapedOreRecipe) recipe;
			return CRBom.of(Util.i.resolveBOM(sRecipe.getInput()),Util.i.resolvePattern(sRecipe.getRecipeSize(),sRecipe.getInput()),false);
		}
	},
	ShapelessRecipes {
		@Override
		public CRBom resolve(IRecipe recipe) {
			// TODO Auto-generated method stub
			return null;
		}
	},
	ShapelessOreRecipe {
		@Override
		public CRBom resolve(IRecipe recipe) {
			// TODO Auto-generated method stub
			return null;
		}
	},
	Other {
		@Override
		public CRBom resolve(IRecipe recipe) {
			// TODO Auto-generated method stub
			return null;
		}
	};

	public abstract CRBom resolve(IRecipe recipe);

}