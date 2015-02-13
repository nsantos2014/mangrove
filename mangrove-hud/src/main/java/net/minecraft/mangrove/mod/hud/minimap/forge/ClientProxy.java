package net.minecraft.mangrove.mod.hud.minimap.forge;

import java.io.File;

import net.minecraft.client.Minecraft;
import net.minecraft.mangrove.mod.hud.minimap.Mw;
import net.minecraft.mangrove.mod.hud.minimap.api.MwAPI;
import net.minecraft.mangrove.mod.hud.minimap.overlay.OverlayChecker;
import net.minecraft.mangrove.mod.hud.minimap.overlay.OverlayGrid;
import net.minecraft.mangrove.mod.hud.minimap.overlay.OverlayMobs;
import net.minecraft.mangrove.mod.hud.minimap.overlay.OverlaySlime;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class ClientProxy extends CommonProxy {
	
	private MwConfig config;
	
	public void preInit(File configFile) {
		this.config = new MwConfig(configFile);
	}
	
	public void load() {
		Mw mw = new Mw(this.config);
		MinecraftForge.EVENT_BUS.register(new MGEventHandler(mw));
		FMLCommonHandler.instance().bus().register(new MwKeyHandler());
		// temporary workaround for user defined key bindings not being loaded
		// at game start. see https://github.com/MinecraftForge/FML/issues/378
		// for more info.
		Minecraft.getMinecraft().gameSettings.loadOptions();
	}
	
	public void postInit() {
		MwAPI.registerDataProvider("Slime", new OverlaySlime());
		MwAPI.registerDataProvider("Grid", new OverlayGrid());
		MwAPI.registerDataProvider("Checker", new OverlayChecker());
		MwAPI.registerDataProvider("Mobs", new OverlayMobs());
		MwAPI.setCurrentDataProvider("Slime");
	}
}
