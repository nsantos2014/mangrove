package net.minecraft.mangrove.core.xray;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.config.Configuration;

public class XRay {
    private int radius = 45;
    private KeyBinding toggleXrayBinding;
    private KeyBinding toggleXrayGui;
    private int displayListid = 0;
    private int cooldownTicks = 0;
    private boolean toggleXray = false;
    private final Minecraft mc;
    private Configuration cfg;
    
    public static final XRay instance=new XRay();
    
    private XRay() {
        this.mc=Minecraft.getMinecraft();        
    }
    public int getCooldownTicks() {
        return cooldownTicks;
    }
    public void setCooldownTicks(int cooldownTicks) {
        this.cooldownTicks = cooldownTicks;
    }
    
    
    public void preInit(FMLPreInitializationEvent event) {
        this.cfg = new Configuration(event.getSuggestedConfigurationFile());
    }
    
    public void init(FMLInitializationEvent event) {
        radius = cfg.get("Xray-Variables", "radius", 45, "Radius for X-ray").getInt();
        toggleXray = cfg.get("Xray-Variables", "toggleXray", false,"X-ray enabled on start-up?").getBoolean(false);
        cfg.save();
        this.toggleXrayBinding = new KeyBinding("Toggle Xray", Keyboard.KEY_X, "Xray");
        this.toggleXrayGui = new KeyBinding("Toggle Xray-Gui", Keyboard.KEY_F7, "Xray");

        ClientRegistry.registerKeyBinding(this.toggleXrayBinding);
        ClientRegistry.registerKeyBinding(this.toggleXrayGui);
        
        displayListid = GL11.glGenLists(5) + 3;
        XRayBlocks.init();
    }
    
    private void compileDL() {
        GL11.glNewList(displayListid, 4864);

        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);

        GL11.glBegin(1);
        WorldClient world = this.mc.theWorld;

        EntityClientPlayerMP player = this.mc.thePlayer;
        if ((world == null) || (player == null))
            return;
        for (int i = (int) player.posX - radius; i <= (int) player.posX
                + radius; ++i) {
            for (int j = (int) player.posZ - radius; j <= (int) player.posZ
                    + radius; ++j) {
                int k = 0;
                Block bId;
                for (int height = world.getHeightValue(i, j); k <= height; ++k) {
                    bId = world.getBlock(i, k, j);
                    if (bId == Blocks.air)
                        continue;
                    if (bId != Blocks.stone)
                        for (XRayBlocks block : XRayBlocks.blocks) {
                            
                            if (block.enabled){
                                Block blocki = (Block) Block.blockRegistry.getObject(block.id);
                                
                                if ((blocki == bId)
                                        && (((block.meta == -1) || (block.meta == world.getBlockMetadata(i, k, j))))) {
//                                    System.out.println("rednerBloc:"+blocki);
                                    renderBlock(i, k, j, block);
//                                    renderBlock(blocki, i, k, j);
                                    break;
                                }
                            }
                        }
                }
            }
        }
        GL11.glEnd();
        GL11.glEnable(2929);
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glEndList();
    }
    private void renderBlock(Block block,int x, int y, int z) {
        RenderBlocks renderer = RenderBlocks.getInstance();
        renderer.renderBlockAllFaces(block, x, y, z);
    }
    
    private void renderBlock(int x, int y, int z, XRayBlocks block) {
        GL11.glColor4ub((byte) block.r, (byte) block.g, (byte) block.b, (byte) block.a);

        GL11.glVertex3f(x, y, z);
        GL11.glVertex3f(x + 1, y, z);

        GL11.glVertex3f(x + 1, y, z);
        GL11.glVertex3f(x + 1, y, z + 1);

        GL11.glVertex3f(x, y, z);
        GL11.glVertex3f(x, y, z + 1);

        GL11.glVertex3f(x, y, z + 1);
        GL11.glVertex3f(x + 1, y, z + 1);

        GL11.glVertex3f(x, y + 1, z);
        GL11.glVertex3f(x + 1, y + 1, z);

        GL11.glVertex3f(x + 1, y + 1, z);
        GL11.glVertex3f(x + 1, y + 1, z + 1);

        GL11.glVertex3f(x, y + 1, z);
        GL11.glVertex3f(x, y + 1, z + 1);

        GL11.glVertex3f(x, y + 1, z + 1);
        GL11.glVertex3f(x + 1, y + 1, z + 1);

        GL11.glVertex3f(x, y, z);
        GL11.glVertex3f(x, y + 1, z);

        GL11.glVertex3f(x, y, z + 1);
        GL11.glVertex3f(x, y + 1, z + 1);

        GL11.glVertex3f(x + 1, y, z);
        GL11.glVertex3f(x + 1, y + 1, z);

        GL11.glVertex3f(x + 1, y, z + 1);
        GL11.glVertex3f(x + 1, y + 1, z + 1);
    }
    
    @SubscribeEvent
    public void keyboardEvent(InputEvent.KeyInputEvent key) {
//        System.out.println("Keyboard: "+toggleXray);
        if (!(this.mc.currentScreen instanceof GuiScreen)) {
            if (this.toggleXrayBinding.getIsKeyPressed()) {
                toggleXray = !(toggleXray);
                if (toggleXray)
                    cooldownTicks = 0;
                else
                    GL11.glDeleteLists(displayListid, 1);
            }

            if (this.toggleXrayGui.getIsKeyPressed())
                XRayGui.show();
        }
    }
    
    @SubscribeEvent
    public void renderWorldLastEvent(RenderWorldLastEvent evt) {
//        System.out.println("renderWorldLastEvent");
        if ((!(toggleXray)) || (this.mc.theWorld == null)){
            return;
        }
        
        double doubleX = this.mc.thePlayer.lastTickPosX
                + (this.mc.thePlayer.posX - this.mc.thePlayer.lastTickPosX)
                * evt.partialTicks;

        double doubleY = this.mc.thePlayer.lastTickPosY
                + (this.mc.thePlayer.posY - this.mc.thePlayer.lastTickPosY)
                * evt.partialTicks;

        double doubleZ = this.mc.thePlayer.lastTickPosZ
                + (this.mc.thePlayer.posZ - this.mc.thePlayer.lastTickPosZ)
                * evt.partialTicks;

        GL11.glPushMatrix();
        GL11.glTranslated(-doubleX, -doubleY, -doubleZ);
        GL11.glCallList(displayListid);
        GL11.glPopMatrix();
    }
    
    @SubscribeEvent
    public boolean onTickInGame(TickEvent.ClientTickEvent e) {
//       
        if ((!(toggleXray)) || (this.mc.theWorld == null)){
            //System.out.println("Tick: "+(!(toggleXray))+" || " +(this.mc.theWorld == null));
            return true;
        }
        if (cooldownTicks < 1) {
//            System.out.println("Compile ?????");
            compileDL();
            cooldownTicks = 80;
        }
        cooldownTicks -= 1;
        return true;
    }
}
