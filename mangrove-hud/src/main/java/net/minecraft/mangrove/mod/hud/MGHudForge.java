package net.minecraft.mangrove.mod.hud;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;

@Mod(modid=MGHudForge.ID, name=MGHudForge.NAME,version=MGHudForge.VERSION,useMetadata=false)
public class MGHudForge {
	public static final String ID = "Mangrove|HUD";
    public static final String NAME = "Mangrove HUD";
    public static final String VERSION = "0.0.1";
    
	@Instance(MGHudForge.ID)
	public static MGHudForge instance;
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {		
		MinecraftForge.EVENT_BUS.register(new HudController(Minecraft.getMinecraft()));					
	}
}
