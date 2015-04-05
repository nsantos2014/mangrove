package net.minecraft.mangrove.core.craftpedia;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

@Mod(modid = MGCraftpediaForge.ID, name = MGCraftpediaForge.NAME, useMetadata = false, version = MGCraftpediaForge.VERSION, dependencies = "required-after:FML")
public class MGCraftpediaForge {
	public static final String ID = "mgcraftpedia";
	public static final String NAME = "Mangrove Craftpedia";
	public static final String VERSION = "8.0.1";

	@Instance(MGCraftpediaForge.ID)
	public static MGCraftpediaForge instance;
	
	private static KeyBinding toggleCraftpediaGui=new KeyBinding("Toggle Craftpedia", Keyboard.KEY_F8, "Craftpedia");
	
	public MGCraftpediaForge() {
    }

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		ClientRegistry.registerKeyBinding(toggleCraftpediaGui);
//		FMLCommonHandler.instance().bus().register(Craftpedia.instance);
		FMLCommonHandler.instance().bus().register(this);
	}
	@EventHandler
	public void postInit(FMLPostInitializationEvent event){	    
	}

	public void addNewRecipes() {
	}
	@SubscribeEvent
    public void renderWorldLastEvent(RenderWorldLastEvent evt) {
	}
	
	@SubscribeEvent
	public void keyboardEvent(InputEvent.KeyInputEvent key) {
		if (!(Minecraft.getMinecraft().currentScreen instanceof GuiScreen)) {
//			if (this.toggleXrayBinding.getIsKeyPressed()) {
//				toggleXray = !(toggleXray);
//				if (toggleXray)
//					cooldownTicks = 0;
//				else
//					GL11.glDeleteLists(displayListid, 1);
//			}

			if (toggleCraftpediaGui.isPressed())
				CraftpediaGui.show();
		}
	}
}
