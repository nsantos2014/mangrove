package net.minecraft.mangrove;

import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.mangrove.core.craftpedia.Craftpedia;
import net.minecraft.mangrove.core.xray.XRayBlocks;
import net.minecraft.mangrove.network.GuiWidgetMessage;
import net.minecraft.mangrove.network.GuiWidgetMessageHandler;
import net.minecraft.mangrove.network.NetBus;
import net.minecraft.mangrove.network.TileEntityMessage;
import net.minecraft.mangrove.network.TileEntityMessageHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.ShapedOreRecipe;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = MangroveForge.ID, name = MangroveForge.NAME, useMetadata = false, version = MangroveForge.VERSION, dependencies = "required-after:FML")
public class MangroveForge {
	public static final String ID = "Mangrove";
	public static final String NAME = "Mangrove";
	public static final String VERSION = "0.0.1";

	// public static final String CLIENT_PROXY = "forge.reference.proxy.Client";
	// public static final String SERVER_PROXY = "forge.reference.proxy.Server";

	@Instance(MangroveForge.ID)
	public static MangroveForge instance;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		NetBus.initialize();
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GUIHandler());
		NetBus.register(GuiWidgetMessageHandler.class, GuiWidgetMessage.class);
		NetBus.register(TileEntityMessageHandler.class,TileEntityMessage.class);
		
		Craftpedia.instance.registerKeys();
//		XRayBlocks.instance.registerKeys();
		
//		MinecraftForge.EVENT_BUS.register(Craftpedia.instance);
//		MinecraftForge.EVENT_BUS.register(XRayBlocks.instance);
		
		FMLCommonHandler.instance().bus().register(Craftpedia.instance);
//		FMLCommonHandler.instance().bus().register(XRayBlocks.instance);
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		
	}

	public void addNewRecipes() {

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.arrow,64), 
			new Object[] { " F ", " S ", " L ", 
			'F',new ItemStack(Items.feather, 1),
			'S',new ItemStack(Items.stick, 1), 
			'L',new ItemStack(Items.flint, 1) 
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Blocks.iron_ore, 1), 
			new Object[] { "XXX", "XXX", "XXX", 
			'X',Blocks.cobblestone 
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Blocks.gravel,4), 
			new Object[] { "DDD", "D D", "DDD", 
			'D',new ItemStack(Blocks.dirt) 
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Blocks.gold_ore, 1), 
			new Object[] { "SDS", "XDX", "DXD", 
			'S',Blocks.cobblestone, 
			'D', Blocks.dirt, 
			'X', Blocks.gravel 
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Blocks.gold_ore, 1), 
			new Object[] { "DDD", "DTD", "DDD", 
			'D',Blocks.dirt, 
			'T', Blocks.iron_bars 
		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Blocks.gold_ore, 2), 
			new Object[] { "GGG", "GTG", "GGG", 
			'G',Blocks.gravel, 
			'T', Blocks.iron_bars 
		}));
	}
	
	@SubscribeEvent
	public boolean onTickInGame(TickEvent.ClientTickEvent e) {
		XRayBlocks.instance.onTick(Minecraft.getMinecraft().theWorld != null);
//		if ((!(toggleXray)) || (this.mc.theWorld == null))
//			return true;
//		if (cooldownTicks < 1) {
//			compileDL();
//			cooldownTicks = 80;
//		}
//		cooldownTicks -= 1;
		return true;
	}
}
