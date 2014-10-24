package net.minecraft.mangrove.core.xray;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.event.RenderWorldLastEvent;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class XRayBlocks {
	public static final XRayBlocks instance = new XRayBlocks();
	private Minecraft mc;

	public String[] blockList = new String[] { "minecraft:stone",
			"minecraft:grass", "minecraft:dirt", "minecraft:cobblestone",
			"minecraft:bedrock", "minecraft:sand", "minecraft:gravel",
			"minecraft:sandstone", "minecraft:netherrack",
			"minecraft:flowing_water", "minecraft:water",
			"minecraft:snow_layer", "minecraft:ice", "minecraft:snow" };
	// public List<KeyBinding> keyBinds = new ArrayList<KeyBinding>();
	public String currentBlocklistName = "DefaultBlockList";
	public boolean toggleXRay = false;
	public boolean toggleCaveFinder = false;
	private Boolean FirstTick = false;

	public static int radius = 45;
	public int displayListid = 0;
	public int cooldownTicks = 0;

	private final KeyBinding xRayKeyBinding;
	private final KeyBinding caveFindKeyBinding;

	public XRayBlocks() {
		this.mc = Minecraft.getMinecraft();

		this.xRayKeyBinding = new KeyBinding("Toggle X-ray", Keyboard.KEY_X,
				"X-ray Mod");
		this.caveFindKeyBinding = new KeyBinding("Toggle Cave Finder",
				Keyboard.KEY_V, "X-ray Mod");
	}

	public void registerKeys() {
		ClientRegistry.registerKeyBinding(this.xRayKeyBinding);
		ClientRegistry.registerKeyBinding(this.caveFindKeyBinding);
	}

	// Tick stuff

	public void onTick(boolean inGame) {
		// if ((minecraftInstance.inGameHasFocus) && (inGame)) {
		// if (FirstTick == false) {
		// FirstTick = true;
		// }
		// if (this.xRayKeyBinding.isPressed()) { // X-ray key
		// if (!Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)
		// && !Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)) {
		// printLineInLog("Toggling X-ray...");
		// toggleXRay = !toggleXRay;
		// toggleCaveFinder = false;
		// // Now refresh the world...
		// minecraftInstance.renderGlobal.loadRenderers();
		// } else {
		// printLineInLog("Displaying menu...");
		// // Display GUI...
		// minecraftInstance.displayGuiScreen(new XrayModMainGui(null,
		// minecraftInstance.gameSettings));
		// }
		// }
		// if (this.caveFindKeyBinding.isPressed()) { // Cave finder key
		// printLineInLog("Toggling Cave Finder...");
		// toggleCaveFinder = !toggleCaveFinder;
		// toggleXRay = false;
		// // Now refresh the world...
		// minecraftInstance.renderGlobal.loadRenderers();
		// }
		// }
	}

	@SubscribeEvent
	public void keyboardEvent(InputEvent.KeyInputEvent key) {
		System.out.println("Keyboard event : " + key);
		if (!(Minecraft.getMinecraft().currentScreen instanceof GuiScreen)) {
			// if (this.toggleXrayBinding.getIsKeyPressed()) {
			// toggleXray = !(toggleXray);
			// if (toggleXray)
			// cooldownTicks = 0;
			// else
			// GL11.glDeleteLists(displayListid, 1);
			// }

			if (this.xRayKeyBinding.getIsKeyPressed()) {
				printLineInLog("Toggling X-ray...");
				toggleXRay = !toggleXRay;
				if (toggleXRay)
					cooldownTicks = 0;
				else
					GL11.glDeleteLists(displayListid, 1);
				toggleCaveFinder = false;
				mc.renderGlobal.loadRenderers();
			}
			if (this.caveFindKeyBinding.isPressed()) {
				printLineInLog("Toggling Cave Finder...");
				toggleCaveFinder = !toggleCaveFinder;
				toggleXRay = false;
				// Now refresh the world...
				mc.renderGlobal.loadRenderers();
			}

		}
	}

	@SubscribeEvent
	public boolean onTickInGame(TickEvent.ClientTickEvent e) {

		if ((!(toggleXRay)) || (this.mc.theWorld == null))
			return true;
		if (cooldownTicks < 1) {
			compileDL();
			cooldownTicks = 80;
		}
		cooldownTicks -= 1;
		return true;
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
					if (bId != Blocks.stone){
						if( bId==Blocks.iron_ore){
							RenderBlocks renderBlocks = new RenderBlocks(world);
							   renderBlocks.renderBlockByRenderType(bId, i, k, j);
						}
//						for (XRayBlocks block : XRayBlocks.blocks) {
//							if (block.enabled)
//								;
//							Block blocki = (Block) Block.blockRegistry
//									.getObject(block.id);
//							if ((blocki == bId)
//									&& (((block.meta == -1) || (block.meta == world
//											.getBlockMetadata(i, k, j))))) {
//								renderBlock(i, k, j, block);
//								break;
//							}
//						}
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

//	private void renderBlock(int x, int y, int z, XRayBlocks block) {
//		GL11.glColor4ub((byte) block.r, (byte) block.g, (byte) block.b,
//				(byte) block.a);
//
//		GL11.glVertex3f(x, y, z);
//		GL11.glVertex3f(x + 1, y, z);
//
//		GL11.glVertex3f(x + 1, y, z);
//		GL11.glVertex3f(x + 1, y, z + 1);
//
//		GL11.glVertex3f(x, y, z);
//		GL11.glVertex3f(x, y, z + 1);
//
//		GL11.glVertex3f(x, y, z + 1);
//		GL11.glVertex3f(x + 1, y, z + 1);
//
//		GL11.glVertex3f(x, y + 1, z);
//		GL11.glVertex3f(x + 1, y + 1, z);
//
//		GL11.glVertex3f(x + 1, y + 1, z);
//		GL11.glVertex3f(x + 1, y + 1, z + 1);
//
//		GL11.glVertex3f(x, y + 1, z);
//		GL11.glVertex3f(x, y + 1, z + 1);
//
//		GL11.glVertex3f(x, y + 1, z + 1);
//		GL11.glVertex3f(x + 1, y + 1, z + 1);
//
//		GL11.glVertex3f(x, y, z);
//		GL11.glVertex3f(x, y + 1, z);
//
//		GL11.glVertex3f(x, y, z + 1);
//		GL11.glVertex3f(x, y + 1, z + 1);
//
//		GL11.glVertex3f(x + 1, y, z);
//		GL11.glVertex3f(x + 1, y + 1, z);
//
//		GL11.glVertex3f(x + 1, y, z + 1);
//		GL11.glVertex3f(x + 1, y + 1, z + 1);
//	}

	@SubscribeEvent
	public void renderWorldLastEvent(RenderWorldLastEvent evt) {
		if ((!(toggleXRay)) || (Minecraft.getMinecraft().theWorld == null))
			return;
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

	// I/O stuff

	public void loadBlockList(String blockListName) {
		printLineInLog("Please wait, loading Block List name: " + blockListName);
		try {
			currentBlocklistName = blockListName;
			String[] blockListBuffer = new String[4096];
			File blockListFile = new File(mc.mcDataDir.getPath()
					+ File.separator + "XRayProfiles", blockListName
					+ ".XRayProfileNew");
			if (blockListFile.exists()) {
				printLineInLog("The block list exists! Loading block list...");
				BufferedReader currentBufferedReader = new BufferedReader(
						new FileReader(blockListFile));
				String currentLine;
				int i;
				for (i = 0; (currentLine = currentBufferedReader.readLine()) != null; ++i) {
					blockListBuffer[i] = currentLine;
				}
				currentBufferedReader.close();
				blockList = new String[i];
				System.arraycopy(blockListBuffer, 0, blockList, 0, i);
				printLineInLog("Read complete!");
			} else {
				printLineInLog("Oh, the block list doesn't exist... Oh well!");
			}
		} catch (Exception currentException) {
			printLineInLog("Oops, looks like there was an error reading the block list! Printing stack trace now...");
			currentException.printStackTrace();
		}
	}

	public void saveBlockList(String blockListName) {
		printLineInLog("Please wait, saving block list name: " + blockListName);
		try {
			currentBlocklistName = blockListName;
			File blockListFolder = new File(mc.mcDataDir, "XRayProfiles");
			boolean canMakeBlockListFolder = blockListFolder.mkdir();
			File blockListFile = new File(blockListFolder, blockListName
					+ ".XRayProfileNew");
			BufferedWriter currentBufferedWriter = new BufferedWriter(
					new FileWriter(blockListFile));
			String[] blockListBuffer = blockList;
			int blockListLength = blockList.length;
			for (int i = 0; i < blockListLength; ++i) {
				currentBufferedWriter.write(blockListBuffer[i] + "\r\n");
			}
			currentBufferedWriter.close();
			printLineInLog("Write complete!");
		} catch (Exception currentException) {
			printLineInLog("Oops, looks like there was an error writing the block list! Printing stack trace now...");
			currentException.printStackTrace();
		}
	}

	// Make things bright stuff

	public void disableBrightLight() {
		// minecraftInstance.thePlayer.removePotionEffect( 16 );
		// minecraftInstance.gameSettings.gammaSetting = 0.2F;
	}

	public void enableBrightLight() {
		// minecraftInstance.thePlayer.addPotionEffect( new PotionEffect( 16,
		// 99999999, 255, true ) );
		// minecraftInstance.gameSettings.gammaSetting = 782;
	}

	// Check then render blocks stuff

	/*
	 * NOTE: Boolean (the object) throws an exception on null. Don't use that!
	 * 
	 * Confused on what this means? b means the side is not going to be
	 * rendered. (aka false) a means the side is going to be rendered. (aka
	 * true) c means the side will be processed by normal means.
	 * 
	 * Still confused? Too bad.
	 */
	public static char blockIsInBlockList(Block currentBlock) {
		if (instance.toggleXRay || instance.toggleCaveFinder) {
			String blockID = Block.blockRegistry.getNameForObject(currentBlock);
			String[] blockListBuffer = instance.blockList;
			int blockListLength = blockListBuffer.length;
			int i;
			String currentID;
			for (i = 0; i < blockListLength; ++i) {
				currentID = blockListBuffer[i];
				if (currentID.equals(blockID)) { // You must use .equals(), not
													// ==, that screwed me over
													// e_e
					if (instance.toggleCaveFinder) { // Only display stone in
														// cave finder
						// mode,
						if (!(blockID.equals("minecraft:stone"))) { // Ignore
																	// stone,
																	// use
																	// normal
																	// behavior
																	// (will be
																	// broken in
																	// 1.8)
							return 'b'; // Don't display this side
						}
					} else {
						return 'b';// Don't display this side
					}
				}
			}
			if (!instance.toggleCaveFinder) { // We want the normal behavior on
												// cave
				// finder (will be broken in 1.8)
				if (blockListLength != 0) { // Nothing in the list, young lads.
					return 'a'; // Display if detected
				}
			}
		}
		return 'c'; // Normal behavior
	}

	// Misc stuff

	public static void printLineInLog(String lineToPrint) {
		System.out.println("[UyjulianXray] " + lineToPrint);
	}

	public static void putLineInChat(String lineToPrint) {
		instance.mc.thePlayer.addChatMessage(new ChatComponentText(
				"§l§o§6[UyjulianXray]§r " + lineToPrint));
	}
}
