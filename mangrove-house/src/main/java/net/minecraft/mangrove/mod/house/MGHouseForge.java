package net.minecraft.mangrove.mod.house;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.mangrove.core.GUIHandler;
import net.minecraft.mangrove.core.MGCoreForge;
import net.minecraft.mangrove.mod.house.block.crate.TileEntityCrate;
import net.minecraft.mangrove.mod.house.block.crate.gui.ContainerCrate;
import net.minecraft.mangrove.mod.house.block.crate.gui.GuiCrate;
import net.minecraft.mangrove.mod.house.proxy.CommonProxy;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

@Mod(modid=MGHouseForge.ID, name=MGHouseForge.NAME,version=MGHouseForge.VERSION,useMetadata=false,dependencies="required-after:"+MGCoreForge.ID)
public class MGHouseForge {
	public static final String ID = "mghouse";
    public static final String NAME = "Mangrove House";
    public static final String VERSION = "0.0.1";
    public static final String CLIENT_SIDE_PROXY="net.minecraft.mangrove.mod.house.proxy.ClientProxy";
    public static final String SERVER_SIDE_PROXY="net.minecraft.mangrove.mod.house.proxy.CommonProxy";
	@Instance(MGHouseForge.ID)
	public static MGHouseForge instance;
	
	@SidedProxy(clientSide = MGHouseForge.CLIENT_SIDE_PROXY, serverSide = MGHouseForge.SERVER_SIDE_PROXY)
	public static CommonProxy proxy;
	public static GUIHandler handler=new GUIHandler();
	//
	public int cooldownTime=20;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent evt) {
		FMLCommonHandler.instance().bus().register(this);
	    MinecraftForge.EVENT_BUS.register(this);
	    MinecraftForge.TERRAIN_GEN_BUS.register(this);
	    MGHouseBlocks.preInit();
	    MGHouseItems.preInit();
//		registerBoatDoor();
//		registerGlassLamp();
//		registerGlowLadder();
//		registerCrate();
//		registerDucts();
//		registerCraftpedia(evt);
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
//		proxy.registerRenderers();
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, handler);
//		FMLCommonHandler.instance().bus().register(this);
//		MinecraftForge.EVENT_BUS.register(this);
		MGHouseBlocks.init();
		MGHouseItems.init(event);
		
	}
	public void registerCrate(){
//		GameRegistry.registerBlock(MGHouseBlocks.crate,"crate");
//		handler.registerClass(TileEntityCrate.class, ContainerCrate.class, GuiCrate.class);
//		harvester.setLightLevel(0.9f);
//		LanguageRegistry.addName(crate, "Crate");
//		TileEntity.addMapping(TileEntityCrate.class, "Crate");
		
//		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MGHouseBlocks.crate), new Object[] {
//				Boolean.valueOf(true),
//				 "www", 
//				 "wiw", 
//				 "www", 
//				 Character.valueOf('i'), Items.iron_ingot, 
//				Character.valueOf('w'),	"plankWood" 
//		}));
	}
	public void registerGlassLamp(){
		GameRegistry.registerBlock(MGHouseBlocks.glass_lamp, "glass_lamp");
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MGHouseBlocks.glass_lamp, 1), 
			Boolean.valueOf(true),
			 "   ", 
			 " g ", 
			 " t ", 
			Character.valueOf('g'),
			Blocks.glass, 
			Character.valueOf('t'),
			Blocks.torch 
			));
	}
	
//	public void registerBoatDoor(){
//		GameRegistry.registerBlock(MGHouseBlocks.boat_door,"boat_door");
//		//LanguageRegistry.addName(boat_door, "Boat Door");
//		GameRegistry.registerItem(MGHouseItems.item_boat_door, "item_boat_door");
//		
//		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MGHouseItems.item_boat_door), new Object[] {
//			Boolean.valueOf(true),
//			 "bb ", 
//			 "bi ", 
//			 "bb ", 
//			 Character.valueOf('b'), new ItemStack(Blocks.iron_bars), 
//			Character.valueOf('i'),	new ItemStack(Items.iron_ingot) 
//		}));
//	}
	public void registerGlowLadder(){
		GameRegistry.registerBlock(MGHouseBlocks.glow_ladder, "glow_ladder");
		GameRegistry.addRecipe(new ItemStack(MGHouseBlocks.glow_ladder,4), new Object[]{
            "T", 
            "L",
            'T', new ItemStack(Blocks.torch),
            'L', new ItemStack(Blocks.ladder)
		});
	}
	
//	public void registerDucts() {
//        GameRegistry.registerBlock(MGHouseBlocks.duct, "duct");
//        TileEntity.addMapping(TileEntityDuct.class, "duct");
//        
//        
//        GameRegistry.registerBlock(MGHouseBlocks.duct_filter, "duct_filter");
//        TileEntity.addMapping(TileEntityGratedHopper.class, "duct_filter");
//        
//       
//        
//        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MGHouseBlocks.duct,16,0), new Object[] {
//            Boolean.valueOf(true), 
//            "igi", 
//            "g g", 
//            "igi",
//            Character.valueOf('i'), Items.iron_ingot,
//            Character.valueOf('g'), Blocks.glass 
//        }));
//        
//        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MGHouseBlocks.duct_filter,1,0), new Object[] {
//            Boolean.valueOf(true), 
//            "   ", 
//            " d ", 
//            " r ",
//            Character.valueOf('r'), Items.redstone,
//            Character.valueOf('d'), MGHouseBlocks.duct 
//        }));
//        
//        handler.registerClass(TileEntityDuct.class, ContainerHopperDuct.class, GuiHopperDuct.class);
//        handler.registerClass(TileEntityGratedHopper.class, ContainerGratedHopper.class, GuiGratedHopper.class);
//    }
	
//	public void registerCraftpedia(FMLPreInitializationEvent event){
//		Configuration cfg = new Configuration(event.getSuggestedConfigurationFile());
//		cfg.load();
////		radius = cfg.get("Xray-Variables", "radius", 45, "Radius for X-ray")
////				.getInt();
////		toggleXray = cfg.get("Xray-Variables", "toggleXray", false,
////				"X-ray enabled on start-up?").getBoolean(false);
//		cfg.save();		
//	}
	
	
	@SubscribeEvent
	public void keyboardEvent(InputEvent.KeyInputEvent key) {
		
//		if (!(Minecraft.getMinecraft().currentScreen instanceof GuiScreen)) {
////			if (this.toggleXrayBinding.getIsKeyPressed()) {
////				toggleXray = !(toggleXray);
////				if (toggleXray)
////					cooldownTicks = 0;
////				else
////					GL11.glDeleteLists(displayListid, 1);
////			}
//
//			if (this.toggleXrayGui.getIsKeyPressed())
//				CraftpediaGui.show();
//		}
	}
}
