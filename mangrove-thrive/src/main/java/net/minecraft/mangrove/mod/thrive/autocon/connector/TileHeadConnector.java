package net.minecraft.mangrove.mod.thrive.autocon.connector;

import net.minecraft.mangrove.mod.thrive.autocon.AbstractTileDuct;
import net.minecraft.util.ResourceLocation;

public class TileHeadConnector extends AbstractTileDuct {
	private static final ResourceLocation glassCyan = new ResourceLocation("textures/blocks/wool_colored_green.png");
	private static final ResourceLocation commandBeam = new ResourceLocation("textures/blocks/comparator_on.png");
	
	@Override
	public ResourceLocation getBlockTexture() {
		// TODO Auto-generated method stub
		return commandBeam;
	}
	@Override
	public ResourceLocation getConnectorTexture() {
		// TODO Auto-generated method stub
		return glassCyan;
	}
}
