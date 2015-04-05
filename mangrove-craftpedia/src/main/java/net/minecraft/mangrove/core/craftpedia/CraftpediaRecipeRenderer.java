package net.minecraft.mangrove.core.craftpedia;

import net.minecraft.client.Minecraft;

public interface CraftpediaRecipeRenderer {
	void setWorldAndResolution(Minecraft mc, int width, int height);
	void renderRecipe(CraftpediaRecipe recipe);
}
