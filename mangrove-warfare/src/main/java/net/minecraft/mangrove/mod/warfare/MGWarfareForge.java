package net.minecraft.mangrove.mod.warfare;

import net.minecraft.item.Item;
import net.minecraft.mangrove.GUIHandler;
import net.minecraft.mangrove.mod.warfare.proxy.CommonProxy;
import net.minecraft.mangrove.mod.warfare.rifle.ItemBlasterRifle;
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
        GameRegistry.registerItem(MGWarfareItems.blasterRifle, "Blaster Rifle");
        GameRegistry.registerItem(MGWarfareItems.x_helmet, "helmetX");
        GameRegistry.registerItem(MGWarfareItems.x_boots, "bootsX");
        GameRegistry.registerItem(MGWarfareItems.x_chestplate, "chestplateX");
        GameRegistry.registerItem(MGWarfareItems.x_leggings, "leggingsX");
    }
    
}
