package net.minecraft.mangrove.mod.vehicles.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.mangrove.mod.vehicles.MGVehiclesForge;
import net.minecraft.mangrove.mod.vehicles.proxy.IKeyControlable.ID;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;

public class VehicleJKeyHandler {
    public static KeyBinding keyFloatMode = new KeyBinding("key.floatMode", Keyboard.KEY_F, MGVehiclesForge.ID);
    public static KeyBinding keyCollectMode = new KeyBinding("key.collectNearby", Keyboard.KEY_H,  MGVehiclesForge.ID);
    public static KeyBinding digAroundMode = new KeyBinding("key.digaround", Keyboard.KEY_J,  MGVehiclesForge.ID);
    public static KeyBinding keyIncSpeed = new KeyBinding("key.incSpeed", Keyboard.KEY_ADD,  MGVehiclesForge.ID);
    public static KeyBinding keyDecSpeed = new KeyBinding("key.decSpeed", Keyboard.KEY_MINUS,  MGVehiclesForge.ID);
    
    private Minecraft mc;
    private GameSettings gameSettings;
    
    public VehicleJKeyHandler() {
        this.mc=Minecraft.getMinecraft();
        this.gameSettings=this.mc.gameSettings;
        
        ClientRegistry.registerKeyBinding(keyFloatMode);
        ClientRegistry.registerKeyBinding(keyCollectMode);
        ClientRegistry.registerKeyBinding(keyIncSpeed);
        ClientRegistry.registerKeyBinding(keyDecSpeed);
    }
    
    @SubscribeEvent
    public void onKeyPress(InputEvent.KeyInputEvent event){
        if(FMLClientHandler.instance().isGUIOpen(GuiChat.class) || mc.currentScreen != null){
            return;
        }
        final EntityPlayer player=this.mc.thePlayer;
        if( player==null){
            return;
        }
        final Entity entity=player.ridingEntity!=null?player.ridingEntity:null;
        if( entity==null){
            return;
        }
        if (entity instanceof IKeyControlable) {
            IKeyControlable controlable = (IKeyControlable) entity;
            if(mc.gameSettings.keyBindForward.isPressed()){//if(accelerateKey.isPressed())
                controlable.pressKey(ID.ACCELERATE, player);
            }
            if(mc.gameSettings.keyBindBack.isPressed()){//if(decelerateKey.isPressed())
                controlable.pressKey(ID.DECELERATE, player);
            }
            if(mc.gameSettings.keyBindLeft.isPressed()){//if(leftKey.isPressed())
                controlable.pressKey(ID.LEFT, player);
            }
            if(mc.gameSettings.keyBindRight.isPressed()){//if(rightKey.isPressed())
                controlable.pressKey(ID.RIGHT, player);
            }
            if(mc.gameSettings.keyBindJump.isPressed()){//if(upKey.isPressed())
                controlable.pressKey(ID.JUMP, player);
            }
            if(mc.gameSettings.keyBindSneak.isPressed()){//if(exitKey.isPressed())
                controlable.pressKey(ID.SNEAK, player);
            }
            if(mc.gameSettings.keyBindInventory.isPressed() /*|| inventoryKey.isPressed()*/){
                controlable.pressKey(ID.INVENTORY, player);
            }
            
            if(keyFloatMode.isPressed()){
                controlable.pressKey(ID.FLOAT, player);
            }
            if(keyCollectMode.isPressed()){
                controlable.pressKey(ID.COLLECT, player);
            }
            if(digAroundMode.isPressed()){
                controlable.pressKey(ID.DIG, player);
            }
            
//          if(mc.gameSettings.keyBindSprint.isPressed()){
                if(keyIncSpeed.isPressed()){
                    controlable.pressKey(ID.INC_SPEED, player);
                }else if(keyDecSpeed.isPressed()){
                    controlable.pressKey(ID.DEC_SPEED, player);
                }
//            }
            
        }        
    }
}
