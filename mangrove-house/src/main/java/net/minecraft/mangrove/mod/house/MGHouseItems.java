package net.minecraft.mangrove.mod.house;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.mangrove.core.recipes.RecipeBuilder;
import net.minecraft.mangrove.mod.house.doors.drawbridge.ItemDrawbridge;
import net.minecraft.mangrove.mod.house.matplace.ItemCobblestonePlacer;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class MGHouseItems {
//	public static ItemDrawbridge drawbridge;
	public static ItemCobblestonePlacer cobblestonePlacer;
	
	public static void preInit() {
//		drawbridge=new ItemDrawbridge(MGHouseBlocks.drawbridge);
//		cobblestonePlacer=new ItemCobblestonePlacer();
	}

	public static void init(FMLInitializationEvent event) {
		
		if (event.getSide() == Side.CLIENT) {
			RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();

			// blocks
			ItemModelMesher itemModelMesher = renderItem.getItemModelMesher();
//			itemModelMesher.register(Item.getItemFromBlock(MGHouseBlocks.crate), 0, new ModelResourceLocation(MGHouseForge.ID + ":" + "crate", "inventory"));
			itemModelMesher.register(Item.getItemFromBlock(MGHouseBlocks.glass_lamp), 0, new ModelResourceLocation(MGHouseForge.ID + ":" + "glass_lamp", "inventory"));
			itemModelMesher.register(Item.getItemFromBlock(MGHouseBlocks.glow_ladder), 0, new ModelResourceLocation(MGHouseForge.ID + ":" + "glow_ladder", "inventory"));
//			itemModelMesher.register(Item.getItemFromBlock(MGHouseBlocks.drawbridge), 0, new ModelResourceLocation(MGHouseForge.ID + ":" + "drawbridge", "inventory"));
			
//			itemModelMesher.register(drawbridge, 0, new ModelResourceLocation(MGHouseForge.ID + ":" + MGHouseItems.drawbridge.getName(), "inventory"));

//			itemModelMesher.register(cobblestonePlacer, 0, new ModelResourceLocation(MGHouseForge.ID + ":" + MGHouseItems.cobblestonePlacer.getName(), "inventory"));

			// items
			// renderItem.getItemModelMesher().register(tutorialItem, 0, new
			// ModelResourceLocation(Reference.MODID + ":" + ((ItemTutorial)
			// tutorialItem).getName(), "inventory"));
		}

//		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MGHouseBlocks.crate), new Object[] { Boolean.valueOf(true), "www", "wiw", "www", Character.valueOf('i'),
//				Items.iron_ingot, Character.valueOf('w'), "plankWood" }));

		GameRegistry.addRecipe(RecipeBuilder.newRecipe().of(MGHouseBlocks.glass_lamp, 8)
				.line( Blocks.glass,  Blocks.glass,  Blocks.glass)
				.line( Blocks.glass, Items.redstone,  Blocks.glass)
				.line( Blocks.glass,  Blocks.glass,  Blocks.glass)
				.build()
		);
		GameRegistry.addRecipe(RecipeBuilder.newRecipe().of(MGHouseBlocks.glow_ladder, 4)
				.line( null,  null,  null)
				.line( null, Blocks.ladder,  null)
				.line( null,  Blocks.torch,  null)
				.build()
		);
		
		GameRegistry.addRecipe(RecipeBuilder.newRecipe().of(MGHouseBlocks.drawbridge, 1)
				.line( Blocks.planks,  Blocks.planks,  Blocks.planks)
				.line( Blocks.planks, Blocks.planks,  Blocks.planks)
				.line( Items.iron_ingot,  Items.iron_ingot,  Items.iron_ingot)
				.build()
		);
		
//		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MGHouseBlocks.glass_lamp, 8), Boolean.valueOf(true), "ggg", "grg", "ggg", Character.valueOf('g'), Blocks.glass,
//				Character.valueOf('r'), Blocks.redstone_ore));
//		GameRegistry.addRecipe(new ItemStack(MGHouseBlocks.glow_ladder, 4), new Object[] { "T", "L", 'T', new ItemStack(Blocks.torch), 'L', new ItemStack(Blocks.ladder) });

	}
}
