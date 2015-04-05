package net.minecraft.mangrove.mod.thrive.probe;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MGProbeOverlay {

	
	private Minecraft mc;
	
	private double zLevel = 0;

	public MGProbeOverlay(Minecraft mc) {
		this.mc = mc;
		// TODO Auto-generated constructor stub
	}

	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void eventHandler(RenderGameOverlayEvent event) {
		
		if (event.isCancelable() || event.type != ElementType.EXPERIENCE) {
			return;
		}
		
		int width = this.mc.currentScreen.width;
		int height= this.mc.currentScreen.height;
		
		GlStateManager.disableLighting();
		GL11.glColor4d(1.0f, 1.0f, 1.0f, 1.0f);
		
		
		GlStateManager.enableLighting();
	}
	
	public void drawTexturedModalRect(int x, int y, int textureX, int textureY,
			int width, int height) {
		 float uScale = 0.00390625F;
		 float vScale = 0.00390625F;
//		float uScale = 1.0F;
//		float vScale = 1.0F;
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		worldrenderer.startDrawingQuads();
		worldrenderer.addVertexWithUV((double) (x + 0), (double) (y + height),
				(double) this.zLevel,
				(double) ((float) (textureX + 0) * uScale),
				(double) ((float) (textureY + height) * vScale));
		worldrenderer.addVertexWithUV((double) (x + width),
				(double) (y + height), (double) this.zLevel,
				(double) ((float) (textureX + width) * uScale),
				(double) ((float) (textureY + height) * vScale));
		worldrenderer.addVertexWithUV((double) (x + width), (double) (y + 0),
				(double) this.zLevel,
				(double) ((float) (textureX + width) * uScale),
				(double) ((float) (textureY + 0) * vScale));
		worldrenderer.addVertexWithUV((double) (x + 0), (double) (y + 0),
				(double) this.zLevel,
				(double) ((float) (textureX + 0) * uScale),
				(double) ((float) (textureY + 0) * vScale));
		tessellator.draw();
	}
}
