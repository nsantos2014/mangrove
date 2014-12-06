/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package net.minecraft.mangrove.core.xray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.mangrove.core.gui.button.GuiSlider;
import net.minecraft.util.RegistryNamespaced;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class XRayAddGui extends GuiScreen {
	String id;
	GuiSlider colorR;
	GuiSlider colorG;
	GuiSlider colorB;
	GuiSlider colorA;
	private GuiButton add;
	private GuiButton cancel;
	private GuiButton matterMeta;
	private GuiButton isEnabled;
	private int selectedIndex;
	Minecraft mc;
	private int r;
	private int g;
	private int b;
	private int a;
	private boolean enabled;
	private boolean bmeta;
	private int meta;
	private int sliderpos;
	private GuiTextField searchbar;
	private ArrayList<String> blocks;

	public XRayAddGui(int r, int g, int b, int a, int meta, String id,	boolean enabled, int index) {
		this();
		this.r = r;		
		this.g = g;
		this.b = b;
		this.a = a;
		this.meta = meta;
		this.id = id;
		this.enabled = enabled;
		this.selectedIndex = index;
	}

	public XRayAddGui() {
		super();
		this.selectedIndex = -1;

		this.r = 128;
		this.g = 128;
		this.b = 128;
		this.a = 255;
		this.enabled = true;
		this.bmeta = false;

		this.blocks = new ArrayList();

		this.mc = Minecraft.getMinecraft();
	}

	public XRayAddGui(XRayBlocks xrayBlocks, int index) {
		this(xrayBlocks.r, xrayBlocks.g, xrayBlocks.b, xrayBlocks.a,
				xrayBlocks.meta, xrayBlocks.id, xrayBlocks.enabled, index);
	}

	protected void actionPerformed(GuiButton par1GuiButton) {
		if (par1GuiButton.id == 0) {
			if (this.selectedIndex != -1)
				XRayBlocks.blocks.remove(this.selectedIndex);
			XRayBlocks.blocks.add(new XRayBlocks(
					(int) (this.colorR.sliderValue * 255.0F),
					(int) (this.colorG.sliderValue * 255.0F),
					(int) (this.colorB.sliderValue * 255.0F),
					(int) (this.colorA.sliderValue * 255.0F),
					(this.bmeta) ? this.meta : -1, this.id, this.enabled));
			try {
				XRayBlocks.save();
			} catch (IOException e) {
				e.printStackTrace();
			}
			this.mc.displayGuiScreen(new XRayGui());
		} else if (par1GuiButton.id == 1) {
			this.mc.displayGuiScreen(new XRayGui());
		} else if (par1GuiButton.id == 6) {
			this.enabled = (!(this.enabled));
			this.isEnabled.displayString = ((this.enabled) ? "Enabled"
					: "Disabled");
		} else if (par1GuiButton.id == 7) {
			this.bmeta = (!(this.bmeta));
			this.matterMeta.displayString = ((this.bmeta) ? "Meta-Check Enabled"
					: "Meta-Check Disabled");
		}

		super.actionPerformed(par1GuiButton);
	}

	public void initGui() {
		super.initGui();
		int width = this.width;
		int height = this.height;
		this.add = new GuiButton(0, width / 2 - 42, height - 22, 40, 20, "Add");
		this.buttonList.add(this.add);
		this.cancel = new GuiButton(1, width / 2 + 42, height - 22, 40, 20,
				"Cancel");
		this.buttonList.add(this.cancel);
		this.colorR = new GuiSlider(2, width - 160, height / 10 * 5,
				"Red-Value", this.r / 255.0F);
		this.buttonList.add(this.colorR);
		this.colorG = new GuiSlider(3, width - 160, height / 10 * 6,
				"Green-Value", this.g / 255.0F);
		this.buttonList.add(this.colorG);
		this.colorB = new GuiSlider(4, width - 160, height / 10 * 7,
				"Blue-Value", this.b / 255.0F);
		this.buttonList.add(this.colorB);
		this.colorA = new GuiSlider(5, width - 160, height / 10 * 8,
				"Alpha-Value", this.a / 255.0F);
		this.buttonList.add(this.colorA);
		this.isEnabled = new GuiButton(6, width - 160, height / 10 * 4, 70, 20,
				(this.enabled) ? "Enabled" : "Disabled");
		this.buttonList.add(this.isEnabled);
		this.matterMeta = new GuiButton(7, width - 90, height / 10 * 4, 80, 20,
				(this.bmeta) ? "Meta-Check Enabled" : "Meta-Check Disabled");
		this.buttonList.add(this.matterMeta);
		Keyboard.enableRepeatEvents(true);
		this.searchbar = new GuiTextField(this.fontRendererObj, 60, 45, 120,
				this.fontRendererObj.FONT_HEIGHT);
		this.searchbar.setMaxStringLength(30);
		this.searchbar.setCanLoseFocus(false);
		this.searchbar.setFocused(true);
		this.searchbar.setTextColor(16777215);
		this.blocks.addAll(Block.blockRegistry.getKeys());
	}

	public void drawScreen(int x, int y, float par3) {
		super.drawScreen(x, y, par3);
		int width = this.width;
		int height = this.height;
		drawBackground(0);

		this.add.enabled = (this.id != null);
		drawInfo();
		this.searchbar.drawTextBox();
		super.drawScreen(x, y, par3);
		ArrayList blockstodraw = getItemStackList();

		int s = 9;
		for (int i = 0; i < blockstodraw.size(); ++i) {
			int ni = i + this.sliderpos * s;
			if (ni < blockstodraw.size()) {
				ItemStack b = (ItemStack) blockstodraw.get(ni);

				if (i == s * 7)
					break;
				try {
					RenderHelper.enableGUIStandardItemLighting();
					drawRect(10 + i % s * 20, 60 + i / s * 20, 10 + i % s
							* 20 + 16, 60 + i / s * 20 + 16, -2130706433);
					RenderHelper.disableStandardItemLighting();
					drawItem(b, 10 + i % s * 20, 60 + i / s * 20, "");
				} catch (Exception e) {
				}

			}

		}

		RenderHelper.enableGUIStandardItemLighting();
		drawRect(
				width / 3 * 2,
				height / 6,
				width - 30,
				height / 6 * 2,
				(((int) (this.colorA.sliderValue * 255.0F) << 8 | (int) (this.colorR.sliderValue * 255.0F)) << 8 | (int) (this.colorG.sliderValue * 255.0F)) << 8
						| (int) (this.colorB.sliderValue * 255.0F));

		GL11.glDisable(2929);
		stringint si = getClickedBlock(x, y);
		if (si != null) {
			drawString(this.fontRendererObj,
					((Block) Block.blockRegistry.getObject(si.id))
							.getLocalizedName(), x - 5, y - 10, 16777215);
		}

		GL11.glEnable(2929);
	}

	public void updateScreen() {
		this.searchbar.updateCursorCounter();
	}

	private void drawInfo() {
		int width = this.width;
		int height = this.height;
		drawString(this.fontRendererObj, "Search", 15, 45, 16777215);

		drawString(
				this.fontRendererObj,
				(this.id == null) ? "No Block selected"
						: ((Block) Block.blockRegistry.getObject(this.id))
								.getLocalizedName(), width / 3 * 2 + 20, 20,
				16777215);
	}

	private void drawItem(ItemStack itemstack, int x, int y, String name) {
		GL11.glColor3ub((byte)0xff,(byte) 0xff,(byte) 0xff);
		GL11.glDisable(2896);
		this.zLevel = 200.0F;
		itemRender.zLevel = 200.0F;
		FontRenderer font = null;
		if (itemstack != null)
			font = itemstack.getItem().getFontRenderer(itemstack);
		if (font == null)
			font = this.fontRendererObj;
		itemRender.renderItemAndEffectIntoGUI(font, this.mc.getTextureManager(),itemstack, x, y);
		itemRender.renderItemOverlayIntoGUI(font, this.mc.getTextureManager(),itemstack, x, y, name);
		this.zLevel = 0.0F;
		itemRender.zLevel = 0.0F;
		GL11.glEnable(2896);
	}

	protected void keyTyped(char par1, int par2) {
		this.searchbar.textboxKeyTyped(par1, par2);
		this.blocks.clear();
		Set<String> s = Block.blockRegistry.getKeys();
		for (String string : s) {
			String sb = this.searchbar.getText();
			Block b = (Block) Block.blockRegistry.getObject(string);
			if (b.getLocalizedName().toLowerCase().contains(sb.toLowerCase()))
				this.blocks.add(string);
		}
		this.sliderpos = 0;
		super.keyTyped(par1, par2);
	}

	protected void mouseClicked(int x, int y, int mouseButton) {
		super.mouseClicked(x, y, mouseButton);
		this.searchbar.mouseClicked(x, y, mouseButton);
		if (mouseButton != 0)
			return;
		stringint s = getClickedBlock(x, y);
		if (s != null) {
			this.id = s.id;
			this.meta = s.meta;
		}
	}

	public void handleMouseInput() {
		super.handleMouseInput();
		int x = Mouse.getEventDWheel();
		ArrayList blockstodraw = getItemStackList();
		int xmax = blockstodraw.size() / 9;
		int xmin = 0;
		if (x > 0) {
			if (this.sliderpos >= xmax)
				return;
			this.sliderpos += 1;
		} else if ((x < 0) && (this.sliderpos > xmin)) {
			this.sliderpos -= 1;
		}
	}

	private ArrayList<ItemStack> getItemStackList() {
		ArrayList blockstodraw = new ArrayList();
		for (int i = 0; i < this.blocks.size(); ++i) {
			Block b = (Block) Block.blockRegistry
					.getObject((String) this.blocks.get(i));
			b.getSubBlocks(new ItemStack(b).getItem(), new CreativeTabs(
					19999, "FakeTab") {
				public Item getTabIconItem() {
					return null;
				}
			}, blockstodraw);
		}

		return blockstodraw;
	}

	private stringint getClickedBlock(int x, int y) {
		int index = 0;
		ArrayList z = new ArrayList();
		for (int i = 0; i < this.blocks.size(); ++i) {
			Block b = (Block) Block.blockRegistry
					.getObject((String) this.blocks.get(i));
			b.getSubBlocks(new ItemStack(b).getItem(), new CreativeTabs(
					19999, "FakeTab") {
				public Item getTabIconItem() {
					return null;
				}
			}, z);
		}

		for (int i = 0; i < this.blocks.size(); ++i) {
			Block b = (Block) Block.blockRegistry.getObject((String) this.blocks.get(i));
			ArrayList blockstodraw = new ArrayList();
			b.getSubBlocks(new ItemStack(b).getItem(), new CreativeTabs(19999, "FakeTab") {
				public Item getTabIconItem() {
					return null;
				}
			}, blockstodraw);

			for (int j = 0; j < blockstodraw.size(); ++j) {
				if (((index + j) % 9 > 9)
						|| ((index + j - (this.sliderpos * 9)) / 9 > 7))
					continue;
				if (index + j - (this.sliderpos * 9) >= 0) {
					int c = (index + j) % 9;
					int v = (index + j - (this.sliderpos * 9)) / 9;
					if ((x > 10 + c * 20) && (x < 26 + c * 20)
							&& (y > v * 20 + 60) && (y < v * 20 + 76)) {
						int smeta = 0;
						try {
							smeta = ((ItemStack) blockstodraw.get(j))
									.getItemDamage();
						} catch (Exception e) {
							smeta = -1;
						}
						return new stringint((String) this.blocks.get(i), smeta);
					}
				}
			}
			index += blockstodraw.size();
		}
		return null;
	}

	private class stringint {
		public int meta;
		public String id;

		public stringint(String paramString, int paramInt) {
			this.id = paramString;
			this.meta = paramInt;
		}
	}
}