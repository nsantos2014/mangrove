/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package net.minecraft.mangrove.core.xray;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class XRayGui extends GuiScreen {
	XRayBlockSlot slot;
	GuiButton add;
	GuiButton del;
	GuiButton edit;
	GuiButton exit;
	FontRenderer render;

	public static void show() {
		Minecraft.getMinecraft().displayGuiScreen(new XRayGui());
	}

	public static void close() {
		Minecraft.getMinecraft().displayGuiScreen(null);
	}

	public void initGui() {
		super.initGui();
		this.render = this.fontRendererObj;
		int width = this.width;
		int height = this.height;
		this.slot = new XRayBlockSlot(Minecraft.getMinecraft(), width, height,
				25, height - 25, 20, this);
		this.add = new GuiButton(0, width / 9, height - 22, 70, 20, "Add Block");
		this.del = new GuiButton(1, width / 9 * 3, height - 22, 70, 20,
				"Delete Block");

		this.del.enabled = false;
		this.edit = new GuiButton(2, width / 9 * 5, height - 22, 70, 20,
				"Edit Block");

		this.edit.enabled = false;
		this.exit = new GuiButton(3, width / 9 * 7, height - 22, 70, 20, "Exit");
		this.buttonList.add(this.add);
		this.buttonList.add(this.del);
		this.buttonList.add(this.edit);
		this.buttonList.add(this.exit);
	}

	public void drawScreen(int par1, int par2, float par3) {
		this.slot.drawScreen(par1, par2, par3);
		super.drawScreen(par1, par2, par3);
		if (this.slot.selectedIndex != -1) {
			this.del.enabled = true;
			this.edit.enabled = true;
		} else {
			this.del.enabled = false;
			this.edit.enabled = false;
		}
	}

	protected void actionPerformed(GuiButton par1GuiButton) {
		switch (par1GuiButton.id) {
		case 0:
			Minecraft.getMinecraft().displayGuiScreen(new XRayAddGui());
			break;
		case 1:
			XRayBlocks.blocks.remove(this.slot.selectedIndex);
			this.slot.selectedIndex = -1;
			try {
				XRayBlocks.save();
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case 2:
			Minecraft.getMinecraft().displayGuiScreen(
					new XRayAddGui((XRayBlocks) XRayBlocks.blocks
							.get(this.slot.selectedIndex),
							this.slot.selectedIndex));

			break;
		case 3:
			Minecraft.getMinecraft().displayGuiScreen(null);
			XRay.instance.setCooldownTicks(0);
			break;
		default:
			this.slot.actionPerformed(par1GuiButton);
		}
	}
}