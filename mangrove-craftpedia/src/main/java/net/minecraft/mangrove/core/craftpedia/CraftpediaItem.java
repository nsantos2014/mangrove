package net.minecraft.mangrove.core.craftpedia;

public class CraftpediaItem {
	public int r;
	public int g;
	public int b;
	public int a;
	public int meta;
	public String id = "";
	public boolean enabled = true;
	
	public String toString() {
		return this.r + " " + this.g + " " + this.b + " " + this.a + " "
				+ this.meta + " " + this.id + " " + this.enabled;
	}

	public static CraftpediaItem fromString(String s) {
		CraftpediaItem result = new CraftpediaItem();
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
}
