package net.minecraft.mangrove.mod.hud.minimap;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.block.statemap.DefaultStateMapper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.init.Blocks;
import net.minecraft.mangrove.mod.hud.minimap.region.BlockColours;
import net.minecraft.mangrove.mod.hud.minimap.region.BlockColours.BlockType;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

// Static class to generate BlockColours.
// This is separate from BlockColours because it needs to run in the GL rendering thread
// whereas the generated BlockColours object is used only in the background thread.
// So basically split to make it clear that BlockColourGen and the generated BlockColours
// must not have any interaction after it is generated.

public class BlockColourGen {

	private static int getIconMapColour(TextureAtlasSprite icon,
			Texture terrainTexture) {
		// flipped icons have the U and V coords reversed (minU > maxU, minV >
		// maxV).
		// thanks go to taelnia for fixing this.
		int iconX = (int) Math.round(((float) terrainTexture.w)
				* Math.min(icon.getMinU(), icon.getMaxU()));
		int iconY = (int) Math.round(((float) terrainTexture.h)
				* Math.min(icon.getMinV(), icon.getMaxV()));
		int iconWidth = (int) Math.round(((float) terrainTexture.w)
				* Math.abs(icon.getMaxU() - icon.getMinU()));
		int iconHeight = (int) Math.round(((float) terrainTexture.h)
				* Math.abs(icon.getMaxV() - icon.getMinV()));

		int[] pixels = new int[iconWidth * iconHeight];

		// MwUtil.log("(%d, %d) %dx%d", iconX, iconY, iconWidth, iconHeight);

		terrainTexture.getRGB(iconX, iconY, iconWidth, iconHeight, pixels, 0,
				iconWidth);

		// need to use custom averaging routine rather than scaling down to one
		// pixel to
		// stop transparent pixel colours being included in the average.
		return Render.getAverageColourOfArray(pixels);
	}

	private static int adjustBlockColourFromType(BlockColours bc,
			int blockAndMeta, int blockColour) {
		// for normal blocks multiply the block colour by the render colour.
		// for other blocks the block colour will be multiplied by the biome
		// colour.
		int blockid = blockAndMeta >> 4;
		Block block = (Block) Block.blockRegistry.getObjectById(blockid);
		BlockType blockType = bc.getBlockType(blockAndMeta);
		switch (blockType) {

		case OPAQUE:
			blockColour |= 0xff000000;
		case NORMAL:
			// fix crash when mods don't implement getRenderColor for all
			// block meta values.
			try {
				int renderColour = block.getRenderColor(block.getBlockState()
						.getBaseState());
				if (renderColour != 0xffffff) {
					blockColour = Render.multiplyColours(blockColour,
							0xff000000 | renderColour);
				}
			} catch (RuntimeException e) {
				// do nothing
			}
			break;
		case LEAVES:
			// leaves look weird on the map if they are not opaque.
			// they also look too dark if the render colour is applied.
			blockColour |= 0xff000000;
			break;
		default:
			break;
		}
		return blockColour;
	}

	private static void genBiomeColours(BlockColours bc) {
		// generate array of foliage, grass, and water colour multipliers
		// for each biome.

		for (int i = 0; i < BiomeGenBase.getBiomeGenArray().length; i++) {
			if (BiomeGenBase.getBiomeGenArray()[i] != null) {
				bc.setBiomeWaterShading(i, BiomeGenBase.getBiomeGenArray()[i]
						.getWaterColorMultiplier() & 0xffffff);
				bc.setBiomeGrassShading(i, BiomeGenBase.getBiomeGenArray()[i]
						.getGrassColorAtPos(new BlockPos(0, 0, 0)) & 0xffffff); // FIXME
																				// 0,0,0?
				bc.setBiomeFoliageShading(i, BiomeGenBase.getBiomeGenArray()[i]
						.getFoliageColorAtPos(new BlockPos(0, 0, 0)) & 0xffffff); // FIXME
																					// 0,0,0?
			} else {
				bc.setBiomeWaterShading(i, 0xffffff);
				bc.setBiomeGrassShading(i, 0xffffff);
				bc.setBiomeFoliageShading(i, 0xffffff);
			}
		}
	}

