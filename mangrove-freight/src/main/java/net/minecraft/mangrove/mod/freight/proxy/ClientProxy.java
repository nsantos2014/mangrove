package net.minecraft.mangrove.mod.freight.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.mangrove.mod.freight.boat.EntityMGBoat;
import net.minecraft.mangrove.mod.freight.boat.RenderMGBoat;
import net.minecraftforge.fml.client.registry.RenderingRegistry;


public class ClientProxy extends CommonProxy{
	RenderMGBoat entityMGBoatRenderer;
	
	public void registerRenderers() {
		entityMGBoatRenderer=new RenderMGBoat(Minecraft.getMinecraft().getRenderManager());
		RenderingRegistry.registerEntityRenderingHandler(EntityMGBoat.class, entityMGBoatRenderer);
	}
}
