/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package net.minecraft.mangrove.core.xray;

import java.util.ArrayList;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.RegistryNamespaced;

public class XRayBlockSlot extends GuiSlot {
	int selectedIndex = -1;
	XRayGui xrayGui;

	public XRayBlockSlot(Minecraft par1Minecraft, int width, int height,
			int top, int bottom, int slotHeight, XRayGui xrayGui) {
		super(par1Minecraft, width, height, top, bottom, slotHeight);
		this.xrayGui = xrayGui;

		XRayBlocks.init();
	}

	protected int getSize() {
		return XRayBlocks.blocks.size();
	}

	protected boolean isSelected(int i) {
		return (i == this.selectedIndex);
	}

	protected void drawBackground() {
	}

	protected void elementClicked(int i, boolean var2, int var3, int var4) {
		this.selectedIndex = i;
	}

	protected void drawSlot(int i, int j, int k, int var4,
			Tessellator var5, int var6, int var7) {
		XRayBlocks xblock = (XRayBlocks) XRayBlocks.blocks.get(i);
		XRayGui.drawRect(175 + j, 1 + k, this.xrayGui.width - j
				- 20, 15 + k, ((0xC800 | xblock.r) << 8 | xblock.g) << 8
				| xblock.b);
		if ((xblock.id == null)
				|| (!(Block.blockRegistry.containsKey(xblock.id))))
			return;
		this.xrayGui.drawString(this.xrayGui.render,((Block) Block.blockRegistry.getObject(xblock.id)).getLocalizedName(), j + 2, k + 1, 16777215);
	}
}