	public static void genBlockColours(BlockColours bc) {

		MwUtil.log("generating block map colours from textures");

		// copy terrain texture to MwRender pixel bytebuffer

		// bind the terrain texture
		// Minecraft.getMinecraft().func_110434_K().func_110577_a(TextureMap.field_110575_b);
		// get the bound texture id
		// int terrainTextureId = Render.getBoundTextureId();

		int terrainTextureId = Minecraft.getMinecraft().renderEngine
				.getTexture(TextureMap.locationBlocksTexture).getGlTextureId();

		// create texture object from the currently bound GL texture
		if (terrainTextureId == 0) {
			MwUtil.log("error: could get terrain texture ID");
			return;
		}
		Texture terrainTexture = new Texture(terrainTextureId);

		double u1Last = 0;
		double u2Last = 0;
		double v1Last = 0;
		double v2Last = 0;
		int blockColourLast = 0;
		int e_count = 0;
		int b_count = 0;
		int s_count = 0;
		World world = Minecraft.getMinecraft().theWorld;
		BlockRendererDispatcher blockrendererdispatcher = Minecraft
				.getMinecraft().getBlockRendererDispatcher();
		TextureMap tmBlocks = Minecraft.getMinecraft().getTextureMapBlocks();
		for (int blockID = 0; blockID < 4096; blockID++) { // TODO: replace
															// hardcoded 4096
															// with actual
															// registry size
			Block block = (Block) Block.blockRegistry.getObjectById(blockID);

			if (block == null || block == Blocks.air) {
				continue;
			}

			DefaultStateMapper statemapper = new DefaultStateMapper();
			Map locations = statemapper.putStateModelLocations(block);
			Iterator it = locations.entrySet().iterator();
			while (it.hasNext()) {
				Entry<?, ?> type = (Entry<Object, Object>) it.next();
				IBlockState blockState = (IBlockState) type.getKey();
				ResourceLocation location = (ResourceLocation) type.getValue();
				int dv = block.getMetaFromState(blockState);

				int blockAndMeta = ((blockID & 0xfff) << 4) | (dv & 0xf);

				int blockColour = 0;
				TextureAtlasSprite icon;

				if (block == Blocks.water) {
					icon=tmBlocks.getTextureExtry("minecraft:blocks/water_still");
				} else if (block == Blocks.flowing_water) {
					icon=tmBlocks.getTextureExtry("minecraft:blocks/water_flow");
				} else if (block == Blocks.lava) {
					icon=tmBlocks.getTextureExtry("minecraft:blocks/lava_still");
				} else if (block == Blocks.flowing_lava) {
					icon=tmBlocks.getTextureExtry("minecraft:blocks/lava_flow");
				} else {

					IBakedModel ibakedmodel = blockrendererdispatcher
							.getModelFromBlockState(blockState, world, null);
					icon = ibakedmodel.getTexture();
				}
				// String textureName = String.format("%s:blocks/%s",
				// location.getResourceDomain(),location.getResourcePath());
				// TextureAtlasSprite icon =
				// tmBlocks.getTextureExtry(textureName);

				if (icon != null && !"missingno".equals(icon.getIconName())) {
					double u1 = icon.getMinU();
					double u2 = icon.getMaxU();
					double v1 = icon.getMinV();
					double v2 = icon.getMaxV();

					if ((u1 == u1Last) && (u2 == u2Last) && (v1 == v1Last)
							&& (v2 == v2Last)) {
						blockColour = blockColourLast;
						s_count++;
					} else {
						blockColour = getIconMapColour(icon, terrainTexture);
						u1Last = u1;
						u2Last = u2;
						v1Last = v1;
						v2Last = v2;
						blockColourLast = blockColour;
						b_count++;
					}
					// if (dv == 0)
					// MwUtil.log("block %03x:%x colour = %08x", blockID, dv,
					// blockColour);
//				} else if (blockID == 8 || blockID == 9) {
//					blockColour = 0xff;
				}

				// doesn't work as some leaves blocks aren't rendered using the
				// biome
				// foliage colour
				// try {
				// if (block.isLeaves(null, 0, 0, 0)) {
				// bc.setBlockType(blockAndMeta, BlockType.LEAVES);
				// }
				// } catch (NullPointerException e) {
				// }

				blockColour = adjustBlockColourFromType(bc, blockAndMeta,
						blockColour);

				bc.setColour(blockAndMeta, blockColour);

				// System.out.println(" State:"+type.getKey()+":"+type.getValue());
			}
			// for (int dv = 0; dv < 16; dv++) {
			//
			// int blockAndMeta = ((blockID & 0xfff) << 4) | (dv & 0xf);
			//
			// int blockColour = 0;
			//
			// if (block != null) {
			//
			//
			//
			//
			//
			//
			// ResourceLocation location = (ResourceLocation)
			// Block.blockRegistry.getNameForObject(block);
			//
			// String textureName = String.format("%s:blocks/%s",
			// location.getResourceDomain(),location.getResourcePath());
			// TextureAtlasSprite icon = tmBlocks.getTextureExtry(textureName);
			//
			// // try {
			// // icon = block.getIcon(1, dv);
			// // } catch (Exception e) {
			// //
			// //MwUtil.log("genFromTextures: exception caught when requesting block texture for %03x:%x",
			// blockID, dv);
			// // //e.printStackTrace();
			// // e_count++;
			// // }
			//
			// if (icon != null) {
			// double u1 = icon.getMinU();
			// double u2 = icon.getMaxU();
			// double v1 = icon.getMinV();
			// double v2 = icon.getMaxV();
			//
			// if ((u1 == u1Last) && (u2 == u2Last) && (v1 == v1Last) && (v2 ==
			// v2Last)) {
			// blockColour = blockColourLast;
			// s_count++;
			// } else {
			// blockColour = getIconMapColour(icon, terrainTexture);
			// u1Last = u1;
			// u2Last = u2;
			// v1Last = v1;
			// v2Last = v2;
			// blockColourLast = blockColour;
			// b_count++;
			// }
			// //if (dv == 0)
			// // MwUtil.log("block %03x:%x colour = %08x", blockID, dv,
			// blockColour);
			// }else if( blockID==8 || blockID==9){
			// blockColour=0xff;
			// }
			//
			// // doesn't work as some leaves blocks aren't rendered using the
			// biome
			// // foliage colour
			// //try {
			// // if (block.isLeaves(null, 0, 0, 0)) {
			// // bc.setBlockType(blockAndMeta, BlockType.LEAVES);
			// // }
			// //} catch (NullPointerException e) {
			// //}
			//
			// blockColour = adjustBlockColourFromType(bc, blockAndMeta,
			// blockColour);
			// }
			// bc.setColour(blockAndMeta, blockColour);
			// }
		}

		MwUtil.log("processed %d block textures, %d skipped, %d exceptions",
				b_count, s_count, e_count);

		genBiomeColours(bc);
	}
}
