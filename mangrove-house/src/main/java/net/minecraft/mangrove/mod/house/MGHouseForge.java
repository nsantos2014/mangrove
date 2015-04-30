package net.minecraft.mangrove.mod.house;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.mangrove.core.GUIHandler;
import net.minecraft.mangrove.core.MGCoreForge;
import net.minecraft.mangrove.mod.house.proxy.CommonProxy;
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
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, handler);
		MGHouseBlocks.init();
		MGHouseItems.init(event);
		
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
	public void registerGlowLadder(){
		GameRegistry.registerBlock(MGHouseBlocks.glow_ladder, "glow_ladder");
		GameRegistry.addRecipe(new ItemStack(MGHouseBlocks.glow_ladder,4), new Object[]{
            "T", 
            "L",
            'T', new ItemStack(Blocks.torch),
            'L', new ItemStack(Blocks.ladder)
		});
	}
	
	@SubscribeEvent
	public void keyboardEvent(InputEvent.KeyInputEvent key) {
	
	}
}
