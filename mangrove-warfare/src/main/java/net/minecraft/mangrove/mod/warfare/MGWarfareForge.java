package net.minecraft.mangrove.mod.warfare;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.mangrove.GUIHandler;
import net.minecraft.mangrove.mod.warfare.proxy.CommonProxy;
import net.minecraft.mangrove.mod.warfare.rifle.ItemBlasterRifle;
import net.minecraftforge.oredict.ShapedOreRecipe;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = MGWarfareForge.ID, name = MGWarfareForge.NAME, version = MGWarfareForge.VERSION, useMetadata = false)
public class MGWarfareForge {
    public static final String ID = "Mangrove|Warefare";
    public static final String NAME = "Mangrove Warefare";
    public static final String VERSION = "0.0.1";
    public static final String CLIENT_SIDE_PROXY = "net.minecraft.mangrove.mod.warfare.proxy.ClientProxy";
    public static final String SERVER_SIDE_PROXY = "net.minecraft.mangrove.mod.warfare.proxy.CommonProxy";

    @Instance(MGWarfareForge.ID)
    public static MGWarfareForge instance;
    @SidedProxy(clientSide = MGWarfareForge.CLIENT_SIDE_PROXY, serverSide = MGWarfareForge.SERVER_SIDE_PROXY)
    public static CommonProxy proxy;
    public static GUIHandler handler = new GUIHandler();

    @EventHandler
    public void load(FMLPreInitializationEvent event) {
        registerSurpass();
        registerXArmor();
//        GameRegistry.registerItem(MGWarfareItems.blasterRifle, "Blaster Rifle");
    }

    private void registerSurpass() {
        GameRegistry.registerItem(MGWarfareItems.surpass, "Surpass");
        
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MGWarfareItems.surpass,1,0), new Object[] {
                Boolean.valueOf(true), 
                "PwA", 
                "wsw", 
                "SwW",
                Character.valueOf('P'), Items.stone_pickaxe, 
                Character.valueOf('A'), Items.stone_axe,
                Character.valueOf('S'), Items.stone_shovel,
                Character.valueOf('W'), Items.stone_sword,
                Character.valueOf('s'), Blocks.stone,
                Character.valueOf('w'), Blocks.planks }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MGWarfareItems.surpass,1,1), new Object[] {
            Boolean.valueOf(true), 
            "PiA", 
            "isi", 
            "SiW",
            Character.valueOf('P'), Items.iron_pickaxe, 
            Character.valueOf('A'), Items.iron_axe,
            Character.valueOf('S'), Items.iron_shovel,
            Character.valueOf('W'), Items.iron_sword,
            
            Character.valueOf('i'), new ItemStack(Items.iron_ingot,4), 
            Character.valueOf('s'), new ItemStack(MGWarfareItems.surpass,1,0) 
         }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MGWarfareItems.surpass,1,2), new Object[] {
            Boolean.valueOf(true), 
            "PdA", 
            "dsd", 
            "SdW",
            Character.valueOf('P'), Items.diamond_pickaxe, 
            Character.valueOf('A'), Items.diamond_axe,
            Character.valueOf('S'), Items.diamond_shovel,
            Character.valueOf('W'), Items.diamond_sword,
            
            Character.valueOf('d'), Items.diamond, 
            Character.valueOf('s'), new ItemStack(MGWarfareItems.surpass,1,1) 
         }));
    }
    
    private void registerXArmor() {        
        GameRegistry.registerItem(MGWarfareItems.x_helmet, "helmetX");
        GameRegistry.registerItem(MGWarfareItems.x_boots, "bootsX");
        GameRegistry.registerItem(MGWarfareItems.x_chestplate, "chestplateX");
        GameRegistry.registerItem(MGWarfareItems.x_leggings, "leggingsX");
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MGWarfareItems.x_helmet), new Object[] {
            Boolean.valueOf(true), 
            "   ", 
            " d ", 
            " r ",
            Character.valueOf('d'), Items.iron_helmet, 
            Character.valueOf('r'), Items.redstone 
         }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MGWarfareItems.x_boots), new Object[] {
            Boolean.valueOf(true), 
            "   ", 
            " d ", 
            " r ",
            Character.valueOf('d'), Items.iron_boots, 
            Character.valueOf('r'), Items.redstone 
         }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MGWarfareItems.x_chestplate), new Object[] {
            Boolean.valueOf(true), 
            "   ", 
            " d ", 
            " r ",
            Character.valueOf('d'), Items.iron_chestplate, 
            Character.valueOf('r'), Items.redstone
         }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(MGWarfareItems.x_leggings), new Object[] {
            Boolean.valueOf(true), 
            "   ", 
            " d ", 
            " r ",
            Character.valueOf('d'), Items.iron_leggings, 
            Character.valueOf('r'), Items.redstone
         }));
    }
}
