package net.minecraft.mangrove.mod.freight;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.init.Items;
import net.minecraft.mangrove.core.recipes.RecipeBuilder;
import net.minecraft.mangrove.mod.freight.boat.ItemMGBoat;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

public class MGFreightItems {
	public static ItemMGBoat itemMGBoat;

	public static void preInit() {
		itemMGBoat = new ItemMGBoat();
	}

	public static void init(FMLInitializationEvent event) {

		if (event.getSide() == Side.CLIENT) {
			RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();

			// blocks
			ItemModelMesher itemModelMesher = renderItem.getItemModelMesher();

			itemModelMesher.register(itemMGBoat, 0, new ModelResourceLocation(MGFreightForge.ID + ":" + MGFreightItems.itemMGBoat.getName(), "inventory"));

		}
		
		GameRegistry.addRecipe(RecipeBuilder.newRecipe().of(MGFreightItems.itemMGBoat, 1)
				.line( null,  null,  null)
				.line( Items.iron_ingot, Items.boat,  Items.iron_ingot)
				.line( Items.iron_ingot,  Items.iron_ingot,  Items.iron_ingot)
				.build()
		);
	}
}
