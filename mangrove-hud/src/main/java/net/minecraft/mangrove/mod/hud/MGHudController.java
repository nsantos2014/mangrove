package net.minecraft.mangrove.mod.hud;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.mangrove.mod.hud.minimap.Render;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import org.lwjgl.opengl.GL11;

public class MGHudController {

	private final Minecraft mc;

	private double zLevel = 0;

	private static final int BUFF_ICON_SIZE = 18;
	private static final int BUFF_ICON_SPACING = BUFF_ICON_SIZE + 2; // 2 pixels
																		// between
																		// buff
																		// icons
	private static final int BUFF_ICON_BASE_U_OFFSET = 0;
	private static final int BUFF_ICON_BASE_V_OFFSET = 198;
	private static final int BUFF_ICONS_PER_ROW = 8;

	private static final ResourceLocation MOON_PHASES = new ResourceLocation(
			"mghud", "textures/hud/hud.png");

	private static String[] moonPhases = { "Full Moon", "Waning Gibbous",
			"Last Quarter", "Waning Crescent", "New Moon", "Waxing Crescent",
			"First Quarter", "Waxing Gibbous" };

	public MGHudController(Minecraft mc) {
		this.mc = mc;
	}

	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void eventHandler(RenderGameOverlayEvent event) {
		//
		// We draw after the ExperienceBar has drawn. The event raised by
		// GuiIngameForge.pre()
		// will return true from isCancelable. If you call
		// event.setCanceled(true) in
		// that case, the portion of rendering which this event represents will
		// be canceled.
		// We want to draw *after* the experience bar is drawn, so we make sure
		// isCancelable() returns
		// false and that the eventType represents the ExperienceBar event.
		if (event.isCancelable() || event.type != ElementType.EXPERIENCE) {
			return;
		}

		WorldClient world = mc.theWorld;

		EntityPlayerSP player = mc.thePlayer;

		if (!mc.gameSettings.showDebugInfo && (world != null)
				&& (player != null)) {
			int x = MathHelper.floor_double(mc.thePlayer.posX);
			int y = MathHelper.floor_double(mc.thePlayer.posY);
			int z = MathHelper.floor_double(mc.thePlayer.posZ);
			int heading = MathHelper
					.floor_double((double) (mc.thePlayer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
			renderDirection(heading);
			renderClock(world, mc.fontRendererObj);

			ArrayList<String> left = new ArrayList<String>();
			Chunk chunk = this.mc.theWorld
					.getChunkFromBlockCoords(new BlockPos(x, 0, z));
			left.add(String.format("[%s]", EnumFacing.getHorizontal(heading)));

			left.add(String.format("x: %d ", x));
			left.add(String.format("y: %d ", y));
			left.add(String.format("z: %d ", z));

			FontRenderer fontRenderer = mc.fontRendererObj;
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glDisable(GL11.GL_LIGHTING);

			for (int i = 0; i < left.size(); i++) {
				String msg = left.get(i);
				if (msg == null)
					continue;
				fontRenderer.drawStringWithShadow(msg, 2, 2 + i * 10, 0xFFFFFF);
			}
		}
	}

	private void renderDirection(int heading) {
		EnumFacing faceDir = EnumFacing.getHorizontal(heading);

		GL11.glPushMatrix();
		this.mc.getTextureManager().bindTexture(MOON_PHASES);
		this.drawTexturedModalRect(0, 0, faceDir.ordinal()*16, 0, 16, 16);
		
		GL11.glTranslatef(100, 100, 0);
		GL11.glScalef(0.5f, 0.5f, 1.0f);
		int offset = 0;

		Render.drawCentredString(0, 0, 0, "[%s]", faceDir.getName());

		GL11.glPopMatrix();

	}

	private void renderClock(World world, FontRenderer fontRendererObj) {

		int moonPhase = world.getMoonPhase();
		long time = world.getWorldTime();
		long days = time / 24000;
		long dayticks = time % 24000;
		long hours = ((dayticks / 1000)) % 24;
		long minutes = (dayticks % 1000) * 6 / 100;
		// long seconds= (dayticks % 1000) * 6 % 100;
		GlStateManager.disableLighting();
		GL11.glPushMatrix();
		GL11.glTranslatef(100, 5, 0);
		GL11.glScalef(0.8f, 0.8f, 1.0f);
		GL11.glColor4d(1.0f, 1.0f, 1.0f, 1.0f);
		fontRendererObj.drawString(String.format("%04d days", days), 0, 0,
				EnumDyeColor.SILVER.getMapColor().colorValue, true);
		GL11.glPopMatrix();

		GL11.glPushMatrix();
		
		this.mc.getTextureManager().bindTexture(MOON_PHASES);
		this.drawTexturedModalRect(100, 20, 0, 20, 16, 16);
		
		GL11.glTranslatef(100, 20, 0);
		GL11.glScalef(0.8f, 0.8f, 1.0f);
		GL11.glColor4d(1.0f, 1.0f, 1.0f, 1.0f);
		fontRendererObj.drawString(
				String.format("%s", moonPhases[world.getMoonPhase()]), 0, 0,
				EnumDyeColor.SILVER.getMapColor().colorValue, true);
		GL11.glPopMatrix();

		GL11.glPushMatrix();
		GL11.glTranslatef(200, 5, 0);
		GL11.glScalef(2.0f, 2.0f, 1.0f);
		GL11.glColor4d(1.0f, 1.0f, 1.0f, 1.0f);
		fontRendererObj.drawString(
				String.format("%02d:%02d", (7 + hours) % 24, minutes), 0, 0,
				EnumDyeColor.LIGHT_BLUE.getMapColor().colorValue, true);

		GL11.glPopMatrix();
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
