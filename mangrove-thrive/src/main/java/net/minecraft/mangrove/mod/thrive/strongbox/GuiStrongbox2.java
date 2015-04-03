package net.minecraft.mangrove.mod.thrive.strongbox;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.mangrove.core.gui.MGGui;
import net.minecraft.mangrove.mod.thrive.autocon.itembroker.EnumConnectionState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiStrongbox2 extends GuiContainer {
	private static final ResourceLocation MERCHANT_GUI_TEXTURE = new ResourceLocation(
			"mgthrive", "textures/gui/container/strongbox.png");
	private static final ResourceLocation creativeInventoryTabs = new ResourceLocation(
			"textures/gui/container/creative_inventory/tabs.png");
	// private static final ResourceLocation craftingTableGuiTextures = new
	// ResourceLocation("textures/gui/container/crafting_table.png");
	// // private static final ResourceLocation craftingTableGuiTextures2 = new
	// ResourceLocation("textures/gui/container/driller.png");
	// private static final ResourceLocation craftingTableGuiTextures2 = new
	// ResourceLocation("textures/gui/container/creative_inventory/tab_items.png");
	// // private static final ResourceLocation ledger = new
	// ResourceLocation("textures/gui/demo_background.png");
	private static final ResourceLocation ledger = new ResourceLocation(
			"textures/gui/container/creative_inventory/tab_items.png");

	private TileEntityStrongbox crate;

//	private GuiButton nextPageButton;
//	private GuiButton prevPageButton;

	private ContainerStrongbox2 container;
	private float currentScroll = 0.0f;
	private Scrollbar scrollBar;

	private GuiStrongbox2(ContainerStrongbox2 container,
			TileEntityStrongbox autobench) {
		super(container);
		this.container = container;
		this.crate = autobench;
		this.xSize = 190;
		this.ySize = 202;
	}

	// public GuiMAV(InventoryPlayer par1InventoryPlayer, TileEntityCrate
	// autobench){
	// this(new ContainerCrateOld(par1InventoryPlayer, autobench),autobench);
	// }
	public GuiStrongbox2(InventoryPlayer par1InventoryPlayer,
			TileEntityStrongbox autobench) {
		this(new ContainerStrongbox2(autobench, par1InventoryPlayer), autobench);
	}

	@Override
	public void initGui() {
		super.initGui();
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;

//		this.prevPageButton = new GuiButton(0, k + (xSize / 2) - 25, l + 60,
//				20, 20, "<");
//		this.nextPageButton = new GuiButton(1, k + (xSize / 2) + 30, l + 60,
//				20, 20, ">");
//		buttonList.add(prevPageButton);
//		buttonList.add(nextPageButton);

		this.scrollBar = new Scrollbar(1, k + 170, l + 22, 88);
		// buttonList.add(scrollBar);

		// container.setProcessingContainer();
	}

	@Override
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
		int i = Mouse.getEventDWheel();
		if (i != 0) {
			int j = 200;
			this.currentScroll = (float) ((double) this.currentScroll - (double) i
					/ (double) j);
			this.currentScroll = MathHelper.clamp_float(this.currentScroll,
					0.0F, 1.0F);

			System.out.println("current scroll:" + currentScroll);
		}
		// int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
		// int mouseY = this.height - Mouse.getEventY() * this.height /
		// this.mc.displayHeight - 1;
		// int m = Mouse.getEventButton();
		// System.out.println(" Mouse event :"+mouseX+":"+mouseY+":"+m);
		// this.scrollBar.handleMouse(mc, mouseX, mouseY,m);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton)
			throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		this.scrollBar.mousePressed(mc, mouseX, mouseY);
	}

	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		super.mouseReleased(mouseX, mouseY, state);
		this.scrollBar.mouseReleased(mouseX, mouseY);
	}

	private void drawVerticalString(int x, int y, String string) {
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, 0);
		GlStateManager.rotate(-90, 0, 0, 1);
		this.fontRendererObj.drawString(string, 0, 0, 4210752);
		GlStateManager.popMatrix();
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of
	 * the items)
	 */
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		// this.fontRendererObj.drawString(String.format("Crate - Pos %d to %d",container.getStartPos(),container.getEndPos()),
		// 28, 6, 4210752);
		// this.fontRendererObj.drawString("Crate", 28, 6, 4210752);
		// //this.fontRendererObj.drawString(I18n.format("container.crafting",
		// new Object[0]), 28, 6, 4210752);
		// // this.fontRendererObj.drawString(I18n.format("container.inventory",
		// new Object[0]), 8, this.ySize - 96 + 2, 4210752);
		//

		drawVerticalString(10, 110, String.format("Crate - Pos %d to %d",
				container.getStartPos(), container.getEndPos()));
		// pushFormCoords();
		drawVerticalString(10, 172,
				I18n.format("container.inventory", new Object[0]));
		// GlStateManager.popMatrix();

	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);

		GlStateManager.disableLighting();
		GlStateManager.disableDepth();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

		GlStateManager.enableLighting();
		GlStateManager.enableDepth();
		RenderHelper.enableStandardItemLighting();
	}

	protected void drawGuiContainerBackgroundLayer(float partialTicks,
			int mouseX, int mouseY) {
		pushFormCoords();

		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(MERCHANT_GUI_TEXTURE);

		this.drawTexturedModalRect(0, 0, 0, 0, this.xSize, this.ySize);

		// // DRAW scrollbar
		// this.mc.getTextureManager().bindTexture(creativeInventoryTabs);
		//
		// this.drawTexturedModalRect(170, 22+(int)(172*currentScroll), 232, 0,
		// 12, 15);

		GlStateManager.popMatrix();

		this.scrollBar.drawButton(mc, mouseX, mouseY);
	}

	private void pushFormCoords() {
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		GlStateManager.pushMatrix();
		GlStateManager.translate(k, l, 0);
	}

	protected void drawGuiContainerBackgroundLayer2(float p_146976_1_,
			int p_146976_2_, int p_146976_3_) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(MERCHANT_GUI_TEXTURE);

		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		// this.drawTexturedModalRect(k, l, 0, 0, 512/2, 512/2);
		// this.drawTexturedModalRect(k, l, 0, 0, xSize, ySize);
		this.drawTexturedModalRect(k, l, 0, 0, xSize - 8, 256);
		this.drawTexturedModalRect(k + xSize - 8, l, 256 - 16, 0, 16, 256);
		this.mc.getTextureManager().bindTexture(MERCHANT_GUI_TEXTURE);
		// this.drawTexturedModalRect(k, l, 0, 0, 352/2, 332/2);
		// this.mc.getTextureManager().bindTexture(craftingTableGuiTextures);
		this.drawTexturedModalRect(k + 6, l + 80, 6, 80, xSize - 6,
				ySize - 70 - 8);

		// if( !toggle){
		// this.mc.getTextureManager().bindTexture(craftingTableGuiTextures);
		// this.drawTexturedModalRect(k, l, 0, 0, xSize, ySize);
		// }else{
		// this.mc.getTextureManager().bindTexture(ledger);
		// this.drawTexturedModalRect(k+4, l+4, 8, 8,120, 68);
		this.mc.getTextureManager().bindTexture(MERCHANT_GUI_TEXTURE);

		this.drawTexturedModalRect(k + 8, l + 16, 8, 16, 54, 55);
		this.drawTexturedModalRect(k + (xSize / 2) - 8, l + 16, 8, 16, 18, 19);
		this.drawTexturedModalRect(k + xSize - 8 - 54, l + 16, 8, 16, 54, 55);

		// this.mc.getTextureManager().bindTexture(TextureMap.locationItemsTexture);
		// this.drawTexturedModelRectFromIcon(k+(xSize/2)-8+1, l+16+1,
		// Items.apple.getIconFromDamage(1), 16, 16);

		// }else{
		//
		// this.mc.getTextureManager().bindTexture(craftingTableGuiTextures);
		//
		// this.drawTexturedModalRect(k+8, l+10, 29, 9, xSize-60, 68);
		// }

	}

	@Override
	protected void mouseClickMove(int mouseX, int mouseY,
			int clickedMouseButton, long timeSinceLastClick) {
		super.mouseClickMove(mouseX, mouseY, clickedMouseButton,
				timeSinceLastClick);
		this.scrollBar.mouseDragged(mc, mouseX, mouseY, clickedMouseButton);
	}

	@Override
	protected void actionPerformed(GuiButton btn) throws IOException {
		super.actionPerformed(btn);
		switch (btn.id) {
		case 1:
			container.nextPage();
			break;
		case 0:
			container.previousPage();
			// this.toggle=!toggle;
			// toggleButton.displayString=this.toggle?"Back":"Template";
			// if( this.toggle){
			// setButton.xPosition=Math.abs(setButton.xPosition);
			// setButton.yPosition=Math.abs(setButton.yPosition);
			// // container.setTemplateContainer();
			// container.hideOperationSlots();
			// container.showTemplateSlots();
			// }else {
			// setButton.xPosition=-Math.abs(setButton.xPosition);
			// setButton.yPosition=-Math.abs(setButton.yPosition);
			// // container.setProcessingContainer();
			// container.hideTemplateSlots();
			// container.showOperationSlots();
			// }
			//
			break;
		}
	}

	@SideOnly(Side.CLIENT)
	 class Scrollbar extends Gui {

		public int width;
		public int height;
		public int xPosition;
		public int yPosition;
		public int id;
		public boolean enabled;
		public boolean visible;
		protected boolean hovered;

		private float scrollPosition;
		private int h;
		private int xThumb;
		private int yThumb;
		private boolean pressed;
		private int pressingY = 0;
		private int currentFirstRow;

		public Scrollbar(int buttonID, int x, int y, int h) {
			this.id = buttonID;
			this.xPosition = x;
			this.yPosition = y;
			this.xThumb = 0;
			this.yThumb = y;
			this.pressed = false;
			this.width = 12;
			this.height = h;
			this.visible = true;
			this.currentFirstRow = 0;
			this.scrollPosition = 0.0f;

		}

		public boolean withinScrollbar(Minecraft mc, int mouseX, int mouseY,
				int button) {
			if (mouseX < xPosition || mouseX > (xPosition + width)
					|| mouseY < yPosition || mouseY > (yPosition + height)) {
				return false;
			}
			return true;

		}

		public boolean withinThumb(Minecraft mc, int mouseX, int mouseY,
				int button) {
			if (mouseX < xPosition || mouseX > (xPosition + 12)
					|| mouseY < yThumb || mouseY > (yThumb + 15)) {
				return false;
			}
			return true;
		}

		protected void mouseDragged(Minecraft mc, int mouseX, int mouseY,
				int clickedMouseButton) {
			if (pressed && withinThumb(mc, mouseX, mouseY, clickedMouseButton)) {
				int delta = mouseY - pressingY;
				if (delta != 0) {
					yThumb += delta;
					pressingY = mouseY;
					if (yThumb < yPosition) {
						yThumb = yPosition;
						this.pressed = false;
					}
					if (yThumb > (yPosition + height - 15)) {
						yThumb = yPosition + height - 15;
						this.pressed = false;
					}
					scrollPosition = (float) (yThumb - yPosition)
							/ (float) (height - 15);

					int firstRow = Math.round(19 * scrollPosition);
					if (firstRow != currentFirstRow) {
						currentFirstRow = firstRow;
//						System.out.println("Change row range:" + scrollPosition
//								+ ":" + firstRow + " to " + (5 + firstRow));
						
						container.setRange(firstRow,5);
					} else {
						System.out.println("dragging:" + mouseX + ":" + mouseY
								+ ":" + delta + ":" + scrollPosition + " :>>>"
								+ firstRow + " to " + (5 + firstRow));
					}
				}
			} else {
				this.pressed = false;
			}

		}

		public void mouseReleased(int mouseX, int mouseY) {
			System.out.println("released:" + mouseX + ":" + mouseY);
			this.pressed = false;
		}

		public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
			System.out.println("pressed:" + mouseX + ":" + mouseY);
			this.pressed = true;
			this.pressingY = mouseY;
			// this.scrollPosition = (float)(mouseY - (this.yPosition + 4)) /
			// (float)(this.height - 8);
			//
			// if (this.scrollPosition < 0.0F)
			// {
			// this.scrollPosition = 0.0F;
			// }
			//
			// if (this.scrollPosition > 1.0F)
			// {
			// this.scrollPosition = 1.0F;
			// }

			return true;
		}

		public void drawButton(Minecraft mc, int mouseX, int mouseY) {
			if (this.visible) {
				// DRAW scrollbar
				GlStateManager.pushMatrix();
				mc.getTextureManager().bindTexture(creativeInventoryTabs);
				if (pressed) {
					this.drawTexturedModalRect(this.xPosition, yThumb, 244, 0,
							12, 15);
				} else {
					this.drawTexturedModalRect(this.xPosition, yThumb, 232, 0,
							12, 15);
				}
				GlStateManager.popMatrix();
			}
		}
	}
}
