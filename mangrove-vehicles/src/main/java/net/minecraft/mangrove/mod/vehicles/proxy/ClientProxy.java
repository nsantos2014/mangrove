package net.minecraft.mangrove.mod.vehicles.proxy;

import net.minecraft.mangrove.mod.vehicles.mav.EntityMAV;
import net.minecraft.mangrove.mod.vehicles.mav.render.RenderMAV;
import cpw.mods.fml.client.registry.RenderingRegistry;


public class ClientProxy extends CommonProxy{
	
	public void registerRenderers() {
	    RenderingRegistry.registerEntityRenderingHandler(EntityMAV.class, new RenderMAV());
	}
}
