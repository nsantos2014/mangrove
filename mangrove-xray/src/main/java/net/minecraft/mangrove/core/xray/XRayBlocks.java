/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package net.minecraft.mangrove.core.xray;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;

public class XRayBlocks {
	public static ArrayList<XRayBlocks> blocks = new ArrayList();
	public int r;
	public int g;
	public int b;
	public int a;
	public int meta;
	public String id = "";
	public boolean enabled = true;

	public XRayBlocks() {
	}

	public XRayBlocks(int r, int g, int b, int a, int meta, String id,
			boolean enabled) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
		this.id = id;
		this.meta = meta;
		this.enabled = enabled;
	}

	public String toString() {
		return this.r + " " + this.g + " " + this.b + " " + this.a + " "
				+ this.meta + " " + this.id + " " + this.enabled;
	}

	public static XRayBlocks fromString(String s) {
		XRayBlocks result = new XRayBlocks();
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

	public static void setStandardList() {
		ArrayList block = new ArrayList();
		block.add(new XRayBlocks(0, 0, 128, 200, -1, "minecraft:lapis_ore",
				true));
		block.add(new XRayBlocks(255, 0, 0, 200, -1, "minecraft:redstone_ore",
				true));
		block.add(new XRayBlocks(255, 255, 0, 200, -1, "minecraft:gold_ore",
				true));
		block.add(new XRayBlocks(0, 255, 0, 200, -1, "minecraft:emerald_ore",
				true));
		block.add(new XRayBlocks(0, 191, 255, 200, -1, "minecraft:diamond_ore",
				true));
		block.add(new XRayBlocks(0, 191, 128, 0, -1, "minecraft:coal_ore",
                true));
		block.add(new XRayBlocks(0, 200, 128, 0, -1, "minecraft:iron_ore",
                true));
		blocks = block;
		try {
			save();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void removeInvalidBlocks() {
		for (int i = 0; i < blocks.size(); ++i) {
			XRayBlocks block = (XRayBlocks) blocks.get(i);
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
		setStandardList();
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
			bw.write(((XRayBlocks) blocks.get(i)).toString());
			bw.newLine();
		}
		bw.flush();
		bw.close();
	}
}