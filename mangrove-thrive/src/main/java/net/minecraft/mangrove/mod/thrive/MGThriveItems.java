package net.minecraft.mangrove.mod.thrive;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class MGThriveItems {

	public static void init(FMLInitializationEvent event) {
		if (event.getSide() == Side.CLIENT) {
			RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
			ItemModelMesher itemModelMesher = renderItem.getItemModelMesher();
			
			
			itemModelMesher.register(
					Item.getItemFromBlock(MGThriveBlocks.duct_conveyor), 0,
					new ModelResourceLocation(MGThriveForge.ID + ":"
							+ MGThriveBlocks.duct_conveyor.getName(), "inventory"));
			itemModelMesher.register(
					Item.getItemFromBlock(MGThriveBlocks.duct_connector), 0,
					new ModelResourceLocation(MGThriveForge.ID + ":"
							+ MGThriveBlocks.duct_connector.getName(), "inventory"));
			
			// blocks
			
			itemModelMesher.register(
					Item.getItemFromBlock(MGThriveBlocks.autobench), 0,
					new ModelResourceLocation(MGThriveForge.ID + ":"
							+ "autobench", "inventory"));
			
//			itemModelMesher.register(
//					Item.getItemFromBlock(MGThriveBlocks.simpleduct), 0,
//					new ModelResourceLocation(MGThriveForge.ID + ":"
//							+ "simpleduct", "inventory"));
//			itemModelMesher.register(
//					Item.getItemFromBlock(MGThriveBlocks.robot_kernel), 0,
//					new ModelResourceLocation(MGThriveForge.ID + ":"
//							+ "robot_kernel", "inventory"));
//			itemModelMesher.register(
//					Item.getItemFromBlock(MGThriveBlocks.robot_link), 0,
//					new ModelResourceLocation(MGThriveForge.ID + ":"
//							+ "robot_link", "inventory"));
//			itemModelMesher.register(
//					Item.getItemFromBlock(MGThriveBlocks.farmer_node), 0,
//					new ModelResourceLocation(MGThriveForge.ID + ":"
//							+ "robot_farmer_head", "inventory"));
//			itemModelMesher.register(
//					Item.getItemFromBlock(MGThriveBlocks.miner_node), 0,
//					new ModelResourceLocation(MGThriveForge.ID + ":"
//							+ "robot_miner_head", "inventory"));

			
//			ItemBlock itemFromBlock = new ItemBlock(MGThriveBlocks.link);
//			itemFromBlock.setUnlocalizedName(MGThriveForge.ID + ":"
//							+ MGThriveBlocks.link.getName()+"_0");
//			
//			itemModelMesher.register(
//					itemFromBlock, 0,
//					new ModelResourceLocation(MGThriveForge.ID + ":"
//							+ MGThriveBlocks.link.getName(), "inventory"));
//			itemFromBlock = new ItemBlock(MGThriveBlocks.link);
//			itemFromBlock.setUnlocalizedName(MGThriveForge.ID + ":"
//							+ MGThriveBlocks.link.getName()+"_1");
//			itemModelMesher.register(
//					itemFromBlock, 1,
//					new ModelResourceLocation(MGThriveForge.ID + ":"
//							+ MGThriveBlocks.link.getName(), "inventory"));
//			itemFromBlock = new ItemBlock(MGThriveBlocks.link);
//			itemFromBlock.setUnlocalizedName(MGThriveForge.ID + ":"
//							+ MGThriveBlocks.link.getName()+"_2");
//			itemModelMesher.register(
//					itemFromBlock, 2,
//					new ModelResourceLocation(MGThriveForge.ID + ":"
//							+ MGThriveBlocks.link.getName(), "inventory"));
//			itemFromBlock = new ItemBlock(MGThriveBlocks.link);
//			itemFromBlock.setUnlocalizedName(MGThriveForge.ID + ":"
//							+ MGThriveBlocks.link.getName()+"_3");
//			itemModelMesher.register(
//					itemFromBlock, 3,
//					new ModelResourceLocation(MGThriveForge.ID + ":"
//							+ MGThriveBlocks.link.getName(), "inventory"));
			
			itemModelMesher.register(
					Item.getItemFromBlock(MGThriveBlocks.item_broker), 0,
					new ModelResourceLocation(MGThriveForge.ID + ":"
							+ MGThriveBlocks.item_broker.getName(), "inventory"));
			itemModelMesher.register(
					Item.getItemFromBlock(MGThriveBlocks.storage_junction), 0,
					new ModelResourceLocation(MGThriveForge.ID + ":"
							+ MGThriveBlocks.storage_junction.getName(), "inventory"));
			
			
			
			itemModelMesher.register(
					Item.getItemFromBlock(MGThriveBlocks.harvester_farmer), 0,
					new ModelResourceLocation(MGThriveForge.ID + ":"
							+ MGThriveBlocks.harvester_farmer.getName(), "inventory"));
			itemModelMesher.register(
					Item.getItemFromBlock(MGThriveBlocks.harvester_miner), 0,
					new ModelResourceLocation(MGThriveForge.ID + ":"
							+ MGThriveBlocks.harvester_miner.getName(), "inventory"));
			
			
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
