package net.minecraft.mangrove.mod.thrive;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class MGThriveItems {

	public static void init(FMLInitializationEvent event) {
		if (event.getSide() == Side.CLIENT) {
			RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();

			// blocks
			ItemModelMesher itemModelMesher = renderItem.getItemModelMesher();
			itemModelMesher.register(
					Item.getItemFromBlock(MGThriveBlocks.autobench), 0,
					new ModelResourceLocation(MGThriveForge.ID + ":"
							+ "autobench", "inventory"));
			itemModelMesher.register(
					Item.getItemFromBlock(MGThriveBlocks.robot_kernel), 0,
					new ModelResourceLocation(MGThriveForge.ID + ":"
							+ "robot_kernel", "inventory"));
			itemModelMesher.register(
					Item.getItemFromBlock(MGThriveBlocks.robot_link), 0,
					new ModelResourceLocation(MGThriveForge.ID + ":"
							+ "robot_link", "inventory"));
			itemModelMesher.register(
					Item.getItemFromBlock(MGThriveBlocks.farmer_node), 0,
					new ModelResourceLocation(MGThriveForge.ID + ":"
							+ "robot_farmer_head", "inventory"));
			itemModelMesher.register(
					Item.getItemFromBlock(MGThriveBlocks.miner_node), 0,
					new ModelResourceLocation(MGThriveForge.ID + ":"
							+ "robot_miner_head", "inventory"));

			// items
			// renderItem.getItemModelMesher().register(tutorialItem, 0, new
			// ModelResourceLocation(Reference.MODID + ":" + ((ItemTutorial)
			// tutorialItem).getName(), "inventory"));
		}
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(
				MGThriveBlocks.autobench, 1), new Object[] {
				Boolean.valueOf(true), "www", "waw", "www",
				Character.valueOf('a'), Blocks.crafting_table,
				Character.valueOf('w'), Blocks.planks }));

	}
}
