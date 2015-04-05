package net.minecraft.mangrove.core;

import java.lang.reflect.Field;
import java.util.Map;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.mangrove.core.items.ItemHardconium;
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
import net.minecraftforge.fml.common.registry.FMLControlledNamespacedRegistry;
import net.minecraftforge.fml.common.registry.GameData;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession;
import net.minecraftforge.oredict.ShapedOreRecipe;

@Mod(modid = MGCoreForge.ID, name = MGCoreForge.NAME, useMetadata = false, version = MGCoreForge.VERSION, dependencies = "required-after:FML")
public class MGCoreForge {
	public static final String ID = "mgcore";
	public static final String NAME = "Mangrove Core";
	public static final String VERSION = "8.0.1";
	
//	private FMLControlledNamespacedRegistry<VillagerProfession> professions;

	// public static final String CLIENT_PROXY = "forge.reference.proxy.Client";
	// public static final String SERVER_PROXY = "forge.reference.proxy.Server";

	@Instance(MGCoreForge.ID)
	public static MGCoreForge instance;
	
	public MGCoreForge() {
//	    FMLCommonHandler.instance().bus().register(XRay.instance);
    }

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
//		professions=GameData.createRegistry("villagerprofessions", VillagerProfession.class, 0, 1024);
//		
//		try {
//			Class<GameData> class1 = GameData.class;
//			Field declaredField = class1.getDeclaredField("genericRegistries");
//			declaredField.setAccessible(true);
//			
//			Field mainDataField = class1.getDeclaredField("mainData");
//			mainDataField.setAccessible(true);
//			
//			GameData mainData = (GameData) mainDataField.get(null);
//			
//			Map<String,FMLControlledNamespacedRegistry<?>> genericRegistries=(Map<String, FMLControlledNamespacedRegistry<?>>) declaredField.get(mainData);
//			
//			genericRegistries.put("villagerprofessions",professions);
//			
//		} catch (NoSuchFieldException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (SecurityException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IllegalArgumentException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
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
		
		MGCoreItems.preInit();
		
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
//	    XRay.instance.init(event);
	    addNewRecipes();
	    ChestGenHooks.addItem(ChestGenHooks.BONUS_CHEST, new WeightedRandomChestContent(new ItemStack(Items.coal, 64, 0), 1, 3, 1000));
	    ChestGenHooks.addItem(ChestGenHooks.BONUS_CHEST, new WeightedRandomChestContent(new ItemStack(Items.iron_ingot, 64, 0), 1, 3, 1000));
	    ChestGenHooks.addItem(ChestGenHooks.BONUS_CHEST, new WeightedRandomChestContent(new ItemStack(Items.bread, 64, 0), 1, 6, 2000));
	    ChestGenHooks.addItem(ChestGenHooks.BONUS_CHEST, new WeightedRandomChestContent(new ItemStack(Blocks.obsidian), 64, 64, 5000));
	    
	    MGCoreItems.registerItems(event);
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
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.dye, 1,EnumDyeColor.BLUE.getMetadata()), 
				new Object[] { "LB", 
				'L',new ItemStack(Items.dye, 1,EnumDyeColor.LIGHT_BLUE.getMetadata()),
				'B',new ItemStack(Items.dye, 1,EnumDyeColor.BLACK.getMetadata())
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
