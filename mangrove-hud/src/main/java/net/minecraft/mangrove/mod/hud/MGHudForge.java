package net.minecraft.mangrove.mod.hud;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

@Mod(modid=MGHudForge.ID, name=MGHudForge.NAME,version=MGHudForge.VERSION,useMetadata=false)
public class MGHudForge {
	public static final String ID = "mghud";
    public static final String NAME = "Mangrove HUD";
    public static final String VERSION = "8.0.1";
    
	@Instance(MGHudForge.ID)
	public static MGHudForge instance;
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {		
		MinecraftForge.EVENT_BUS.register(new HudController(Minecraft.getMinecraft()));					
	}
}
