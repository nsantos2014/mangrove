package net.minecraft.mangrove.mod.warfare;

import static net.minecraftforge.common.ChestGenHooks.STRONGHOLD_CORRIDOR;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.mangrove.mod.warfare.proxy.CommonProxy;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.ShapedOreRecipe;

@Mod(modid = MGWarfareForge.ID, name = MGWarfareForge.NAME, version = MGWarfareForge.VERSION, useMetadata = false)
public class MGWarfareForge {
    public static final String ID = "mgwarfare";
    public static final String NAME = "Mangrove Warfare";
    public static final String VERSION = "8.0.1";
    public static final String CLIENT_SIDE_PROXY = "net.minecraft.mangrove.mod.warfare.proxy.ClientProxy";
    public static final String SERVER_SIDE_PROXY = "net.minecraft.mangrove.mod.warfare.proxy.CommonProxy";

    @Instance(MGWarfareForge.ID)
    public static MGWarfareForge instance;
    @SidedProxy(clientSide = MGWarfareForge.CLIENT_SIDE_PROXY, serverSide = MGWarfareForge.SERVER_SIDE_PROXY)
    public static CommonProxy proxy;
//    public static GUIHandler handler = new GUIHandler();
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event){
    	MGWarfareItems.preInit();
    }
    @EventHandler
    public void init(FMLInitializationEvent event) {
    	MGWarfareItems.registerSurpass(event);
    	MGWarfareItems.registerArmor(event);
    	
    	ChestGenHooks.addItem(ChestGenHooks.BONUS_CHEST, new WeightedRandomChestContent(new ItemStack(MGWarfareItems.woodenSurpass), 1, 21, 521){
    		@Override
    		protected ItemStack[] generateChestContent(Random random, IInventory newInventory) {
    			int size = newInventory.getSizeInventory();
    			for( int i=0; i< size; i++){
    				ItemStack iStack=newInventory.getStackInSlot(i);
    				if( iStack.getItem()==MGWarfareItems.woodenSurpass){
    					return new ItemStack[0];		
    				}
    			}
    			return new ItemStack[]{new ItemStack(MGWarfareItems.woodenSurpass)};
    		}
    	});
    	
    	ChestGenHooks.addItem(ChestGenHooks.BONUS_CHEST, new WeightedRandomChestContent(new ItemStack(MGWarfareItems.woodenSurpass), 1, 91, 5121){
    		@Override
    		protected ItemStack[] generateChestContent(Random random, IInventory newInventory) {
    			int size = newInventory.getSizeInventory();
    			for( int i=0; i< size; i++){
    				ItemStack iStack=newInventory.getStackInSlot(i);
    				if( iStack!=null && iStack.getItem()==MGWarfareItems.woodenSurpass){
    					return new ItemStack[0];		
    				}
    			}
    			return new ItemStack[]{new ItemStack(MGWarfareItems.woodenSurpass)};
    		}
    	});
    	
//		ChestGenHooks.getInfo(ChestGenHooks.BONUS_CHEST).addItem(surplus);
//        registerXArmor();
//        GameRegistry.registerItem(MGWarfareItems.blasterRifle, "Blaster Rifle");
    }

    private void registerSurpass() {
    	
    	
        //GameRegistry.registerItem(MGWarfareItems.surpass, "Surpass");
        
        
    }
    
//    private void registerXArmor() {        
//        GameRegistry.registerItem(MGWarfareItems.x_helmet, "helmetX");
//        GameRegistry.registerItem(MGWarfareItems.x_boots, "bootsX");
//        GameRegistry.registerItem(MGWarfareItems.x_chestplate, "chestplateX");
//        GameRegistry.registerItem(MGWarfareItems.x_leggings, "leggingsX");
//        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MGWarfareItems.x_helmet), new Object[] {
//            Boolean.valueOf(true), 
//            "   ", 
//            " d ", 
//            " r ",
//            Character.valueOf('d'), Items.iron_helmet, 
//            Character.valueOf('r'), Items.redstone 
//         }));
//        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MGWarfareItems.x_boots), new Object[] {
//            Boolean.valueOf(true), 
//            "   ", 
//            " d ", 
//            " r ",
//            Character.valueOf('d'), Items.iron_boots, 
//            Character.valueOf('r'), Items.redstone 
//         }));
//        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MGWarfareItems.x_chestplate), new Object[] {
//            Boolean.valueOf(true), 
//            "   ", 
//            " d ", 
//            " r ",
//            Character.valueOf('d'), Items.iron_chestplate, 
//            Character.valueOf('r'), Items.redstone
//         }));
//        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MGWarfareItems.x_leggings), new Object[] {
//            Boolean.valueOf(true), 
//            "   ", 
//            " d ", 
//            " r ",
//            Character.valueOf('d'), Items.iron_leggings, 
//            Character.valueOf('r'), Items.redstone
//         }));
//    }
}
