/**
 * Copyright (c) 2011-2014, SpaceToad and the BuildCraft Team
 * http://www.mod-buildcraft.com
 *
 * BuildCraft is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package net.minecraft.mangrove.core.gui.widgets;


import java.io.DataInputStream;
import java.io.IOException;

import net.minecraft.inventory.ICrafting;
import net.minecraft.mangrove.core.gui.MGContainer;
import net.minecraft.mangrove.core.gui.MGGui;
import net.minecraft.mangrove.core.gui.tooltip.IToolTipProvider;
import net.minecraft.mangrove.core.gui.tooltip.ToolTip;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class Widget implements IToolTipProvider {

	public final int x;
	public final int y;
	public final int u;
	public final int v;
	public final int w;
	public final int h;
	public boolean hidden;
	protected MGContainer container;

	public Widget(int x, int y, int u, int v, int w, int h) {
		this.x = x;
		this.y = y;
		this.u = u;
		this.v = v;
		this.w = w;
		this.h = h;
	}

	public void addToContainer(MGContainer container) {
		this.container = container;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public final boolean isMouseOver(int mouseX, int mouseY) {
		return mouseX >= x - 1 && mouseX < x + w + 1 && mouseY >= y - 1 && mouseY < y + h + 1;
	}

	@SideOnly(Side.CLIENT)
	public boolean handleMouseClick(int mouseX, int mouseY, int mouseButton) {
		return false;
	}

	@SideOnly(Side.CLIENT)
	public void handleMouseRelease(int mouseX, int mouseY, int eventType) {
	}

	@SideOnly(Side.CLIENT)
	public void handleMouseMove(int mouseX, int mouseY, int mouseButton, long time) {
	}

	@SideOnly(Side.CLIENT)
	public void handleClientPacketData(DataInputStream data) throws IOException {
	}

	@SideOnly(Side.CLIENT)
	public void draw(MGGui gui, int guiX, int guiY, int mouseX, int mouseY) {
		gui.drawTexturedModalRect(guiX + x, guiY + y, u, v, w, h);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public ToolTip getToolTip() {
		return null;
	}

	@Override
	public boolean isToolTipVisible() {
		return true;
	}

	public void initWidget(ICrafting player) {
	}

	public void updateWidget(ICrafting player) {
	}
}