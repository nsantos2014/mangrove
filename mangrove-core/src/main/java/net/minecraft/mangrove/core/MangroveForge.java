package net.minecraft.mangrove.core;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.mangrove.network.GuiWidgetMessage;
import net.minecraft.mangrove.network.GuiWidgetMessageHandler;
import net.minecraft.mangrove.network.NetBus;
import net.minecraft.mangrove.network.TileEntityMessage;
import net.minecraft.mangrove.network.TileEntityMessageHandler;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

@Mod(modid = MangroveForge.ID, name = MangroveForge.NAME, useMetadata = false, version = MangroveForge.VERSION, dependencies = "required-after:FML")
public class MangroveForge {
	public static final String ID = "mgcore";
	public static final String NAME = "Mangrove Core";
	public static final String VERSION = "8.0.1";

	// public static final String CLIENT_PROXY = "forge.reference.proxy.Client";
	// public static final String SERVER_PROXY = "forge.reference.proxy.Server";

	@Instance(MangroveForge.ID)
	public static MangroveForge instance;
	
	public MangroveForge() {
//	    FMLCommonHandler.instance().bus().register(XRay.instance);
    }

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		NetBus.initialize();
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GUIHandler());
		NetBus.register(GuiWidgetMessageHandler.class, GuiWidgetMessage.class);
		NetBus.register(TileEntityMessageHandler.class,TileEntityMessage.class);
		
//		Craftpedia.instance.registerKeys();
////		XRayBlocks.instance.registerKeys();
//		
////		MinecraftForge.EVENT_BUS.register(Craftpedia.instance);
////		MinecraftForge.EVENT_BUS.register(XRayBlocks.instance);
//		
//		XRay.instance.preInit(event);
//		
//		FMLCommonHandler.instance().bus().register(Craftpedia.instance);
//		FMLCommonHandler.instance().bus().register(XRay.instance);
//		MinecraftForge.EVENT_BUS.register(XRay.instance);
		MinecraftForge.EVENT_BUS.register(this);
		
		
//		FMLCommonHandler.instance().bus().register(XRayBlocks.instance);
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
//	    XRay.instance.init(event);
	    addNewRecipes();
	    ChestGenHooks.addItem(ChestGenHooks.BONUS_CHEST, new WeightedRandomChestContent(new ItemStack(Items.coal, 64, 0), 1, 3, 1000));
	    ChestGenHooks.addItem(ChestGenHooks.BONUS_CHEST, new WeightedRandomChestContent(new ItemStack(Items.iron_ingot, 64, 0), 1, 3, 1000));
	    ChestGenHooks.addItem(ChestGenHooks.BONUS_CHEST, new WeightedRandomChestContent(new ItemStack(Items.bread, 64, 0), 1, 6, 2000));
	    ChestGenHooks.addItem(ChestGenHooks.BONUS_CHEST, new WeightedRandomChestContent(new ItemStack(Blocks.obsidian), 64, 64, 5000));
	}
	@EventHandler
	public void postInit(FMLPostInitializationEvent event){
	    
	}

	public void addNewRecipes() {

//		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.arrow,64), 
//			new Object[] { " F ", " S ", " L ", 
//			'F',new ItemStack(Items.feather, 1),
//			'S',new ItemStack(Items.stick, 1), 
//			'L',new ItemStack(Items.flint, 1) 
//		}));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Blocks.iron_ore, 1), 
			new Object[] { "XXX", "XXX", "XXX", 
			'X',Blocks.cobblestone 
		}));
		
//		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Blocks.gravel,4), 
//			new Object[] { "DDD", "D D", "DDD", 
//			'D',new ItemStack(Blocks.dirt) 
//		}));
//
//		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Blocks.gold_ore, 1), 
//			new Object[] { "SDS", "XDX", "DXD", 
//			'S',Blocks.cobblestone, 
//			'D', Blocks.dirt, 
//			'X', Blocks.gravel 
//		}));
//		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Blocks.gold_ore, 1), 
//			new Object[] { "DDD", "DTD", "DDD", 
//			'D',Blocks.dirt, 
//			'T', Blocks.iron_bars 
//		}));
//
//		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Blocks.gold_ore, 2), 
//			new Object[] { "GGG", "GTG", "GGG", 
//			'G',Blocks.gravel, 
//			'T', Blocks.iron_bars 
//		}));
	}
	
//	@SubscribeEvent
//	public boolean onTickInGame(TickEvent.ClientTickEvent e) {
//		XRayBlocks.instance.onTick(Minecraft.getMinecraft().theWorld != null);
////		if ((!(toggleXray)) || (this.mc.theWorld == null))
////			return true;
////		if (cooldownTicks < 1) {
////			compileDL();
////			cooldownTicks = 80;
////		}
////		cooldownTicks -= 1;
//		return true;
//	}
	@SubscribeEvent
    public void renderWorldLastEvent(RenderWorldLastEvent evt) {
//	    XRay.instance.renderWorldLastEvent(evt);
	    
//	    System.out.println("===========================================================================================");
//	    System.out.println("Villages");
////      final EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
//      final World world = Minecraft.getMinecraft().theWorld;
//      
//      for(Object vilObj: world.villageCollectionObj.getVillageList()){
//          System.out.println("Vil:"+vilObj);
//      }
//      System.out.println("===========================================================================================");
	}
}