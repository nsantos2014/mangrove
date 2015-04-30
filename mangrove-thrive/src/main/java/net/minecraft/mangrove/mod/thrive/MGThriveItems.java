package net.minecraft.mangrove.mod.thrive;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.mangrove.core.recipes.RecipeBuilder;
import net.minecraft.mangrove.mod.thrive.probe.ItemProbe;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class MGThriveItems {
	
	public static ItemProbe probe;
	
	
	public static void init(FMLInitializationEvent event) {
		probe=new ItemProbe();
		
		if (event.getSide() == Side.CLIENT) {
			RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
			ItemModelMesher itemModelMesher = renderItem.getItemModelMesher();
			
			// blocks
			
//			itemModelMesher.register(
//					probe, 0,
//					new ModelResourceLocation(MGThriveForge.ID + ":"
//							+ MGThriveItems.probe.getName(), "inventory"));
			itemModelMesher.register(
					probe,new ItemMeshDefinition(){
	            public ModelResourceLocation getModelLocation(ItemStack stack){
	                return new ModelResourceLocation("filled_map", "inventory");
	            }
	        });
			itemModelMesher.register(
					Item.getItemFromBlock(MGThriveBlocks.strongbox), 0,
					new ModelResourceLocation(MGThriveForge.ID + ":"
							+ MGThriveBlocks.strongbox.getName(), "inventory"));
			
			
			
			itemModelMesher.register(
					Item.getItemFromBlock(MGThriveBlocks.pump), 0,
					new ModelResourceLocation(MGThriveForge.ID + ":"
							+ MGThriveBlocks.pump.getName(), "inventory"));
			
			itemModelMesher.register(
					Item.getItemFromBlock(MGThriveBlocks.cistern), 0,
					new ModelResourceLocation(MGThriveForge.ID + ":"
							+ MGThriveBlocks.cistern.getName(), "inventory"));
			itemModelMesher.register(
					Item.getItemFromBlock(MGThriveBlocks.cistern), 0,
					new ModelResourceLocation(MGThriveForge.ID + ":"
							+ MGThriveBlocks.cistern.getName(), "inventory"));
			
			itemModelMesher.register(
					Item.getItemFromBlock(MGThriveBlocks.autobench), 0,
					new ModelResourceLocation(MGThriveForge.ID + ":"
							+ MGThriveBlocks.autobench.getName(), "inventory"));
			
			
		
			itemModelMesher.register(
					Item.getItemFromBlock(MGThriveBlocks.item_broker), 0,
					new ModelResourceLocation(MGThriveForge.ID + ":"
							+ MGThriveBlocks.item_broker.getName(), "inventory"));
			itemModelMesher.register(
					Item.getItemFromBlock(MGThriveBlocks.storage_junction), 0,
					new ModelResourceLocation(MGThriveForge.ID + ":"
							+ MGThriveBlocks.storage_junction.getName(), "inventory"));
			
			itemModelMesher.register(
					Item.getItemFromBlock(MGThriveBlocks.duct_conveyor), 0,
					new ModelResourceLocation(MGThriveForge.ID + ":"
							+ MGThriveBlocks.duct_conveyor.getName(), "inventory"));
			
			itemModelMesher.register(
					Item.getItemFromBlock(MGThriveBlocks.duct_connector), 0,
					new ModelResourceLocation(MGThriveForge.ID + ":"
							+ MGThriveBlocks.duct_connector.getName(), "inventory"));
			
			
			itemModelMesher.register(
					Item.getItemFromBlock(MGThriveBlocks.harvester_farmer), 0,
					new ModelResourceLocation(MGThriveForge.ID + ":"
							+ MGThriveBlocks.harvester_farmer.getName(), "inventory"));
			itemModelMesher.register(
					Item.getItemFromBlock(MGThriveBlocks.harvester_miner), 0,
					new ModelResourceLocation(MGThriveForge.ID + ":"
							+ MGThriveBlocks.harvester_miner.getName(), "inventory"));
			
			
		}
		
	}
}
