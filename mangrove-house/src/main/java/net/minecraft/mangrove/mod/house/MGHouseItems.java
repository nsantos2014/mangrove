package net.minecraft.mangrove.mod.house;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class MGHouseItems {

	public static void preInit() {
	}

	public static void init(FMLInitializationEvent event) {
		if (event.getSide() == Side.CLIENT) {
			RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();

			// blocks
			ItemModelMesher itemModelMesher = renderItem.getItemModelMesher();
//			itemModelMesher.register(Item.getItemFromBlock(MGHouseBlocks.crate), 0, new ModelResourceLocation(MGHouseForge.ID + ":" + "crate", "inventory"));
			itemModelMesher.register(Item.getItemFromBlock(MGHouseBlocks.glass_lamp), 0, new ModelResourceLocation(MGHouseForge.ID + ":" + "glass_lamp", "inventory"));
			itemModelMesher.register(Item.getItemFromBlock(MGHouseBlocks.glow_ladder), 0, new ModelResourceLocation(MGHouseForge.ID + ":" + "glow_ladder", "inventory"));

			// items
			// renderItem.getItemModelMesher().register(tutorialItem, 0, new
			// ModelResourceLocation(Reference.MODID + ":" + ((ItemTutorial)
			// tutorialItem).getName(), "inventory"));
		}

//		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MGHouseBlocks.crate), new Object[] { Boolean.valueOf(true), "www", "wiw", "www", Character.valueOf('i'),
//				Items.iron_ingot, Character.valueOf('w'), "plankWood" }));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MGHouseBlocks.glass_lamp, 8), Boolean.valueOf(true), "ggg", "grg", "ggg", Character.valueOf('g'), Blocks.glass,
				Character.valueOf('r'), Blocks.redstone_ore));
		GameRegistry.addRecipe(new ItemStack(MGHouseBlocks.glow_ladder, 4), new Object[] { "T", "L", 'T', new ItemStack(Blocks.torch), 'L', new ItemStack(Blocks.ladder) });

	}
}
