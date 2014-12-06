package net.minecraft.mangrove.mod.maps.forge;

import java.io.File;

import net.minecraft.client.Minecraft;
import net.minecraft.mangrove.mod.maps.Mw;
import net.minecraft.mangrove.mod.maps.api.MwAPI;
import net.minecraft.mangrove.mod.maps.overlay.OverlayChecker;
import net.minecraft.mangrove.mod.maps.overlay.OverlayGrid;
import net.minecraft.mangrove.mod.maps.overlay.OverlayMobs;
import net.minecraft.mangrove.mod.maps.overlay.OverlaySlime;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLCommonHandler;

public class ClientProxy extends CommonProxy {
	
	private MwConfig config;
	
	public void preInit(File configFile) {
		this.config = new MwConfig(configFile);
	}
	
	public void load() {
		Mw mw = new Mw(this.config);
		MinecraftForge.EVENT_BUS.register(new EventHandler(mw));
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
