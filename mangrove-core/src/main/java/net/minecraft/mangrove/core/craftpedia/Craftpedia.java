/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package net.minecraft.mangrove.core.craftpedia;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.RegistryNamespaced;

public class Craftpedia {
public static final Craftpedia instance=new Craftpedia();
	
//	private KeyBinding toggleXrayBinding;
	private KeyBinding toggleXrayGui;
	
	public static ArrayList<Craftpedia> blocks = new ArrayList();
	
	public int r;
	public int g;
	public int b;
	public int a;
	public int meta;
	public String id = "";
	public boolean enabled = true;

	private Craftpedia() {
		this.toggleXrayGui = new KeyBinding("Toggle Xray-Gui", Keyboard.KEY_F1, "Craftpedia");
	}
	public void registerKeys(){
		ClientRegistry.registerKeyBinding(this.toggleXrayGui);
	}
	
	@SubscribeEvent
	public void keyboardEvent(InputEvent.KeyInputEvent key) {
		if (!(Minecraft.getMinecraft().currentScreen instanceof GuiScreen)) {
//			if (this.toggleXrayBinding.getIsKeyPressed()) {
//				toggleXray = !(toggleXray);
//				if (toggleXray)
//					cooldownTicks = 0;
//				else
//					GL11.glDeleteLists(displayListid, 1);
//			}

			if (this.toggleXrayGui.getIsKeyPressed())
				CraftpediaGui.show();
		}
	}
//	public Craftpedia(int r, int g, int b, int a, int meta, String id,
//			boolean enabled) {
//		this.r = r;
//		this.g = g;
//		this.b = b;
//		this.a = a;
//		this.id = id;
//		this.meta = meta;
//		this.enabled = enabled;
//	}

	public String toString() {
		return this.r + " " + this.g + " " + this.b + " " + this.a + " "
				+ this.meta + " " + this.id + " " + this.enabled;
	}

	public static Craftpedia fromString(String s) {
		Craftpedia result = new Craftpedia();
		String[] info = s.split(" ");
		result.r = Integer.parseInt(info[0]);
		result.g = Integer.parseInt(info[1]);
		result.b = Integer.parseInt(info[2]);
		result.a = Integer.parseInt(info[3]);
		result.meta = Integer.parseInt(info[4]);
		result.id = info[5];
		result.enabled = Boolean.parseBoolean(info[6]);
		return result;
	}

//	public static void setStandardList() {
//		ArrayList block = new ArrayList();
//		block.add(new Craftpedia(0, 0, 128, 200, -1, "minecraft:lapis_ore",
//				true));
//		block.add(new Craftpedia(255, 0, 0, 200, -1, "minecraft:redstone_ore",
//				true));
//		block.add(new Craftpedia(255, 255, 0, 200, -1, "minecraft:gold_ore",
//				true));
//		block.add(new Craftpedia(0, 255, 0, 200, -1, "minecraft:emerald_ore",
//				true));
//		block.add(new Craftpedia(0, 191, 255, 200, -1, "minecraft:diamond_ore",
//				true));
//
//		blocks = block;
//		try {
//			save();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}

	public static void removeInvalidBlocks() {
		for (int i = 0; i < blocks.size(); ++i) {
			Craftpedia block = (Craftpedia) blocks.get(i);
			if (Block.blockRegistry.containsKey(block.id))
				continue;
			blocks.remove(block);
		}
	}

	public static void init() {
		try {
			load();
		} catch (Exception e) {
			e.printStackTrace();
		}
		removeInvalidBlocks();
		if (blocks.size() != 0)
			return;
//		setStandardList();
	}

	private static void load() throws Exception {
		File toLoad = new File(Minecraft.getMinecraft().mcDataDir,"XRayBlocks.dat");
		if ((toLoad.exists()) && (!(toLoad.isDirectory()))) {
			ArrayList block = new ArrayList();
			BufferedReader br = new BufferedReader(new FileReader(toLoad));
			String s;
			for (; (s = br.readLine()) != null; block.add(fromString(s)))
				;
			br.close();
			blocks = block;
		}
	}

	static void save() throws IOException {
		File toSave = new File(Minecraft.getMinecraft().mcDataDir,"XRayBlocks.dat");
		if (toSave.exists()) {
			toSave.delete();
		}
		BufferedWriter bw = new BufferedWriter(new FileWriter(toSave));
		for (int i = 0; i < blocks.size(); ++i) {
			bw.write(((Craftpedia) blocks.get(i)).toString());
			bw.newLine();
		}
		bw.flush();
		bw.close();
	}
}