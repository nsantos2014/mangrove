package net.minecraft.mangrove.mod.hud;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import org.lwjgl.opengl.GL11;

public class HudController {

	private final Minecraft mc;

	private static final int BUFF_ICON_SIZE = 18;
	private static final int BUFF_ICON_SPACING = BUFF_ICON_SIZE + 2; // 2 pixels
																		// between
																		// buff
																		// icons
	private static final int BUFF_ICON_BASE_U_OFFSET = 0;
	private static final int BUFF_ICON_BASE_V_OFFSET = 198;
	private static final int BUFF_ICONS_PER_ROW = 8;

	private static String[] moonPhases = { "Full Moon", "Waning Gibbous",
			"Last Quarter", "Waning Crescent", "New Moon", "Waxing Crescent",
			"First Quarter", "Waxing Gibbous" };

	public HudController(Minecraft mc) {
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
			ArrayList<String> left = new ArrayList<String>();
			Chunk chunk = this.mc.theWorld.getChunkFromBlockCoords(new BlockPos(x,0, z));
			left.add(String.format("[%s]", EnumFacing.getHorizontal(heading)));
			left.add(String.format("x: %d ", x));
			left.add(String.format("y: %d ", y));
			left.add(String.format("z: %d ", z));
			long time = world.getWorldTime();
			int moonPhase = world.getMoonPhase();

			long days = time / 24000;
			long dayticks = time % 24000;
			long hours = ((dayticks/ 1000)) % 24;
			long minutes = (dayticks % 1000) * 6 / 100;
//			long seconds= (dayticks % 1000) * 6 % 100;
			left.add(String.format("Days: %02d %s", days, moonPhases[world.getMoonPhase()]));

			left.add(String.format("Time: %02d:%02d",(7+hours)%24, minutes));
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
}
